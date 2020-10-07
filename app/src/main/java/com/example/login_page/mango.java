package com.example.login_page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class mango extends AppCompatActivity {
TextView t;
TextView p;
int price=25;
int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mango);
        t=(TextView)findViewById(R.id.mango_quantity);
        p=(TextView)findViewById(R.id.man_price);

    }
    public void max(View v)
    {
        i=i+1;
        t.setText(String.valueOf(i));
        p.setText("Price :"+String.valueOf(price*i)+" LKR");
    }
    public void min(View v)
    {
        i=i-1;
        if(i<1)
        {
            i=1;
        }
        t.setText(String.valueOf(i));
        p.setText("Price :"+String.valueOf(price*(i))+ " LKR");
    }
}