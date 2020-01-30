package com.example.studentpersonalityquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
    private TextView textViewResult;
    private int score;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        score = extras.getInt("score");
        textViewResult = (TextView) (findViewById(R.id.textView_result));
        progressBar = (ProgressBar)(findViewById(R.id.progressBar_result));
        new LoaderAsyncTask(this).execute(10);
    }

    private void checkPersonality(int score) {
        if(score<40)
        {
            textViewResult.setText(getString(R.string.result3));
        }
        else if(score<=80)
        {
            textViewResult.setText(getString(R.string.result2));
        }
        else
        {
            textViewResult.setText(getString(R.string.result1));
        }
    }

    public static class LoaderAsyncTask extends AsyncTask<Integer, Integer, String> {
        private WeakReference<ResultActivity> activityWeakReference;

        LoaderAsyncTask(ResultActivity activity) {
            activityWeakReference = new WeakReference<ResultActivity>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ResultActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
        }

        @Override
        protected String doInBackground(Integer... integers) {
            for (int i = 0; i <= integers[0]; i++) {
                publishProgress(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ResultActivity activity = activityWeakReference.get();
            return activity.getString(R.string.pb_Status);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);

            ResultActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ResultActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setProgress(0);
            activity.progressBar.setVisibility(View.INVISIBLE);
            activity.checkPersonality(activity.score);
        }
    }
}
