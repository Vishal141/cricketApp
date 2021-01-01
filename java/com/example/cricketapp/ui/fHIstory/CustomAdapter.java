package com.example.cricketapp.ui.fHIstory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cricketapp.R;
import com.example.cricketapp.ui.matchHistory.Scoreboard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private static Activity context;
    private static ArrayList<String> emails,matchKeys;

    public CustomAdapter(Activity c,ArrayList<String> e,ArrayList<String> m){
        context = c;
        emails = e;
        matchKeys = m;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(context).inflate(R.layout.match_layout,parent,false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter.ViewHolder holder, final int position) {
        holder.setLayout(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Scoreboard.class);
                intent.putExtra("url",holder.url);
                intent.putExtra("email",emails.get(position));
                intent.putExtra("key",matchKeys.get(position));
                intent.putExtra("commentUrl",holder.commentUrl);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return matchKeys.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView team1,team2,score1,score2,over1,over2,bt1,bt2,bl1,bl2,result;
        View view;
        String url,commentUrl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            team1 = itemView.findViewById(R.id.team1);
            team2 = itemView.findViewById(R.id.team2);
            score1 = itemView.findViewById(R.id.score1);
            score2 = itemView.findViewById(R.id.score2);
            over1 = itemView.findViewById(R.id.over1);
            over2 = itemView.findViewById(R.id.over2);
            bt1 = itemView.findViewById(R.id.bt1);
            bt2 = itemView.findViewById(R.id.bt2);
            bl1 = itemView.findViewById(R.id.bl1);
            bl2 = itemView.findViewById(R.id.bl2);
            result = itemView.findViewById(R.id.status);
        }

        public void setLayout(int index){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dref = firebaseDatabase.getReference().child("Matches").child(emails.get(index)).child(matchKeys.get(index));
            dref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String,String> map = (HashMap<String, String>)snapshot.getValue();
                    team1.setText(map.get("team 1"));
                    team2.setText(map.get("team 2"));
                    score1.setText(map.get("score1"));
                    score2.setText(map.get("score2"));
                    over1.setText(map.get("over1"));
                    over2.setText(map.get("over2"));
                    bt1.setText(map.get("current bat 1"));
                    bt2.setText(map.get("current bat 2"));
                    bl1.setText(map.get("current bowl 1"));
                    bl2.setVisibility(View.INVISIBLE);
                    result.setVisibility(View.INVISIBLE);

                    url = map.get("scoreboard");
                    commentUrl = map.get("CommentUrl");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
