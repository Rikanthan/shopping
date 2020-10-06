package com.example.login_page;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class imageupload extends AppCompatActivity {
    Button ch,up;
    ImageView img;
    DatabaseReference myreff;
    long imgnewid=0;
    private EditText productname;
    private EditText catergory;
    private EditText quantity;
    private EditText price;
    private RadioButton kg;
    private RadioButton m;
    private RadioButton l;
    private RadioButton no;
    StorageReference mStorageRef;
    private StorageTask uploadTask;
    Product product;
    public Uri imguri;
    public  String str="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageupload);
      /*  productname=findViewById(R.id.productname);
        catergory=findViewById(R.id.catergory);
        quantity=findViewById(R.id.quantity);
        price=findViewById(R.id.price);
        kg=findViewById(R.id.kg);
        no=findViewById(R.id.other);
        m=findViewById(R.id.meter);
        l=findViewById(R.id.litre);
*/
        product =new Product();
        myreff=FirebaseDatabase.getInstance().getReference().child("Product");
        myreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imgnewid=(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mStorageRef= FirebaseStorage.getInstance().getReference("Images");
        ch=(Button)findViewById(R.id.choosefile);
        img=(ImageView)findViewById(R.id.imageupload);
        up=(Button)findViewById(R.id.upload);
        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filechooser();
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask !=null && uploadTask.isInProgress())
                {
                Toast.makeText(imageupload.this,"Upload in progress",Toast.LENGTH_LONG).show();
                }
                else

                Fileuploader();

            }
        });
    }
    private  String getExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType( uri));
    }
    private  void Fileuploader()
    {
        String imageid;
        imageid=System.currentTimeMillis()+"."+getExtension(imguri);
        product.setProductName(productname.getText().toString().trim());
        product.setCatergory(catergory.getText().toString().trim());
        //product.setTypequantity(str.trim());
        int q=Integer.parseInt(quantity.getText().toString().trim());
        int p=Integer.parseInt(price.getText().toString().trim());
       // product.setQuantity(q);
        product.setPrice(p);
        myreff.push().setValue(product);


        StorageReference Ref=mStorageRef.child(imageid);
       uploadTask= Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                      //  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(imageupload.this,"Image uploades successfully",Toast.LENGTH_LONG).show();
                     }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }
    private void Filechooser()
    {
        Intent intent=new Intent();
        intent.setType("image/'");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data !=null && data.getData()!=null)
        {
            imguri=data.getData();
            img.setImageURI(imguri);
        }
    }

   /* public void getquantity(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.kg:
                if(checked)
                    str = "kg";
                break;
            case R.id.litre:
                if(checked)
                    str = "l";
                break;
            case R.id.other:
                if(checked)
                    str = "no";
                break;
            case R.id.meter:
                if(checked)
                    str = "m";
                break;
        }
        //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }*/
}