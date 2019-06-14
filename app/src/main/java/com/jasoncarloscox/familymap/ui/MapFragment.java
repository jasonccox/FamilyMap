package com.jasoncarloscox.familymap.ui;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Event;
import com.jasoncarloscox.familymap.model.EventFilter;
import com.jasoncarloscox.familymap.model.Gender;
import com.jasoncarloscox.familymap.model.Model;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.HashMap;
import java.util.Map;

/**
 * A fragment displaying a map of events.
 *
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment
        implements OnMapReadyCallback, OnMarkerClickListener, OnInfoWindowClickListener {

    private static final String TAG = "MapFragment";

    private static final String KEY_EVENT_ID = "event_id";

    private static final int EVENT_ICON_SIZE_DP = 30;

    private GoogleMap map;
    private TextView txtInstructions;
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

        // initialize Iconify library
        Iconify.with(new FontAwesomeModule());

        initComponents(view);
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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);

        EventFilter filter = model.getFilter();

        for (Event event : filter.filter(model.getEvents())) {
            addEventMarker(event);
        }

        setSelectedEvent(selectedEvent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        setSelectedEvent((Event) marker.getTag());

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d(TAG, "info window clicked");
        Event event = (Event) marker.getTag();

        Intent intent = new Intent(getActivity(), PersonActivity.class);
        intent.putExtra(PersonActivity.KEY_PERSON_ID, event.getPersonId());
        startActivity(intent);
    }

    /**
     * Initializes all of the components that need to be accessed by grabbing
     * them from the view and adding necessary listeners.
     *
     * @param view the View containing all the components
     */
    private void initComponents(View view) {
        txtInstructions = (TextView) view.findViewById(R.id.map_txt_instructions);
        imgEventIcon = (ImageView) view.findViewById(R.id.map_event_icon);

        imgEventIcon.setImageDrawable(generateMarkerIcon());

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

    private void addEventMarker(Event event) {
        LatLng ll = new LatLng(event.getLatitude(), event.getLongitude());

        int genderStrRes = Gender.MALE.equals(event.getPerson().getGender()) ?
                R.string.male_short : R.string.female_short;
        String title = getString(R.string.event_popup_title,
                                 event.getPerson().getFirstName(),
                                 event.getPerson().getLastName(),
                                 getString(genderStrRes),
                                 event.getType());

        String snippet = getString(R.string.event_date_and_location, event.getYear(),
                                   event.getCity(), event.getCountry());

        Marker marker = map.addMarker(new MarkerOptions()
                                .position(ll)
                                .title(title)
                                .snippet(snippet));
        marker.setTag(event);
        model.addMarker(marker, event);
    }

    private void setSelectedEvent(Event event) {
        selectedEvent = event;

        if (selectedEvent != null) {
            moveCameraToEvent(selectedEvent);
            model.getMarker(selectedEvent).showInfoWindow();
        }
    }

    private void moveCameraToEvent(Event event) {
        LatLng ll = new LatLng(event.getLatitude(), event.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(ll));
    }

    private Drawable generateMarkerIcon() {
        IconDrawable icon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker);
        icon.colorRes(R.color.colorAccent);
        icon.sizeDp(EVENT_ICON_SIZE_DP);

        return icon;
    }
}
