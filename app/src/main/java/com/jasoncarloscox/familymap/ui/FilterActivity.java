package com.jasoncarloscox.familymap.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.FilterItem;
import com.jasoncarloscox.familymap.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity displaying a list of possible ways to filter event data.
 */
public class FilterActivity extends AppCompatActivity {

    // views
    private RecyclerView recycler;

    // member variables
    private Model model = Model.instance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.filter_activity_title);

        initViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /**
     * Initializes all of the views that need to be accessed.
     */
    private void initViews() {
        recycler = findViewById(R.id.filter_recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new FilterListAdapter(getFilterItems()));
    }

    /**
     * @return a list of FilterItems to be used with the FilterListAdapter to
     *         display filters in the RecyclerView
     */
    private List<FilterItem> getFilterItems() {
        List<FilterItem> items = new ArrayList<>();

        FilterItem male = new FilterItem(getString(R.string.filter_title_male),
                                         getString(R.string.filter_description_male),
                                         model.shouldShowMale());
        male.setListener(new FilterItem.Listener() {
            @Override
            public void onFilterStatusChange(boolean filtered) {
                model.setShouldShowMale(filtered);
            }
        });
        items.add(male);

        FilterItem female = new FilterItem(getString(R.string.filter_title_female),
                                           getString(R.string.filter_description_female),
                                           model.shouldShowFemale());
        female.setListener(new FilterItem.Listener() {
            @Override
            public void onFilterStatusChange(boolean filtered) {
                model.setShouldShowFemale(filtered);
            }
        });
        items.add(female);

        FilterItem paternal = new FilterItem(getString(R.string.filter_title_paternal),
                getString(R.string.filter_description_paternal),
                model.shouldShowPaternalSide());
        paternal.setListener(new FilterItem.Listener() {
            @Override
            public void onFilterStatusChange(boolean filtered) {
                model.setShouldShowPaternalSide(filtered);
            }
        });
        items.add(paternal);

        FilterItem maternal = new FilterItem(getString(R.string.filter_title_maternal),
                getString(R.string.filter_description_maternal),
                model.shouldShowMaternalSide());
        maternal.setListener(new FilterItem.Listener() {
            @Override
            public void onFilterStatusChange(boolean filtered) {
                model.setShouldShowMaternalSide(filtered);
            }
        });
        items.add(maternal);

        for (String type : model.getEventTypes()) {
            items.add(getEventTypeFilterItem(type));
        }

        return items;
    }

    /**
     * @param type the type of event that this item will filter
     * @return a FilterItem to be used with the FilterListAdapter to display
     *         filters in the RecyclerView
     */
    private FilterItem getEventTypeFilterItem(final String type) {
        FilterItem item = new FilterItem(getString(R.string.filter_title_type, type),
                                         getString(R.string.filter_description_type, type),
                                         model.shouldShow(type));
        item.setListener(new FilterItem.Listener() {
            @Override
            public void onFilterStatusChange(boolean filtered) {
                model.setShouldShow(type, filtered);
            }
        });

        return item;
    }
}
