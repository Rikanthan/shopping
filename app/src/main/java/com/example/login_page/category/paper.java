package com.example.login_page.category;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login_page.Holder.Product_View_holder;
import com.example.login_page.Images.ImageAdapter;
import com.example.login_page.Images.Upload;
import com.example.login_page.R;
import com.example.login_page.Views.Individual_items;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class paper extends AppCompatActivity implements ImageAdapter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Paper");
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
                    String uploadId = upload.getmuploadId();
                    upload.setImageUrl(categoryImageUrl);
                    upload.setmCatergory(categoryDescription);
                    upload.setmuploadId(uploadId);
                    upload.setmPrice(categoryPrice);
                    upload.setName(Name);
                    upload.setmQuantity(quantity);
                    Upload uploads=new Upload(Name,categoryImageUrl,categoryPrice,quantity,categoryDescription,uploadId);
                    mUploads.add(uploads);
                }
                mAdapter = new ImageAdapter(paper.this, mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(paper.this);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(paper.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
//    public void show_individual(View v)
//    {
//        Intent i=new Intent(this, Individual_items.class);
//        i.putExtra("Category","Paper");
//        startActivity(i);
//    }


    @Override
    public void onItemClick(int position) {
        Intent i=new Intent(this, Individual_items.class);
        i.putExtra("Category","Paper");
        i.putExtra("index",position);
        startActivity(i);

    }
}