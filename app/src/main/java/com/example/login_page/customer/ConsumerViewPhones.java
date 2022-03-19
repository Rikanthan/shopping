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
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
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
    public void contactSellerClick(View v, int position) {
        Intent i = new Intent(this, ContactSeller.class);
        String seller = mPhoneDetails.get(position).getMember();
        String id = mPhoneDetails.get(position).getId();
        i.putExtra("seller",seller);
        i.putExtra("id",id);
        startActivity(i);

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
        final String[] items = {"All","Xiomi","Samsung","Iphone","Others"};
        final boolean[] checkedItems = {false, false, false, false, false};
        final List<PhoneDetails> searchDetails = new ArrayList<>();
        searchDetails.addAll(mPhoneDetails);
        mPhoneDetails.clear();
        alertDialog.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                switch (which) {
                    case 0:
                        if(isChecked)
                            isClicked = true;
                            show();
                        break;
                    case 1:
                        if(isChecked)
                            isClicked = true;
                            for(PhoneDetails details: searchDetails)
                            {
                                if(details.getPhone().toLowerCase().contains("xia"))
                                {
                                    mPhoneDetails.add(details);
                                }
                                mAdapter = new ImageAdapter(ConsumerViewPhones.this, mPhoneDetails,ConsumerViewPhones.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        break;
                    case 2:
                        if(isChecked)
                            isClicked = true;
                            for(PhoneDetails details: searchDetails)
                            {
                                if(details.getPhone().toLowerCase().contains("samsung"))
                                {
                                    mPhoneDetails.add(details);
                                }
                                mAdapter = new ImageAdapter(ConsumerViewPhones.this, mPhoneDetails,ConsumerViewPhones.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        break;
                    case 3:
                        if(isChecked)
                            isClicked = true;
                            for(PhoneDetails details: searchDetails)
                            {
                                if(details.getPhone().toLowerCase().contains("iphone"))
                                {
                                    mPhoneDetails.add(details);
                                }
                                mAdapter = new ImageAdapter(ConsumerViewPhones.this, mPhoneDetails,ConsumerViewPhones.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        break;
                    case 4:
                        if(isChecked)
                            isClicked = true;
                            checkedItems[4] = !checkedItems[4];
                            for(PhoneDetails details: searchDetails)
                            {
                                if(!details.getPhone().toLowerCase().contains("iphone")
                                        &&!details.getPhone().toLowerCase().contains("xiaomi")
                                         && !details.getPhone().toLowerCase().contains("samsung"))
                                {
                                    mPhoneDetails.add(details);
                                }
                                mAdapter = new ImageAdapter(ConsumerViewPhones.this, mPhoneDetails,ConsumerViewPhones.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        break;
                    default:
                        show();
                }
            }
        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if(!isClicked)
//                {
//                    show();
//                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}