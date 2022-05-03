package com.example.bloodcamp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.Donor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.UserWriteRecord;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewUsers extends AppCompatActivity implements UserAdapter.UserAdapterListener{
    private RecyclerView mRecyclerView;
    private UserAdapter userAdapter;
    private FirebaseFirestore firestore;
    private List<Donor> donorList;
    private String key = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);
        mRecyclerView = findViewById(R.id.user_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        donorList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        showUsers();
    }

    public void showUsers()
    {
        donorList.clear();
        firestore.collection("Donor")
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful())
                   {
                       for(QueryDocumentSnapshot document: task.getResult())
                       {
                           if(document.exists())
                           {
                               Donor donor = document.toObject(Donor.class);
                                key = document.getId();
                               donorList.add(donor);
                           }
                       }
                       userAdapter = new UserAdapter(ViewUsers.this,donorList, ViewUsers.this);
                       mRecyclerView.setAdapter(userAdapter);
                   }
                });
    }
    @Override
    public void deleteClick(View v, int position) {
        Toast.makeText(ViewUsers.this,"Id is "+key,Toast.LENGTH_SHORT).show();
    }
}