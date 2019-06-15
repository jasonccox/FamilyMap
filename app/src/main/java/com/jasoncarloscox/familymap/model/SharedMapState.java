package com.jasoncarloscox.familymap.model;

import android.content.res.Resources;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.util.ResourceGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class SharedMapState {
    
    private Model model;
    private Resources res;
    private Set<EventMarker> markers = new HashSet<>();
    private Set<Line> lines = new HashSet<>();
    private Map<String, Float> markerColors = new HashMap<>();

    private boolean markerColorsInvalid = true;
    private boolean markersInvalid = true;
    private boolean linesInvalid = true;
    private boolean mapTypeInvalid = true;

    private Map<EventMap, UpdateTimes> mapUpdateTimes = new HashMap<>();
    private UpdateTimes lastStateUpdate = new UpdateTimes();


    /**
     * Creates a new SharedMapState.
     */
    protected SharedMapState(Model model) {
        this.model = model;
    }

    /**
     * Updates the given map based on the current shared map state.
     *
     * @param map the map to be updated
     * @param res the Resources object to be used to resolve string and color
     *            resources
     */
    protected void pushMapUpdatesTo(EventMap map, Resources res) {
        if (map == null) {
            return;
        }

        this.res = res;

        refreshInvalidData();

        UpdateTimes mapTimes = mapUpdateTimes.get(map);

        if (mapTimes == null) {
            mapTimes = new UpdateTimes();
            mapUpdateTimes.put(map, mapTimes);
        }

        if (lastStateUpdate.markers > mapTimes.markers) {
            map.updateMarkers(markers);
            mapTimes.markers = System.currentTimeMillis();
        }

        if (lastStateUpdate.lines > mapTimes.lines) {
            map.updateLines(lines);
            mapTimes.lines = System.currentTimeMillis();
        }

        if (lastStateUpdate.mapType > mapTimes.mapType) {
            map.updateMapType(model.getMapType());
            mapTimes.mapType = System.currentTimeMillis();
        }
    }

    /**
     * Indicate that the current marker colors are no longer valid and should be
     * updated.
     */
    protected void invalidateMarkerColors() {
        markerColorsInvalid = true;
        markersInvalid = true;
    }

    /**
     * Indicate the the current markers are no longer valid and should be
     * updated.
     */
    protected void invalidateMarkers() {
        markersInvalid = true;
    }

    /**
     * Indicate that the current lines are no longer valid and should be updated.
     */
    protected void invalidateLines() {
        linesInvalid = true;
    }

    /**
     * Indicate that the current map type is invalid and should be updated.
     */
    protected void invalidateMapType() {
        mapTypeInvalid = true;
    }

    private void refreshInvalidData() {
        if (markerColorsInvalid) {
            updateMarkerColors();
            markerColorsInvalid = false;
        }

        if (markersInvalid) {
            updateMarkers();
            markersInvalid = false;
        }

        if (linesInvalid) {
            updateLines();
            linesInvalid = false;
        }

        if (mapTypeInvalid) {
            updateMapType();
            mapTypeInvalid = false;
        }
    }

    private void updateMarkerColors() {
        markerColors.clear();

        for (String type : model.getEventTypes()) {
            markerColors.put(type, Math.abs(Float.valueOf(type.hashCode() % 360)));
        }
    }

    private void updateMapType() {
        lastStateUpdate.mapType = System.currentTimeMillis();
    }

    private void updateLines() {
        clearLines();
        generateLines();

        lastStateUpdate.lines = System.currentTimeMillis();
    }

    private void generateLines() {
        if (model.shouldShowLifeLines()) {
            generateLifeStoryLines();
        }

        if (model.shouldShowTreeLines()) {
            generateTreeLines();
        }

        if (model.shouldShowSpouseLines()) {
            generateSpouseLines();
        }
    }

    private void generateLifeStoryLines() {
        int color = res.getColor(model.getLifeLineColor().getResource());

        for (Person person : model.getPersons()) {
            generateLifeStoryLines(person, color);
        }
    }

    private void generateLifeStoryLines(Person person, int color) {
        List<Event> events = model.filter(person.getEvents());

        for (int i = 1; i < events.size(); i++) {
            lines.add(generateLine(events.get(i - 1), events.get(i),
                                   Line.BASE_WIDTH / 2, color));
        }
    }

    private void generateSpouseLines() {
        int color = res.getColor(model.getSpouseLineColor().getResource());

        Set<Person> alreadyDone = new HashSet<>();

        for (Person person : model.getPersons()) {
            if (person.getSpouse() != null && !alreadyDone.contains(person)) {
                Line line = generateLine(person.getFirstEvent(),
                                         person.getSpouse().getFirstEvent(),
                                         Line.BASE_WIDTH / 2, color);

                if (line != null) {
                    lines.add(line);
                }

                alreadyDone.add(person.getSpouse());
            }
        }
    }

    private void generateTreeLines() {
        int color = res.getColor(model.getTreeLineColor().getResource());

        generateLinesToParents(model.getRootPerson(), Line.BASE_WIDTH, color);
    }

    private void generateLinesToParents(Person child, float width, int color) {
        if (child.getFather() != null) {
            Line line = generateLine(child.getFirstEvent(),
                                     child.getFather().getFirstEvent(),
                                     width, color);

            if (line != null) {
                lines.add(line);
            }

            generateLinesToParents(child.getFather(), width / 2, color);
        }

        if (child.getMother() != null) {
            Line line = generateLine(child.getFirstEvent(),
                                     child.getMother().getFirstEvent(),
                                     width, color);

            if (line != null) {
                lines.add(line);
            }

            generateLinesToParents(child.getMother(), width / 2, color);
        }
    }

    private Line generateLine(Event event1, Event event2, float width, int color) {
        if (event1 == null || event2 == null) {
            return null;
        }

        return new Line(event1, event2, width, color);
    }

    private void clearLines() {
        lines.clear();
    }

    private void updateMarkers() {
        clearMarkers();
        generateMarkers();

        lastStateUpdate.markers = System.currentTimeMillis();
    }

    private void generateMarkers() {
        for (Event event : model.getEvents()) {
            if (model.shouldShow(event)) {
                markers.add(new EventMarker(event));
            }
        }
    }

    private void clearMarkers() {
        markers.clear();
    }

    class Line {

        private static final float BASE_WIDTH = 12; // pixels

        private LatLng[] endpoints = new LatLng[2];
        private int color;
        private float width;

        private Line(Event event1, Event event2, float width, int color) {
            endpoints[0] = new LatLng(event1.getLatitude(), event1.getLongitude());
            endpoints[1] = new LatLng(event2.getLatitude(), event2.getLongitude());

            this.width = width;
            this.color = color;
        }

        public PolylineOptions getOptions() {
            return new PolylineOptions().add(endpoints).width(width).color(color);
        }
    }

    class EventMarker {

        private Event event;
        private LatLng position;
        private String title;
        private String snippet;

        private EventMarker(Event event) {
            this.event = event;
            position = new LatLng(event.getLatitude(), event.getLongitude());
            title = res.getString(R.string.event_popup_title,
                    event.getPerson().getFullName(),
                    ResourceGenerator.genderStringShort(event.getPerson().getGender(), res),
                    event.getType());
            snippet = res.getString(R.string.event_date_and_location, event.getYear(),
                    event.getCity(), event.getCountry());
        }

        public MarkerOptions getOptions() {
            return new MarkerOptions().position(position).title(title)
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(markerColors.get(event.getType())));
        }

        public Event getEvent() {
            return event;
        }
    }

    private class UpdateTimes {

        private long markers;
        private long lines;
        private long mapType;

    }
}
