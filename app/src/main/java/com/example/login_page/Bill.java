package com.example.login_page;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Bill extends AppCompatActivity {
    private TextClock tClock;
    private TextView tView;
    private Button btn;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        tView=(TextView)findViewById(R.id.time);
        tClock = (TextClock)findViewById(R.id.textClock2);


    }
}