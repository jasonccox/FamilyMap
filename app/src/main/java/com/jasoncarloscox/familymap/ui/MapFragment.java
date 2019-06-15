package com.jasoncarloscox.familymap.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Event;
import com.jasoncarloscox.familymap.model.EventMap;
import com.jasoncarloscox.familymap.model.Model;
import com.jasoncarloscox.familymap.util.ResourceGenerator;

/**
 * A fragment displaying a map of events.
 *
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment
        implements OnMapReadyCallback, OnMarkerClickListener, OnInfoWindowClickListener {

    private static final String KEY_EVENT_ID = "event_id";

    private static final int EVENT_ICON_SIZE_DP = 30;

    private EventMap map;
    private ImageView imgEventIcon;

    private Event selectedEvent;
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
            args.putString(KEY_EVENT_ID, eventId);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedEvent = model.getEvent(getArguments().getString(KEY_EVENT_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        initViews(view);
        restoreState(savedInstanceState);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (selectedEvent != null) {
            savedInstanceState.putString(KEY_EVENT_ID, selectedEvent.getId());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (model.isLoggedIn()) {
            model.pushMapUpdatesTo(map, getResources());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);

        map = new EventMap(googleMap);
        model.pushMapUpdatesTo(map, getResources());

        setSelectedEvent(selectedEvent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        setSelectedEvent((Event) marker.getTag());

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Event event = (Event) marker.getTag();

        Intent intent = new Intent(getActivity(), PersonActivity.class);
        intent.putExtra(PersonActivity.KEY_PERSON_ID, event.getPersonId());
        startActivity(intent);
    }

    /**
     * Initializes all of the views that need to be accessed
     *
     * @param parent the View containing all the components
     */
    private void initViews(View parent) {
        imgEventIcon = parent.findViewById(R.id.map_event_icon);

        imgEventIcon.setImageDrawable(ResourceGenerator
                .markerIcon(getActivity(), EVENT_ICON_SIZE_DP, R.color.colorAccent));

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_map);

        mapFragment.getMapAsync(this);
    }

    /**
     * Restores the state of the map fragment.
     *
     * @param savedInstanceState a Bundle containing the saved state
     */
    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        selectedEvent = model.getEvent(savedInstanceState.getString(KEY_EVENT_ID));
    }

    /**
     * @param event the event whose details should be focused on the map
     */
    private void setSelectedEvent(Event event) {
        selectedEvent = event;

        if (selectedEvent != null) {
            map.setFocusedEvent(selectedEvent);
        }
    }
}
