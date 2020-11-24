package com.example.login_page.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;

import android.widget.EditText;


import android.os.Bundle;

import com.example.login_page.Admin.admin;
import com.example.login_page.Admin.admin_catergory;
import com.example.login_page.Images.ImagesActivity;
import com.example.login_page.Images.imageupload;
import com.example.login_page.Product.Show_items_Activity;
import com.example.login_page.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
   private EditText name;
   private EditText pass;
   DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=findViewById(R.id.editTextTextPersonName);
        pass=findViewById(R.id.editTextTextPassword);


    }
    private boolean valideusername()
    {
        String user=name.getText().toString().trim();

        if(user.isEmpty())
        {
            name.setError("username can't be empty");
            return false;
        }
        return  true;


    }
    private boolean validepass()
    {
        String passwd=pass.getText().toString().trim();

        if(passwd.isEmpty())
        {
            pass.setError("password can't be empty");
            return false;
        }
        return  true;


    }

    public  void signup(View v)
    {
        Intent i=new Intent(this, admin.class);
        startActivity(i);

    }
    public  void forgetpassword(View v)
    {
        Intent i=new Intent(this, admin_catergory.class);
        startActivity(i);

    }
    public  void login(View v)
    {
        myRef=FirebaseDatabase.getInstance().getReference().child("Member");



       /* if(!validepass() | !valideusername()){
            return;
        }*/

        Intent i=new Intent(this,admin.class);
        startActivity(i);

    }
}