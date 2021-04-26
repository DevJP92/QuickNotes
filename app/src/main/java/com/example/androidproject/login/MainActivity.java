package com.example.androidproject.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.R;
import com.example.androidproject.note.NoteActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView textViewRegister, textViewForgotPw;
    private EditText editTextEmail, editTextPassword;
    private Button loginButton;
    private ProgressBar progressBar;
    private CheckBox checkBox;

    // SharedPreferences
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        textViewRegister = findViewById(R.id.textViewRegister);
        textViewForgotPw = findViewById(R.id.textViewForgotPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);
        checkBox = findViewById(R.id.checkBoxUserID);

        loginButton.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);
        textViewForgotPw.setOnClickListener(this);

        // SharedPreferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
        checkSharedPreferences();
    }

    // check shared preferences, and set email, checkbox accordingly
    private void checkSharedPreferences(){
        // SharedPreferences: get boolean checkbox, false as default
        String cbox = mSharedPreferences.getString(getString(R.string.checkbox), "False");
        // SharedPreferences: get ID, empty string as default
        String email = mSharedPreferences.getString(getString(R.string.id), "");
        editTextEmail.setText(email);

        if(cbox.equals("True")){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }
    }

    // switch onClickListener
    @Override
    public void onClick(View v){
        switch (v.getId()){
            // Start RegisterUserActivity to register a user
            case R.id.textViewRegister:
                startActivity(new Intent(this, RegisterUserActivity.class));
                break;
            // Call userLogin()
            case R.id.buttonLogin:
                userLogin();
                break;
            // Start ForgotPasswordActivity to reset password
            case R.id.textViewForgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;

        }
    }

    // Validate user account, then sign in with email and password
    private void userLogin(){

        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

//        final String email = "heeju.park1221@gmail.com";
//        final String password = "123456";


        // Validating user's input when login
        if(email.isEmpty()){
            editTextEmail.setError("Username is required.");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email.");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }

        // SharedPreferences
        // Save email(username) when checkbox is checked
        if(checkBox.isChecked()){
            // Set checkbox as true and save
            mEditor.putString(getString(R.string.checkbox), "True");
            mEditor.commit();

            // Save email address
            mEditor.putString(getString(R.string.id), email);
            mEditor.commit();
        }else {
            // Set checkbox as false and save
            mEditor.putString(getString(R.string.checkbox), "False");
            mEditor.commit();

            // Set email address as empty string and save
            mEditor.putString(getString(R.string.id), "");
            mEditor.commit();
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            // if user's email is verified, start NoteActivity
                            if(user.isEmailVerified()){
                                Log.w("MYDEBUG", "Login Successful");
                                Intent i = NoteActivity.createIntent(MainActivity.this);
                                startActivity(i);
                                progressBar.setVisibility(View.GONE);
                            }else {
                                // if user's email is not verified, show toast msg
                                user.sendEmailVerification();
                                Toast.makeText(MainActivity.this, "Check your email to verify your account.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        } else {
                            // if task is not successful, show toast msg
                            Log.w("MYDEBUG","Login Unsuccessful");
                            Toast.makeText(MainActivity.this, "Failed to login. Please try again.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


}