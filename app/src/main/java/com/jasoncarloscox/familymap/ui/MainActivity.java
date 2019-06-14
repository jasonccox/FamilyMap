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
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.logging.Filter;

public class MainActivity extends AppCompatActivity
        implements LoginFragment.Listener{

    public static final String KEY_NEEDS_REFRESH = "needs_refresh";

    private LoginFragment loginFragment;
    private MapFragment mapFragment;

    private Model model = Model.instance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getBooleanExtra(KEY_NEEDS_REFRESH, false)) {
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public void onLoginComplete() {
        showMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (model.isLoggedIn()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);

            menu.findItem(R.id.main_menu_search)
                    .setIcon(new IconDrawable(this, FontAwesomeIcons.fa_search)
                            .colorRes(R.color.colorBackground)
                            .actionBarSize());

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

        item.setIcon(new IconDrawable(this, icon)
                .colorRes(R.color.colorBackground)
                .actionBarSize());

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, activityToStart);
                startActivityForResult(intent, 0);

                return true;
            }
        });
    }
}
