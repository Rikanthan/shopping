package com.example.bloodcamp.Bloodcamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.bloodcamp.Images.ImageAdapter;
import com.example.bloodcamp.Images.imageupload;
import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SeePosts extends AppCompatActivity implements ImageAdapter.ImageAdapterListener {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgress;
    private FirebaseFirestore firestore;
    private List<Post> mPost, backupPost;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_posts);
        mRecyclerView = findViewById(R.id.show_bloodcamp);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgress = findViewById(R.id.progress_circle);
        mPost = new ArrayList<>();
        backupPost = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        showPostForBloodCamp();

    }
    public void showPostForBloodCamp()
    {
        String id = firebaseAuth.getCurrentUser().getUid();
        firestore.collection("Post")
                .whereEqualTo("bloodCampId", id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.exists())
                            {
                                Post post = document.toObject(Post.class);
                                    mPost.add(post);
                                    backupPost.add(post);
                            }
                        }
                        mAdapter = new ImageAdapter(SeePosts.this, mPost, SeePosts.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mProgress.setVisibility(View.GONE);
                    }
                    else {
                        mProgress.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin__new,menu);
        final View addPost = menu.findItem(R.id.add_phone).getActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_phone:
                Intent intent = new Intent(this, imageupload.class);
                intent.putExtra("isUpdate",false);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void editClick(View v, int position) {

    }

    @Override
    public void deleteClick(View v, int position) {

    }

    @Override
    public void itemClick(View v, int position) {

    }

    @Override
    public void attendClick(View v, int position) {

    }

    @Override
    public void notAttendClick(View v, int position) {

    }

    @Override
    public void interestedClick(View v, int position) {

    }
}