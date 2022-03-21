package com.example.login_page.Images;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.login_page.customer.ConsumerViewPhones;
import com.example.login_page.R;
import com.example.login_page.Views.PhoneDetails;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class imageupload extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST=1;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private EditText phone, description, price, battery,camera,ram,storage,fingerPrint,connection;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    SimpleDateFormat format;
    Date currentTime;
    private StorageReference mStorageRef;
    private String  downloadImageUrl;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    long id = 0;
    long adminId = 0;
    int count = 0;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageupload);
        Button mButtonChooseImage = findViewById(R.id.button_choose_image);
        Button mButtonUpload = findViewById(R.id.button_upload);
        mImageView=findViewById(R.id.image_view);
        mProgressBar=findViewById(R.id.progress_bar);
        phone = findViewById(R.id.phone_name);
        camera = findViewById(R.id.camera);
        battery = findViewById(R.id.battery);
        ram = findViewById(R.id.ram);
        storage = findViewById(R.id.storage);
        description = findViewById(R.id.description);
        fingerPrint = findViewById(R.id.fingerprint);
        connection = findViewById(R.id.network);
        price = findViewById(R.id.price);
        currentTime = new Date();
        format = new SimpleDateFormat(DATE_FORMAT);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageRef= FirebaseStorage.getInstance().getReference("product");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Phone");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    id=(snapshot.getChildrenCount());
                    count = 0;
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    if(snapshot1.exists())
                    {
                        PhoneDetails details = snapshot1.getValue(PhoneDetails.class);
                        if(details.getMember().equals(uid))
                        {
                            count++;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openFileChooser();
            }
        });
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mUploadTask !=null && mUploadTask.isInProgress()){
                Toast.makeText(imageupload.this,"Upload in progress",Toast.LENGTH_SHORT).show();
            }
            else
                {
                    uploadFile();
                }
            }

        });

    }
    private void openFileChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null)
        {
            mImageUri=data.getData();
            // mImageView.setImageURI(mImageUri);
            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile()
    {

        if (mImageUri != null && count <5) {
            System.out.println(count);
            System.out.print("count");
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            final UploadTask uploadTask=fileReference.putFile(mImageUri);
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(imageupload.this, "Upload successful", Toast.LENGTH_LONG).show();
                           Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                               @Override
                               public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                   if(!task.isSuccessful())
                                   {
                                       throw task.getException();
                                   }
                                   downloadImageUrl=fileReference.getDownloadUrl().toString();
                                   return fileReference.getDownloadUrl();
                               }
                           }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                               @Override
                               public void onComplete(@NonNull Task<Uri> task) {
                                   if(task.isSuccessful())
                                   {
                                       String dateNow = format.format(currentTime);
                                       downloadImageUrl=task.getResult().toString();
                                       PhoneDetails details = new PhoneDetails();
                                       details.setBattery(battery.getText().toString());
                                       details.setId(String.valueOf(id+1));
                                       details.setCamera(camera.getText().toString());
                                       details.setImageUri(downloadImageUrl);
                                       details.setFingerPrint(fingerPrint.getText().toString());
                                       details.setConnection(connection.getText().toString());
                                       details.setDescription(description.getText().toString());
                                       details.setRam(ram.getText().toString());
                                       details.setStorage(storage.getText().toString());
                                       details.setPrice(Double.parseDouble(price.getText().toString()));
                                       details.setMember(uid);
                                       details.setUploadTime(dateNow);
                                       details.setPhone(phone.getText().toString());
                                       String uploadId = mDatabaseRef.push().getKey();
                                       mDatabaseRef.child(String.valueOf(id+1)).setValue(details);
                                   }
                               }
                           });

                            /**/
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(imageupload.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        }
        else if(count >= 5){
            Toast.makeText(this, "Upload limit exceeds", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Wait upload counts", Toast.LENGTH_SHORT).show();
            System.out.print("count : "+count);
        }

    }
    

    private  void showimages()
    {
        Intent i=new Intent(this, ConsumerViewPhones.class);
        startActivity(i);

    }


}