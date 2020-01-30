package com.example.studentpersonalityquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

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

        //read DB to questions according to app lang
        QuestionsDB DB = readFromFile(checkLang());

    }


    public String checkLang()
    {
        if (true)
        {
            return "DBhebrew.json";
        }
        else
        {
            return "DBenglish.json";
        }
    }
    public String readJSONFromAsset(String Path) {
        String json = null;
        try {
            InputStream is = getAssets().open("DBhebrew.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    private QuestionsDB readFromFile(String Path) {
        try {
            GsonBuilder g = new GsonBuilder().disableHtmlEscaping();
            Gson gg = g.create();
            String json = readJSONFromAsset(Path);
            return  (gg.fromJson(json, QuestionsDB.class));

        } catch (JsonSyntaxException | JsonIOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
