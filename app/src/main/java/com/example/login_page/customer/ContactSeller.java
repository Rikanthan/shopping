package com.example.login_page.customer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.login_page.R;
import com.example.login_page.Views.Member;
import com.example.login_page.Views.PhoneDetails;
import com.google.firebase.firestore.FirebaseFirestore;

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
private FirebaseFirestore firestore;
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
        firestore = FirebaseFirestore.getInstance();
        showSeller();
        showPhone();
    }

    public void showSeller()
    {
        firestore.collection("Seller")
                .document(sellerId)
                .get()
                .addOnSuccessListener(snapshot ->{
                   if(snapshot.exists())
                   {
                       Member member = snapshot.toObject(Member.class);
                       sellerName.setText(member.getName());
                       sellerEmail.setText(member.getEmail());
                       sellerPhone.setText(String.valueOf(member.getMobile()));
                       sellerLocation.setText(member.getLocation());
                   }
                });
    }
    public void showPhone()
    {
        firestore.collection("Phone")
                .document(uploadId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists())
                    {
                        PhoneDetails details = documentSnapshot.toObject(PhoneDetails.class);
                        phone.setText(details.getPhone());
                        description.setText(details.getDescription());
                        battery.setText(details.getBattery());
                        camera.setText(details.getCamera());
                        ram.setText(details.getRam());
                        storage.setText(details.getStorage());
                        fingerPrint.setText(details.getFingerPrint());
                        connection.setText(details.getConnection());
                    }
                });
    }
}