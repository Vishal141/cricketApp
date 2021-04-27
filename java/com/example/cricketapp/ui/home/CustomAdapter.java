package com.example.cricketapp.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cricketapp.R;

import com.example.cricketapp.chatWithFriend;
import com.example.cricketapp.ui.gallery.GalleryFragment;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private ArrayList<String> names,emails,imageUrls;
    private static Activity context;

    public CustomAdapter(Activity context,ArrayList<String> names, ArrayList<String> emails, ArrayList<String> imageUrls){
        this.context = context;
        this.emails = emails;
        this.imageUrls = imageUrls;
        this.names = names;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, final int position) {
        holder.name.setText(names.get(position));
        holder.email.setText(emails.get(position));
        Picasso.with(context).load(imageUrls.get(position)).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, chatWithFriend.class);
                intent.putExtra("email",emails.get(position));
                intent.putExtra("name",names.get(position));
                intent.putExtra("imageUrl",imageUrls.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageView;
        private TextView name,email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profile_picture);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
        }
    }
}
