package com.jasoncarloscox.familymap.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.EventFilter;
import com.jasoncarloscox.familymap.model.FilterItem;
import com.jasoncarloscox.familymap.model.Model;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    // views
    private RecyclerView recyclerFilters;

    // member variables
    private Model model = Model.instance();
    private EventFilter filter = model.getFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.filter_activity_title);

        initComponents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onReturnToMainActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onReturnToMainActivity();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /**
     * Initializes all of the components that need to be accessed by grabbing
     * them from the view and adding necessary listeners.
     */
    private void initComponents() {
        recyclerFilters = findViewById(R.id.filter_recycler);

        recyclerFilters.setLayoutManager(new LinearLayoutManager(this));
        recyclerFilters.setAdapter(new FilterListAdapter(getFilterItems()));
    }

    private List<FilterItem> getFilterItems() {
        List<FilterItem> items = new ArrayList<>();

        FilterItem male = new FilterItem(getString(R.string.filter_title_male),
                                         getString(R.string.filter_description_male),
                                         filter.showMale());
        male.setListener(new FilterItem.Listener() {
            @Override
            public void onCheckedChange(boolean checked) {
                filter.setShowMale(checked);
            }
        });
        items.add(male);

        FilterItem female = new FilterItem(getString(R.string.filter_title_female),
                                           getString(R.string.filter_description_female),
                                           filter.showFemale());
        female.setListener(new FilterItem.Listener() {
            @Override
            public void onCheckedChange(boolean checked) {
                filter.setShowFemale(checked);
            }
        });
        items.add(female);

        FilterItem paternal = new FilterItem(getString(R.string.filter_title_paternal),
                getString(R.string.filter_description_paternal),
                filter.showPaternalSide());
        paternal.setListener(new FilterItem.Listener() {
            @Override
            public void onCheckedChange(boolean checked) {
                filter.setShowPaternalSide(checked);
            }
        });
        items.add(paternal);

        FilterItem maternal = new FilterItem(getString(R.string.filter_title_maternal),
                getString(R.string.filter_description_maternal),
                filter.showMaternalSide());
        maternal.setListener(new FilterItem.Listener() {
            @Override
            public void onCheckedChange(boolean checked) {
                filter.setShowMaternalSide(checked);
            }
        });
        items.add(maternal);

        for (String type : model.getEventTypes()) {
            items.add(getEventTypeFilterItem(type));
        }

        return items;
    }

    private FilterItem getEventTypeFilterItem(final String type) {
        FilterItem item = new FilterItem(getString(R.string.filter_title_type, type),
                                         getString(R.string.filter_description_type, type),
                                         filter.showType(type));
        item.setListener(new FilterItem.Listener() {
            @Override
            public void onCheckedChange(boolean checked) {
                filter.setShowType(type, checked);
            }
        });

        return item;
    }

    private void onReturnToMainActivity() {
        if (filter.isAltered()) {
            model.getMapState().onFilterUpdate();
            filter.resetAltered();
        }
    }
}
