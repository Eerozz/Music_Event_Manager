package com.example.gruppprojektet_musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.example.gruppprojektet_musicapp.Database.InputValid;
import com.example.gruppprojektet_musicapp.Database.Database;

//Thanks to http://www.androidtutorialshub.com/android-login-and-register-with-sqlite-database-tutorial/ for some of the code

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatActivity activity = LoginActivity.this;

    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPassword;

    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;

    private InputValid inputValid;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        init();
    }

    private void init() {

        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        AppCompatButton buttonLogin = findViewById(R.id.buttonLogin);
        AppCompatButton buttonRegister = findViewById(R.id.LinkRegister);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        database = new Database(activity);
        inputValid = new InputValid(activity);

        //Show succsefull register if directed from register
        if(this.getIntent().getStringExtra("Register") != null) {
            Snackbar.make(findViewById(R.id.nestedScrollView), this.getIntent().getStringExtra("Register"), Snackbar.LENGTH_LONG).show();
        }


    }


   @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                Login();
                break;
            case R.id.LinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }


    private void Login() {
        if (!inputValid.isInputEditTextFilled(inputEmail, layoutEmail, getString(R.string.error_message_email))) {
            return;
        }

        if (!inputValid.isInputEditTextEmail(inputEmail, layoutEmail, getString(R.string.error_message_email))) {
            return;
        }

        if (!inputValid.isInputEditTextFilled(inputPassword, layoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        if (database.checkUser(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim())) {
            Intent accountsIntent = new Intent(activity, MainActivity.class);
            accountsIntent.putExtra("EMAIL", inputEmail.getText().toString().trim());
            startActivity(accountsIntent);
        }

        else {
            Snackbar.make(findViewById(R.id.nestedScrollView), getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }

    }
}