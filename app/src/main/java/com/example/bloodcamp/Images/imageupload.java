package com.example.bloodcamp.Images;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodcamp.Admin.ShowPosts;
import com.example.bloodcamp.Views.Post;
import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.Vote;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class imageupload extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    protected Context context;
    private double latitude, longitude;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private TextView header,chooseDate,chooseTime;
    private EditText bloodCampName, organizer, description, location;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    SimpleDateFormat format;
    Date currentTime;
    boolean isUpdate;
    boolean isClicked;
    private StorageReference mStorageRef;
    private String downloadImageUrl;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore firestore;
    private StorageTask mUploadTask;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME = 1000; // 1 second
    private final long MIN_DIST = 5; // 5 Meters
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String format1 = "";
    String date = "";
    String time = "";
    String getHour = "";
    String getMinute = "";
    String getDigital = "";
    private LatLng latLng;
    long id = 0;
    String uploadId = "";
    int count = 0;
    String uid;
    String action = "upload";
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageupload);
        Button mButtonChooseImage = findViewById(R.id.button_choose_image);
        Button mButtonUpload = findViewById(R.id.button_upload);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        description = findViewById(R.id.description);
        bloodCampName = findViewById(R.id.blood_camp_name);
        organizer = findViewById(R.id.organizer_name);
        location = findViewById(R.id.location);
        header = findViewById(R.id.addnew);
        chooseDate = findViewById(R.id.choosed_date);
        chooseTime = findViewById(R.id.choosed_time);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        post = new Post();
        isUpdate = getIntent().getBooleanExtra("isUpdate",false);
        uploadId = getIntent().getStringExtra("itemid");
        if(isUpdate)
        {
            header.setText("Edit Post");
            mButtonUpload.setText("Update");
            action = "update";
        }
        currentTime = new Date();
        format = new SimpleDateFormat(DATE_FORMAT);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference("product");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //SupportMapFragment googleMap=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("post");
        mButtonChooseImage.setOnClickListener(v -> openFileChooser());
        mButtonUpload.setOnClickListener(v -> {
        if(mUploadTask !=null && mUploadTask.isInProgress()){
            Toast.makeText(imageupload.this,action+" in progress",Toast.LENGTH_SHORT).show();
        }
        else
            {
                uploadFile();
            }
        });

    }
    private void addPost(Post post,String id)
    {
        firestore
                .collection("Post")
                .document(id)
                .set(post);
    }
    private void openFileChooser()
    {
        isClicked = true;
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            mImageUri = data.getData();
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
        if (mImageUri != null  || isUpdate && isClicked ) {
            System.out.println(count);
            System.out.print("count");
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            final UploadTask uploadTask=fileReference.putFile(mImageUri);
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Handler handler = new Handler();
                        handler.postDelayed(
                                () -> mProgressBar.setProgress(0), 500);
                        Toast.makeText(imageupload.this, action+" successful", Toast.LENGTH_LONG).show();
                       Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                           if(!task.isSuccessful())
                           {
                               throw task.getException();
                           }
                           downloadImageUrl = fileReference.getDownloadUrl().toString();
                           return fileReference.getDownloadUrl();
                       }).addOnCompleteListener(task -> {
                           if(task.isSuccessful())
                           {
                               if(!isUpdate)
                               {
                                   uploadId = mDatabaseRef.push().getKey();
                               }
                               downloadImageUrl = task.getResult().toString();
                               addDetails();
                           }
                       });
                    })
                    .addOnFailureListener(e -> Toast.makeText(imageupload.this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        mProgressBar.setProgress((int) progress);
                    });
            count++;
        }
        else if(isUpdate && !isClicked)
        {
            addDetails();
            Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, ShowPosts.class);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "Please choose the file", Toast.LENGTH_SHORT).show();
        }

    }

    public void chooseDate(View v)
    {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        if(dayOfMonth < 10 && monthOfYear < 10)
                        {
                            date = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        }
                        else if(dayOfMonth < 10)
                        {
                            date = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                        }
                        else if(monthOfYear < 10)
                        {
                            date = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                        else
                        {
                            date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }
                        chooseDate.setText(date);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
    }

    public void choosenTime(View v)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
            final Calendar c1 = Calendar.getInstance();
            mHour = c1.get(Calendar.HOUR_OF_DAY);
            mMinute = c1.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (vie, hour, minute) -> {
                        getHour = String.valueOf(hour);
                        getMinute = String.valueOf(minute);
                        if (hour == 0) {
                            hour += 12;
                            format1 = ":00 AM";
                        } else if (hour == 12) {
                            format1 = ":00 PM";
                        } else if (hour > 12) {
                            hour -= 12;
                            format1 = ":00 PM";
                        } else {
                            format1 = ":00 AM";
                        }
                        if(minute < 10 && hour <10)
                        {
                            time = "0"+hour+":0"+minute+format1;
                        }
                        else if(minute < 10)
                        {
                            time = hour+":0"+minute+format1;
                        }
                        else if(hour < 10)
                        {
                            time = "0"+hour+":"+minute+format1;
                        }
                        else
                        {
                            time = hour + ":" + minute+format1;
                        }
                        chooseTime.setText(time);
                    }, mHour, mMinute, false);
            timePickerDialog.show();

    }


    private void addDetails()
    {
        Post post = new Post();
        String dateNow = format.format(currentTime);
        Vote vote = new Vote();
        post.setLocation(location.getText().toString());
        post.setPostedDate(dateNow);
        post.setVote(vote);
        post.setOrganizerName(organizer.getText().toString());
        post.setDescription(description.getText().toString());
        post.setBloodCampName(bloodCampName.getText().toString());
        post.setImageUri(downloadImageUrl);
        post.setPostId(uploadId);
        post.setBloodCampId(uid);
        post.setLongitude(longitude);
        post.setLatitude(latitude);
        post.setDate(date);
        post.setTime(time);
        addPost(post,uploadId);
        mDatabaseRef.child(uploadId).setValue(post);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        locationListener = location -> {
            try{
                latLng = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("My position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }

    }

}