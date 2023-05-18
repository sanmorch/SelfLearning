package com.example.selflearning.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.selflearning.R;
import com.example.selflearning.ViewModel.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;

public class SignUpFragment extends Fragment {

    private AuthViewModel viewModel;
    private NavController navController;

    private EditText edSignUpName, edSignUpEmail, edSignUpUsername, edSignUpPassword;
    private TextView alreadySignedUp;
    private Button signUpButton;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        init(view);
    }

    private void init(View view) {
        edSignUpName = view.findViewById(R.id.signup_name);
        edSignUpEmail = view.findViewById(R.id.signup_email);
        edSignUpUsername = view.findViewById(R.id.signup_username);
        edSignUpPassword = view.findViewById(R.id.signup_password);

        // if user click on button "Sign Up"
        signUpButton = view.findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(view1 -> {

            // getting data from EditTexts from fragment
            String name = edSignUpName.getText().toString().trim();
            String email = edSignUpEmail.getText().toString().trim();
            String username = edSignUpUsername.getText().toString().trim();
            String password = edSignUpPassword.getText().toString().trim();

            // checking is everything okay with the data from user
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

            // sign up user if everything okay
            viewModel.signUp(email, password, name, username);

            // navigate user to the home page
            viewModel.getFirebaseUserMutableLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
                if (firebaseUser != null) {
                    navController.navigate(R.id.homeFragment);
                }
            });

        });

        // if user click on "I have an account already"
        alreadySignedUp = view.findViewById(R.id.loginRedirectText);
        // then redirect him to the signIn fragment
        alreadySignedUp.setOnClickListener(view12 -> navController.navigate(R.id.action_signInFragment_to_signUpFragment));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }


}