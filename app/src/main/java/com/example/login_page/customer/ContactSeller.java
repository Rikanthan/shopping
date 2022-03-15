package com.example.login_page.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.login_page.R;
import com.example.login_page.Views.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactSeller extends AppCompatActivity {
private TextView sellerName,sellerPhone,sellerEmail,sellerLocation;
String sellerId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_seller);
        sellerId = getIntent().getStringExtra("seller");
        sellerName = findViewById(R.id.seller_name);
        sellerEmail = findViewById(R.id.seller_email);
        sellerPhone = findViewById(R.id.seller_phone);
        sellerLocation = findViewById(R.id.seller_location);
        showSeller();
    }
    public void showSeller()
    {
        FirebaseDatabase
                .getInstance()
                .getReference("Member")
                .child(sellerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Member member = snapshot.getValue(Member.class);
                    sellerName.setText(member.getName());
                    sellerEmail.setText(member.getEmail());
                    sellerPhone.setText(String.valueOf(member.getMobile()));
                    sellerLocation.setText(member.getLocation());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}