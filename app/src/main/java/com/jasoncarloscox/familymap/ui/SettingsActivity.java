package com.jasoncarloscox.familymap.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.EventMap;
import com.jasoncarloscox.familymap.model.LineSetting;
import com.jasoncarloscox.familymap.model.Model;
import com.jasoncarloscox.familymap.server.GetDataTask;
import com.jasoncarloscox.familymap.server.request.DataRequest;
import com.jasoncarloscox.familymap.server.result.ApiResult;

public class SettingsActivity extends AppCompatActivity
        implements GetDataTask.Context{

    private static final String TAG = "SettingsActivity";

    // views
    private LineSettingsView lsvLifeLines;
    private LineSettingsView lsvTreeLines;
    private LineSettingsView lsvSpouseLines;
    private Spinner spinnerMapType;
    private TextView txtSync;
    private TextView txtLogout;
    private ProgressBar progressSpinner;

    // member variables
    private Model model = Model.instance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.settings_activity_title);

        initComponents();
    }

    @Override
    public void onGetDataComplete(ApiResult result) {
        if (result.isSuccess()) {
            progressSpinner.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), R.string.data_request_succeeded,
                    Toast.LENGTH_SHORT).show();

            finish(); // return to MainActivity
        } else {
            Log.e(TAG, "Failed to fetch data: " + result.getMessage());
            Toast.makeText(getApplicationContext(), R.string.data_request_failed,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Initializes all of the components that need to be accessed by grabbing
     * them from the view and adding necessary listeners.
     */
    private void initComponents() {
        lsvLifeLines = findViewById(R.id.settings_life_lines);
        lsvTreeLines = findViewById(R.id.settings_tree_lines);
        lsvSpouseLines = findViewById(R.id.settings_spouse_lines);
        spinnerMapType = findViewById(R.id.settings_map_type_spinner);
        txtSync = findViewById(R.id.settings_sync);
        txtLogout = findViewById(R.id.settings_logout);
        progressSpinner = findViewById(R.id.settings_progress_spinner);

        LineSetting lifeLineSetting = new LineSetting(getString(
                R.string.line_setting_label_life),
                getString(R.string.line_setting_description_life),
                model.shouldShowLifeLines(),
                model.getLifeLineColor());
        lifeLineSetting.setListener(new LineSetting.Listener() {
            @Override
            public void onVisibilityChanged(boolean visible) {
                model.setShouldShowLifeLines(visible);
            }

            @Override
            public void onColorSelected(LineSetting.Color color) {
                model.setLifeLineColor(color);
            }
        });
        lsvLifeLines.setLineSetting(lifeLineSetting);

        LineSetting treeLineSetting = new LineSetting(getString(
                R.string.line_setting_label_tree),
                getString(R.string.line_setting_description_tree),
                model.shouldShowTreeLines(),
                model.getTreeLineColor());
        treeLineSetting.setListener(new LineSetting.Listener() {
            @Override
            public void onVisibilityChanged(boolean visible) {
                model.setShouldShowTreeLines(visible);
            }

            @Override
            public void onColorSelected(LineSetting.Color color) {
                model.setTreeLineColor(color);
            }
        });
        lsvTreeLines.setLineSetting(treeLineSetting);

        LineSetting spouseLineSetting = new LineSetting(getString(
                R.string.line_setting_label_spouse),
                getString(R.string.line_setting_description_spouse),
                model.shouldShowSpouseLines(),
                model.getSpouseLineColor());
        spouseLineSetting.setListener(new LineSetting.Listener() {
            @Override
            public void onVisibilityChanged(boolean visible) {
                model.setShouldShowSpouseLines(visible);
            }

            @Override
            public void onColorSelected(LineSetting.Color color) {
                model.setSpouseLineColor(color);
            }
        });
        lsvSpouseLines.setLineSetting(spouseLineSetting);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.map_types_array,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMapType.setAdapter(spinnerAdapter);
        spinnerMapType.setSelection(model.getMapType().ordinal());
        spinnerMapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model.setMapType(EventMap.Type.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        txtSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressSpinner.setVisibility(View.VISIBLE);

                Log.i(TAG, "Starting GetDataTask");
                new GetDataTask(SettingsActivity.this, model.getUser().getPersonId())
                        .execute(new DataRequest(model.getUser().getAuthToken()));
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.logout();
                finish(); // return to MainActivity
            }
        });
    }
}
