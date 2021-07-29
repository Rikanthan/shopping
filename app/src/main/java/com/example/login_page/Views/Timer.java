package com.example.login_page.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.login_page.R;

import java.util.concurrent.TimeUnit;

public class Timer extends AppCompatActivity {
    TextView textView;
    private static final String FORMAT = "%02d:%02d:%02d";
    int seconds , minutes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        textView = findViewById(R.id.timeView3);
        new CountDownTimer(16069000, 1000) {

            public void onTick(long millisUntilFinished) {
                textView.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
               textView.setText("done!");
            }

        }.start();
    }
}