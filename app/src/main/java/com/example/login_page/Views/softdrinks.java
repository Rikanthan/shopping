package com.example.login_page.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.GridView;

import com.example.login_page.R;
import com.example.login_page.adapter.softdrinks_Adapter;

public class softdrinks extends AppCompatActivity {
    String softdrinksLabels[];
    String softdrinksPrices[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softdrinks);
        Resources res=getResources();
        softdrinksLabels=res.getStringArray(R.array.softdrink);
        softdrinksPrices=res.getStringArray(R.array.softdrink_prices);


        final GridView grid=(GridView) findViewById(R.id.soft_grid);
        softdrinks_Adapter myadapter=new softdrinks_Adapter(getApplicationContext(),softdrinksLabels,softdrinksPrices);
        grid.setAdapter(myadapter);


    }
}