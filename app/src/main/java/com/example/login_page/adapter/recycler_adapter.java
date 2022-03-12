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
    public String url[] ={"https://i5.walmartimages.ca/images/Enlarge/094/514/6000200094514.jpg","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUWvjiMCFD7nMkFtWIF1WHPlL5IAUgF28H2A&usqp=CAU"}; //"https://s3.amazonaws.com/koya-dev-videos/kindness/8da807aa-1e1e-413d-bf9b-5bb084646593/medialibrary/9456621508/videos/1eb78337-d569-41bd-95ad-153d9098de03.png";


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
       // Glide.with(mContext).load(url[position]).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
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
