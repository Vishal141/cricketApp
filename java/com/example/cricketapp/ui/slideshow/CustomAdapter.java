package com.example.cricketapp.ui.slideshow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cricketapp.R;
import com.example.cricketapp.chatActivity;
import com.example.cricketapp.register;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.cricketapp.ui.slideshow.SlideshowFragment.currentUserEmail;
import static com.example.cricketapp.ui.slideshow.SlideshowFragment.currentUserImageUrl;
import static com.example.cricketapp.ui.slideshow.SlideshowFragment.currentUserName;
import static com.example.cricketapp.ui.slideshow.SlideshowFragment.dref;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private static ArrayList<String> names,emails,imageUrls;
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
        return names.size();
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
            System.out.println("clicked");
            TextView e = v.findViewById(R.id.email);
            TextView n = v.findViewById(R.id.name);
            String em = e.getText().toString();
            dref.child("Friends").child(register.getMd5(currentUserEmail)).
                    child(register.getMd5(em)).child("email").setValue(e.getText().toString());
            dref.child("Friends").child(register.getMd5(currentUserEmail)).
                    child(register.getMd5(em)).child("name").setValue(n.getText().toString());
            dref.child("Friends").child(register.getMd5(currentUserEmail)).child(register.getMd5(em)).
                    child("imageUrl").setValue(imageUrls.get(emails.indexOf(em)));
            dref.child("Friends").child(register.getMd5(em)).
                    child(register.getMd5(currentUserEmail)).child("email").setValue(currentUserEmail);
            dref.child("Friends").child(register.getMd5(em)).
                    child(register.getMd5(currentUserEmail)).child("name").setValue(currentUserName);
            dref.child("Friends").child(register.getMd5(em)).
                    child(register.getMd5(currentUserEmail)).child("imageUrl").setValue(currentUserImageUrl);

            Toast.makeText(context,"Friend Added",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, chatActivity.class);
            context.startActivity(intent);
        }
    }
}
