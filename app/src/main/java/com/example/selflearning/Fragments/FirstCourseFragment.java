package com.example.selflearning.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.selflearning.Activities.SubjectPageActivity;
import com.example.selflearning.Constant;
import com.example.selflearning.DBobjects.Subject;
import com.example.selflearning.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;


public class FirstCourseFragment extends Fragment implements View.OnClickListener {

    protected MaterialButtonToggleGroup toggleGroup;

    private DatabaseReference databaseReference;
    private TextView semesterName;
    private Button firstSemesterButton, secondSemesterButton;
    private String SUBJECT_KEY = "Subjects";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_course, container, false);
        init(view);
        //getDataFromDB();
        return view;
    }

    //initialize variables
    protected void init(View view) {

        semesterName = view.findViewById(R.id.nameSemesterFirstCourse);

        //init for toggleGroup
        toggleGroup = view.findViewById(R.id.toggleButtonGroup);

        // init for first semester
        firstSemesterButton = view.findViewById(R.id.firstSemesterButton);
        firstSemesterButton.setOnClickListener(this);
        // init for second semester
        secondSemesterButton = view.findViewById(R.id.secondSemesterButton);
        secondSemesterButton.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference(SUBJECT_KEY);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.firstSemesterButton:
                semesterName.setText("Первый семестер");
                break;
            case R.id.secondSemesterButton:
                semesterName.setText("Второй семестер");
                break;
        }
    }
}