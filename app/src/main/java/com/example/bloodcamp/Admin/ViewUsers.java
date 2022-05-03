package com.example.bloodcamp.Admin;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.Donor;
import com.example.bloodcamp.customer.ConsumerViewPhones;
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
        android.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewUsers.this);
        alertDialog.setTitle("Conformation");
        alertDialog.setMessage("Do you want to delete user data?");
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            firestore
                    .collection("Donor")
                    .document(donorList.remove(position).getId())
                    .delete()
                    .addOnSuccessListener(
                            unused ->
                                    Toast.makeText(ViewUsers.this,"Id is "+key,Toast.LENGTH_SHORT)
                                            .show());
            donorList.remove(position);
            showUsers();
        });
        alertDialog.setNegativeButton("no" ,((dialog, which) -> {
        }));
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();


    }
}