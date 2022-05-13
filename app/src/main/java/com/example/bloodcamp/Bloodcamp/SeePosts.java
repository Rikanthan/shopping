package com.example.bloodcamp.Bloodcamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodcamp.Admin.ShowDetails;
import com.example.bloodcamp.Post.PostAdapter;
import com.example.bloodcamp.Post.PostUpload;
import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SeePosts extends AppCompatActivity implements PostAdapter.ImageAdapterListener {
    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private ProgressBar mProgress;
    private FirebaseFirestore firestore;
    private List<Post> mPost, backupPost;
    private FirebaseAuth firebaseAuth;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_posts);
        mRecyclerView = findViewById(R.id.show_bloodcamp);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgress = findViewById(R.id.progress_circle);
        mPost = new ArrayList<>();
        textView = findViewById(R.id.warning_text);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        showPostForBloodCamp();
        if(mPost != null && mPost.isEmpty())
        {
            textView.setVisibility(View.VISIBLE);
        }
    }
    public void showPostForBloodCamp()
    {
        mPost.clear();
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
                            }
                        }
                        mAdapter = new PostAdapter(SeePosts.this, mPost, SeePosts.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mProgress.setVisibility(View.GONE);
                        if(!mPost.isEmpty())
                        {
                            textView.setVisibility(View.GONE);
                        }
                    }
                    else {
                        mProgress.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin__new,menu);
       // final View addPost = menu.findItem(R.id.add_phone).getActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.profile:
                Intent i = new Intent(this, ShowDetails.class);
                i.putExtra("id",firebaseAuth.getUid());
                i.putExtra("UserRole","BloodCamp");
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    public void addPost(View v)
    {
        Intent intent = new Intent(this, PostUpload.class);
        intent.putExtra("isUpdate",false);
        startActivity(intent);
    }
    @Override
    public void editClick(View v, int position) {
        Intent intent = new Intent(SeePosts.this,PostUpload.class);
        intent.putExtra("Action",true);
        intent.putExtra("PostId",mPost.get(position).getPostId());
        startActivity(intent);
    }

    @Override
    public void deleteClick(View v, int position) {
        android.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(SeePosts.this);
        alertDialog.setTitle("Conformation");
        alertDialog.setMessage("Do you want to delete post?");
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            Post post = mPost.get(position);
            mPost.remove(position);
            firestore
                    .collection("Post")
                    .document(post.getPostId())
                    .delete()
                    .addOnSuccessListener( task -> {
                        Toast.makeText(SeePosts.this,"Post delete successfully",Toast.LENGTH_SHORT).show();
                        showPostForBloodCamp();
                    });
        });
        alertDialog.setNegativeButton("No",(dialog,which) ->{

        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
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