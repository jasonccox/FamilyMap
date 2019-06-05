package com.jasoncarloscox.familymap.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.jasoncarloscox.familymap.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // keys for saving state on destroy/create
    private static final String KEY_HOST = "host";
    private static final String KEY_PORT = "port";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CONFIRM_PASSWORD = "confirmpassword";
    private static final String KEY_FIRST_NAME = "firstname";
    private static final String KEY_LAST_NAME = "lastname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MALE = "male";
    private static final String KEY_FEMALE = "female";
    private static final String KEY_REGISTER = "register";

    // editable fields
    private EditText editHost;
    private EditText editPort;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private RadioButton radioMale;
    private RadioButton radioFemale;

    // rows that hide/show based on whether user is registering
    private TableRow rowConfirmPassword;
    private TableRow rowFirstName;
    private TableRow rowLastName;
    private TableRow rowEmail;
    private TableRow rowGender;

    // other UI elements
    private TextView txtToggleRegister;
    private Button btnLogin;

    /** Whether the user is logging in (false) or registering (true) */
    private boolean register = false;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initComponents(view);
        restoreState(savedInstanceState);
        toggleRegister(register);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(KEY_HOST, editHost.getText().toString());
        savedInstanceState.putString(KEY_PORT, editPort.getText().toString());
        savedInstanceState.putString(KEY_USERNAME, editUsername.getText().toString());
        savedInstanceState.putString(KEY_PASSWORD, editPassword.getText().toString());
        savedInstanceState.putString(KEY_CONFIRM_PASSWORD, editConfirmPassword.getText().toString());
        savedInstanceState.putString(KEY_FIRST_NAME, editFirstName.getText().toString());
        savedInstanceState.putString(KEY_LAST_NAME, editLastName.getText().toString());
        savedInstanceState.putString(KEY_EMAIL, editEmail.getText().toString());
        savedInstanceState.putBoolean(KEY_MALE, radioMale.hasSelection());
        savedInstanceState.putBoolean(KEY_FEMALE, radioFemale.hasSelection());
        savedInstanceState.putBoolean(KEY_REGISTER, register);
    }

    /**
     * Initializes all of the components that need to be accessed by grabbing
     * them from the view.
     *
     * @param view the View containing all the components
     */
    private void initComponents(View view) {
        editHost = (EditText) view.findViewById(R.id.login_edit_host);
        editPort = (EditText) view.findViewById(R.id.login_edit_port);
        editUsername = (EditText) view.findViewById(R.id.login_edit_username);
        editPassword = (EditText) view.findViewById(R.id.login_edit_password);
        editConfirmPassword = (EditText) view.findViewById(R.id.login_edit_confirm_password);
        editFirstName = (EditText) view.findViewById(R.id.login_edit_first_name);
        editLastName = (EditText) view.findViewById(R.id.login_edit_last_name);
        editEmail = (EditText) view.findViewById(R.id.login_edit_email);
        radioMale = (RadioButton) view.findViewById(R.id.login_radio_male);
        radioFemale = (RadioButton) view.findViewById(R.id.login_radio_female);

        rowConfirmPassword = (TableRow) view.findViewById(R.id.login_row_confirm_password);
        rowFirstName = (TableRow) view.findViewById(R.id.login_row_first_name);
        rowLastName = (TableRow) view.findViewById(R.id.login_row_last_name);
        rowEmail = (TableRow) view.findViewById(R.id.login_row_email);
        rowGender = (TableRow) view.findViewById(R.id.login_row_gender);

        txtToggleRegister = (TextView) view.findViewById(R.id.login_txt_toggle_register);
        btnLogin = (Button) view.findViewById(R.id.login_btn_login);
    }

    /**
     * Restores the state of the login fragment.
     *
     * @param savedInstanceState a Bundle containing the saved state
     */
    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        editHost.setText(savedInstanceState.getString(KEY_HOST));
        editPort.setText(savedInstanceState.getString(KEY_PORT));
        editUsername.setText(savedInstanceState.getString(KEY_USERNAME));
        editPassword.setText(savedInstanceState.getString(KEY_PASSWORD));
        editConfirmPassword.setText(savedInstanceState.getString(KEY_CONFIRM_PASSWORD));
        editFirstName.setText(savedInstanceState.getString(KEY_FIRST_NAME));
        editLastName.setText(savedInstanceState.getString(KEY_LAST_NAME));
        editEmail.setText(savedInstanceState.getString(KEY_EMAIL));
        radioMale.setSelected(savedInstanceState.getBoolean(KEY_MALE));
        radioFemale.setSelected(savedInstanceState.getBoolean(KEY_FEMALE));

        register = savedInstanceState.getBoolean(KEY_REGISTER);
    }

    /**
     * Adjusts the UI based on whether the user is logging in or registering
     *
     * @param register whether the user is logging in (false) or registering (true)
     */
    private void toggleRegister(boolean register) {
        int visibility = register ? View.VISIBLE : View.GONE;

        rowConfirmPassword.setVisibility(visibility);
        rowFirstName.setVisibility(visibility);
        rowLastName.setVisibility(visibility);
        rowEmail.setVisibility(visibility);
        rowGender.setVisibility(visibility);

        if (register) {
            txtToggleRegister.setText(R.string.already_registered);
            btnLogin.setText(R.string.register);

            txtToggleRegister.setOnClickListener(new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleRegister(false);
                }
            });

        } else {
            txtToggleRegister.setText(R.string.not_registered);
            btnLogin.setText(R.string.login);

            txtToggleRegister.setOnClickListener(new TextView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleRegister(true);
                }
            });
        }

        // TODO: add login button click listener

        this.register = register;
    }
}
