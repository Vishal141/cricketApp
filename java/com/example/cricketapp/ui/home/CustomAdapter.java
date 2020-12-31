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
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        Picasso.with(context).load(imageUrls.get(position)).into(holder.getImageView());
        holder.getName().setText(names.get(position));
        holder.getEmail().setText(emails.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView name,email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            imageView = itemView.findViewById(R.id.profile_picture);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
        }

        public ImageView getImageView(){
            return this.imageView;
        }
        public TextView getName(){
            return this.name;
        }
        public TextView getEmail(){
            return this.email;
        }

        @Override
        public void onClick(View v) {
            TextView em = v.findViewById(R.id.email);
            TextView n = v.findViewById(R.id.name);
            Intent intent = new Intent(context, chatWithFriend.class);
            intent.putExtra("email",em.getText().toString());
            intent.putExtra("name",n.getText().toString());
            context.startActivity(intent);
        }
    }
}
