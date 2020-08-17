package com.example.login_page;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.GridView;

public class Home extends AppCompatActivity {

    String[] lables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Resources res=getResources();
        lables=res.getStringArray(R.array.headers);

        GridView grid=(GridView) findViewById(R.id.Items);
        customAdapter myadapter=new customAdapter(getApplicationContext(),lables);
        grid.setAdapter(myadapter);
        /*
        fruits
        vegetables
        softdrinks
        cosmetric
        bakery
        hygiene
        meat
        snacks and crackers
        produce
        other items
         */

    }
}