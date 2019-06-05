package com.jasoncarloscox.familymap.view;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jasoncarloscox.familymap.R;

public class MainActivity extends AppCompatActivity {

    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();
        loginFragment = (LoginFragment) fm.findFragmentById(R.id.main_frame);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            fm.beginTransaction().add(R.id.main_frame, loginFragment).commit();
        }
    }

}
