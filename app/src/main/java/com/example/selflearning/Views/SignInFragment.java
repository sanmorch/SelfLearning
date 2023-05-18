package com.example.selflearning.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selflearning.R;
import com.example.selflearning.ViewModel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignInFragment extends Fragment {

    private AuthViewModel viewModel;
    private NavController navController;

    private EditText edSignInEmail, edSignInPassword;
    private TextView signUpText;
    private Button signInButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        edSignInEmail = view.findViewById(R.id.signIn_email);
        edSignInPassword = view.findViewById(R.id.signIn_password);

        // if user click on "sign in" button
        signInButton = view.findViewById(R.id.signIn_button);
        signInButton.setOnClickListener(view1 -> {

            // get data from user
            String email = edSignInEmail.getText().toString().trim();
            String password = edSignInPassword.getText().toString().trim();

            // check is everything okay with data from user
            if(email.isEmpty()) {
                edSignInEmail.setError("Необходимо ввести email");
                edSignInEmail.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edSignInEmail.setError("Введите корректный email");
                edSignInEmail.requestFocus();
                return;
            }
            if(password.isEmpty()) {
                edSignInPassword.setError("Необходимо ввести пароль");
                edSignInPassword.requestFocus();
                return;
            }
            if (!email.isEmpty() && !password.isEmpty()) {
                viewModel.signIn(email, password);
                viewModel.getFirebaseUserMutableLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
                    if (firebaseUser != null) {
                        navController.navigate(R.id.action_signInFragment_to_homeFragment);
                    }
                });
            }
            else {
                Toast.makeText(getContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show();
            }

        });

        signUpText = view.findViewById(R.id.signupRedirectText);
        signUpText.setOnClickListener(view12 -> navController.navigate(R.id.action_signInFragment_to_signUpFragment));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getActivity().getApplication())).get(AuthViewModel.class);
    }
}