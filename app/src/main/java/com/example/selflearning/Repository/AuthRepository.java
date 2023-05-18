package com.example.selflearning.Repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.selflearning.DBobjects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class AuthRepository {
    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private FirebaseAuth mAuth;

    public AuthRepository(Application application) {
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(String email, String password, String name, String username) {
        // sign up user (creating profile for authorization Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    // if user created
                    if(task.isSuccessful()) {
                        // then create a new object of User in Firebase
                        // and add user to the list
                        firebaseUserMutableLiveData.postValue(mAuth.getCurrentUser());
                        User user = new User(name, email, username);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    // if object (User) created in Firebase database
                                    if(task1.isSuccessful()) {
                                        Toast.makeText(application, "Вы успешно зарегистрированы", Toast.LENGTH_SHORT).show();
                                        mAuth.signInWithEmailAndPassword(email,password);
                                    }
                                    else {
                                        Toast.makeText(application, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else {
                        Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                firebaseUserMutableLiveData.postValue(mAuth.getCurrentUser());
            }
            else {
                Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signOut() {
        // sign out
        FirebaseAuth.getInstance().signOut();
    }
}
