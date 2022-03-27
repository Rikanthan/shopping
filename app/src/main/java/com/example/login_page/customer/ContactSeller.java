package com.example.login_page.customer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.login_page.R;
import com.example.login_page.Views.Member;
import com.example.login_page.Views.PhoneDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactSeller extends AppCompatActivity {
private TextView sellerName,
        sellerPhone,
        sellerEmail,
        sellerLocation,
        phone,
        description,
        price,
        battery,
        camera,
        ram,
        storage,
        fingerPrint,
        connection;
String sellerId, uploadId;
public LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_seller);
        sellerId = getIntent().getStringExtra("seller");
        uploadId = getIntent().getStringExtra("id");
        sellerName = findViewById(R.id.seller_name);
        sellerEmail = findViewById(R.id.seller_email);
        sellerPhone = findViewById(R.id.seller_phone);
        sellerLocation = findViewById(R.id.seller_location);
        phone = findViewById(R.id.phone_name);
        description = findViewById(R.id.description);
        battery = findViewById(R.id.battery);
        camera = findViewById(R.id.camera);
        ram = findViewById(R.id.ram);
        storage = findViewById(R.id.storage);
        fingerPrint = findViewById(R.id.fingerprint);
        connection = findViewById(R.id.network);
        layout = findViewById(R.id.seller_details);
        showSeller();
        showPhone();
    }
    public void showSell(View v)
    {
        layout.setVisibility(View.VISIBLE);
    }
    public void showSeller()
    {
        FirebaseDatabase
                .getInstance()
                .getReference("Member")
                .child(sellerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Member member = snapshot.getValue(Member.class);
                    sellerName.setText(member.getName());
                    sellerEmail.setText(member.getEmail());
                    sellerPhone.setText(String.valueOf(member.getMobile()));
                    sellerLocation.setText(member.getLocation());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void showPhone()
    {
        FirebaseDatabase
                .getInstance()
                .getReference("Phone")
                .child(uploadId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    PhoneDetails details = snapshot.getValue(PhoneDetails.class);
                    phone.setText(details.getPhone());
                    description.setText(details.getDescription());
                    battery.setText(details.getBattery());
                    camera.setText(details.getCamera());
                    ram.setText(details.getRam());
                    storage.setText(details.getStorage());
                    fingerPrint.setText(details.getFingerPrint());
                    connection.setText(details.getConnection());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}