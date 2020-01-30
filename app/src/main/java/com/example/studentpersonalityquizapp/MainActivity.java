package com.example.studentpersonalityquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private EditText nameEditText;
    private Switch langSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) (findViewById(R.id.btn_start));
        nameEditText = (EditText) (findViewById(R.id.editText_studentName));
        langSwitch = (Switch) (findViewById(R.id.switchLang));
        langSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean change) {
                if (langSwitch.isChecked()) {
                    updateResources(getBaseContext(), "iw");
                    System.out.println("The language is: "+Locale.getDefault().getLanguage());
                } else {
                    updateResources(getBaseContext(), "en");
                }


            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.getText().toString().equals("")) {
                    nameEditText.setError("The name can't be empty!");
                } else {
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

    private void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        this.recreate();
    }

}
