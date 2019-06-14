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

public class SharedMapState {
    
    private FamilyTree tree;
    private Settings settings;
    private EventFilter filter;
    private Resources res;
    private Set<EventMarker> markers = new HashSet<>();
    private Set<Line> lines = new HashSet<>();
    private Map<String, Float> markerColors = new HashMap<>();

    private Map<EventMap, UpdateTimes> mapUpdateTimes = new HashMap<>();

    private UpdateTimes lastStateUpdate = new UpdateTimes();


    public SharedMapState(FamilyTree tree, Settings settings, EventFilter filter, Resources res) {
        this.tree = tree;
        this.settings = settings;
        this.filter = filter;
        this.res = res;

        init();
    }

    public void pushUpdatesToMap(EventMap map) {
        if (map == null) {
            return;
        }

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
            map.updateMapType(settings.getMapType());
            mapTimes.mapType = System.currentTimeMillis();
        }
    }

    public void onFullUpdate() {
        updateMarkerColors();
        updateMarkers();
        updateLines();
        updateMapType();
    }

    public void onTreeUpdate() {
        updateMarkerColors();
        updateMarkers();
        updateLines();
    }

    public void onSettingsUpdate() {
        if (settings.areLinesAltered()) {
            updateLines();
        }

        if (settings.isMapTypeAltered()) {
            updateMapType();
        }
    }

    public void onFilterUpdate() {
        updateMarkers();
        updateLines();
    }

    private void init() {
        onFullUpdate();
    }

    private void updateMarkerColors() {
        markerColors.clear();

        for (String type : tree.getEventTypes()) {
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
        if (settings.showLifeLines()) {
            generateLifeStoryLines();
        }

        if (settings.showTreeLines()) {
            generateTreeLines();
        }

        if (settings.showSpouseLines()) {
            generateSpouseLines();
        }
    }

    private void generateLifeStoryLines() {
        int color = res.getColor(settings.getLifeLineColor().getResource());

        for (Person person : tree.getPersons()) {
            generateLifeStoryLines(person, color);
        }
    }

    private void generateLifeStoryLines(Person person, int color) {
        List<Event> events = filter.filter(person.getEvents());

        for (int i = 1; i < events.size(); i++) {
            lines.add(generateLine(events.get(i - 1), events.get(i),
                                   Line.BASE_WIDTH / 2, color));
        }
    }

    private void generateSpouseLines() {
        int color = res.getColor(settings.getSpouseLineColor().getResource());

        Set<Person> alreadyDone = new HashSet<>();

        for (Person person : tree.getPersons()) {
            if (person.getSpouse() != null && !alreadyDone.contains(person)) {
                Line line = generateLine(person.getFirstEvent(filter),
                                         person.getSpouse().getFirstEvent(filter),
                                         Line.BASE_WIDTH / 2,
                                         color);

                if (line != null) {
                    lines.add(line);
                }

                alreadyDone.add(person.getSpouse());
            }
        }
    }

    private void generateTreeLines() {
        int color = res.getColor(settings.getTreeLineColor().getResource());

        generateLinesToParents(tree.getPerson(tree.getRootPerson().getId()),
                Line.BASE_WIDTH,
                color);
    }

    private void generateLinesToParents(Person child, float width, int color) {
        if (child.getFather() != null) {
            Line line = generateLine(child.getFirstEvent(filter),
                                     child.getFather().getFirstEvent(filter),
                                     width,
                                     color);

            if (line != null) {
                lines.add(line);
            }

            generateLinesToParents(child.getFather(), width / 2, color);
        }

        if (child.getMother() != null) {
            Line line = generateLine(child.getFirstEvent(filter),
                                     child.getMother().getFirstEvent(filter),
                                     width,
                                     color);

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
        for (Event event : tree.getEvents()) {
            if (filter.showEvent(event)) {
                markers.add(new EventMarker(event));
            }
        }
    }

    private void clearMarkers() {
        markers.clear();
    }

    public class Line {

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

    public class EventMarker {

        private Event event;
        private LatLng position;
        private String title;
        private String snippet;

        private EventMarker(Event event) {
            this.event = event;
            position = new LatLng(event.getLatitude(), event.getLongitude());
            title = res.getString(R.string.event_popup_title,
                    event.getPerson().getFirstName(),
                    event.getPerson().getLastName(),
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
