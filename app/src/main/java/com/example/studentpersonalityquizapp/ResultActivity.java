package com.example.studentpersonalityquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class ResultActivity extends AppCompatActivity {
    private TextView resultTextView;
    private int score;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        score = extras.getInt("score");
        resultTextView = (TextView) (findViewById(R.id.textView_result));
        progressBar = (ProgressBar) (findViewById(R.id.progressBar_result));
        new LoaderAsyncTask(this).execute(10);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.share) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.titleShare));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, resultTextView.getText().toString());
                startActivity(Intent.createChooser(sharingIntent,getString(R.string.share)));
        }

        return true;

    }


    private void checkPersonality(int score) {
        if (score < 40) {
            resultTextView.setText(getString(R.string.result3));
        } else if (score <= 80) {
            resultTextView.setText(getString(R.string.result2));
        } else {
            resultTextView.setText(getString(R.string.result1));
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
            activity.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            for (int i = 0; i <= integers[0]; i++) {
                publishProgress(i * 100 / integers[0]);
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
