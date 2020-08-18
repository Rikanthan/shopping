package com.example.login_page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class fruit extends AppCompatActivity {
    String [] fruitLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);

        Resources res=getResources();
        fruitLabels=res.getStringArray(R.array.fruits);


        final GridView grid=(GridView) findViewById(R.id.fruitgrid);
        fruitAdapter myadapter=new fruitAdapter(getApplicationContext(),fruitLabels);
        grid.setAdapter(myadapter);
    }
}