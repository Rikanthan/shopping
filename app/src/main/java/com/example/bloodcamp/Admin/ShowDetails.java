package com.example.bloodcamp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bloodcamp.Login_front.SignIn;
import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.Donor;
import com.example.bloodcamp.Views.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowDetails extends AppCompatActivity {
    private TextView name,email,location,phone;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String uid,userRole;
    private Button editUser,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_show_details);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.show_name);
        email = findViewById(R.id.show_email);
        location = findViewById(R.id.show_Location);
        phone = findViewById(R.id.show_phoneno);
        uid = getIntent().getStringExtra("id");
        userRole = getIntent().getStringExtra("UserRole");
        editUser = findViewById(R.id.user_edit);
        delete = findViewById(R.id.user_delete);
        if(userRole.contains("Admin"))
        {
            showAdmin();
        }
        else if(userRole.contains("Donor"))
        {
            showDonor();
        }
        else
        {
            showBloodCamp();
        }
        hidebutton();
    }
    private void hidebutton()
    {
        if(uid.contains(firebaseAuth.getUid()))
        {
            editUser.setVisibility(View.VISIBLE);
        }
        else
        {
            editUser.setVisibility(View.GONE);
        }
    }
    public void edit(View v)
    {
        Intent intent = new Intent(ShowDetails.this, SignIn.class);
        intent.putExtra("Action",true);
        intent.putExtra("UserType",userRole);
        startActivity(intent);
    }
    private void showDonor()
    {
        firestore
                .collection("Donor")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Donor donor = task.getResult().toObject(Donor.class);
                        name.setText(donor.getName());
                        email.setText(donor.getEmail());
                        location.setText(donor.getCity());
                        phone.setText(donor.getPhoneNumber());
                    }
                });
    }
    private void showBloodCamp()
    {
        firestore
                .collection("Member")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Member donor = task.getResult().toObject(Member.class);
                        name.setText(donor.getName());
                        email.setText(donor.getEmail());
                        location.setText(donor.getLocation());
                        phone.setText(String.valueOf(donor.getMobile()));
                    }
                });
    }
    private void showAdmin()
    {
        firestore
                .collection("Admin")
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Member donor = task.getResult().toObject(Member.class);
                        name.setText(donor.getName());
                        email.setText(donor.getEmail());
                        location.setText(donor.getLocation());
                        phone.setText(String.valueOf(donor.getMobile()));
                    }
                });
    }
}