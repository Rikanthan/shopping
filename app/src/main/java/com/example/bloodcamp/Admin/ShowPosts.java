package com.example.bloodcamp.Admin;

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
import android.widget.SearchView;
import android.widget.Toast;
import com.example.bloodcamp.Images.ImageAdapter;
import com.example.bloodcamp.Images.imageupload;
import com.example.bloodcamp.MapsActivity;
import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.Donor;
import com.example.bloodcamp.Views.Post;
import com.example.bloodcamp.Views.Vote;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowPosts extends AppCompatActivity implements ImageAdapter.ImageAdapterListener{
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private DatabaseReference mDatabaseRef;
    private FirebaseFirestore firestore;
    private List<Post> mPost;
    private List<Post> backupPost;
    private SearchView mSearchView;
    private boolean isClicked = false;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);
        mSearchView = findViewById(R.id.search_items);
        mPost = new ArrayList<>();
        backupPost = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Post");
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mSearchView.setVisibility(View.GONE);
        getDonorLocation();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query != null )
                {
                    if(!query.isEmpty())
                    {
                        search(query);

                    }
                    else
                    {
                      getDonorLocation();
                    }
                }
                else
                {
                  getDonorLocation();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(query != null )
                {
                    if(!query.isEmpty())
                    {
                        search(query);

                    }
                    else
                    {
                       getDonorLocation();
                    }
                }
                else
                {
                   getDonorLocation();
                }
                return true;
            }
        });
    }

    public void search(String s)
    {
        List<Post> searchPost = new ArrayList<>();
        searchPost.addAll(mPost);
        mPost.clear();
        for(Post post: searchPost)
        {
            System.out.println(post.getBloodCampName().toLowerCase());
            if(post.getBloodCampName() != null
                    && post.getBloodCampName().toLowerCase().contains(s.toLowerCase()))
            {
                mPost.add(post);
            }
            mAdapter = new ImageAdapter(ShowPosts.this, mPost, ShowPosts.this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
    public static  ArrayList<Post> removeDuplicates(ArrayList<Post> list)
    {
        ArrayList<Post> newList = new ArrayList<Post>();
        for (Post element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        return newList;
    }
    public void getDonorLocation()
    {
            firestore.collection("Donor")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            Donor donor = task.getResult().toObject(Donor.class);
                            showPost(donor.getLongitude(),donor.getLatitude());
                        }
                    });
    }

    public void showPost(double longitude, double latitude)
    {
        firestore.collection("Post")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.exists())
                            {
                                Post post = document.toObject(Post.class);
                                if(post.getLatitude() - latitude < 1.0 &&
                                        post.getLongitude() - longitude < 1.0)
                                {
                                    mPost.add(post);
                                    backupPost.add(post);
                                }
                            }

                        }
                        mAdapter = new ImageAdapter(ShowPosts.this, mPost, ShowPosts.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mProgressCircle.setVisibility(View.GONE);
                    }
                    else {
                        mProgressCircle.setVisibility(View.INVISIBLE);
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
        Intent i = new Intent(this,imageupload.class);
        i.putExtra("isUpdate",true);
        //String id = mPost.get(position).getId();
        i.putExtra("itemid","id");
        startActivity(i);
    }

    @Override
    public void deleteClick(View v, int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowPosts.this);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Are you want to delete this item?");
        alertDialog.setPositiveButton("ok", (dialog, which) -> {
            String id = mPost.get(position).getPostId();
            mPost.remove(position);
            firestore
                    .collection("Post")
                    .document(id)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(),"Delete Success", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ShowPosts.this, ShowPosts.class);
                        startActivity(i);
                    });
            mDatabaseRef.child(id).removeValue();
        });

        alertDialog.setNegativeButton("cancel", (dialog, which) -> {
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    @Override
    public void itemClick(View v, int postion) {
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("long",mPost.get(postion).getLongitude());
        i.putExtra("lat",mPost.get(postion).getLatitude());
        i.putExtra("name",mPost.get(postion).getBloodCampName());
        startActivity(i);
//        startActivity(i);
    }

    @Override
    public void attendClick(View v, int position) {
        Post rePost = mPost.get(position);
        List<Post> attendPost = new ArrayList<>();
        attendPost.addAll(mPost);
        attendPost.remove(position);
        Vote vote = rePost.getVote();
        vote.setAttendVote();
        rePost.setVote(vote);
        firestore.collection("Post")
                .document(rePost.getPostId()).set(rePost);
        mPost.clear();
        mProgressCircle.setVisibility(View.VISIBLE);
      getDonorLocation();
    }

    @Override
    public void notAttendClick(View v, int position) {
        Post rePost = mPost.get(position);
        Vote vote = new Vote();
              vote = rePost.getVote();
        List<String> voted = new ArrayList<>();
//        if(vote.getVotedPeople().size() > 0)
//        {
//            //voted.addAll(vote.getVotedPeople());
//        }
        voted.add(firebaseAuth.getUid());
        vote.setVotedPeople(voted);
        vote.setNotAttendVote();
        rePost.setVote(vote);
        firestore.collection("Post")
                .document(rePost.getPostId()).set(rePost);
        mPost.clear();
//        firestore.collection("Vote")
//                .document(rePost.getPostId())
//                .set(voted);
        mProgressCircle.setVisibility(View.VISIBLE);
        getDonorLocation();;
    }

    @Override
    public void interestedClick(View v, int position) {
        Post rePost = mPost.get(position);
        Vote vote = rePost.getVote();
        vote.setInterestedVote();
        rePost.setVote(vote);
        firestore.collection("Post")
                .document(rePost.getPostId()).set(rePost);
        mPost.clear();
        mProgressCircle.setVisibility(View.VISIBLE);
        getDonorLocation();
    }
}