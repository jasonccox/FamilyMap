package com.jasoncarloscox.familymap.model;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Container class for a GoogleMap that provides useful methods for interfacing
 * with SharedMapState and the UI.
 */
public class EventMap {

    /**
     * Possible values for the map's value.
     */
    public enum Type {

        // IMPORTANT: the order of the enum values must stay in sync with the
        // map_types_array in strings.xml

        NORMAL(GoogleMap.MAP_TYPE_NORMAL),
        SATELLITE(GoogleMap.MAP_TYPE_SATELLITE),
        TERRAIN(GoogleMap.MAP_TYPE_TERRAIN),
        HYBRID(GoogleMap.MAP_TYPE_HYBRID);

        private int value;

        /**
         * @return a constant used by the GoogleMap to identify its value
         */
        public int getValue() {
            return value;
        }

        /**
         * @param value a constant used by the GoogleMap to identify its value
         */
        Type(int value) {
            this.value = value;
        }
    }

    private GoogleMap map;
    private Map<Event, Marker> markersByEvent = new HashMap<>();
    private Set<Polyline> lines = new HashSet<>();

    /**
     * Creates a new EventMap.
     *
     * @param map the GoogleMap on which to place markers and lines
     */
    public EventMap(GoogleMap map) {
        this.map = map;
    }

    /**
     * Focuses the map view on the given Event.
     *
     * @param focused the Event on which to focus
     */
    public void setFocusedEvent(Event focused) {
        moveCameraToEvent(focused);
        markersByEvent.get(focused).showInfoWindow();
    }

    /**
     * Updates the map's markers, including removing markers no longer needed
     * and adding new ones.
     *
     * @param markers the markers to be displayed on the map
     */
    protected void updateMarkers(Collection<SharedMapState.EventMarker> markers) {
        removeMarkers();

        for (SharedMapState.EventMarker marker : markers) {
            Marker m = map.addMarker(marker.getOptions());
            m.setTag(marker.getEvent());
            this.markersByEvent.put(marker.getEvent(), m);
        }
    }

    /**
     * Updates the map's lines, including removing lines no longer needed and
     * adding new ones.
     *
     * @param lines the lines to be displayed on the map
     */
    protected void updateLines(Collection<SharedMapState.Line> lines) {
        removeLines();

        for (SharedMapState.Line line : lines) {
            Polyline pl = map.addPolyline(line.getOptions());
            this.lines.add(pl);
        }
    }

    /**
     * Updates the map's value.
     *
     * @param type the map's new value
     */
    protected void updateMapType(Type type) {
        map.setMapType(type.getValue());
    }

    /**
     * Removes all markers from the map.
     */
    private void removeMarkers() {
        for (Marker marker : markersByEvent.values()) {
            marker.remove();
        }

        markersByEvent.clear();
    }

    /**
     * Removes all lines from the map.
     */
    private void removeLines() {
        for (Polyline line : lines) {
            line.remove();
        }

        lines.clear();
    }

    /**
     * Focuses the camera on a given Event.
     *
     * @param event the Event on which to focus
     */
    private void moveCameraToEvent(Event event) {
        final float ZOOM = 3;

        LatLng ll = new LatLng(event.getLatitude(), event.getLongitude());

        map.moveCamera(CameraUpdateFactory.newLatLng(ll));
    }
}
