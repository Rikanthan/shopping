package com.example.login_page.Images;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.login_page.R;
import com.example.login_page.Views.IndividualItems;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;
public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Fruit");
        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    String Name = upload.getName();
                    String categoryDescription = upload.getmCatergory();
                    String categoryPrice = upload.getmPrice();
                    String categoryImageUrl = upload.getImageUrl();
                    String quantity = upload.getmQuantity();
                    String uploadId = upload.getmuploadId();
                    String catergoryId = upload.getmCatergoryId();
                    Upload uploads=new Upload(
                                                Name,
                                                categoryImageUrl,
                                                categoryPrice,
                                                quantity,
                                                categoryDescription,
                                                uploadId ,
                                                catergoryId
                                                    );
                    mUploads.add(uploads);
                }

                mAdapter = new ImageAdapter(ImagesActivity.this, mUploads);
                mAdapter.setOnItemClickListener(ImagesActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent i=new Intent(ImagesActivity.this, IndividualItems.class);
        i.putExtra("Category","Fruit");
        i.putExtra("index",position);
        startActivity(i);
    }
}