package com.example.bloodcamp.Images;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.bloodcamp.Admin.SellerView;
import com.example.bloodcamp.Views.Post;
import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.Vote;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Date;

public class imageupload extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST=1;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private TextView header;
    private EditText bloodCampName,organizer,description,location;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    SimpleDateFormat format;
    Date currentTime;
    boolean isUpdate;
    boolean isClicked;
    private StorageReference mStorageRef;
    private String  downloadImageUrl;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore firestore;
    private StorageTask mUploadTask;
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
        post = new Post();
        isUpdate = getIntent().getBooleanExtra("isUpdate",false);
        uploadId = getIntent().getStringExtra("itemid");
        if(isUpdate)
        {
            header.setText("Edit Phone");
            mButtonUpload.setText("Update");
            action = "update";
        }
        currentTime = new Date();
        format = new SimpleDateFormat(DATE_FORMAT);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference("product");
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
    private void addPhone(Post post,String id)
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
        if (mImageUri != null && count <5 || isUpdate && isClicked ) {
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
            Intent i = new Intent(this, SellerView.class);
            startActivity(i);
        }
        else if(count >= 5 && !isUpdate){
            Toast.makeText(this, "Upload limit exceeds", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Wait upload counts", Toast.LENGTH_SHORT).show();
            System.out.print("count : "+count);
        }

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
        addPhone(post,uploadId);
        mDatabaseRef.child(uploadId).setValue(post);
    }
}