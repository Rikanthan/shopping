package com.example.login_page.customer;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login_page.Images.ImageAdapter;
import com.example.login_page.Images.imageupload;
import com.example.login_page.R;
import com.example.login_page.Views.PhoneDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConsumerViewPhones extends AppCompatActivity implements ImageAdapter.ImageAdapterListener{
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private List<PhoneDetails> mPhoneDetails;
    private SearchView mSearchView;
    private boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);
        mSearchView = findViewById(R.id.search_items);
        mPhoneDetails = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Phone");
        show();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query != null )
                {
                    if(!query.isEmpty())
                    {
                        search(query);

                    }
                    else
                    {
                        show();
                    }
                }
                else
                {
                    show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(query != null )
                {
                    if(!query.isEmpty())
                    {
                        search(query);
                    }
                    else
                    {
                        show();
                    }
                }
                else
                {
                    show();
                }
                return true;
            }
        });
    }

    public void search(String s)
    {
        List<PhoneDetails> searchDetails = new ArrayList<>();
        searchDetails.addAll(mPhoneDetails);
        mPhoneDetails.clear();
        for(PhoneDetails details: searchDetails)
        {
            System.out.println(details.getPhone().toLowerCase());
            if(details.getPhone() != null
                    && details.getPhone().toLowerCase().contains(s.toLowerCase()))
            {
                mPhoneDetails.add(details);
            }
            mAdapter = new ImageAdapter(ConsumerViewPhones.this, mPhoneDetails,ConsumerViewPhones.this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
    public void show()
    {
        mDatabaseRef.orderByChild("uploadTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPhoneDetails.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(postSnapshot.exists()) {
                        PhoneDetails upload = postSnapshot.getValue(PhoneDetails.class);
                        mPhoneDetails.add(upload);
                    }
                }

                mAdapter = new ImageAdapter(ConsumerViewPhones.this, mPhoneDetails,ConsumerViewPhones.this);
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ConsumerViewPhones.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void filter(String field)
    {
        mDatabaseRef.orderByChild(field).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPhoneDetails.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(postSnapshot.exists()) {
                        PhoneDetails upload = postSnapshot.getValue(PhoneDetails.class);
                        mPhoneDetails.add(upload);
                    }
                }

                mAdapter = new ImageAdapter(ConsumerViewPhones.this, mPhoneDetails,ConsumerViewPhones.this);
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ConsumerViewPhones.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.consumermenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.filter:
                showAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConsumerViewPhones.this);
        alertDialog.setTitle("Filter by Names");
        final String[] items = {"All","Samsung","Redmi","Iphone","Other"};
        final String[] sortedItems ={"Name","Location","Seller","Date","Price"};
        final boolean[] checkedItems = {false, false, false, false, false};
        final List<PhoneDetails> searchDetails = new ArrayList<>();
        searchDetails.addAll(mPhoneDetails);
        mPhoneDetails.clear();
        alertDialog.setSingleChoiceItems(sortedItems, 0, (dialog, which) -> {
            switch (which){
                case 0:
                    filter("Phone");
                    break;
                case 1:
                    filter("id");
                    break;
                case 2:
                    filter("Member");
                    break;
                case 3:
                    filter("uploadTime");
                    break;
                case 4:
                    filter("price");
                    break;
            }
        });

        alertDialog.setPositiveButton("Ok", (dialog, which) -> {
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    @Override
    public void editClick(View v, int position) {

    }

    @Override
    public void deleteClick(View v, int position) {

    }

    @Override
    public void itemClick(View v, int postion) {
        Intent i = new Intent(this,ContactSeller.class);
        i.putExtra("seller",mPhoneDetails.get(postion).getMember());
        i.putExtra("id",mPhoneDetails.get(postion).getId());
        startActivity(i);
    }
}