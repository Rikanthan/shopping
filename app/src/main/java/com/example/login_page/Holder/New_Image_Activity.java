package com.example.login_page.Holder;



import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login_page.Admin.ui.EditItems;
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

public class New_Image_Activity extends AppCompatActivity implements ImageAdapter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private List<Upload> mUploads;
    public String pname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads");
        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    String Name = upload.getName();
                    pname=Name;
                    String categoryDescription = upload.getmCatergory();
                    String categoryPrice = upload.getmPrice();
                    String categoryImageUrl = upload.getImageUrl();
                    String quantity=upload.getmQuantity();
                    String uploadId = upload.getmuploadId();
                    upload.setImageUrl(categoryImageUrl);
                    upload.setmCatergory(categoryDescription);
                    upload.setmPrice(categoryPrice);
                    upload.setName(Name);
                    upload.setmQuantity(quantity);
                    upload.setmuploadId(uploadId);
                    Upload uploads=new Upload(Name,categoryImageUrl,categoryPrice,quantity,categoryDescription,uploadId);
                    mUploads.add(uploads);
                }

                mAdapter = new ImageAdapter(New_Image_Activity.this, mUploads);
                mAdapter.setOnItemClickListener(New_Image_Activity.this);
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(New_Image_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent i=new Intent(New_Image_Activity.this, EditItems.class);
        i.putExtra("index",position);
        startActivity(i);
    }
}