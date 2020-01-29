package com.example.studentpersonalityquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton=(Button)(findViewById(R.id.btn_start));
        nameEditText=(EditText)(findViewById(R.id.editText_studentName));
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(nameEditText.getText().toString().equals(""))
                {
                    nameEditText.setError("The name can't be empty!");
                }
                else
                {
                    startQuiz();
                }
            }
        });


    }

    private void startQuiz() {
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        Bundle extras = new Bundle();
        extras.putString("studentName", nameEditText.getText().toString());
        intent.putExtras(extras);
        startActivity(intent);
    }
}
