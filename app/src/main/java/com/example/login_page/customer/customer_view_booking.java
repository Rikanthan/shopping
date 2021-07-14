package com.example.login_page.customer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.login_page.Holder.BookingHolder;
import com.example.login_page.Holder.Bookings;
import com.example.login_page.R;
import com.example.login_page.Views.ShowBookings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class customer_view_booking extends AppCompatActivity implements  BookingHolder.OnItemClickListener {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth firebaseAuth;
    FirebaseUser fuser;
    BookingHolder mAdapter;
    List<Bookings> newcartlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_booking);
        recyclerView=findViewById(R.id.show_bookings);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        newcartlist=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser();

        databaseReference= FirebaseDatabase.getInstance().getReference("Confirmedbooking");
        process();

    }
    @Override
    public void onItemClick(int position)
    {


    }
    public void process()
    {
        final String uid=firebaseAuth.getCurrentUser().getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                        boolean itsMe = false;
                        Bookings mycart = dataSnapshot.getValue(Bookings.class);
                        String name=mycart.getName();
                        String phone = mycart.getPhone();
                        String location = mycart.getLocation();
                        String price = mycart.getPrice();
                        String date = mycart.getDate();
                        String userId = mycart.getId();
                        mycart.setDate(date);
                        mycart.setPhone(phone);
                        mycart.setLocation(location);
                        mycart.setId(userId);
                        mycart.setName(name);
                        mycart.setPrice(price);
                        if(userId.contains(uid))
                        {
                            itsMe = true;
                            Bookings mybookings = new Bookings(userId,price,name,phone,location,date);
                            newcartlist.add(mybookings);
                        }

                }

                mAdapter = new BookingHolder(customer_view_booking.this, newcartlist);
                mAdapter.setOnItemClickListener(customer_view_booking.this);
                recyclerView.setAdapter(mAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(customer_view_booking.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}