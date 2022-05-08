package com.example.usermanagement.Login_front;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usermanagement.Home.MainActivity;
import com.example.usermanagement.Views.User;
import com.example.usermanagement.Views.Member;
import usermanagement.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {
    DatabaseReference reff;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[0-9])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{8,15}" +
                    "$");
    private static final Pattern EMAIL=Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    private EditText fullname;
    private EditText email;
    private EditText mobile;
    private EditText location;
    private EditText pswd;
    private EditText conpswd;
    private EditText nic;
    private EditText dob;
    private EditText city;
    private static  final int REQUEST_LOCATION=1;
    LocationManager locationManager;
    String latitude,longitude;
    Member member;
    User user;
    private String userType ="BloodCamp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        fullname = findViewById(R.id.fullName);
        email = findViewById(R.id.userEmailId);
        mobile = findViewById(R.id.mobileNumber);
        location = findViewById(R.id.location);
        pswd = findViewById(R.id.password);
        conpswd = findViewById(R.id.confirmPassword);
        nic = findViewById(R.id.NIC);
        dob = findViewById(R.id.DOB);
        city = findViewById(R.id.city);
        member = new Member();
        user = new User();
        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }
    private void addUser(Member member,String uid)
    {
        firestore.collection("Member")
                .document(uid).set(member);
    }
    private void addUserType(String userId)
    {
        Map<String ,String> userRole = new HashMap<>();
        userRole.put(userId,userType);
        firestore.collection("UserRole").
                document(userId).set(userRole);
    }
    private void addUser(User user, String uid)
    {
        firestore.collection("User")
                .document(uid).set(user);
    }

    private boolean validefullname()
    {
        String full=fullname.getText().toString().trim();

        if(full.isEmpty())
        {
            fullname.setError("Fullname can't be empty");
            return false;
        }
        return  true;
    }
    private boolean validelocation()
    {
        String place=location.getText().toString().trim();
        if(place.isEmpty())
        {
            location.setError("location can't be empty");
            return false;
        }
        return  true;
    }
    private boolean validemobile()
    {
        String mob=mobile.getText().toString().trim();
        if(mob.isEmpty())
        {
            mobile.setError("mobile number can't be empty");
            return false;
        }
        return  true;
    }
    private boolean validecon()
    {
        String con = conpswd.getText().toString().trim();
        String ps = pswd.getText().toString().trim();
        if(con.isEmpty())
        {
            conpswd.setError("Field can't be empty");
            return false;
        }
        else if(!con.equals(ps))
        {
            conpswd.setError("Password didn't match");
            return  false;
        }
        return  true;
    }

    private boolean valideEmail() {
        String emailinput = email.getText().toString().trim();
        if (emailinput.isEmpty()) {
            email.setError("Email can't be empty");
            return false;
        } else if (!EMAIL.matcher(emailinput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validePassword() {
        String passinput = pswd.getText().toString().trim();
        if (passinput.isEmpty()) {
            pswd.setError("password can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passinput).matches()) {
            pswd.setError("Password too weak");
            return false;
        } else {
            pswd.setError(null);
            return true;
        }
    }

    public void signin(View v) {

        if(userType.contains("BloodCamp"))
        {
            member.setName(fullname.getText().toString().trim());
            member.setEmail(email.getText().toString().trim());
            try {
                Long phn=Long.parseLong(mobile.getText().toString().trim());
                member.setMobile(phn);
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
            member.setLocation(location.getText().toString().trim());
            member.setUserType(userType);
        }
        else
        {
            user.setName(fullname.getText().toString().trim());
            user.setEmail(email.getText().toString().trim());
            user.setPhoneNumber(mobile.getText().toString().trim());
            user.setAddress(location.getText().toString().trim());
            user.setDOB(dob.getText().toString().trim());
            user.setNIC(nic.getText().toString().trim());
            user.setCity(city.getText().toString().trim());
            user.setLatitude(Double.parseDouble(latitude));
            user.setLongitude(Double.parseDouble(longitude));
            user.setPassword(pswd.getText().toString().trim());
        }
        if (!valideEmail() | !validePassword() | !validecon() |!validefullname() |!validelocation() |!validemobile()) {
            return;
        }
        String input = "Email: " + email.getText().toString();
        input += "\n";
        firebaseAuth
                .createUserWithEmailAndPassword(
                        email.getText().toString().trim(),
                        pswd.getText().toString().trim())
                .addOnCompleteListener(
                        task -> {
                            if(task.isSuccessful())
                            {
                                String uid = task.getResult().getUser().getUid();
                                if(userType.contains("BloodCamp"))
                                {
                                    reff.child(uid).setValue(member);
                                    addUser(member,uid);
                                }
                                else
                                {
                                    user.setId(uid);
                                    reff.child(uid).setValue(user);
                                    addUser(user,uid);
                                }
                                addUserType(uid);
                                Toast.makeText(SignIn.this, "Data input & user created successfully", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(SignIn.this, MainActivity.class);
                                startActivity(i);
                            }
                            else if(!task.isSuccessful())
                            {
                                Toast.makeText(SignIn.this,"Email id already exists",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }

}