package com.example.selflearning;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FirstCourseFragment extends Fragment {

    protected ListView listViewFirstSemester, listViewSecondSemester;
    private ArrayAdapter<String> adapterFirstSemester, adapterSecondSemester;
    private List<String> listDataFirstSemester, listDataSecondSemester;
    private DatabaseReference databaseReference;
    private String SUBJECT_KEY = "Subjects";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_course, container, false);

        // init
        listViewFirstSemester = view.findViewById(R.id.listViewFirstSemester);
        listViewSecondSemester = view.findViewById(R.id.listViewSecondSemester);
        // init for first semester
        listDataFirstSemester = new ArrayList<>();
        adapterFirstSemester = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,listDataFirstSemester);
        listViewFirstSemester.setAdapter(adapterFirstSemester);
        // init for second semester
        listDataSecondSemester = new ArrayList<>();
        adapterSecondSemester = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,listDataSecondSemester);
        listViewSecondSemester.setAdapter(adapterSecondSemester);

        databaseReference = FirebaseDatabase.getInstance().getReference(SUBJECT_KEY);

        getDataFromDB();

        return view;
    }

    private void getDataFromDB() {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listDataFirstSemester.size() > 0) {listDataFirstSemester.clear();}
                if (listDataSecondSemester.size() > 0) {listDataSecondSemester.clear();}
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Subject subject = ds.getValue(Subject.class);
                    if (subject.course == 1 && subject.semester == 1) {
                        assert subject != null;
                        listDataFirstSemester.add(subject.name);
                    }
                    if (subject.course == 1 && subject.semester == 2) {
                        assert subject != null;
                        listDataSecondSemester.add(subject.name);
                    }
                }
                adapterFirstSemester.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(vListener);
    }

}