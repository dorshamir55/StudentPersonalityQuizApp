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
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Result;

public class QuizActivity extends AppCompatActivity {
    private int score = 0;
    private TextView welcomeTextView;
    private TextView textViewQuestion;
    private RadioGroup radioGroupAnswers;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private Button buttonNext;
    private Map<Integer, String> questionsMap;
    private Map<Integer, Map<Integer, String>> answersMap;
    private int questionCounter = 1; // count number of question to move next activity after 10 questions


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String studentName = extras.getString("studentName");

        welcomeTextView = (TextView) (findViewById(R.id.textView_welcome));
        textViewQuestion = (TextView) (findViewById(R.id.textView_question));
        radioGroupAnswers = (RadioGroup) (findViewById(R.id.radioGroup_answers));
        radioButton1 = (RadioButton) (findViewById(R.id.radio_button1));
        radioButton2 = (RadioButton) (findViewById(R.id.radio_button2));
        radioButton3 = (RadioButton) (findViewById(R.id.radio_button3));
        radioButton4 = (RadioButton) (findViewById(R.id.radio_button4));
        buttonNext = (Button) (findViewById(R.id.btn_next));

        //set welcome msg to the student
        welcomeTextView.append(", " + studentName);
        //read DB to questions according to app lang
        QuestionsDB DB = readFromFile(checkLang());
        questionsMap = DB.getQuestion();
        answersMap = DB.getAnswers();
        nextQuestion();
        buttonNext.setOnClickListener(new View.OnClickListener() {

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

        if (questionCounter < 10) {
            textViewQuestion.setText(questionsMap.get(questionCounter));
            radioButton1.setText(answersMap.get(questionCounter).get(1));
            radioButton2.setText(answersMap.get(questionCounter).get(4));
            radioButton3.setText(answersMap.get(questionCounter).get(7));
            radioButton4.setText(answersMap.get(questionCounter).get(10));



        } else if (questionCounter == 10) // last question
        {
            textViewQuestion.setText(questionsMap.get(questionCounter));
            radioButton1.setText(answersMap.get(questionCounter).get(1));
            radioButton2.setText(answersMap.get(questionCounter).get(4));
            radioButton3.setText(answersMap.get(questionCounter).get(7));
            radioButton4.setText(answersMap.get(questionCounter).get(10));
            buttonNext.setText(getString(R.string.finish_btn));

        } else {
            finishQuiz();
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


    public String checkLang() {
        if (Locale.getDefault().getLanguage().equals(new Locale("en").getLanguage())) //if the code lang will change
        {
            return "DBenglish.json";
        } else//lang = iw - hebrew
        {
            return "DBhebrew.json";
        }
    }

    public String readJSONFromAsset(String Path) {
        String json = null;
        try {
            InputStream is = getAssets().open(Path);
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
            return (gg.fromJson(json, QuestionsDB.class));

        } catch (JsonSyntaxException | JsonIOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
