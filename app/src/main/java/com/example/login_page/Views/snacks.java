package com.example.login_page.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.GridView;

import com.example.login_page.R;
import com.example.login_page.adapter.snacks_Adapter;


public class snacks extends AppCompatActivity {
    String snackLabels[];
    String snackPrices[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

        Resources res=getResources();
        snackLabels=res.getStringArray(R.array.snacks);
        snackPrices=res.getStringArray(R.array.softdrink_prices);


        final GridView grid=(GridView) findViewById(R.id.snack_grid);
        snacks_Adapter myadapter=new snacks_Adapter(getApplicationContext(),snackLabels,snackPrices);
        grid.setAdapter(myadapter);
    }
}