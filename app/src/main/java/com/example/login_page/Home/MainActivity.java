package com.example.login_page.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import android.widget.EditText;


import android.os.Bundle;
import android.widget.Toast;

import com.example.login_page.Admin.admin;
import com.example.login_page.Admin.admin_catergory;
import com.example.login_page.Images.ImagesActivity;
import com.example.login_page.Images.imageupload;
import com.example.login_page.Login_front.forgetpassword;
import com.example.login_page.Login_front.sign_in;
import com.example.login_page.Product.Show_items_Activity;
import com.example.login_page.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
   private EditText email;
   private EditText pass;
   DatabaseReference myRef;
   FirebaseAuth firebaseAuth;
   FirebaseUser fuser;

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.editEmail);
        pass=findViewById(R.id.editTextTextPassword);
        firebaseAuth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser();
    }
    private boolean valideemail()
    {
        String user=email.getText().toString().trim();

        if(user.isEmpty())
        {
            email.setError("username can't be empty");
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
        Intent i=new Intent(this, sign_in.class);
        startActivity(i);

    }
    public  void forgetpassword(View v)
    {
        Intent i=new Intent(this, forgetpassword.class);
        startActivity(i);

    }
    public  void login(View v)
    {
        myRef=FirebaseDatabase.getInstance().getReference().child("Member");
       /* if(!validepass() | !valideusername()){
            return;
        }*/
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),pass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    // there was an error
                    if (pass.length() < 8) {
                        Toast.makeText(getApplicationContext(),"Password must be more than 8 digit",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Enter the correct email and password",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(task.isSuccessful()) {
//                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                    String uid= fuser.getUid();
//                    editor.putString("uid",uid);
//                    editor.apply();

                    Intent i=new Intent(MainActivity.this,Home.class);
                    startActivity(i);
                    //finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Enter the correct email and password",Toast.LENGTH_SHORT).show();
                }

            }
        });
//        Intent i=new Intent(this,Home.class);
//        startActivity(i);


    }
}