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
import com.example.bloodcamp.Views.Donor;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ImageViewHolder> {
    private final Context mContext;
    public static UserAdapterListener mClickListener;
    private final List<Donor> mDonor;
    public UserAdapter(Context context, List<Donor> Donors, UserAdapterListener listener) {
        mContext = context;
        mDonor = Donors;
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
        Donor donor = mDonor.get(position);
        holder.name.setText(donor.getName());
        holder.phone.setText(donor.getPhoneNumber());
        holder.address.setText(donor.getAddress());
        holder.dob.setText(donor.getDOB());
        holder.nic.setText(donor.getNIC());
        holder.email.setText(donor.getEmail());
        holder.delete.setOnClickListener(
                v -> mClickListener.deleteClick(v,position)
        );
    }
    @Override
    public int getItemCount() {
        return mDonor.size();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name,address,dob,nic,email,phone;
        public ImageButton delete;
        public LinearLayout linearLayoutManager;

        @Override
        public void onClick(View v) {


        }

        public ImageViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            address = itemView.findViewById(R.id.location);
            dob = itemView.findViewById(R.id.dob);
            nic = itemView.findViewById(R.id.nic);
            delete = itemView.findViewById(R.id.delete);
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

