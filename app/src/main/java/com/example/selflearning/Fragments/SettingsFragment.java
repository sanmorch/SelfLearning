package com.example.selflearning.Fragments;
import static com.example.selflearning.Constant.SHARED_PREFS;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selflearning.Activities.LoginActivity;
import com.example.selflearning.DBobjects.User;
import com.example.selflearning.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingsFragment extends Fragment implements View.OnClickListener {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private TextView tvUserName, tvUserUsername;
    private TextInputEditText tvUserEmail, tvUserNameDouble, tvUserUsernameDouble;
    private SharedPreferences sharedPreferences;


    private String USER_USERNAME, USER_NAME, USER_EMAIL;


    private Button updateDataButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        init(view);
        return view;
    }


    protected void init(View view) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        tvUserName = view.findViewById(R.id.profileUserName);
        tvUserUsername = view.findViewById(R.id.profileUserUsername);
        tvUserEmail = view.findViewById(R.id.profileUserEmail);
        tvUserNameDouble = view.findViewById(R.id.profileUserNameDouble);
        tvUserUsernameDouble = view.findViewById(R.id.profileUserUsernameDouble);
        updateDataButton = view.findViewById(R.id.updateData);
        updateDataButton.setOnClickListener(this);

        update(reference);
    }

    void update(DatabaseReference reference) {
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    USER_NAME = userProfile.name;
                    USER_USERNAME = userProfile.username;
                    USER_EMAIL = userProfile.email;

                    tvUserName.setText(USER_NAME);
                    tvUserNameDouble.setText(USER_NAME);
                    tvUserUsername.setText(USER_USERNAME);
                    tvUserUsernameDouble.setText(USER_USERNAME);
                    tvUserEmail.setText(USER_EMAIL);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "There is a mistake, i am sorry", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateData:
                updateData();
                break;
        }
    }

    public void updateData() {
        if (isNameChanged() || isUsernameChanged() || isEmailChanged()) {
            Toast.makeText(getActivity(), "Данные были обновлены", Toast.LENGTH_LONG).show();
            update(reference);
            }
    }



    private boolean isEmailChanged() {
        if (!USER_EMAIL.equals(tvUserEmail.getEditableText().toString())) {
            user.updateEmail(tvUserEmail.getEditableText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    reference.child(userID).child("email").setValue(tvUserEmail.getText().toString());
                    logOut();
                } else
                    Toast.makeText(getActivity(),task.getException().getMessage(), Toast.LENGTH_LONG).show();
            });
            return true;
        } else return false;
    }

    private boolean isUsernameChanged() {
        if (!USER_USERNAME.equals(tvUserUsernameDouble.getEditableText().toString())) {
            reference.child(userID).child("username").setValue(tvUserUsernameDouble.getEditableText().toString());
            return true;
        } else return false;
    }

    private boolean isNameChanged() {
        if (!USER_NAME.equals(tvUserNameDouble.getEditableText().toString())) {
            reference.child(userID).child("name").setValue(tvUserNameDouble.getEditableText().toString());
            return true;
        } else return false;
    }

    // if user changed his email
    private void logOut() {
        // for not login user automatically
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", "false");
        editor.apply();

        // go to the login page
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}