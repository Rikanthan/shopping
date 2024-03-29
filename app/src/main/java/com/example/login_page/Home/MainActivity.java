package com.example.login_page.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


import android.os.Bundle;
import android.widget.Toast;

import com.example.login_page.Admin.AdminCatorgory;
import com.example.login_page.Login_front.ForgetPassword;
import com.example.login_page.Login_front.SignIn;
import com.example.login_page.R;
import com.example.login_page.Views.SeeTimer;
import com.example.login_page.notification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String CHANNEL_ID = "100 " ;
    EditText email,pass;
    Button login;
   DatabaseReference myRef;
   FirebaseAuth firebaseAuth;
   FirebaseUser fuser;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    String userEmail,userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        onTokenRefresh();
        setContentView(R.layout.activity_main);
        email=(EditText) findViewById(R.id.editEmail);
        pass=(EditText) findViewById(R.id.editPassword);
        login = (Button) findViewById(R.id.userlogin);
        firebaseAuth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        saveLoginCheckBox = (CheckBox)findViewById(R.id.rempasswordcheckbox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            email.setText(loginPreferences.getString("username", ""));
            pass.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
        login.setOnClickListener(this);
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

    private void onTokenRefresh() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                   Log.e("Token",instanceIdResult.getToken());
                    }
                });
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationChannel";
            String description = "New Booking receive";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setLightColor(Color.CYAN);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public  void signup(View v)
    {
        Intent i=new Intent(this, SignIn.class);
        startActivity(i);
    }
    public  void forgetpassword(View v)
    {
        Intent i=new Intent(this, ForgetPassword.class);
        startActivity(i);
    }
    public void setValidLogin()
    {
        myRef=FirebaseDatabase.getInstance().getReference().child("Member");
        if(!validepass() | !valideemail()){
            return;
        }
        String userEmail = email.getText().toString().trim();
        String userPassword = pass.getText().toString().trim();
        firebaseAuth.signInWithEmailAndPassword(
                userEmail, userPassword)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
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
                                    String uid=firebaseAuth.getCurrentUser().getUid();
                                    System.out.println(uid);
                                    boolean admin = false;
                                    if(uid.contains("4VUgoUAvIgSNWgPFVCEYaFh1Mfd2"))
                                    {
                                        UpdateToken();
                                        admin = true;
                                        Intent i=new Intent(MainActivity.this, AdminCatorgory.class);
                                        startActivity(i);
                                    }
                                    else if(!admin)
                                    {
                                        UpdateToken();
                                        Intent i=new Intent(MainActivity.this,Home.class);
                                        startActivity(i);
                                    }
                                    //finish();
                                }
                                else {
                                    System.out.println(email.getText().toString()+pass.getText().toString());
                                    Toast.makeText(getApplicationContext(),"Enter the correct email and password",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(email.getWindowToken(), 0);

        userEmail = email.getText().toString();
        userPassword = pass.getText().toString();

        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", userEmail);
            loginPrefsEditor.putString("password", userPassword);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
        setValidLogin();
    }
    public void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

}