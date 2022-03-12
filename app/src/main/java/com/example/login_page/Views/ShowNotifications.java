package com.example.login_page.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.login_page.Holder.NotificationHolder;
import com.example.login_page.R;
import com.example.login_page.notification.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowNotifications extends AppCompatActivity
        implements NotificationHolder.OnItemClickListener {
    NotificationHolder adapter;
    String uid;
    List<Data> dataList;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notifications);
        recyclerView = findViewById(R.id.notification_recycle);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        dataList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
        databaseReference = FirebaseDatabase
                                .getInstance()
                                .getReference("Notification")
                                .child(uid);
        showNotification();
    }

    public void showNotification()
    {
        databaseReference.orderByValue().addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dataList.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            if(dataSnapshot.exists())
                            {
                                Data data = dataSnapshot.getValue(Data.class);
                                dataList.add(data);
                            }
                        }
                        Collections.reverse(dataList);
                        adapter = new NotificationHolder(ShowNotifications.this,dataList);
                        adapter.setOnItemClickListener(ShowNotifications.this);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ShowNotifications.this,
                                            error.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Data myData = dataList.get(position);
        databaseReference.child(myData.getDate()).removeValue();
        Data newData = new Data(myData.getTitle(), myData.getMessage(), myData.getDate(), "Read");
        databaseReference.child(myData.getDate()).setValue(newData);
    }
    public void markAll(View v)
    {
        databaseReference.removeValue();
        for(Data editData: dataList)
        {
            editData.setStatus("Read");
            databaseReference.child(editData.getDate()).setValue(editData);
        }

    }
    public void clearAll(View v)
    {
        dataList.clear();
        databaseReference.removeValue();
    }
}