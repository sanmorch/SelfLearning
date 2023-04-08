package com.example.selflearning;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SubjectPageActivity extends AppCompatActivity {

    private TextView subject_name, subject_desc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_page);
        init();
        getIntentMain();
    }

    private void init() {
        subject_name = findViewById(R.id.subject_name);
        subject_desc = findViewById(R.id.subject_desc);
    }

    private void getIntentMain() {
        Intent intent = getIntent();
        if (intent != null) {
            subject_name.setText(intent.getStringExtra(Constant.SUBJECT_NAME));
            subject_desc.setText(intent.getStringExtra(Constant.SUBJECT_DESC));
        }
    }
}
