package com.jasoncarloscox.familymap.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Model;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity
        implements LoginFragment.Listener{

    private static boolean showingLogin = true;

    private LoginFragment loginFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        if (showingLogin) {
            showLogin();
        } else {
            showMap();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof LoginFragment) {
            ((LoginFragment) fragment).setListener(this);
        }
    }

    @Override
    public void onLoginComplete() {
        showMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!showingLogin) {
            getMenuInflater().inflate(R.menu.menu_main, menu);

            menu.findItem(R.id.main_menu_search)
                    .setIcon(new IconDrawable(this, FontAwesomeIcons.fa_search)
                            .colorRes(R.color.colorBackground)
                            .actionBarSize());

            menu.findItem(R.id.main_menu_filter)
                    .setIcon(new IconDrawable(this, FontAwesomeIcons.fa_filter)
                            .colorRes(R.color.colorBackground)
                            .actionBarSize());

            menu.findItem(R.id.main_menu_settings)
                    .setIcon(new IconDrawable(this, FontAwesomeIcons.fa_gear)
                        .colorRes(R.color.colorBackground)
                        .actionBarSize());
        }

        return true;
    }

    private void showLogin() {
        showingLogin = true;

        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        Fragment existingFragment = fm.findFragmentById(R.id.main_frame);

        if (existingFragment instanceof LoginFragment) {
            transaction.commit();
            return;
        } else if (existingFragment instanceof MapFragment) {
            transaction.remove(existingFragment);
            mapFragment = null;
        }

        loginFragment = LoginFragment.newInstance();
        transaction.add(R.id.main_frame, loginFragment);
        transaction.commit();

        invalidateOptionsMenu();
    }

    private void showMap() {
        showingLogin = false;

        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        Fragment existingFragment = fm.findFragmentById(R.id.main_frame);

        if (existingFragment instanceof MapFragment) {
            transaction.commit();
            return;
        } else if (existingFragment instanceof LoginFragment) {
            transaction.remove(existingFragment);
            loginFragment = null;
        }

        mapFragment = MapFragment.newInstance(null);
        transaction.add(R.id.main_frame, mapFragment);
        transaction.commit();

        invalidateOptionsMenu();
    }
}
