package com.example.login_page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class mango extends AppCompatActivity {
TextView t,t1;
TextView p,p1;
int price=25;
int price3=60;
int i=1;
int j=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mango);
        t=(TextView)findViewById(R.id.mango_quantity);
        p=(TextView)findViewById(R.id.man_price);
        t1=(TextView)findViewById(R.id.mango3);
        p1=(TextView)findViewById(R.id.man3_price);

    }
    public void max(View v)
    {
        i=i+1;
        t.setText(String.valueOf(i));
        p.setText("Price :"+String.valueOf(price*i)+" LKR");
    }
    public void min(View v) {
        i = i - 1;
        if (i < 1) {
            i = 1;
        }
        t.setText(String.valueOf(i));
        p.setText("Price :" + String.valueOf(price * (i)) + " LKR");
    }
    public void max3(View v)
    {
        int i=1;
        j=j+3;
        t1.setText(String.valueOf(j));
        p1.setText("Price :"+String.valueOf(price3*(j/3))+" LKR");
    }
    public void min3(View v) {
        j = j - 3;
        if (j < 3) {
            j = 3;
        }
        t1.setText(String.valueOf(j));
        p1.setText("Price :" + String.valueOf(price3 * (j/3)) + " LKR");
    }
    public void order(View v)
    {

        Toast.makeText(getApplicationContext(), "Your order added to cart successfully", Toast.LENGTH_LONG).show();
    }
}