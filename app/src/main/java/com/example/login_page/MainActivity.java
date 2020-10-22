package com.example.login_page;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Intent;
import android.view.View;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;

import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import android.os.Bundle;

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
        Intent i=new Intent(this,sign_in.class);
        startActivity(i);

    }
    public  void forgetpassword(View v)
    {
        Intent i=new Intent(this,forgetpassword.class);
        startActivity(i);

    }
    private void validation()
    {
        String user=name.getText().toString();
        String pass=name.getText().toString();
        myRef=FirebaseDatabase.getInstance().getReference().child("Member");


    }

    public  void login(View v)
    {
        myRef=FirebaseDatabase.getInstance().getReference().child("Member");



        if(!validepass() | !valideusername()){
            return;
        }

        Intent i=new Intent(this,Home.class);
        startActivity(i);

    }
}