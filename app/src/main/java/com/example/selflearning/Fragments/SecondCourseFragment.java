package com.example.selflearning.Fragments;

import static com.example.selflearning.Constant.SUBJECT_DESC;
import static com.example.selflearning.Constant.SUBJECT_ID;
import static com.example.selflearning.Constant.SUBJECT_KEY;
import static com.example.selflearning.Constant.SUBJECT_NAME;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.selflearning.DBobjects.Subject;
import com.example.selflearning.R;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SecondCourseFragment extends Fragment implements View.OnClickListener {

    // for buttons
    protected MaterialButtonToggleGroup toggleGroup;
    private Button firstSemesterButton, secondSemesterButton;
    private TextView semesterName;

    // for DB
    private DatabaseReference databaseReference;

    // for listView
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listData;
    private List<Subject> listSubject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_second_course, container, false);
        init(view);
        getDataFromDB(2,1);
        setOnClickItem();
        return view;
    }

    //initialize variables
    private void init(View view) {

        semesterName = view.findViewById(R.id.nameSemesterSecondCourse);

        //init for list
        listView = view.findViewById(R.id.listSecondCourse);
        listData = new ArrayList<>();
        listSubject = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);


        //init for toggleGroup
        toggleGroup = view.findViewById(R.id.toggleButtonGroup);

        // init for first semester
        firstSemesterButton = view.findViewById(R.id.firstSemesterButton);
        firstSemesterButton.setOnClickListener(this);
        // init for second semester
        secondSemesterButton = view.findViewById(R.id.secondSemesterButton);
        secondSemesterButton.setOnClickListener(this);

        //init for DB
        databaseReference = FirebaseDatabase.getInstance().getReference(SUBJECT_KEY);
    }

    // for getting data from db to listView
    private void getDataFromDB(int course, int semester) {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listData.size() > 0) listData.clear();
                if (listSubject.size() > 0) listSubject.clear();
                // record data from database from first course, first semester
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Subject subject = ds.getValue(Subject.class);
                    assert subject != null;
                    if (subject.course == course && subject.semester == semester) {
                        listData.add(subject.name);
                        listSubject.add(subject);
                    }
                }
                // notify arrayAdapter about changing data
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(vListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.firstSemesterButton:
                semesterName.setText("Первый семестр");
                getDataFromDB(2,1);
                break;
            case R.id.secondSemesterButton:
                semesterName.setText("Второй семестр");
                getDataFromDB(2, 2);
                break;
        }
    }

    private void setOnClickItem() {
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Subject subjectSelected = listSubject.get(i);
            Intent intent = new Intent(getActivity(), SubjectPageActivity.class);
            intent.putExtra(SUBJECT_NAME, subjectSelected.name);
            intent.putExtra(SUBJECT_DESC, subjectSelected.description);
            intent.putExtra(SUBJECT_ID, subjectSelected.id);
            startActivity(intent);
        });
    }
}