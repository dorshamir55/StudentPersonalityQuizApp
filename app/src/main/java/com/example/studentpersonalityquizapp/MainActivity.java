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

//import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button startButton;
    private EditText nameEditText;
    //private Switch langSwitch;
    private ImageView imageCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton =  (findViewById(R.id.btn_start));
        nameEditText =  (findViewById(R.id.editText_studentName));
        imageCall = findViewById(R.id.image_call);
        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.getText().toString().trim().length()==0) { //trim=remove spaces to avoid blank name
                    nameEditText.setError(getString(R.string.check_editText));
                } else {
                    startQuiz();
                }
            }
        });

//        langSwitch = (Switch) (findViewById(R.id.switchLang));
//        langSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean change) {
//                if (langSwitch.isChecked()) {
//                    if (Locale.getDefault().getLanguage().equals(new Locale("en").getLanguage())) {
//                        updateResources(MainActivity.this, "iw");
//
//                    } else {
//                        System.out.println("the lanauge is"+""+Locale.getDefault().getDisplayLanguage() );
//                        updateResources(MainActivity.this, "en");
//                    }
//                }
//            }
//        });


    }


    private void makePhoneCall(){
        String number = getString(R.string.phoneContact);
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) //check premission
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        else{
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
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

//    private void updateResources(Context context, String language) {
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//        Resources resources = context.getResources();
//
//        Configuration configuration = resources.getConfiguration();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            configuration.setLocale(locale);
//        } else {
//            configuration.locale = locale;
//        }
//
//        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//        // this.recreate();
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
//    }

}
