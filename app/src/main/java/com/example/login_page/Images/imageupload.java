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

import com.example.login_page.Admin.admin;
import com.example.login_page.Holder.Items;
import com.example.login_page.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class imageupload extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST=1;
    private EditText mTextFileName;
    private EditText mTextPrice;
    private EditText mTextQuantity;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private String CategoryName, Description, Price, Pname, downloadImageUrl;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private String mCatergory;
    private String productRandomKey;

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
        mCatergory=getIntent().getExtras().get("category").toString();
        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference(mCatergory);


//mCatergory="fruit";

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
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            final UploadTask uploadTask=fileReference.putFile(mImageUri);
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
                                       downloadImageUrl=task.getResult().toString();
                                       Upload upload = new Upload(mTextFileName.getText().toString().trim(),
                                               downloadImageUrl,mTextPrice.getText().toString(),mTextQuantity.getText().toString(),mCatergory);
                                       String uploadId = mDatabaseRef.push().getKey();
                                       mDatabaseRef.child(uploadId).setValue(upload);
                                       Items items=new Items(downloadImageUrl,mCatergory,mTextQuantity.getText().toString(),mTextFileName.getText().toString(),mTextPrice.getText().toString());
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
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);

        mDatabaseRef.updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(imageupload.this, admin.class);
                            startActivity(intent);


                            Toast.makeText(imageupload.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(imageupload.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private  void showimages()
    {
        Intent i=new Intent(this,ImagesActivity.class);
        startActivity(i);

    }


}