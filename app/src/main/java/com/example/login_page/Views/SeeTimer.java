package com.example.login_page.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.example.login_page.R;
import com.example.login_page.notification.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SeeTimer extends AppCompatActivity {
    DatabaseReference databaseReference;
    String date;
    TextView timeView;
    SimpleDateFormat format;
    Date currentTime;
    private static final String FORMAT = "%02d:%02d:%02d";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_timer);
        databaseReference = FirebaseDatabase.getInstance().getReference("Timer");
        timeView = findViewById(R.id.setCounter);
        currentTime = new Date();
        format = new SimpleDateFormat(DATE_FORMAT);
        getDate();

    }

    public void getDate()
    {
        databaseReference
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    try {
                                        date = snapshot.getValue(String.class);
                                        Date eventDate = format.parse(date);
                                        SetCountdown(eventDate.getTime() - currentTime.getTime());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );
    }
    public void SetCountdown(long miliSeconds)
    {
        new CountDownTimer(miliSeconds, 1000) {

            public void onTick(long millisUntilFinished) {
                timeView.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                timeView.setText("Exceeded!");
            }

        }.start();
    }
}