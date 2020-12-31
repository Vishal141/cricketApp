package com.example.cricketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView image1;
    TextView textView;
    Animation top_anim,ball_anim;

    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image1 = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        top_anim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        ball_anim = AnimationUtils.loadAnimation(this,R.anim.ball_anim);

        image1.setAnimation(top_anim);
        textView.setAnimation(ball_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context,login.class);

                Pair[] pairs = new Pair[2];
                pairs[0]=new Pair<View,String>(textView,"heading");
                pairs[1] = new Pair<View,String>(image1,"loginCard");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);

                startActivity(intent,options.toBundle());
                finish();
            }
        },5000);

    }
}