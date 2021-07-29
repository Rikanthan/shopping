package com.example.login_page.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.login_page.Home.MainActivity;
import com.example.login_page.Images.imageupload;
import com.example.login_page.R;
import com.example.login_page.notification.SendNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class AdminCatorgory extends AppCompatActivity {
    Toolbar tool;
    TextView bookingText;
    long bookingCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_catergory);
        tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
    }

    private void setSupportActionBar(Toolbar tool) {
    }

    public void veg(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Vegetables");
        startActivity(i);
    }
    public void fru(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Fruit");
        startActivity(i);
    }
    public void med(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Medical");
        startActivity(i);
    }
    public void pap(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Paper");
        startActivity(i);
    }
    public void hyg(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Hygiene");
        startActivity(i);
    }
    public void cos(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Cosmetrics");
        startActivity(i);
    }
    public void sof(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Softdrinks");
        startActivity(i);
    }
    public void sna(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Snacks");
        startActivity(i);
    }
    public void bak(View v)
    {
        Intent i=new Intent(this, imageupload.class);
        i.putExtra("category","Bakery");
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navi,menu);
        final View showBooking = menu.findItem(R.id.customerBookings).getActionView();
        bookingText = (TextView) showBooking.findViewById(R.id.update_bookings_text);
        long bookings = getBookingCount();
            bookingText.setText("3");
            bookingText.setVisibility(View.VISIBLE);

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.customerBookings:
                Intent i1 = new Intent(this, AdminViewConfirmBookings.class);
                startActivity(i1);
                return true;
            case R.id.inventory:
                Intent i2 = new Intent(this, AdminViewProducts.class);
                startActivity(i2);
                return true;
            case R.id.addNewProducts:
                Intent i3 = new Intent(this, SendNotification.class);
                startActivity(i3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public long getBookingCount()
    {
        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance()
                .getReference("booking")
                .child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            bookingCount = snapshot.getChildrenCount();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
        return bookingCount;
    }

}