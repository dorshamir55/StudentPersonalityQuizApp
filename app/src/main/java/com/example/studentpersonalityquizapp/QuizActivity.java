package com.example.studentpersonalityquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {
    // declare variables
    private TextView welcomeTextView;
    private TextView questionTextView;
    private TextView questionNumberTextView;
    private RadioGroup radioGroupAnswers;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private Button nextButton;
    private Map<Integer, String> questionsMap;
    private Map<Integer, ArrayList<String>> answersMap;
    private int questionCounter = 1; // count number of question to move next activity after 10 questions
    private int score = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // initialize variables
        welcomeTextView = (TextView) (findViewById(R.id.textView_welcome));
        questionTextView = (TextView) (findViewById(R.id.textView_question));
        questionNumberTextView = (TextView) (findViewById(R.id.textView_questionNumber));
        radioGroupAnswers = (RadioGroup) (findViewById(R.id.radioGroup_answers));
        radioButton1 = (RadioButton) (findViewById(R.id.radio_button1));
        radioButton2 = (RadioButton) (findViewById(R.id.radio_button2));
        radioButton3 = (RadioButton) (findViewById(R.id.radio_button3));
        radioButton4 = (RadioButton) (findViewById(R.id.radio_button4));
        nextButton = (Button) (findViewById(R.id.btn_next));

        // get student name from bundle/intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        assert extras != null;
        String studentName = extras.getString("studentName");

        //set welcome msg to the student with his name from intent
        welcomeTextView.append(", " + studentName);

        //read DB for questions&answers according to phone lang(heb/eng)
        QuestionsDB DB = readFromFile(checkLang());
        assert DB != null;
        questionsMap = DB.getQuestion();
        answersMap = DB.getAnswers();
        //start the quiz
        nextQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (radioGroupAnswers.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(QuizActivity.this, getString(R.string.select_Answer), Toast.LENGTH_SHORT).show();
                } else {
                    checkAnswer();
                    nextQuestion();
                }
            }
        });
    }

    private void checkAnswer() {
        RadioButton selected = findViewById(radioGroupAnswers.getCheckedRadioButtonId());
        int answer = radioGroupAnswers.indexOfChild(selected) + 1;

        // points per answer
        switch (answer) {
            case 1:
                score += 1;
                break;
            case 2:
                score += 4;
                break;
            case 3:
                score += 7;
                break;
            case 4:
                score += 10;
                break;
            default:
                break;
        }
    }

    private void nextQuestion() {
        radioGroupAnswers.clearCheck();
        // initialize questions and answers to the objects from the maps
        try {
            if (questionCounter < 10) {
                questionTextView.setText(questionsMap.get(questionCounter));
                questionNumberTextView.setText(String.format("%s.", String.valueOf(questionCounter)));
                radioButton1.setText(Objects.requireNonNull(answersMap.get(questionCounter)).get(0));
                radioButton2.setText(Objects.requireNonNull(answersMap.get(questionCounter)).get(1));
                radioButton3.setText(Objects.requireNonNull(answersMap.get(questionCounter)).get(2));
                radioButton4.setText(Objects.requireNonNull(answersMap.get(questionCounter)).get(3));

            } else if (questionCounter == 10) // last question
            {
                questionTextView.setText(questionsMap.get(questionCounter));
                questionNumberTextView.setText(String.format("%s.", String.valueOf(questionCounter)));
                radioButton1.setText(Objects.requireNonNull(answersMap.get(questionCounter)).get(0));
                radioButton2.setText(Objects.requireNonNull(answersMap.get(questionCounter)).get(1));
                radioButton3.setText(Objects.requireNonNull(answersMap.get(questionCounter)).get(2));
                radioButton4.setText(Objects.requireNonNull(answersMap.get(questionCounter)).get(3));
                nextButton.setText(getString(R.string.finish_btn));

            } else {
                finishQuiz();
            }
        } catch (NullPointerException e)
        {
            e.printStackTrace();
            Toast.makeText(this,getString(R.string.exception),Toast.LENGTH_SHORT).show();
        }
        questionCounter++;
    }

    private void finishQuiz() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("score", score);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private String checkLang() {
        if (Locale.getDefault().getLanguage().equals(new Locale("en").getLanguage())) //if the code lang will change
        {
            return "DBenglish.json";
        } else//lang = iw - hebrew
        {
            return "DBhebrew.json";
        }
    }

    private String readJsonFromAsset(String Path) {
        String json;

        try {
            InputStream is = getAssets().open(Path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    private QuestionsDB readFromFile(String Path) {
        // using gson to read the json files
        try {
            GsonBuilder g = new GsonBuilder().disableHtmlEscaping();
            Gson gson = g.create();
            String json = readJsonFromAsset(Path);

            return (gson.fromJson(json, QuestionsDB.class));
        } catch (JsonSyntaxException | JsonIOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
