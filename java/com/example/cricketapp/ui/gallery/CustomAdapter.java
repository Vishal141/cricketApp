package com.example.cricketapp.ui.gallery;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cricketapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<String> emails,urls,comments;

    public CustomAdapter(Activity context, ArrayList<String> emails,ArrayList<String> urls,ArrayList<String> comments)
    {
        this.context = context;
        this.emails = emails;
        this.urls = urls;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        holder.getEmail().setText(emails.get(position));
        holder.getComment().setText(comments.get(position));
        Picasso.with(context).load(urls.get(position)).into(holder.getPost());
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView email,comment;
        private ImageView post;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.email);
            post = itemView.findViewById(R.id.post);
            comment = itemView.findViewById(R.id.comment);
        }

        public TextView getEmail(){
            return this.email;
        }

        public TextView getComment(){
            return this.comment;
        }

        public ImageView getPost(){
            return this.post;
        }
    }
}
