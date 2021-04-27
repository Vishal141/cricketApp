package com.example.cricketapp;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWithFriend extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dref;

    private CircleImageView profile_image;
    private TextView profile_name;

    private String email,name,currentUserEmail,imageUrl;

    EditText msg;
    LinearLayout msgView;
    NestedScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_friend);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        name = intent.getStringExtra("name");
        imageUrl = intent.getStringExtra("imageUrl");

        profile_image = findViewById(R.id.chatImage);
        profile_name = findViewById(R.id.name);

        profile_name.setText(name);
        Picasso.with(getApplicationContext()).load(imageUrl).into(profile_image);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dref = firebaseDatabase.getReference().child("chat");

        currentUserEmail = user.getEmail();

        msg = findViewById(R.id.msg);
        msgView = findViewById(R.id.msgView);
        scroll = findViewById(R.id.sView);
        receive();
    }

    public void send(View view){
        String message = msg.getText().toString();
        String time = getTime();
        dref.child(register.getMd5(currentUserEmail)).child(register.getMd5(email)).
                child(time).child("send").setValue(message);
        dref.child(register.getMd5(email)).child(register.getMd5(currentUserEmail)).
                child(time).child("receive").setValue(message);

        msg.setText("");
        msg.clearFocus();
    }

    public View getTextView(String message,String type){
        LayoutInflater li = LayoutInflater.from(this);
        View v;
        if (type.equals("send"))
            v = li.inflate(R.layout.msg_right,null);
        else
            v = li.inflate(R.layout.msg_text_left,null);
        TextView textView = v.findViewById(R.id.msg);
        textView.setText(message);
        ((RelativeLayout)v).setPadding(10,10,10,10);
        return v;
    }

    public void receive(){
        dref.child(register.getMd5(currentUserEmail)).child(register.getMd5(email)).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        msgView.removeAllViews();
                        for (DataSnapshot ds:snapshot.getChildren()){
                            HashMap<String,String> map = (HashMap<String, String>)ds.getValue();
                            String ms = ds.getKey();
                            String ms1 = map.get("send");
                            String ms2 = map.get("receive");
                            if (ms1 != null)
                                msgView.addView(getTextView(ms+": "+ms1,"send"));
                            if (ms2!=null)
                                msgView.addView(getTextView(ms+": "+ms2,"receive"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String currentDate = format.format(date);
        return currentDate;
    }

}