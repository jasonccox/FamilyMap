package com.jasoncarloscox.familymap.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Event;
import com.jasoncarloscox.familymap.model.Model;
import com.jasoncarloscox.familymap.model.Person;
import com.jasoncarloscox.familymap.util.ResourceGenerator;

import java.util.List;

public class PersonActivity extends AppCompatActivity {

    // keys
    public static final String KEY_PERSON_ID = "person_id";
    private static final String KEY_EVENTS_EXPANDED = "events_expanded";
    private static final String KEY_FAMILY_EXPANDED = "family_expanded";

    // constants
    private static final int PERSON_ICON_SIZE_DP = 50;

    // views
    private ImageView imgPerson;
    private TextView txtFirstName;
    private TextView txtLastName;
    private TextView txtGender;
    private View viewEventsHeader;
    private View viewFamilyHeader;
    private TextView txtEventsHeaderIndicator;
    private TextView txtFamilyHeaderIndicator;
    private RecyclerView recyclerEvents;
    private RecyclerView recyclerFamily;

    // member variables
    private Model model = Model.instance();
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        person = model.getPerson(getIntent().getExtras().getString(KEY_PERSON_ID));

        restoreState(savedInstanceState);

        setPerson(person);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (person != null) {
            savedInstanceState.putString(KEY_PERSON_ID, person.getId());
        }

        savedInstanceState.putBoolean(KEY_EVENTS_EXPANDED,
                recyclerEvents.getVisibility() == View.VISIBLE);

        savedInstanceState.putBoolean(KEY_FAMILY_EXPANDED,
                recyclerFamily.getVisibility() == View.VISIBLE);
    }

    /**
     * Initializes all of the views that need to be accessed
     */
    private void initViews() {
        imgPerson = findViewById(R.id.person_img_person);
        txtFirstName = findViewById(R.id.person_first_name);
        txtLastName = findViewById(R.id.person_last_name);
        txtGender = findViewById(R.id.person_gender);
        viewEventsHeader = findViewById(R.id.person_header_events);
        viewFamilyHeader = findViewById(R.id.person_header_family);
        txtEventsHeaderIndicator = findViewById(R.id.person_header_indicator_events);
        txtFamilyHeaderIndicator = findViewById(R.id.person_header_indicator_family);
        recyclerEvents = findViewById(R.id.person_recycler_events);
        recyclerFamily = findViewById(R.id.person_recycler_family);

        viewEventsHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShowEventsList();
            }
        });

        viewFamilyHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShowFamilyList();
            }
        });
    }

    /**
     * Restores the state of the activity.
     *
     * @param savedInstanceState a Bundle containing the saved state
     */
    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        person = model.getPerson(savedInstanceState.getString(KEY_PERSON_ID));

        if (!savedInstanceState.getBoolean(KEY_EVENTS_EXPANDED)) {
            toggleShowEventsList();
        }

        if (!savedInstanceState.getBoolean(KEY_FAMILY_EXPANDED)) {
            toggleShowFamilyList();
        }
    }

    /**
     * @param person the Person whose details should be displayed
     */
    private void setPerson(Person person) {
        this.person = person;

        if (person == null) {
            return;
        }

        setTitle(getString(R.string.person_activity_title, person.getFullName()));

        imgPerson.setImageDrawable(ResourceGenerator
                .genderIcon(this, person.getGender(), PERSON_ICON_SIZE_DP));

        txtFirstName.setText(person.getFirstName());
        txtLastName.setText(person.getLastName());
        txtGender.setText(ResourceGenerator.genderString(person.getGender(), getResources()));

        List<Event> filteredEvents = model.filter(person.getEvents());

        recyclerEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerEvents.setAdapter(new EventListAdapter(filteredEvents));

        recyclerFamily.setLayoutManager(new LinearLayoutManager(this));
        recyclerFamily.setAdapter(new RelativeListAdapter(person.getFamily()));
    }

    /**
     * Toggles the visibility of the event list and updates the header to show
     * its visibility
     */
    private void toggleShowEventsList() {
        if (recyclerEvents.getVisibility() == View.VISIBLE) {
            recyclerEvents.setVisibility(View.GONE);
            txtEventsHeaderIndicator.setText(R.string.expand);
        } else {
            recyclerEvents.setVisibility(View.VISIBLE);
            txtEventsHeaderIndicator.setText(R.string.collapse);
        }
    }

    /**
     * Toggles the visibility of the family list and updates the header to show
     * its visibility
     */
    private void toggleShowFamilyList() {
        if (recyclerFamily.getVisibility() == View.VISIBLE) {
            recyclerFamily.setVisibility(View.GONE);
            txtFamilyHeaderIndicator.setText(R.string.expand);
        } else {
            recyclerFamily.setVisibility(View.VISIBLE);
            txtFamilyHeaderIndicator.setText(R.string.collapse);
        }
    }

}
