package com.example.login_page.Product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.login_page.R;

import com.example.login_page.Images.Upload;
import com.example.login_page.adapter.recycler_adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Show_items_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    private DatabaseReference myRef;
    private ArrayList<Upload> messagesList;
    private recycler_adapter recyclerAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items_);

        recyclerView=findViewById(R.id.new_recycler);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        myRef= FirebaseDatabase.getInstance().getReference();

        messagesList=new ArrayList<>();
        ClearAll();
        GetDataFromFirebase();




    }

    private void GetDataFromFirebase()
    {
        Query query=myRef.child(("uploads"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshotn:snapshot.getChildren() )
                {
                    Upload messages=new Upload();

                    messages.setImageUrl(snapshotn.child("imageUrl").getValue().toString());
                    messages.setName(snapshotn.child("name").getValue().toString());

                    messagesList.add(messages);
                }
                recyclerAdapter=new recycler_adapter(getApplicationContext(),messagesList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void ClearAll()
    {
        if(messagesList !=null)
        {
            messagesList.clear();
            if(recyclerAdapter !=null)
            {
                recyclerAdapter.notifyDataSetChanged();
            }
        }

        messagesList=new ArrayList<>();
    }
}