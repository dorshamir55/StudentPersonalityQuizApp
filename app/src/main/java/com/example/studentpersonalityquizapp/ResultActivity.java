package com.example.studentpersonalityquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView textViewResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int score = extras.getInt("score");
        textViewResult = (TextView) (findViewById(R.id.textView_result));
        checkPersonality(score);

    }

    private void checkPersonality(int score) {
        if(score<40)
        {
            textViewResult.setText(getString(R.string.result3));
        }
        else if(score<70)
        {
            textViewResult.setText(getString(R.string.result2));
        }
        else
        {
            textViewResult.setText(getString(R.string.result1));
        }
    }
}
