package com.example.selflearning.Activities;

import static com.example.selflearning.Constant.SHARED_PREFS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.selflearning.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private EditText edLoginEmail, edLoginPassword;
    FirebaseAuth mAuth;


    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        rememberMe();
    }

    // if user was loged in and check the 'remember me' checkbox
    private void rememberMe() {
         sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
         String check = sharedPreferences.getString("name", "");
         if (check.equals("true")) {
             Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
             startActivity(intent);
             finish();
         }

    }

    protected void init() {
        edLoginEmail = findViewById(R.id.login_email);
        edLoginPassword = findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        loginFun();
    }

    private void loginFun() {
        String email = edLoginEmail.getText().toString().trim();
        String password = edLoginPassword.getText().toString().trim();

        if(email.isEmpty()) {
            edLoginEmail.setError("Необходимо ввести email");
            edLoginEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edLoginEmail.setError("Введите корректный email");
            edLoginEmail.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            edLoginPassword.setError("Необходимо ввести пароль");
            edLoginPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                // for remember me and remembering user
                sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", "true");
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(LoginActivity.this, "Вход не удался. Проверьте данные", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
