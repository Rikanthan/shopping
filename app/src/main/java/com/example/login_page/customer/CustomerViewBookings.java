package com.example.login_page.customer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.login_page.Holder.BookingHolder;
import com.example.login_page.Holder.Bookings;
import com.example.login_page.Home.Home;
import com.example.login_page.Home.MainActivity;
import com.example.login_page.R;
import com.example.login_page.Views.ShowConfirmOrders;
import com.example.login_page.Views.ShowOrders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class CustomerViewBookings extends AppCompatActivity implements  BookingHolder.OnItemClickListener {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    String date = "";
    FirebaseAuth firebaseAuth;
    FirebaseUser fuser;
    BookingHolder mAdapter;
    List<Bookings> newcartlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view_booking);
        recyclerView  =  findViewById(R.id.show_bookings);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        newcartlist=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        if(process("Confirmedbooking") == 0)
        {
            process("booking");
        }
    }
    @Override
    public void onItemClick(int position)
    {
        Bookings myBookings = newcartlist.get(position);
        Intent intent = new Intent(this, ShowConfirmOrders.class);
        intent.putExtra("id",myBookings.getId());
        intent.putExtra("date",myBookings.getDate());
        startActivity(intent);
    }
    public int process(String getBooking)
    {
        final String uid    =   firebaseAuth.getCurrentUser().getUid();
        databaseReference.child(getBooking).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(dataSnapshot.exists())
                    {
                        boolean itsMe = false;
                        Bookings mycart = dataSnapshot.getValue(Bookings.class);
                        String name=mycart.getName();
                        String phone = mycart.getPhone();
                        String location = mycart.getLocation();
                        String price = mycart.getPrice();
                        date = mycart.getDate();
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
                            Bookings mybookings = new Bookings(userId,price,name,phone,location,dataSnapshot.getKey());
                            newcartlist.add(mybookings);
                        }
                    }

                }

                mAdapter = new BookingHolder(CustomerViewBookings.this, newcartlist);
                mAdapter.setOnItemClickListener(CustomerViewBookings.this);
                recyclerView.setAdapter(mAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CustomerViewBookings.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
     return newcartlist.size();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.customer_home:
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                return true;
            case R.id.show_cart:
                Intent i = new Intent(this, ShowOrders.class);
                startActivity(i);
                return true;
            case R.id.cust_bookings:
                Intent i1 = new Intent(this, CustomerViewBookings.class);
                startActivity(i1);
                return true;
            case R.id.customer_logout:
                Intent i2 = new Intent(this, MainActivity.class);
                startActivity(i2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}