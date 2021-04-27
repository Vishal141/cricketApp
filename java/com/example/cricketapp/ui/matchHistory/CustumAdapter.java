package com.example.cricketapp.ui.matchHistory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cricketapp.R;
import com.example.cricketapp.register;
import com.example.cricketapp.ui.fHIstory.CustomAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class CustumAdapter extends RecyclerView.Adapter<CustumAdapter.ViewHolder> {

    private static Activity context;
    private static ArrayList<String> matchKeys;
    private static String currentUserEmail;

    public CustumAdapter(Activity c, ArrayList<String> keys,String email){
        context = c;
        matchKeys = keys;
        currentUserEmail = email;
    }

    @NonNull
    @Override
    public CustumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_layout,parent,false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustumAdapter.ViewHolder holder, final int position) {
        setLayout(position,holder);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context,Scoreboard.class);
               intent.putExtra("url",holder.url);
               intent.putExtra("commentUrl",holder.commentUrl);
               intent.putExtra("email",register.getMd5(currentUserEmail));
               intent.putExtra("key",matchKeys.get(position));
               context.startActivity(intent);
            }
        });
    }

    public void setLayout(int index, final ViewHolder holder){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dref = firebaseDatabase.getReference().child("Matches").
                child(register.getMd5(currentUserEmail)).child(matchKeys.get(index));
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> map = (HashMap<String, String>)snapshot.getValue();
                holder.team1.setText(map.get("team 1"));
                holder.team2.setText(map.get("team 2"));
                holder.score1.setText(map.get("score1"));
                holder.score2.setText(map.get("score2"));
                holder.over1.setText(map.get("over1"));
                holder.over2.setText(map.get("over2"));
                if (map.get("current bat 1")!=null)
                {
                    holder.bt1.setText(map.get("current bat 1"));
                    holder.bt2.setText(map.get("current bat 2"));
                    holder.bl1.setText(map.get("current bowl 1"));
                }else
                {
                    holder.bt1.setVisibility(View.GONE);
                    holder.bt2.setVisibility(View.GONE);
                    holder.bl1.setVisibility(View.GONE);
                    holder.bowling.setVisibility(View.GONE);
                    holder.batting.setVisibility(View.GONE);
                }
                holder.bl2.setVisibility(View.GONE);
                holder.result.setText(map.get("status"));
                holder.url = map.get("scoreboard");
                holder.commentUrl = map.get("CommentUrl");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return matchKeys.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView team1,team2,score1,score2,over1,over2,bt1,bt2,bl1,bl2,result,batting,bowling;
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
            batting = itemView.findViewById(R.id.textView7);
            bowling = itemView.findViewById(R.id.textView11);
        }
    }
}
