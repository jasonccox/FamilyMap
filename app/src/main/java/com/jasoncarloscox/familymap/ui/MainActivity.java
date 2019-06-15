package com.jasoncarloscox.familymap.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Model;
import com.jasoncarloscox.familymap.util.ResourceGenerator;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

public class MainActivity extends AppCompatActivity
        implements LoginFragment.Listener {

    private LoginFragment loginFragment;
    private MapFragment mapFragment;

    private Model model = Model.instance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayFragment();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof LoginFragment) {
            ((LoginFragment) fragment).setListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayFragment();
    }

    @Override
    public void onLoginComplete() {
        displayMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // only display the menu when the MapFragment is showing
        if (model.isLoggedIn()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);

            MenuItem search = menu.findItem(R.id.main_menu_search);
            initMenuItem(FontAwesomeIcons.fa_search, SearchActivity.class, search);

            MenuItem filter = menu.findItem(R.id.main_menu_filter);
            initMenuItem(FontAwesomeIcons.fa_filter, FilterActivity.class, filter);

            MenuItem settings = menu.findItem(R.id.main_menu_settings);
            initMenuItem(FontAwesomeIcons.fa_gear, SettingsActivity.class, settings);
        }

        return true;
    }

    /**
     * Determines which fragment should be shown and displays it
     */
    private void displayFragment() {
        if (model.isLoggedIn()) {
            displayMap();
        } else {
            displayLogin();
        }
    }

    /**
     * Removes the MapFragment (if present) and displays the LoginFragment
     */
    private void displayLogin() {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction;

        Fragment existingFragment = fm.findFragmentById(R.id.main_frame);

        if (existingFragment instanceof LoginFragment) {
            return;
        } else if (existingFragment instanceof MapFragment) {
            transaction = fm.beginTransaction().remove(existingFragment);
            mapFragment = null;
        } else {
            transaction = fm.beginTransaction();
        }

        loginFragment = LoginFragment.newInstance();
        transaction.add(R.id.main_frame, loginFragment);
        transaction.commit();

        invalidateOptionsMenu();
    }

    /**
     * Removes the LoginFragment (if present) and displays the MapFragment
     */
    private void displayMap() {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction;

        Fragment existingFragment = fm.findFragmentById(R.id.main_frame);

        if (existingFragment instanceof MapFragment) {
            return;
        } else if (existingFragment instanceof LoginFragment) {
            transaction = fm.beginTransaction().remove(existingFragment);
            loginFragment = null;
        } else {
            transaction = fm.beginTransaction();
        }

        mapFragment = MapFragment.newInstance(null);
        transaction.add(R.id.main_frame, mapFragment);
        transaction.commit();

        invalidateOptionsMenu();
    }

    /**
     * Adds the icon and click listener to a MenuItem
     *
     * @param icon the icon for the MenuItem
     * @param activityToStart the activity to be started when the MenuItem is
     *                        clicked
     * @param item the MenuItem being initialized
     */
    private void initMenuItem(FontAwesomeIcons icon, final Class activityToStart,
                              MenuItem item) {

        item.setIcon(ResourceGenerator
                .iconActionBar(this, R.color.colorBackground, icon));

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, activityToStart);
                startActivity(intent);

                return true;
            }
        });
    }
}
