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

    public static final String KEY_NEEDS_REFRESH = "needs_refresh";

    private LoginFragment loginFragment;
    private MapFragment mapFragment;

    private Model model = Model.instance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
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

        initFragments();
    }

    @Override
    public void onLoginComplete() {
        showMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (model.isLoggedIn()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);

            MenuItem search = menu.findItem(R.id.main_menu_search);
            initMenuItem(search, FontAwesomeIcons.fa_search, SearchActivity.class);

            MenuItem filter = menu.findItem(R.id.main_menu_filter);
            initMenuItem(filter, FontAwesomeIcons.fa_filter, FilterActivity.class);

            MenuItem settings = menu.findItem(R.id.main_menu_settings);
            initMenuItem(settings, FontAwesomeIcons.fa_gear, SettingsActivity.class);
        }

        return true;
    }

    private void initFragments() {
        if (model.isLoggedIn()) {
            showMap();
        } else {
            showLogin();
        }
    }

    private void showLogin() {
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

    private void showMap() {
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

    private void initMenuItem(MenuItem item, FontAwesomeIcons icon,
                              final Class activityToStart) {

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
