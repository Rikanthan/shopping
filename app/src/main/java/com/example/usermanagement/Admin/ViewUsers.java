package com.example.usermanagement.Admin;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import usermanagement.R;
import com.example.usermanagement.Views.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewUsers extends AppCompatActivity implements UserAdapter.UserAdapterListener{
    private RecyclerView mRecyclerView;
    private UserAdapter userAdapter;
    private FirebaseFirestore firestore;
    private List<User> userList;
    private String key = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);
        mRecyclerView = findViewById(R.id.user_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        showUsers();
    }

    public void showUsers()
    {
        userList.clear();
        firestore.collection("User")
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful())
                   {
                       for(QueryDocumentSnapshot document: task.getResult())
                       {
                           if(document.exists())
                           {
                               User user = document.toObject(User.class);
                                key = document.getId();
                               userList.add(user);
                           }
                       }
                       userAdapter = new UserAdapter(ViewUsers.this,userList, ViewUsers.this);
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
            Objects.requireNonNull(FirebaseAuth
                    .getInstance()
                    .signInWithEmailAndPassword(
                            userList.get(position).getEmail(),
                            userList.get(position).getPassword())
                            .addOnCompleteListener(task -> {
                               if(task.isSuccessful())
                               {
                                   task.getResult().getUser().delete();
                               }
                            }));
            firestore
                    .collection("User")
                    .document(userList.remove(position).getId())
                    .delete()
                    .addOnSuccessListener(
                            unused ->
                                    Toast.makeText(ViewUsers.this,"User delete successfully",Toast.LENGTH_SHORT)
                                            .show());
            //userList.remove(position);
            showUsers();
        });
        alertDialog.setNegativeButton("no" ,((dialog, which) -> {
        }));
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();


    }
}