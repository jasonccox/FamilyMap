package com.jasoncarloscox.familymap.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Event;
import com.jasoncarloscox.familymap.model.Model;

public class EventActivity extends AppCompatActivity {

    // keys
    public static final String KEY_EVENT_ID = "event_id";

    // fragment
    private MapFragment mapFragment;

    // member variables
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventId = getIntent().getExtras().getString(KEY_EVENT_ID);

        setTitle(R.string.event_activity_title);

        showMap();
    }

    private void showMap() {
        FragmentManager fm = this.getSupportFragmentManager();
        mapFragment = (MapFragment) fm.findFragmentById(R.id.event_frame);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(eventId);
            fm.beginTransaction().add(R.id.event_frame, mapFragment).commit();
        }
    }
}
