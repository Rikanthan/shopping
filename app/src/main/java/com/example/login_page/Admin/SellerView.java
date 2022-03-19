package com.example.login_page.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.login_page.Images.ImageAdapter;
import com.example.login_page.Images.imageupload;
import com.example.login_page.R;
import com.example.login_page.Views.PhoneDetails;
import com.example.login_page.customer.ContactSeller;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SellerView extends AppCompatActivity implements ImageAdapter.ImageAdapterListener{
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private List<PhoneDetails> mPhoneDetails;
    private List<PhoneDetails> backupDetails;
    private SearchView mSearchView;
    private boolean isClicked = false;
    FirebaseAuth firebaseAuth;
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
        backupDetails = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Phone");
        firebaseAuth = FirebaseAuth.getInstance();
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
            mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
    public static  ArrayList<PhoneDetails> removeDuplicates(ArrayList<PhoneDetails> list)
    {
        ArrayList<PhoneDetails> newList = new ArrayList<PhoneDetails>();
        for (PhoneDetails element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        return newList;
    }
    public void show()
    {
        String id = firebaseAuth.getCurrentUser().getUid();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPhoneDetails.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(postSnapshot.exists() ) {
                        PhoneDetails upload = postSnapshot.getValue(PhoneDetails.class);
                        if(!mPhoneDetails.contains(upload) && upload.getMember().equals(id))
                        {
                            mPhoneDetails.add(upload);
                            backupDetails.add(upload);
                        }
                    }
                }
                mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SellerView.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.admin__new,menu);
        final View addPhone = menu.findItem(R.id.add_phone).getActionView();
        final View filter = menu.findItem(R.id.filter).getActionView();
        //filter.setVisibility(View.GONE);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_phone:
                Intent intent = new Intent(this, imageupload.class);
                startActivity(intent);
            case R.id.filter:
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SellerView.this);
        alertDialog.setTitle("Filter by Names");
        final String[] items = {"All","Name A-Z","Date Descending ","Seller A-Z","Location A-Z"};
        final boolean[] checkedItems = {false, false, false, false, false};
        mPhoneDetails.clear();
        alertDialog.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                switch (which) {
                    case 0:
                        if(isChecked)
                        {
                            mPhoneDetails.clear();
                            isClicked = true;
                            mPhoneDetails.addAll(backupDetails);
                            mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                         else
                        {
                            mPhoneDetails.clear();
                            mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                        break;
                    case 1:
                        if(isChecked)
                        {
                            isClicked = true;
                            for(PhoneDetails details: backupDetails)
                            {
                                if(details.getPhone().toLowerCase().contains("xia") && !mPhoneDetails.contains(details))
                                {
                                    mPhoneDetails.add(details);
                                }
                                mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }
                        else
                        {
                            isClicked = false;
                            for(PhoneDetails details: backupDetails)
                            {
                                if(details.getPhone().toLowerCase().contains("xia") && mPhoneDetails.contains(details))
                                {
                                    mPhoneDetails.remove(details);
                                }
                                mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }
                        break;
                    case 2:
                        if(isChecked)
                        {
                            isClicked = true;
                            for(PhoneDetails details: backupDetails)
                            {
                                if(details.getPhone().toLowerCase().contains("samsung") && !mPhoneDetails.contains(details))
                                {
                                    mPhoneDetails.add(details);
                                }
                                mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }
                        else
                        {
                            isClicked = false;
                            for(PhoneDetails details: backupDetails)
                            {
                                if(details.getPhone().toLowerCase().contains("samsung") && mPhoneDetails.contains(details))
                                {
                                    mPhoneDetails.remove(details);
                                }
                                mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }
                        break;
                    case 3:
                        if(isChecked)
                        {
                            isClicked = true;
                            for(PhoneDetails details: backupDetails)
                            {
                                if(details.getPhone().toLowerCase().contains("iphone")&& !mPhoneDetails.contains(details))
                                {
                                    mPhoneDetails.add(details);
                                }
                                mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }
                        else{
                            isClicked = false;
                            for(PhoneDetails details: backupDetails)
                            {
                                if(details.getPhone().toLowerCase().contains("iphone") && mPhoneDetails.contains(details))
                                {
                                    mPhoneDetails.remove(details);
                                }
                                mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }
                        break;
                    case 4:
                        if(isChecked)
                        {
                            isClicked = true;
                            for(PhoneDetails details: backupDetails)
                            {
                                if(!details.getPhone().toLowerCase().contains("iphone")
                                        &&!details.getPhone().toLowerCase().contains("xiaomi")
                                        && !details.getPhone().toLowerCase().contains("samsung") && !mPhoneDetails.contains(details))
                                {
                                    mPhoneDetails.add(details);
                                }
                                mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }
                        else
                        {
                            isClicked = false;
                            for(PhoneDetails details: backupDetails)
                            {
                                if(!details.getPhone().toLowerCase().contains("iphone")
                                        &&!details.getPhone().toLowerCase().contains("xiaomi")
                                        && !details.getPhone().toLowerCase().contains("samsung") && mPhoneDetails.contains(details))
                                {
                                    mPhoneDetails.remove(details);
                                }
                                mAdapter = new ImageAdapter(SellerView.this, mPhoneDetails,SellerView.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
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