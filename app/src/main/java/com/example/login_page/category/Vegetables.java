package com.example.login_page.category;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login_page.Home.Home;
import com.example.login_page.Home.MainActivity;
import com.example.login_page.Images.ImageAdapter;
import com.example.login_page.Images.Upload;
import com.example.login_page.R;
import com.example.login_page.Views.IndividualItems;
import com.example.login_page.Views.ShowOrders;
import com.example.login_page.customer.CustomerViewBookings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Vegetables extends AppCompatActivity implements ImageAdapter.OnItemClickListener{
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

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Vegetables");
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
                    String catergoryId = upload.getmCatergoryId();
                    upload.setmuploadId(uploadId);                               upload.setmCatergoryId(catergoryId);
                    upload.setImageUrl(categoryImageUrl);
                    upload.setmCatergory(categoryDescription);
                    upload.setmPrice(categoryPrice);
                    upload.setName(Name);
                    upload.setmQuantity(quantity);
                    Upload uploads=new Upload(Name,categoryImageUrl,categoryPrice,quantity,categoryDescription,uploadId ,catergoryId);
                    mUploads.add(uploads);
                }

                mAdapter = new ImageAdapter(Vegetables.this, mUploads);
                mAdapter.setOnItemClickListener(Vegetables.this);
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Vegetables.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Intent i=new Intent(this, IndividualItems.class);
        i.putExtra("Category","Vegetables");
        i.putExtra("index",position);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.customer_home:
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                return true;
            case R.id.show_cart:
                Intent i = new Intent(this, ShowOrders.class);
                startActivity(i);
                return true;
            case R.id.cust_bookings:
                Intent i1 = new Intent(this, CustomerViewBookings.class);
                startActivity(i1);
                return true;
            case R.id.customer_logout:
                Intent i2 = new Intent(this, MainActivity.class);
                startActivity(i2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}