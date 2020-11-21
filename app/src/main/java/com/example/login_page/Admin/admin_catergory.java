package com.example.login_page.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.login_page.Images.imageupload;
import com.example.login_page.R;

public class admin_catergory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_catergory);
    }
    public void veg(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Vegetables");
        startActivity(i);
    }
    public void fru(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Fruit");
        startActivity(i);
    }
    public void med(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Medical");
        startActivity(i);
    }
    public void pap(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Paper");
        startActivity(i);
    }
    public void hyg(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Hygiene");
        startActivity(i);
    }
    public void cos(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Cosmetrics");
        startActivity(i);
    }
    public void sof(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Softdrinks");
        startActivity(i);
    }
    public void sna(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Snacks");
        startActivity(i);
    }
    public void bak(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Bakery");
        startActivity(i);
    }




}