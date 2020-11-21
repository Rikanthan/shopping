package com.example.login_page.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login_page.Images.Upload;
import com.example.login_page.R;

import java.util.ArrayList;

public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.ViewHolder> {

    private static final String Tag="RecyclerView";
    private Context mContext;
    private ArrayList<Upload> messagesList;


    public recycler_adapter(Context mContext,ArrayList<Upload> messagesList)
    {
        this.mContext=mContext;
        this.messagesList=messagesList;
    }

    @NonNull
    @Override
    public recycler_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.items_show,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycler_adapter.ViewHolder holder, int position) {

        holder.textView.setText(messagesList.get(position).getName());
        Glide.with(mContext).load(messagesList.get(position).getImageUrl()).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.show_image);
            textView=itemView.findViewById(R.id.show_text);

        }
    }

}
