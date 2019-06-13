package com.jasoncarloscox.familymap.ui;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Gender;
import com.jasoncarloscox.familymap.model.Model;
import com.jasoncarloscox.familymap.model.Person;
import com.jasoncarloscox.familymap.model.Relative;
import com.jasoncarloscox.familymap.util.FAIconGenerator;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

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

        initComponents();

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
     * Initializes all of the components that need to be accessed by grabbing
     * them from the view and adding necessary listeners.
     */
    private void initComponents() {
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

    private void setPerson(Person person) {
        this.person = person;

        if (person == null) {
            return;
        }

        setTitle(getString(R.string.person_activity_title, person.getFirstName(),
                           person.getLastName()));

        imgPerson.setImageDrawable(FAIconGenerator
                .genderIcon(this, person.getGender(), PERSON_ICON_SIZE_DP));

        txtFirstName.setText(person.getFirstName());
        txtLastName.setText(person.getLastName());
        txtGender.setText(generateGenderString(person.getGender()));

        recyclerEvents.setLayoutManager(new LinearLayoutManager(this));
        recyclerEvents.setAdapter(new EventListAdapter(person.getEvents()));

        recyclerFamily.setLayoutManager(new LinearLayoutManager(this));
        recyclerFamily.setAdapter(new RelativeListAdapter(person.getRelatives()));
    }

    private String generateGenderString(String gender) {
        if (Gender.MALE.equals(gender)) {
            return getString(R.string.male);
        }

        if (Gender.FEMALE.equals(gender)) {
            return getString(R.string.female);
        }

        return getString(R.string.unknwon);
    }

    private void toggleShowEventsList() {
        if (recyclerEvents.getVisibility() == View.VISIBLE) {
            recyclerEvents.setVisibility(View.GONE);
            txtEventsHeaderIndicator.setText(R.string.expand);
        } else {
            recyclerEvents.setVisibility(View.VISIBLE);
            txtEventsHeaderIndicator.setText(R.string.collapse);
        }
    }

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
