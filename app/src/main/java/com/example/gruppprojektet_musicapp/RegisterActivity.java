package com.example.gruppprojektet_musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.example.gruppprojektet_musicapp.Database.InputValid;
import com.example.gruppprojektet_musicapp.Database.Database;
import com.example.gruppprojektet_musicapp.Database.User;


//Thanks to http://www.androidtutorialshub.com/android-login-and-register-with-sqlite-database-tutorial/ for some of the code

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;
    private NestedScrollView nestedScrollView;
    private TextInputLayout layoutName;
    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPassword;
    private TextInputLayout layoutConfirmPassword;
    private TextInputLayout layoutAddres;
    private TextInputLayout layoutCity;
    private TextInputLayout layoutPostalcode;
    private TextInputLayout layoutAge;
    private TextInputEditText inputName;
    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;
    private TextInputEditText inputConfirmPassword;
    private TextInputEditText inputAdress;
    private TextInputEditText inputCity;
    private TextInputEditText inputPostalcode;
    private TextInputEditText inputAge;
    AppCompatSpinner dropdownGender;
    private InputValid inputValidation;
    private Database databaseHelper;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        init();
    }

    private void init() {
        nestedScrollView = findViewById(R.id.nestedScrollView);

        layoutName = findViewById(R.id.textInputLayoutName);
        layoutEmail =  findViewById(R.id.textInputLayoutEmail);
        layoutPassword = findViewById(R.id.textInputLayoutPassword);
        layoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);
        layoutAddres = findViewById(R.id.textInputLayoutAdress);
        layoutCity = findViewById(R.id.textInputLayoutCity);
        layoutAge = findViewById(R.id.textInputLayoutAge);
        layoutPostalcode = findViewById(R.id.textInputLayoutPostalcode);
        inputName =  findViewById(R.id.textInputEditTextName);
        inputEmail =  findViewById(R.id.textInputEditTextEmail);
        inputPassword =  findViewById(R.id.textInputEditTextPassword);
        inputConfirmPassword =  findViewById(R.id.textInputEditTextConfirmPassword);
        inputAdress = findViewById(R.id.textInputEditTextAdress);
        inputAge = findViewById(R.id.textInputEditTextAge);
        inputCity = findViewById(R.id.textInputEditTextCity);
        inputPostalcode = findViewById(R.id.textInputEditTextPostalcode);
        dropdownGender = findViewById(R.id.InputGender);
        AppCompatButton buttonRegister =  findViewById(R.id.appCompatButtonRegister);
        AppCompatButton backToLgoinButton = findViewById(R.id.appCompatTextViewLoginLink);

        //make gender choice adapter
        String[] items = new String[]{"male", "female", "none"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownGender.setAdapter(adapter);


        buttonRegister.setOnClickListener(this);
        backToLgoinButton.setOnClickListener(this);

        inputValidation = new InputValid(activity);
        databaseHelper = new Database(activity);
        user = new User();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(inputName, layoutName, "Enter Name")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(inputEmail, layoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(inputPassword, layoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(inputPassword, inputConfirmPassword,
                    layoutConfirmPassword, getString(R.string.error_password_match))) {
                return;
        }
        if (!inputValidation.isInputEditTextFilled(inputAdress, layoutAddres, "Enter Addres")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(inputAge, layoutAge, "Type in Age")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(inputCity, layoutCity, "Type in City")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(inputPostalcode, layoutPostalcode, "Type in your Postalcode")) {
            return;
        }

        if (!databaseHelper.checkUser(inputEmail.getText().toString().trim())) {

            user.setName(inputName.getText().toString().trim());
            user.setEmail(inputEmail.getText().toString().trim());
            user.setPassword(inputPassword.getText().toString().trim());
            user.setAdress(inputAdress.getText().toString().trim());
            user.setCity(inputCity.getText().toString().trim());
            user.setDateofbirth(inputAge.getText().toString().trim());
            user.setGender(dropdownGender.getSelectedItem().toString().trim());
            user.setPostalcode(inputPostalcode.getText().toString().trim());
            databaseHelper.addUser(user);
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();

            //send intent back that login was succsefull
            Intent registerdoneintent = new Intent(activity, LoginActivity.class);
            registerdoneintent.putExtra("Register", "Register Complete. Login Above");
            startActivity(registerdoneintent);

        } else {
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }
    }

}
