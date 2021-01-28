package com.example.login_page.category;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login_page.Images.ImageAdapter;
import com.example.login_page.Images.ImagesActivity;
import com.example.login_page.Images.Upload;
import com.example.login_page.R;
import com.example.login_page.Views.Individual_items;
import com.example.login_page.adapter.recycler_adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class medical_activity extends AppCompatActivity implements ImageAdapter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private List<Upload> mUploads;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Medical");
        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    String Name = upload.getName();
                    String categoryDescription = upload.getmCatergory();
                    String categoryPrice = upload.getmPrice();
                    String categoryImageUrl = upload.getImageUrl();
                    String quantity=upload.getmQuantity();
                    upload.setImageUrl(categoryImageUrl);
                    upload.setmCatergory(categoryDescription);
                    upload.setmPrice(categoryPrice);
                    upload.setName(Name);
                    upload.setmQuantity(quantity);
                    Upload uploads=new Upload(Name,categoryImageUrl,categoryPrice,quantity,categoryDescription);
                    mUploads.add(uploads);

                }
                mAdapter = new ImageAdapter(medical_activity.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(medical_activity.this);
                mRecyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       count=mUploads.size();
                       System.out.println(count);
                    }
                });
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(medical_activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }


    @Override
    public void onItemClick(int position) {
        Intent i=new Intent(this, Individual_items.class);
        i.putExtra("Category","Medical");
        i.putExtra("index",position);
        startActivity(i);
    }
}