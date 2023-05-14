package com.example.selflearning.Activities;

import static com.example.selflearning.Constant.SHARED_PREFS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.selflearning.DBobjects.User;
import com.example.selflearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edSignUpName, edSignUpEmail, edSignUpUsername, edSignUpPassword;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    protected void init() {
        mAuth = FirebaseAuth.getInstance();
        edSignUpName = findViewById(R.id.signup_name);
        edSignUpEmail = findViewById(R.id.signup_email);
        edSignUpUsername = findViewById(R.id.signup_username);
        edSignUpPassword = findViewById(R.id.signup_password);
    }

    public void signUp(View view) {
        signUpUser();
    }

    private void signUpUser() {
        String name = edSignUpName.getText().toString().trim();
        String email = edSignUpEmail.getText().toString().trim();
        String username = edSignUpUsername.getText().toString().trim();
        String password = edSignUpPassword.getText().toString().trim();

        if(name.isEmpty()) {
            edSignUpName.setError("Необходимо ввести имя");
            edSignUpName.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            edSignUpEmail.setError("Необходимо ввести email");
            edSignUpEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edSignUpEmail.setError("Введите корректный email");
            edSignUpEmail.requestFocus();
            return;
        }

        if(username.isEmpty()) {
            edSignUpUsername.setError("Необходимо ввести логин");
            edSignUpUsername.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            edSignUpPassword.setError("Необходимо ввести пароль");
            edSignUpPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            edSignUpPassword.setError("Пароль должен быть длиннее 6 символов");
            edSignUpPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(name, email, username);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("name", "true");
                                                editor.apply();

                                                Toast.makeText(SignupActivity.this, "Вы успешно зарегистрированы", Toast.LENGTH_SHORT).show();
                                                mAuth.signInWithEmailAndPassword(email,password);
                                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                Toast.makeText(SignupActivity.this, "Регистрация не удалась", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(SignupActivity.this, "Регистрация не удалась", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
