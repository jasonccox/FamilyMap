package com.jasoncarloscox.familymap.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Model;
import com.jasoncarloscox.familymap.model.Person;

public class MainActivity extends AppCompatActivity
        implements LoginFragment.Listener{

    private LoginFragment loginFragment;
    private MapFragment mapFragment;

    private Model model = Model.instance();

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

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof LoginFragment) {
            ((LoginFragment) fragment).setListener(this);
        }
    }

    @Override
    public void onLoginComplete() {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(loginFragment);

        mapFragment = MapFragment.newInstance(null);
        transaction.add(R.id.main_frame, mapFragment);

        transaction.commit();

        showWelcome();
    }

    /**
     * Shows a welcome message to the logged in user
     */
    private void showWelcome() {
        Person userPerson = model.getPerson(model.getUser().getPersonId());

        String msg = getString(R.string.welcome) + " " + userPerson.getFirstName() +
                " " + userPerson.getLastName();

        Toast toast = Toast.makeText(getApplicationContext(), msg,
                Toast.LENGTH_LONG);
        toast.show();
    }
}
