package com.jasoncarloscox.familymap.model;

import android.content.res.Resources;

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

public class EventMap {

    private GoogleMap map;
    private Map<Event, Marker> markersByEvent = new HashMap<>();
    private Set<Polyline> lines = new HashSet<>();

    public EventMap(GoogleMap map) {
        this.map = map;
    }

    public void setFocusedEvent(Event focused, boolean zoom) {
        moveCameraToEvent(focused, zoom);
        markersByEvent.get(focused).showInfoWindow();
    }

    public void updateMarkers(Collection<SharedMapState.EventMarker> markers) {
        removeMarkers();

        for (SharedMapState.EventMarker marker : markers) {
            Marker m = map.addMarker(marker.getOptions());
            m.setTag(marker.getEvent());
            this.markersByEvent.put(marker.getEvent(), m);
        }
    }

    public void updateLines(Collection<SharedMapState.Line> lines) {
        removeLines();

        for (SharedMapState.Line line : lines) {
            Polyline pl = map.addPolyline(line.getOptions());
            this.lines.add(pl);
        }
    }

    public void updateMapType(MapType type) {
        map.setMapType(type.getType());
    }

    private void removeLines() {
        for (Polyline line : lines) {
            line.remove();
        }

        lines.clear();
    }

    private void moveCameraToEvent(Event event, boolean zoom) {
        final float ZOOM = 3;

        LatLng ll = new LatLng(event.getLatitude(), event.getLongitude());

        if (zoom) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, ZOOM));
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLng(ll));
        }
    }

    private void removeMarkers() {
        for (Marker marker : markersByEvent.values()) {
            marker.remove();
        }

        markersByEvent.clear();
    }
}
