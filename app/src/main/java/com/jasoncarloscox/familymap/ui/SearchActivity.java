package com.jasoncarloscox.familymap.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Model;
import com.jasoncarloscox.familymap.util.ResourceGenerator;

import java.util.List;

/**
 * Activity displaying a search box and the results of the search
 */
public class SearchActivity extends AppCompatActivity {

    private static final String KEY_QUERY = "query";
    private static final String KEY_SHOW_RESULTS = "show_results";

    private static final int SEARCH_ICON_SIZE_DP = 30;

    private EditText editQuery;
    private ImageButton btnSearch;
    private RecyclerView recycler;
    private boolean showResults;

    private Model model = Model.instance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.search);

        initViews();
        restoreState(savedInstanceState);

        if (showResults) {
            search(editQuery.getText().toString());
        } else {
            recycler.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KEY_QUERY, editQuery.getText().toString());
        outState.putBoolean(KEY_SHOW_RESULTS, showResults);
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        editQuery.setText(savedInstanceState.getString(KEY_QUERY));
        showResults = savedInstanceState.getBoolean(KEY_SHOW_RESULTS);
    }

    /**
     * Initializes all of the views that need to be accessed
     */
    private void initViews() {
        editQuery = findViewById(R.id.search_query);
        btnSearch = findViewById(R.id.search_btn);
        recycler = findViewById(R.id.search_recycler);

        editQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search(editQuery.getText().toString());

                return true;
            }
        });

        btnSearch.setImageDrawable(ResourceGenerator
                .searchIcon(this, SEARCH_ICON_SIZE_DP, R.color.colorAccent));
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(editQuery.getText().toString());
            }
        });
    }

    /**
     * Performs a search and displays the results
     * @param query the search query
     */
    private void search(String query) {
        List<Object> results = model.searchPersonsAndEvents(query);

        displaySearchResults(results);
    }

    /**
     * Displays the search results on the Activity
     * @param results the results (Persons and Events) to be displayed
     */
    private void displaySearchResults(List<Object> results) {
        showResults = true;

        recycler.setVisibility(View.VISIBLE);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new SearchResultAdapter(results));
    }
}
