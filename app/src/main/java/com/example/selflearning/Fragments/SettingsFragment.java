package com.example.selflearning.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selflearning.DBobjects.User;
import com.example.selflearning.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingsFragment extends Fragment {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private TextView tvUserName, tvUserUsername, tvUserEmail, tvUserNameDuble, tvUserUsernameDouble;

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
        tvUserNameDuble = view.findViewById(R.id.profileUserNameDuble);
        tvUserUsernameDouble = view.findViewById(R.id.profileUserUsernameDuble);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String name = userProfile.name;
                    String username = userProfile.username;
                    String email = userProfile.email;

                    tvUserName.setText(name);
                    tvUserNameDuble.setText(name);
                    tvUserUsername.setText(username);
                    tvUserUsernameDouble.setText(username);
                    tvUserEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "There is a mistake, i am sorry", Toast.LENGTH_SHORT).show();
            }
        });
    }
}