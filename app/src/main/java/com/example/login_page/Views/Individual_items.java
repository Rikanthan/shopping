package com.example.login_page.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.login_page.Images.Upload;
import com.example.login_page.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Individual_items extends AppCompatActivity {
ImageView imageView;
TextView textname,textprice;
String productName,productCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_items);
        productName=getIntent().getStringExtra("name");
        productCategory=getIntent().getStringExtra("category");
        imageView=(ImageView)findViewById(R.id.indi_img);
        textname=(TextView)findViewById(R.id.indi_name);
        textprice=(TextView)findViewById(R.id.indi_price);

        getDetails(productName);

    }
    private void getDetails(String productName)
    {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child(productCategory);
        productsRef.child(productName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                        {
                            Upload upload=snapshot.getValue(Upload.class);
                            textname.setText(upload.getName());
                            textprice.setText(upload.getmPrice());
                            Glide.with(getApplicationContext()).load(upload.getImageUrl()).into(imageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }
}