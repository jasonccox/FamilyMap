package com.jasoncarloscox.familymap.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Gender;
import com.jasoncarloscox.familymap.model.Model;
import com.jasoncarloscox.familymap.model.Person;
import com.jasoncarloscox.familymap.model.User;
import com.jasoncarloscox.familymap.server.GetDataTask;
import com.jasoncarloscox.familymap.server.LoginTask;
import com.jasoncarloscox.familymap.server.RegisterTask;
import com.jasoncarloscox.familymap.server.ServerProxy;
import com.jasoncarloscox.familymap.server.request.DataRequest;
import com.jasoncarloscox.familymap.server.request.LoginRequest;
import com.jasoncarloscox.familymap.server.request.RegisterRequest;
import com.jasoncarloscox.familymap.server.result.ApiResult;
import com.jasoncarloscox.familymap.server.result.LoginResult;

/**
 * A fragment allowing users to login/register.
 *
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements LoginTask.Context,
        RegisterTask.Context, GetDataTask.Context {

    public interface Listener {

        /**
         * Called when the user has been logged in and this fragment can be
         * removed.
         */
        void onLoginComplete();
    }

    private static final String TAG = "LoginFragment";

    // keys for saving state on destroy/create
    private static final String KEY_HOST = "host";
    private static final String KEY_PORT = "port";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CONFIRM_PASSWORD = "confirm_password";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MALE = "male";
    private static final String KEY_FEMALE = "female";
    private static final String KEY_REGISTER = "register";
    private static final String KEY_VALID_HOST = "valid_host";
    private static final String KEY_VALID_PORT = "valid_port";
    private static final String KEY_VALID_USERNAME = "valid_username";
    private static final String KEY_VALID_PASSWORD = "valid_password";
    private static final String KEY_VALID_FIRST_NAME = "valid_first_name";
    private static final String KEY_VALID_LAST_NAME = "valid_last_name";
    private static final String KEY_VALID_EMAIL = "valid_email";
    private static final String KEY_VALID_GENDER = "valid_gender";
    private static final String KEY_VISIBILITY_ERROR_HOST = "visibility_error_host";
    private static final String KEY_VISIBILITY_ERROR_PORT = "visibility_error_port";
    private static final String KEY_VISIBILITY_ERROR_USERNAME = "visibility_error_username";
    private static final String KEY_VISIBILITY_ERROR_PASSWORD = "visibility_error_password";
    private static final String KEY_VISIBILITY_ERROR_CONFIRM_PASSWORD = "visibility_error_confirm_password";
    private static final String KEY_VISIBILITY_ERROR_FIRST_NAME = "visibility_error_first_name";
    private static final String KEY_VISIBILITY_ERROR_LAST_NAME = "visibility_error_last_name";
    private static final String KEY_VISIBILITY_ERROR_EMAIL = "visibility_error_email";
    private static final String KEY_VISIBILITY_ERROR_GENDER = "visibility_error_gender";
    private static final String KEY_MSG_ERROR_HOST = "msg_error_host";
    private static final String KEY_MSG_ERROR_PORT = "msg_error_port";
    private static final String KEY_MSG_ERROR_USERNAME = "msg_error_username";
    private static final String KEY_MSG_ERROR_PASSWORD = "msg_error_password";
    private static final String KEY_MSG_ERROR_CONFIRM_PASSWORD = "msg_error_confirm_password";
    private static final String KEY_MSG_ERROR_FIRST_NAME = "msg_error_first_name";
    private static final String KEY_MSG_ERROR_LAST_NAME = "msg_error_last_name";
    private static final String KEY_MSG_ERROR_EMAIL = "msg_error_email";
    private static final String KEY_MSG_ERROR_GENDER = "msg_error_gender";

    // editable fields
    private EditText editHost;
    private EditText editPort;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private RadioGroup radioGrpGender;
    private RadioButton radioMale;
    private RadioButton radioFemale;

    // error display labels
    private TextView txtErrorHost;
    private TextView txtErrorPort;
    private TextView txtErrorUsername;
    private TextView txtErrorPassword;
    private TextView txtErrorConfirmPassword;
    private TextView txtErrorFirstName;
    private TextView txtErrorLastName;
    private TextView txtErrorEmail;
    private TextView txtErrorGender;

    // rows that hide/shouldShow based on whether user is registering
    private TableRow rowConfirmPassword;
    private TableRow rowFirstName;
    private TableRow rowLastName;
    private TableRow rowEmail;
    private TableRow rowGender;

    // other UI elements
    private TextView txtToggleRegister;
    private Button btnLogin;
    private ProgressBar progressSpinner;

    // member variables
    private boolean register = false; // logging in = false, registering = true
    private Model model = Model.instance();
    private User user;
    private Listener listener;

    // keeping track of form validity
    private boolean hostValid = false;
    private boolean portValid = false;
    private boolean usernameValid = false;
    private boolean passwordValid = false;
    private boolean firstNameValid = false;
    private boolean lastNameValid = false;
    private boolean emailValid = false;
    private boolean genderValid = false;


    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
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
        setRegister(register);

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

        savedInstanceState.putBoolean(KEY_VALID_HOST, hostValid);
        savedInstanceState.putBoolean(KEY_VALID_PORT, portValid);
        savedInstanceState.putBoolean(KEY_VALID_USERNAME, usernameValid);
        savedInstanceState.putBoolean(KEY_VALID_PASSWORD, passwordValid);
        savedInstanceState.putBoolean(KEY_VALID_FIRST_NAME, firstNameValid);
        savedInstanceState.putBoolean(KEY_VALID_LAST_NAME, lastNameValid);
        savedInstanceState.putBoolean(KEY_VALID_EMAIL, emailValid);
        savedInstanceState.putBoolean(KEY_VALID_GENDER, genderValid);
        
        savedInstanceState.putInt(KEY_VISIBILITY_ERROR_HOST, txtErrorHost.getVisibility());
        savedInstanceState.putInt(KEY_VISIBILITY_ERROR_PORT, txtErrorPort.getVisibility());
        savedInstanceState.putInt(KEY_VISIBILITY_ERROR_USERNAME, txtErrorUsername.getVisibility());
        savedInstanceState.putInt(KEY_VISIBILITY_ERROR_PASSWORD, txtErrorPassword.getVisibility());
        savedInstanceState.putInt(KEY_VISIBILITY_ERROR_CONFIRM_PASSWORD, txtErrorConfirmPassword.getVisibility());
        savedInstanceState.putInt(KEY_VISIBILITY_ERROR_FIRST_NAME, txtErrorFirstName.getVisibility());
        savedInstanceState.putInt(KEY_VISIBILITY_ERROR_LAST_NAME, txtErrorLastName.getVisibility());
        savedInstanceState.putInt(KEY_VISIBILITY_ERROR_EMAIL, txtErrorEmail.getVisibility());
        savedInstanceState.putInt(KEY_VISIBILITY_ERROR_GENDER, txtErrorGender.getVisibility());
        
        savedInstanceState.putString(KEY_MSG_ERROR_HOST, txtErrorHost.getText().toString());
        savedInstanceState.putString(KEY_MSG_ERROR_PORT, txtErrorPort.getText().toString());
        savedInstanceState.putString(KEY_MSG_ERROR_USERNAME, txtErrorUsername.getText().toString());
        savedInstanceState.putString(KEY_MSG_ERROR_PASSWORD, txtErrorPassword.getText().toString());
        savedInstanceState.putString(KEY_MSG_ERROR_CONFIRM_PASSWORD, txtErrorConfirmPassword.getText().toString());
        savedInstanceState.putString(KEY_MSG_ERROR_FIRST_NAME, txtErrorFirstName.getText().toString());
        savedInstanceState.putString(KEY_MSG_ERROR_LAST_NAME, txtErrorLastName.getText().toString());
        savedInstanceState.putString(KEY_MSG_ERROR_EMAIL, txtErrorEmail.getText().toString());
        savedInstanceState.putString(KEY_MSG_ERROR_GENDER, txtErrorGender.getText().toString());
    }

    /**
     * Called when a GetDataRequest completes
     *
     * @param result the result of getting data
     */
    @Override
    public void onGetDataComplete(ApiResult result) {
        if (result.isSuccess()) {
            showProgressSpinner(false);
            showWelcome();
            listener.onLoginComplete();
        } else {
            Log.e(TAG, "Failed to fetch data: " + result.getMessage());
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.data_request_failed,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called when a LoginTask completes
     *
     * @param result the result of logging in
     */
    @Override
    public void onLoginComplete(LoginResult result) {
        if (result.isSuccess()) {
            handleLoginSuccess(result);
        } else {
            handleLoginFailure(result);
        }
    }

    /**
     * Called when a RegisterTask completes
     *
     * @param result the result of registering
     */
    @Override
    public void onRegisterComplete(LoginResult result) {
        if (result.isSuccess()) {
            handleLoginSuccess(result);
        } else {
            handleRegisterFailure(result);
        }
    }

    /**
     * Initializes all of the components that need to be accessed by grabbing
     * them from the view and adding necessary listeners.
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
        radioGrpGender = (RadioGroup) view.findViewById(R.id.login_radiogrp_gender);
        radioMale = (RadioButton) view.findViewById(R.id.login_radio_male);
        radioFemale = (RadioButton) view.findViewById(R.id.login_radio_female);

        txtErrorHost = (TextView) view.findViewById(R.id.login_txt_error_host);
        txtErrorPort = (TextView) view.findViewById(R.id.login_txt_error_port);
        txtErrorUsername = (TextView) view.findViewById(R.id.login_txt_error_username);
        txtErrorPassword = (TextView) view.findViewById(R.id.login_txt_error_password);
        txtErrorConfirmPassword = (TextView) view.findViewById(R.id.login_txt_error_confirm_password);
        txtErrorFirstName = (TextView) view.findViewById(R.id.login_txt_error_first_name);
        txtErrorLastName = (TextView) view.findViewById(R.id.login_txt_error_last_name);
        txtErrorEmail = (TextView) view.findViewById(R.id.login_txt_error_email);
        txtErrorGender = (TextView) view.findViewById(R.id.login_txt_error_gender);

        rowConfirmPassword = (TableRow) view.findViewById(R.id.login_row_confirm_password);
        rowFirstName = (TableRow) view.findViewById(R.id.login_row_first_name);
        rowLastName = (TableRow) view.findViewById(R.id.login_row_last_name);
        rowEmail = (TableRow) view.findViewById(R.id.login_row_email);
        rowGender = (TableRow) view.findViewById(R.id.login_row_gender);

        txtToggleRegister = (TextView) view.findViewById(R.id.login_txt_toggle_register);
        btnLogin = (Button) view.findViewById(R.id.login_btn_login);
        progressSpinner = (ProgressBar) view.findViewById(R.id.login_progress_spinner);

        txtToggleRegister.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRegister(!register);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnClick();
            }
        });

        addValidateListeners();
    }

    /**
     * Adds listeners to input fields to validate them and enable/disable the
     * login/register button based on validation.
     */
    private void addValidateListeners() {
        editHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 0) {
                    return;
                }
                onEditHostInteraction();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editHost.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onEditHostInteraction();
                }
            }
        });

        editPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 0) {
                    return;
                }
                onEditPortInteraction();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editPort.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onEditPortInteraction();
                }
            }
        });

        editUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 0) {
                    return;
                }
                onEditUsernameInteraction();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onEditUsernameInteraction();
                }
            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 0) {
                    return;
                }
                onEditPasswordInteraction();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onEditPasswordInteraction();
                }
            }
        });

        editConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 0) {
                    return;
                }
                onEditConfirmPasswordInteraction();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onEditConfirmPasswordInteraction();
                }
            }
        });

        editFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 0) {
                    return;
                }
                onEditFirstNameInteraction();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onEditFirstNameInteraction();
                }
            }
        });

        editLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 0) {
                    return;
                }
                onEditLastNameInteraction();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onEditLastNameInteraction();
                }
            }
        });

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && before == 0) {
                    return;
                }
                onEditEmailInteraction();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    onEditEmailInteraction();
                }
            }
        });

        radioGrpGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRadioGrpGenderInteraction();
            }
        });

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

        hostValid = savedInstanceState.getBoolean(KEY_VALID_HOST);
        portValid = savedInstanceState.getBoolean(KEY_VALID_PORT);
        usernameValid = savedInstanceState.getBoolean(KEY_VALID_USERNAME);
        passwordValid = savedInstanceState.getBoolean(KEY_VALID_PASSWORD);
        firstNameValid = savedInstanceState.getBoolean(KEY_VALID_FIRST_NAME);
        lastNameValid = savedInstanceState.getBoolean(KEY_VALID_LAST_NAME);
        emailValid = savedInstanceState.getBoolean(KEY_VALID_EMAIL);
        genderValid = savedInstanceState.getBoolean(KEY_VALID_GENDER);

        txtErrorHost.setVisibility(savedInstanceState.getInt(KEY_VISIBILITY_ERROR_HOST));
        txtErrorPort.setVisibility(savedInstanceState.getInt(KEY_VISIBILITY_ERROR_PORT));
        txtErrorUsername.setVisibility(savedInstanceState.getInt(KEY_VISIBILITY_ERROR_USERNAME));
        txtErrorPassword.setVisibility(savedInstanceState.getInt(KEY_VISIBILITY_ERROR_PASSWORD));
        txtErrorConfirmPassword.setVisibility(savedInstanceState.getInt(KEY_VISIBILITY_ERROR_CONFIRM_PASSWORD));
        txtErrorFirstName.setVisibility(savedInstanceState.getInt(KEY_VISIBILITY_ERROR_FIRST_NAME));
        txtErrorLastName.setVisibility(savedInstanceState.getInt(KEY_VISIBILITY_ERROR_LAST_NAME));
        txtErrorEmail.setVisibility(savedInstanceState.getInt(KEY_VISIBILITY_ERROR_EMAIL));
        txtErrorGender.setVisibility(savedInstanceState.getInt(KEY_VISIBILITY_ERROR_GENDER));
        
        txtErrorHost.setText(savedInstanceState.getString(KEY_MSG_ERROR_HOST));
        txtErrorPort.setText(savedInstanceState.getString(KEY_MSG_ERROR_PORT));
        txtErrorUsername.setText(savedInstanceState.getString(KEY_MSG_ERROR_USERNAME));
        txtErrorPassword.setText(savedInstanceState.getString(KEY_MSG_ERROR_PASSWORD));
        txtErrorConfirmPassword.setText(savedInstanceState.getString(KEY_MSG_ERROR_CONFIRM_PASSWORD));
        txtErrorFirstName.setText(savedInstanceState.getString(KEY_MSG_ERROR_FIRST_NAME));
        txtErrorLastName.setText(savedInstanceState.getString(KEY_MSG_ERROR_LAST_NAME));
        txtErrorEmail.setText(savedInstanceState.getString(KEY_MSG_ERROR_EMAIL));
        txtErrorGender.setText(savedInstanceState.getString(KEY_MSG_ERROR_GENDER));
    }

    /**
     * Called when a user interacts with the editHost text box
     */
    private void onEditHostInteraction() {
        hostValid = validateHost();
        setLoginBtnEnabled(areFieldsValid());
    }

    /**
     * Called when a user interacts with the editPort text box
     */
    private void onEditPortInteraction() {
        portValid = validatePort();
        setLoginBtnEnabled(areFieldsValid());
    }

    /**
     * Called when a user interacts with the editUsername text box
     */
    private void onEditUsernameInteraction() {
        usernameValid = validateUsername();
        setLoginBtnEnabled(areFieldsValid());
    }

    /**
     * Called when a user interacts with the editPassword text box
     */
    private void onEditPasswordInteraction() {
        passwordValid = validatePassword();
        setLoginBtnEnabled(areFieldsValid());
    }

    /**
     * Called when a user interacts with the editConfirmPassword text box
     */
    private void onEditConfirmPasswordInteraction() {
        passwordValid = validateConfirmPassword();
        setLoginBtnEnabled(areFieldsValid());
    }

    /**
     * Called when a user interacts with the editFirstName text box
     */
    private void onEditFirstNameInteraction() {
        firstNameValid = validateFirstName();
        setLoginBtnEnabled(areFieldsValid());
    }

    /**
     * Called when a user interacts with the editLastName text box
     */
    private void onEditLastNameInteraction() {
        lastNameValid = validateLastName();
        setLoginBtnEnabled(areFieldsValid());
    }

    /**
     * Called when a user interacts with the editEmail text box
     */
    private void onEditEmailInteraction() {
        emailValid = validateEmail();
        setLoginBtnEnabled(areFieldsValid());
    }

    /**
     * Called when a user interacts with the gender radio group
     */
    private void onRadioGrpGenderInteraction() {
        genderValid = validateGender();
        setLoginBtnEnabled(areFieldsValid());
    }

    /**
     * Enables/disables the login button and changes its color.
     * @param enabled whether the button should be enabled
     */
    private void setLoginBtnEnabled(boolean enabled) {
        btnLogin.setEnabled(enabled);
        if (enabled) {
            btnLogin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            btnLogin.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
        }
    }

    /**
     * Adjusts the UI based on whether the user is logging in or registering,
     * and sets the instance variable register.
     *
     * @param register whether the user is logging in (false) or registering (true)
     */
    private void setRegister(boolean register) {
        int visibility = register ? View.VISIBLE : View.GONE;

        // shouldShow or hide register fields
        rowConfirmPassword.setVisibility(visibility);
        rowFirstName.setVisibility(visibility);
        rowLastName.setVisibility(visibility);
        rowEmail.setVisibility(visibility);
        rowGender.setVisibility(visibility);

        // modify button text and text allowing user to toggle login/register
        if (register) {
            txtToggleRegister.setText(R.string.already_registered);
            btnLogin.setText(R.string.register);
        } else {
            txtToggleRegister.setText(R.string.not_registered);
            btnLogin.setText(R.string.login);
        }

        // let the fragment know if it is in register mode or not
        this.register = register;

        setLoginBtnEnabled(areFieldsValid());
    }

    /**
     * @return whether all of the field entries are valid
     */
    private boolean areFieldsValid() {

        boolean valid;

        if (register) {
            valid = hostValid && portValid && usernameValid && passwordValid &&
                    firstNameValid && lastNameValid && emailValid && genderValid;
        } else {
            valid = hostValid && portValid && usernameValid && passwordValid;
        }

        return valid;
    }

    /**
     * Checks if the host entry is valid, showing an error message if needed.
     *
     * @return whether the host entry is valid
     */
    private boolean validateHost() {
        String contents = editHost.getText().toString();

        if (contents.isEmpty()) {
            txtErrorHost.setText(R.string.must_not_be_blank);
            txtErrorHost.setVisibility(View.VISIBLE);
            return false;
        }

        txtErrorHost.setVisibility(View.GONE);
        return true;
    }

    /**
     * Checks if the port entry is valid, showing an error message if needed.
     *
     * @return whether the port entry is valid
     */
    private boolean validatePort() {
        String contents = editPort.getText().toString();

        if (contents.isEmpty()) {
            txtErrorPort.setText(R.string.must_not_be_blank);
            txtErrorPort.setVisibility(View.VISIBLE);
            return false;
        }

        final int MAX_PORT = 65536;

        if (Integer.parseInt(contents) > MAX_PORT) {
            txtErrorPort.setText(R.string.port_invalid);
            txtErrorPort.setVisibility(View.VISIBLE);
            return false;
        }

        txtErrorPort.setVisibility(View.GONE);
        return true;
    }

    /**
     * Checks if the username entry is valid, showing an error message if needed.
     *
     * @return whether the username entry is valid
     */
    private boolean validateUsername() {
        String contents = editUsername.getText().toString();

        if (contents.isEmpty()) {
            txtErrorUsername.setText(R.string.must_not_be_blank);
            txtErrorUsername.setVisibility(View.VISIBLE);
            return false;
        }

        txtErrorUsername.setVisibility(View.GONE);
        return true;
    }

    /**
     * Checks if the password entry is valid, showing an error message if needed.
     *
     * @return whether the password entry is valid
     */
    private boolean validatePassword() {
        String contents = editPassword.getText().toString();

        if (contents.isEmpty()) {
            txtErrorPassword.setText(R.string.must_not_be_blank);
            txtErrorPassword.setVisibility(View.VISIBLE);
            return false;
        } else {
            txtErrorPassword.setVisibility(View.GONE);
        }

        if (register && !contents.equals(editConfirmPassword.getText().toString())) {
            txtErrorConfirmPassword.setText(R.string.password_must_match);
            txtErrorConfirmPassword.setVisibility(View.VISIBLE);
            return false;
        }

        txtErrorConfirmPassword.setVisibility(View.GONE);
        return true;
    }

    /**
     * Checks if the confirm password entry is valid, showing an error message
     * if needed.
     *
     * @return whether the confirm password entry is valid
     */
    private boolean validateConfirmPassword() {
        String contents = editConfirmPassword.getText().toString();

        if (contents.isEmpty()) {
            txtErrorConfirmPassword.setText(R.string.must_not_be_blank);
            txtErrorConfirmPassword.setVisibility(View.VISIBLE);
            return false;
        }

        if (!contents.equals(editPassword.getText().toString())) {
            txtErrorConfirmPassword.setText(R.string.password_must_match);
            txtErrorConfirmPassword.setVisibility(View.VISIBLE);
            return false;
        }

        txtErrorConfirmPassword.setVisibility(View.GONE);
        return true;
    }

    /**
     * Checks if the first name entry is valid, showing an error message if
     * needed.
     *
     * @return whether the first name entry is valid
     */
    private boolean validateFirstName() {
        String contents = editFirstName.getText().toString();

        if (contents.isEmpty()) {
            txtErrorFirstName.setText(R.string.must_not_be_blank);
            txtErrorFirstName.setVisibility(View.VISIBLE);
            return false;
        }

        txtErrorFirstName.setVisibility(View.GONE);
        return true;
    }

    /**
     * Checks if the last name entry is valid, showing an error message if
     * needed.
     *
     * @return whether the last name entry is valid
     */
    private boolean validateLastName() {
        String contents = editLastName.getText().toString();

        if (contents.isEmpty()) {
            txtErrorLastName.setText(R.string.must_not_be_blank);
            txtErrorLastName.setVisibility(View.VISIBLE);
            return false;
        }

        txtErrorLastName.setVisibility(View.GONE);
        return true;
    }

    /**
     * Checks if the email entry is valid, showing an error message if needed.
     *
     * @return whether the email entry is valid
     */
    private boolean validateEmail() {
        String contents = editEmail.getText().toString();

        if (contents.isEmpty()) {
            txtErrorEmail.setText(R.string.must_not_be_blank);
            txtErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }

        // TODO: check for well-formed email address

        txtErrorEmail.setVisibility(View.GONE);
        return true;
    }

    /**
     * Checks if the gender entry is valid, showing an error message if needed.
     *
     * @return whether the gender entry is valid
     */
    private boolean validateGender() {
        if (radioGrpGender.getCheckedRadioButtonId() == -1) {
            txtErrorGender.setText(R.string.must_not_be_blank);
            txtErrorGender.setVisibility(View.VISIBLE);
            return false;
        }

        txtErrorGender.setVisibility(View.GONE);
        return true;
    }

    /**
     * Called when the login/register button is clicked.
     */
    private void onBtnClick() {
        showProgressSpinner(true);

        boolean allFieldsValid = areFieldsValid();

        if (!allFieldsValid) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                    R.string.fix_invalid_fields,
                    Toast.LENGTH_LONG);

            toast.show();

            showProgressSpinner(false);

            return;
        }

        initServer();

        user = generateUser();

        if (register) {
            Log.i(TAG, "Starting RegisterTask");
            new RegisterTask(this).execute(new RegisterRequest(user));
        } else {
            Log.i(TAG, "Starting LoginTask");
            new LoginTask(this).execute(new LoginRequest(user.getUsername(),
                                                         user.getPassword()));
        }
    }

    /**
     * Sets the server host and port.
     */
    private void initServer() {
        ServerProxy.setHost(editHost.getText().toString());
        ServerProxy.setPort(Integer.parseInt(editPort.getText().toString()));
    }

    /**
     * Generates a user based on the entry fields
     *
     * @return the generated user
     */
    private User generateUser() {
        User u = new User(editUsername.getText().toString(),
                          editPassword.getText().toString());

        // don't set the register fields if user isn't registering

        if (!register) {
            return u;
        }

        u.setFirstName(editFirstName.getText().toString());
        u.setLastName(editLastName.getText().toString());
        u.setEmail(editEmail.getText().toString());

        if (radioGrpGender.getCheckedRadioButtonId() == radioMale.getId()) {
            u.setGender(Gender.MALE);
        } else {
            // we can safely assume gender was selected because of validation
            u.setGender(Gender.FEMALE);
        }

        return u;
    }

    /**
     * Processes a successful login result
     *
     * @param result the successful result
     */
    private void handleLoginSuccess(LoginResult result) {
        user.setPersonId(result.getPersonID());
        user.setAuthToken(result.getAuthToken());
        model.login(user);

        Log.i(TAG, "Starting GetDataTask");
        new GetDataTask(this, user.getPersonId())
                .execute(new DataRequest(result.getAuthToken()));
    }

    /**
     * Processes a failed login result
     *
     * @param result the failed result
     */
    private void handleLoginFailure(LoginResult result) {
        int toastMessageId;

        if (result.getMessage().startsWith(LoginResult.USER_NOT_FOUND)){
            toastMessageId = R.string.user_not_found;
        } else if (result.getMessage().startsWith(LoginResult.WRONG_PASSWORD_ERROR)) {
            toastMessageId = R.string.wrong_password;
        } else {
            toastMessageId = R.string.generic_request_failed;
            Log.e(TAG, "Failed to login: " + result.getMessage());
        }

        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                     toastMessageId,
                                     Toast.LENGTH_LONG);
        toast.show();

        showProgressSpinner(false);
    }

    /**
     * Processes a failed register result
     *
     * @param result the failed result
     */
    private void handleRegisterFailure(LoginResult result) {
        int toastMessageId;

        // unfortunately the server doesn't give back a nice predictable prefix
        // on the error message if the username is taken, so more complex
        // matching is needed
        if (result.getMessage().matches("(.*)The username(.*)is already in use(.*)")){
            toastMessageId = R.string.username_taken;
        } else {
            toastMessageId = R.string.generic_request_failed;
            Log.e(TAG, "Failed to login: " + result.getMessage());
        }

        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                     toastMessageId,
                                     Toast.LENGTH_LONG);
        toast.show();

        showProgressSpinner(false);
    }

    /**
     * Shows or hides the progress spinner
     *
     * @param show whether to shouldShow the spinner
     */
    private void showProgressSpinner(boolean show) {
        if (show) {
            btnLogin.setVisibility(View.GONE);
            progressSpinner.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            progressSpinner.setVisibility(View.GONE);
        }
    }

    /**
     * Shows a welcome message to the logged in user
     */
    private void showWelcome() {
        Person userPerson = model.getPerson(model.getUser().getPersonId());

        String msg = getString(R.string.welcome, userPerson.getFirstName(),
                userPerson.getLastName());

        Toast toast = Toast.makeText(getActivity().getApplicationContext(), msg,
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
