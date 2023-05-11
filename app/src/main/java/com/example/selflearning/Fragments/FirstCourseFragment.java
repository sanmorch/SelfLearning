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


public class FirstCourseFragment extends Fragment  {

    protected ListView listViewFirstSemester, listViewSecondSemester;
    protected MaterialButtonToggleGroup toggleGroup;
    protected Button firstSemesterButton, secondSemesterButton;
    private ArrayAdapter<String> adapterFirstSemester, adapterSecondSemester;
    private List<String> listDataFirstSemester, listDataSecondSemester;
    private List<Subject> listSubject;
    private DatabaseReference databaseReference;
    private String SUBJECT_KEY = "Subjects";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_course, container, false);
        init(view);
        return view;
    }

    //initialize variables
    protected void init(View view) {
        listSubject = new ArrayList<>();

        //init for toggleGroup
        toggleGroup = view.findViewById(R.id.toggleButtonGroup);

        // init for first semester
        listViewFirstSemester = view.findViewById(R.id.listViewFirstSemester);
        firstSemesterButton = view.findViewById(R.id.firstSemesterButton);
        listDataFirstSemester = new ArrayList<>();
        adapterFirstSemester = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,listDataFirstSemester);
        listViewFirstSemester.setAdapter(adapterFirstSemester);
        // init for second semester
        listViewSecondSemester = view.findViewById(R.id.listViewSecondSemester);
        secondSemesterButton = view.findViewById(R.id.secondSemesterButton);
        listDataSecondSemester = new ArrayList<>();
        adapterSecondSemester = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,listDataSecondSemester);
        listViewSecondSemester.setAdapter(adapterSecondSemester);

        databaseReference = FirebaseDatabase.getInstance().getReference(SUBJECT_KEY);

        getDataFromDB();
    }

    private void getDataFromDB() {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listDataFirstSemester.size() > 0) {listDataFirstSemester.clear();}
                if (listDataSecondSemester.size() > 0) {listDataSecondSemester.clear();}
                if (listSubject.size() > 0) {listSubject.clear();}
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Subject subject = ds.getValue(Subject.class);
                    if (subject.course == 1 && subject.semester == 1) {
                        assert subject != null;
                        listDataFirstSemester.add(subject.name);
                        listSubject.add(subject);
                    }
                    if (subject.course == 1 && subject.semester == 2) {
                        assert subject != null;
                        listDataSecondSemester.add(subject.name);
                        listSubject.add(subject);
                    }
                }
                adapterFirstSemester.notifyDataSetChanged();
                adapterSecondSemester.notifyDataSetChanged();
                setOnClickItem();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(vListener);
    }

    private void setOnClickItem() {
        listViewFirstSemester.setOnItemClickListener((adapterView, view, position, l) -> {
            Subject subject = listSubject.get(position);
            Intent intent = new Intent(getActivity(), SubjectPageActivity.class);
            intent.putExtra(Constant.SUBJECT_NAME, subject.name);
            intent.putExtra(Constant.SUBJECT_DESC, subject.description);
            startActivity(intent);
        });
    }


    public void changeColor(View view) {
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                }
            }
        });
    }
}