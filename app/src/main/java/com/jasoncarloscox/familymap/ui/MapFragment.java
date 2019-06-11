package com.jasoncarloscox.familymap.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Event;
import com.jasoncarloscox.familymap.model.EventFilter;
import com.jasoncarloscox.familymap.model.Model;

/**
 * A fragment displaying a map of events.
 *
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapFragment";

    private static final String EVENT_ID = "event_id";

    private GoogleMap map;

    private Event displayedEvent;
    private Model model = Model.instance();

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param eventId the id of the event to be focused on the map, or null to
     *                not focus any event
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance(String eventId) {
        MapFragment fragment = new MapFragment();
        if (eventId != null) {
            Bundle args = new Bundle();
            args.putString(EVENT_ID, eventId);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            displayedEvent = model.getEvent(getArguments().getString(EVENT_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_map);

        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Map loaded.");
        map = googleMap;

        addEventMarkers();
    }

    private void addEventMarkers() {
        for (Event event : model.getFilteredEvents()) {
            addEventMarker(event);
        }

        if (displayedEvent != null) {
            LatLng displayedEventLL = new LatLng(displayedEvent.getLatitude(),
                                                 displayedEvent.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(displayedEventLL));
        }
    }

    private void addEventMarker(Event event) {
        LatLng ll = new LatLng(event.getLatitude(), event.getLongitude());
        map.addMarker(new MarkerOptions().position(ll));
    }
}
