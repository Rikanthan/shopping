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
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_page.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class imageupload extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST=1;
    private EditText mTextFileName;
    private EditText mTextPrice;
    private EditText mTextQuantity;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private String mCatergory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageupload);
        Button mButtonChooseImage = findViewById(R.id.button_choose_image);
        Button mButtonUpload = findViewById(R.id.button_upload);
        TextView mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mImageView=findViewById(R.id.image_view);
        mProgressBar=findViewById(R.id.progress_bar);
        mTextFileName = findViewById(R.id.edit_text_file_name);
        mTextPrice=findViewById(R.id.edit_text_price);
        mTextQuantity=findViewById(R.id.edit_text_quantiy);
        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");
        mCatergory=getIntent().getExtras().get("category").toString();



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
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showimages();
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
        if(mImageUri !=null)
        {
        StorageReference fileReference=mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

        mUploadTask= fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setProgress(0);
                    }
                },500);
                Toast.makeText(imageupload.this,"Upload successfully",Toast.LENGTH_SHORT).show();

                Upload upload=new Upload(mTextFileName.getText().toString().trim(),taskSnapshot.getUploadSessionUri().toString(),mTextPrice.getText().toString().trim(),mTextQuantity.getText().toString().trim(),mCatergory);
                String uploadId=mDatabaseRef.push().getKey();
                mDatabaseRef.child(uploadId).setValue(upload);

            }
        }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Toast.makeText(imageupload.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress =(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        mProgressBar.setProgress((int)progress);
                    }
                });
        }
        else
        {
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
        }

    }

    private  void showimages()
    {
        Intent i=new Intent(this,ImagesActivity.class);
        startActivity(i);

    }


}