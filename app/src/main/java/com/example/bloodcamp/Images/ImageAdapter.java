package com.example.bloodcamp.Images;
import android.annotation.SuppressLint;
import android.content.Context;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final Context mContext;
    public static ImageAdapterListener mClickListener;
    private final List<Post> mPosts;
    private static OnItemClickListener mListener;
    String userRole = "";
    public ImageAdapter(Context context, List<Post> uploads,ImageAdapterListener listener) {
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
      FirebaseDatabase.getInstance()
              .getReference("user")
              .addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      if(snapshot.exists())
                      {
                          userRole = snapshot.getValue(String.class);
                          assert userRole != null;
                          if(userRole.equals("seller"))
                          {
                              holder.edit.setVisibility(View.VISIBLE);
                              holder.delete.setVisibility(View.VISIBLE);
                          }
                          Post uploadCurrent = mPosts.get(position);
                          holder.bloodcamp.setText(uploadCurrent.getBloodCampName());
                          holder.organizer.setText(uploadCurrent.getOrganizerName());
                          holder.location.setText(uploadCurrent.getLocation());
                          holder.posted.setText(uploadCurrent.getPostedDate());
                          Glide.with(mContext)
                                  .load(uploadCurrent.getImageUri())
                                  .placeholder(R.mipmap.loading)
                                  .into(holder.imageView);
                          holder.description.setText(uploadCurrent.getDescription());
                          int total = uploadCurrent.getVote().getTotalVote();
                          int interst = uploadCurrent.getVote().getInterestedVote()*100;
                          int notInter = uploadCurrent.getVote().getNotAttendVote()*100;
                          int attend = uploadCurrent.getVote().getAttendVote()*100;
                          holder.interestedBar.setProgress((int) interst/total) ;
                          holder.notAttedBar.setProgress((int) notInter/total);
                          holder.attendBar.setProgress((int)attend/total);

                      }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

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
        public TextView bloodcamp,organizer,location,description,posted;
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