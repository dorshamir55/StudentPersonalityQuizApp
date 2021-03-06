package com.example.studentpersonalityquizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private EditText nameEditText;
    private ImageView imageCall;
    private ImageView imageEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton =  (findViewById(R.id.btn_start));
        nameEditText =  (findViewById(R.id.editText_studentName));
        imageEmail = findViewById(R.id.image_email);
        imageCall = findViewById(R.id.image_call);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.getText().toString().trim().length() == 0) { //trim=remove spaces to avoid blank name
                    nameEditText.setError(getString(R.string.check_editText));
                } else {
                    startQuiz();
                }
            }
        });

        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        imageEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }

    private void sendMail() {
        String mail = "contact@gmail.com";
        String subject= "StudentPersonalityQuizApp - support mail";
        String mailTo = "mailto:" + mail +
                "?&subject=" + Uri.encode(subject) +
                "&body=" ;
        Intent emailIntent = new Intent(Intent.ACTION_VIEW);
        emailIntent.setData(Uri.parse(mailTo));
        startActivity(emailIntent);

    }

    private void makePhoneCall(){
        String number = getString(R.string.phoneContact);
        //check permission
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        else{
            String dial = String.format("tel:%s", number);
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
                //after we ask for permission we check if user allowed this
            }
            else{
                Toast.makeText(this,getString(R.string.permission),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startQuiz() {
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        Bundle extras = new Bundle();
        extras.putString("studentName", nameEditText.getText().toString());
        intent.putExtras(extras);
        startActivity(intent);
    }
}
