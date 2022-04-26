package com.example.bloodcamp.Admin;

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

import com.example.bloodcamp.Images.ImageAdapter;
import com.example.bloodcamp.Images.imageupload;
import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.PhoneDetails;
import com.example.bloodcamp.customer.ContactSeller;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SellerView extends AppCompatActivity implements ImageAdapter.ImageAdapterListener{
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore firestore;
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
        firestore = FirebaseFirestore.getInstance();
        showPhone();
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
                        showPhone();
                    }
                }
                else
                {
                    showPhone();
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
                        showPhone();
                    }
                }
                else
                {
                    showPhone();
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
    public void showPhone()
    {
        String id = firebaseAuth.getCurrentUser().getUid();
        firestore.collection("Phone")
                .whereEqualTo("member", id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.exists())
                            {
                                PhoneDetails upload = document.toObject(PhoneDetails.class);
                                if(!mPhoneDetails.contains(upload))
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
                    else {
                        mProgressCircle.setVisibility(View.INVISIBLE);
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin__new,menu);
        final View addPhone = menu.findItem(R.id.add_phone).getActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_phone:
                Intent intent = new Intent(this, imageupload.class);
                intent.putExtra("isUpdate",false);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void editClick(View v, int position) {
        Intent i = new Intent(this,imageupload.class);
        i.putExtra("isUpdate",true);
        String id = mPhoneDetails.get(position).getId();
        i.putExtra("itemid",id);
        startActivity(i);
    }

    @Override
    public void deleteClick(View v, int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SellerView.this);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Are you want to delete this item?");
        alertDialog.setPositiveButton("ok", (dialog, which) -> {
            String id = mPhoneDetails.get(position).getId();
            mPhoneDetails.remove(position);
            firestore
                    .collection("Phone")
                    .document(id)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(),"Delete Success", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(SellerView.this,SellerView.class);
                        startActivity(i);
                    });
            mDatabaseRef.child(id).removeValue();
        });

        alertDialog.setNegativeButton("cancel", (dialog, which) -> {
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    @Override
    public void itemClick(View v, int postion) {
        Intent i = new Intent(this,ContactSeller.class);
        i.putExtra("seller",mPhoneDetails.get(postion).getMember());
        i.putExtra("id",mPhoneDetails.get(postion).getId());
        startActivity(i);
    }

    @Override
    public void attendClick(View v, int position) {

    }

    @Override
    public void notAttendClick(View v, int position) {

    }

    @Override
    public void interestedClick(View v, int position) {

    }
}