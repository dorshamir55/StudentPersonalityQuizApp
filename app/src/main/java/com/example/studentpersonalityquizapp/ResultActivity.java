package com.example.studentpersonalityquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {
    //declare variables
    private TextView resultTextView;
    private ProgressBar progressBar;
    private ImageView resultImageView;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        // get score info from bundle
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        score = extras.getInt("score");
        //initialize variables
        resultTextView = (TextView) (findViewById(R.id.textView_result));
        progressBar = (ProgressBar) (findViewById(R.id.progressBar_result));
        resultImageView = (ImageView) (findViewById(R.id.imageView_result));
        new LoaderAsyncTask(this).execute(10);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // share by click menu on top app
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // initialize share option
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
        // show personality results of the quiz according to score
        if (score < 40) {
            resultTextView.setText(getString(R.string.result3));
            resultImageView.setImageResource(R.drawable.bad_student);
            addStars(1);
        } else if (score <= 80) {
            resultTextView.setText(getString(R.string.result2));
            resultImageView.setImageResource(R.drawable.average_student);
            addStars(3);
        } else {
            resultTextView.setText(getString(R.string.result1));
            resultImageView.setImageResource(R.drawable.successful_student);
            addStars(5);
        }

    }

    private void addStars(int numberOfStars){
        // create dynamic stars according to the points
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout_result_activity);
        for(int i=0; i < numberOfStars; i++)
        {
            float width = resultTextView.getX();
            float height = resultTextView.getY();
            ImageView image = new ImageView(this);
            // set positions according to locale
            image.setX(width+(200*checkLang()));
            image.setY(height);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80+100*i,60));
            image.setMaxHeight(20);
            image.setMaxWidth(20);
            image.setImageResource(R.drawable.ic_star);
            // Adds the view to the layout
            layout.addView(image);
        }
    }

    public int checkLang() {
        if (Locale.getDefault().getLanguage().equals(new Locale("en").getLanguage())) //if the code lang will change
        {
            return 1;
        } else//lang = iw - hebrew
        {
            return -1;
        }
    }

    public static class LoaderAsyncTask extends AsyncTask<Integer, Integer, String> {
        private WeakReference<ResultActivity> activityWeakReference;

        LoaderAsyncTask(ResultActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
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
