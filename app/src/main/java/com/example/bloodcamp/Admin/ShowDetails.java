package com.example.bloodcamp.Admin;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodcamp.Home.MainActivity;
import com.example.bloodcamp.Login_front.SignIn;
import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.Donor;
import com.example.bloodcamp.Views.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ShowDetails extends AppCompatActivity {
    private TextView name,email,location,phone;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String uid,userRole;
    private Button editUser,delete;
    Member admin,bloodcamp;
    private ProgressBar progressBar;
    Donor donor;
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
        progressBar = findViewById(R.id.delete_progress);
        uid = getIntent().getStringExtra("id");
        userRole = getIntent().getStringExtra("UserRole");
        editUser = findViewById(R.id.user_edit);
        delete = findViewById(R.id.user_delete);
        admin = new Member();
        bloodcamp = new Member();
        donor = new Donor();
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
    public void delete(View v)
    {
        String email = "";
        String password = "";
        String reference = "";
        if(userRole.contains("Donor"))
        {
            email = donor.getEmail();
            password = donor.getPassword();
            reference = "Donor";
        }
        else if(userRole.contains("Admin"))
        {
            email = admin.getEmail();
            password = admin.getPassword();
            reference = "Member";
        }
        else
        {
            email = bloodcamp.getEmail();
            password = bloodcamp.getPassword();
            reference = "Member";
        }
        android.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowDetails.this);
        alertDialog.setTitle("Conformation");
        alertDialog.setMessage("Do you want to delete user data?");
        String finalEmail = email;
        String finalPassword = password;
        String finalReference = reference;
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            delete.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            Objects.requireNonNull(FirebaseAuth
                    .getInstance()
                    .signInWithEmailAndPassword(
                            finalEmail,
                            finalPassword)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            task.getResult()
                                    .getUser()
                                    .delete()
                                    .addOnSuccessListener(
                                            task1 ->{
                                                firestore
                                                        .collection(finalReference)
                                                        .document(uid)
                                                        .delete()
                                                        .addOnSuccessListener(
                                                                unused ->
                                                                {
                                                                    deleteFullData();
                                                                });
                                            }
                                    );
                        }
                    }));
        });
        alertDialog.setNegativeButton("no" ,((dialog, which) -> {
        }));
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
    private void deleteFullData()
    {
        firestore
                .collection("UserRole")
                .document(uid)
                .delete()
                .addOnSuccessListener(
                        task ->
                        {
                            Toast.makeText(ShowDetails.this,"User delete successfully",Toast.LENGTH_SHORT)
                                    .show();
                            Intent i = new Intent(ShowDetails.this, MainActivity.class);
                            startActivity(i);
                        });
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
                         donor = task.getResult().toObject(Donor.class);
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
                        bloodcamp = task.getResult().toObject(Member.class);
                        name.setText(bloodcamp.getName());
                        email.setText(bloodcamp.getEmail());
                        location.setText(bloodcamp.getLocation());
                        phone.setText(String.valueOf(bloodcamp.getMobile()));
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
                        admin = task.getResult().toObject(Member.class);
                        name.setText(admin.getName());
                        email.setText(admin.getEmail());
                        location.setText(admin.getLocation());
                        phone.setText(String.valueOf(admin.getMobile()));
                    }
                });
    }
}