package com.example.login_page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class display_page extends AppCompatActivity {
    Intent page=getIntent();
    String myPage=page.getStringExtra("newpage");
    View view=(View) findViewById(R.id.pageview);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);

    }
}