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
    }
    public void fru(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Fruit");
    }
    public void med(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Medical");
    }
    public void pap(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Paper");
    }
    public void hyg(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Hygiene");
    }
    public void cos(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Cosmetrics");
    }
    public void sof(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Softdrinks");
    }
    public void sna(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Snacks");
    }
    public void bak(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Bakery");
    }




}