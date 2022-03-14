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
import com.example.login_page.R;
import com.example.login_page.Views.IndividualItems;
import com.example.login_page.Views.PhoneDetails;
import com.example.login_page.Views.ShowOrders;
import com.example.login_page.customer.CustomerViewBookings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllCatergories extends AppCompatActivity implements ImageAdapter.ImageAdapterListener{
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private List<PhoneDetails> mPhoneDetailss;
    String product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);
        mPhoneDetailss = new ArrayList<>();
        product = getIntent().getStringExtra("Product");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(product);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPhoneDetailss.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(postSnapshot.exists())
                    {
                        PhoneDetails upload = postSnapshot.getValue(PhoneDetails.class);
                        mPhoneDetailss.add(upload);
                    }
                }
                mAdapter = new ImageAdapter(AllCatergories.this, mPhoneDetailss,AllCatergories.this);
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AllCatergories.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
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

    @Override
    public void contactSellerClick(View v, int position) {
        System.out.println(mPhoneDetailss.get(position).getMember());
    }
}
