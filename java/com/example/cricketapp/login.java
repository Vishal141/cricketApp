package com.example.cricketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private SharedPreferences sp;

    EditText email,password;
    CardView cardView;
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        heading = findViewById(R.id.heading);
        cardView = findViewById(R.id.cardView);

        mAuth = FirebaseAuth.getInstance();

        sp = getSharedPreferences("com.example.cricketapp", Context.MODE_PRIVATE);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Intent intent = getIntent();
        Boolean logout = intent.getBooleanExtra("logout",false);

        if (logout.booleanValue())
            sp.edit().putString("code","notNow");

        String code = sp.getString("code","notNow");
        System.out.println(code);
        if (code.equals("save"))
        {
            String em = sp.getString("email","a@gmail.com");
            String ps = sp.getString("password","*@");

            email.setText(em);
            password.setText(ps);

           // signIn(new View(this));
            gotoChatActivity();
        }

    }

    public void forgotPassword(View view){
        if (email.getText()!=null && !(email.getText().toString().equals("")))
        {
            mAuth.sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Email sent",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void signIn(final View view){
       if (email.getText()!=null && !(email.getText().toString().equals("")) && password.getText()!=null && !(password.getText().toString().equals(""))){
           mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()){
                               Toast.makeText(getApplicationContext(),"signIn successful",Toast.LENGTH_LONG).show();
                               String code = sp.getString("code","notNow");
                               if (code.equals("notNow"))
                                   saveAlert();
                               else{
                                   gotoChatActivity();
                               }
                           }
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                           view.setBackgroundColor(Color.parseColor("#4807D8"));
                       }
                   });
       }else {
           Toast.makeText(this,"all fields are mandatory",Toast.LENGTH_LONG).show();
       }
       view.setBackgroundColor(Color.GREEN);
    }

    public void signUp(final View view){
        Intent intent = new Intent(this,register.class);
        Pair[] pairs = new Pair[2];
        pairs[0]=new Pair<View,String>(heading,"heading");
        pairs[1]=new Pair<View,String>(cardView,"loginCard");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,pairs);
        startActivity(intent,options.toBundle());
    }

    public void saveEmailAndPassword(){
        sp.edit().putString("code","save").apply();
        sp.edit().putString("email",email.getText().toString()).apply();
        sp.edit().putString("password",password.getText().toString()).apply();
    }

    public void saveAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Do you want to save email and password?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveEmailAndPassword();
                        gotoChatActivity();
                    }
                })
                .setNeutralButton("not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp.edit().putString("code","notNow").apply();
                        gotoChatActivity();
                    }
                })
                .setNegativeButton("never", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp.edit().putString("code","never").apply();
                        gotoChatActivity();
                    }
                });
        alert.show();
    }

    public void gotoChatActivity(){
        Intent intent = new Intent(this,chatActivity.class);
        startActivity(intent);
    }


}