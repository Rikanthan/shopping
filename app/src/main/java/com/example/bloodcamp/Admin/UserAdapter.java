package com.example.bloodcamp.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.UserRole;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ImageViewHolder> {
    private final Context mContext;
    public static UserAdapterListener mClickListener;
    private final List<UserRole> mUserRole;
    public UserAdapter(Context context, List<UserRole> UserRoles, UserAdapterListener listener) {
        mContext = context;
        mUserRole = UserRoles;
        this.mClickListener = listener;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_users, parent, false);
        return new ImageViewHolder(v);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        UserRole userRole = mUserRole.get(position);
        holder.name.setText(userRole.getName());
        holder.userType.setText(userRole.getUserRole());
    }
    @Override
    public int getItemCount() {
        return mUserRole.size();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name,userType;
        public ImageButton delete;
        public LinearLayout linearLayoutManager;

        @Override
        public void onClick(View v) {


        }

        public ImageViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            userType = itemView.findViewById(R.id.type);
            linearLayoutManager = (LinearLayout)itemView.findViewById(R.id.show_details);
            linearLayoutManager.setOnClickListener(v -> mClickListener
                    .deleteClick(v,ImageViewHolder.this.getAdapterPosition()));
            itemView.setOnClickListener(this);
        }

    }


    public interface UserAdapterListener{
        void deleteClick(View v, int position);
    }
  

}

