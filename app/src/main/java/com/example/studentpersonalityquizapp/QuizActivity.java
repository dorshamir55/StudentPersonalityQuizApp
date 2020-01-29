package com.example.studentpersonalityquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {
    private int score;
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String studentName = extras.getString("studentName");
        welcomeTextView = (TextView)(findViewById(R.id.textView_welcome));
        welcomeTextView.append(", " +studentName);
    }
}
