package com.jasoncarloscox.familymap.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.LineSetting;

/**
 * Custom compound view containing all the views needed to display the settings
 * for a line type.
 */
public class LineSettingsView extends LinearLayout {

    // views
    private TextView txtLabel;
    private TextView txtDescription;
    private Switch switchSetting;
    private Spinner spinnerColor;

    // member variables
    private LineSetting lineSetting;

    public LineSettingsView(Context context) {
        super(context);
        inflate();
        initViews();
    }

    public LineSettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate();
        initViews();
    }

    public LineSettingsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate();
        initViews();
    }

    /**
     * Sets the child views to display the necessary information and perform any
     * needed actions for the type of line it represents.
     *
     * @param lineSetting the LineSetting object containing all the data about
     *                    the type of line, its color, and whether it is enabled
     */
    public void setLineSetting(final LineSetting lineSetting) {
        this.lineSetting = lineSetting;

        txtLabel.setText(getResources().getString(R.string.line_settings_label,
                                                  lineSetting.getType()));

        switchSetting.setChecked(lineSetting.isInitialVisibility());
        switchSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lineSetting.getListener().onVisibilityChanged(isChecked);
                spinnerColor.setEnabled(isChecked);
                toggleDescriptionText(isChecked);
            }
        });

        toggleDescriptionText(lineSetting.isInitialVisibility());

        if (lineSetting.isInitialVisibility()) {
            txtDescription.setText(getResources().getString(
                    R.string.line_settings_description_show,
                    lineSetting.getDescription()));
        } else {
            txtDescription.setText(getResources().getString(
                    R.string.line_settings_description_hide,
                    lineSetting.getDescription()));
        }

        spinnerColor.setEnabled(lineSetting.isInitialVisibility());
        spinnerColor.setSelection(lineSetting.getInitialColor().ordinal());
        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lineSetting.getListener().onColorSelected(LineSetting.Color.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Inflates the view
     */
    private void inflate() {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_line_settings, this);
    }

    /**
     * Initializes all of the views that need to be accessed
     */
    private void initViews() {
        txtLabel = findViewById(R.id.line_settings_label);
        txtDescription = findViewById(R.id.line_settings_description);
        switchSetting = findViewById(R.id.line_settings_switch);
        spinnerColor = findViewById(R.id.line_settings_color_spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.colors_array,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(spinnerAdapter);
    }

    /**
     * Modifies the description text based on whether the line type is currently
     * enabled or not
     *
     * @param enabled whether the line type is currently enabled
     */
    private void toggleDescriptionText(boolean enabled) {
        if (enabled) {
            txtDescription.setText(getResources().getString(
                    R.string.line_settings_description_show,
                    lineSetting.getDescription()));
        } else {
            txtDescription.setText(getResources().getString(
                    R.string.line_settings_description_hide,
                    lineSetting.getDescription()));
        }
    }
}
