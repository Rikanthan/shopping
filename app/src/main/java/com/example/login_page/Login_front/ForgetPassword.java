package com.example.login_page.Login_front;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.login_page.Home.MainActivity;
import com.example.login_page.R;
import com.google.firebase.auth.FirebaseAuth;



public class ForgetPassword extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar progressBar;
    EditText email;
    Button button;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        firebaseAuth=FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.editTextTextEmailAddress);
    }
    public void reset(View v)
    {
        firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim());
        Toast.makeText(this,"Please open your email and reset the password",Toast.LENGTH_LONG);
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }
}