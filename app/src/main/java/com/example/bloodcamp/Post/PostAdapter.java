package com.example.bloodcamp.Post;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ImageViewHolder> {
    private final Context mContext;
    public static ImageAdapterListener mClickListener;
    private final List<Post> mPosts;
    private static OnItemClickListener mListener;
    String userRole = "";
    public PostAdapter(Context context, List<Post> uploads, ImageAdapterListener listener) {
        mContext = context;
        mPosts = uploads;
        this.mClickListener = listener;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {
        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore
                .getInstance()
                .collection("UserRole")
                .document(uid)
                .get()
                .addOnCompleteListener(snapshot -> {
                    String userRole = snapshot.getResult().getData().toString();
                        if(userRole.contains("Blood"))
                        {
                            holder.edit.setVisibility(View.VISIBLE);
                            holder.delete.setVisibility(View.VISIBLE);
                        }
                        Post uploadCurrent = mPosts.get(position);
                        holder.bloodcamp.setText(uploadCurrent.getBloodCampName());
                        holder.organizer.setText("Organized By \n"+uploadCurrent.getOrganizerName());
                        holder.location.setText(uploadCurrent.getLocation());
                        holder.posted.setText(uploadCurrent.getPostedDate());
                        Glide.with(mContext)
                                .load(uploadCurrent.getImageUri())
                                .placeholder(R.drawable.loader)
                                .into(holder.imageView);
                        holder.description.setText(uploadCurrent.getDescription());
                        for(String s : uploadCurrent.getVote().getVotedPeople())
                        {
                            if(s.contains("not") && s.contains(uid))
                            {
                                holder.notAttend.setBackgroundColor(Color.GREEN);
                            }
                            else if(s.contains("attend") && s.contains(uid))
                            {
                                holder.attend.setBackgroundColor(Color.GREEN);
                            }
                            else if(s.contains("interest") && s.contains(uid))
                            {
                                holder.interested.setBackgroundColor(Color.GREEN);
                            }
                        }
                        int total = uploadCurrent.getVote().getTotalVote();
                        int interst = uploadCurrent.getVote().getInterestedVote()*100;
                        int notInter = uploadCurrent.getVote().getNotAttendVote()*100;
                        int attend = uploadCurrent.getVote().getAttendVote()*100;
                        if(total > 0)
                        {
                            holder.interestedBar.setProgress((int) interst/total) ;
                            holder.notAttedBar.setProgress((int) notInter/total);
                            holder.attendBar.setProgress((int)attend/total);
                            holder.attendper.setText(String.valueOf(attend/total)+"%");
                            holder.notattendper.setText(String.valueOf(notInter/total)+"%");
                            holder.interestper.setText(String.valueOf(interst/total)+"%");
                        }
                        else
                        {
                            holder.interestedBar.setProgress(total) ;
                            holder.notAttedBar.setProgress(total);
                            holder.attendBar.setProgress(total);
                        }

                });



        holder.edit.setOnClickListener(
                v -> mClickListener.editClick(v,position)
        );
        holder.delete.setOnClickListener(
                v -> mClickListener.deleteClick(v,position)
        );
        holder.attend.setOnClickListener(
                v -> mClickListener.attendClick(v,position)
        );
        holder.notAttend.setOnClickListener(
                v -> mClickListener.notAttendClick(v,position)
        );
        holder.interested.setOnClickListener(
                v -> mClickListener.interestedClick(v,position)
        );


    }
    @Override
    public int getItemCount() {
        return mPosts.size();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public TextView bloodcamp,organizer,location,description,posted,attendper,notattendper,interestper;
        public ImageButton edit,delete,view, attend,notAttend, interested;
        public ImageView imageView;
        public LinearLayout linearLayoutManager;
        public ProgressBar attendBar,notAttedBar, interestedBar;
        @Override
        public void onClick(View v) {
            if(mListener !=null)
            {
                int position = getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION)
                {
                    mListener.onItemClick(position);
                }
            }
        }

        public ImageViewHolder(View itemView) {
            super(itemView);
            bloodcamp = itemView.findViewById(R.id.blood_camp_name);
            organizer = itemView.findViewById(R.id.organizer_name);
            location = itemView.findViewById(R.id.location);
            posted = itemView.findViewById(R.id.posteddate);
            description = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.image_view_upload);
            linearLayoutManager = (LinearLayout)itemView.findViewById(R.id.show_phone);
            edit = itemView.findViewById(R.id.edit);
            interestper = itemView.findViewById(R.id.interstedper);
            attendper = itemView.findViewById(R.id.attendper);
            notattendper = itemView.findViewById(R.id.notattendper);
            delete = itemView.findViewById(R.id.delete);
            view = itemView.findViewById(R.id.view);
            attend = itemView.findViewById(R.id.voteattend);
            notAttend = itemView.findViewById(R.id.votenot);
            interested = itemView.findViewById(R.id.voteintersted);
            attendBar = itemView.findViewById(R.id.progressBarAttend);
            notAttedBar = itemView.findViewById(R.id.progressBarNot);
            interestedBar = itemView.findViewById(R.id.progressBarInterested);
            linearLayoutManager.setOnClickListener(v -> mClickListener
                    .editClick(v, ImageViewHolder.this.getAdapterPosition()));
            linearLayoutManager.setOnClickListener(v -> mClickListener.
                    deleteClick(v,ImageViewHolder.this.getAdapterPosition()));
            linearLayoutManager.setOnClickListener(v -> mClickListener.
                    attendClick(v,ImageViewHolder.this.getAdapterPosition()));
            linearLayoutManager.setOnClickListener(v -> mClickListener.
                    notAttendClick(v,ImageViewHolder.this.getAdapterPosition()));
            linearLayoutManager.setOnClickListener(v -> mClickListener.
                    interestedClick(v,ImageViewHolder.this.getAdapterPosition()));
            linearLayoutManager.setOnClickListener(v -> mClickListener.
                    itemClick(v,ImageViewHolder.this.getAdapterPosition()));
            itemView.setOnClickListener(this);
        }
    }
    public interface ImageAdapterListener{
        void editClick(View v,int position);
        void deleteClick(View v,int position);
        void itemClick(View v,int position);
        void attendClick(View v,int position);
        void notAttendClick(View v,int position);
        void interestedClick(View v,int position);
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}