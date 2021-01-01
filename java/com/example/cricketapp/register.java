package com.example.cricketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.UUID;

public class register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dref;

    EditText name,email,password;

    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        sp = getSharedPreferences("com.example.cricketapp",MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dref = firebaseDatabase.getReference();

    }

    public void signUp(final View view){
        if ((name.getText()!=null && !(name.getText().toString().equals(""))) &&
                email.getText()!=null && !(email.getText().toString().equals("")) &&
                password.getText()!=null && !(password.getText().toString().equals("")))
        {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task!=null){
                                Toast.makeText(getApplicationContext(),"user created",Toast.LENGTH_LONG).show();
                                signIn(email.getText().toString(),password.getText().toString());
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
        }else{
            Toast.makeText(this,"all fields are mandatory",Toast.LENGTH_LONG).show();
        }
        view.setBackgroundColor(Color.GREEN);
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
        finish();
    }

    public void saveEmailAndPassword(){
        sp.edit().putString("code","save").apply();
        sp.edit().putString("email",email.getText().toString()).apply();
        sp.edit().putString("password",password.getText().toString()).apply();
    }

    public void signIn(final String email, String pass){
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String path = getMd5(email);
                        dref.child("Users").child(path).child("email").setValue(email);
                        dref.child("Users").child(path).child("name").setValue(name.getText().toString());
                        dref.child("Users").child(path).child("imageUrl").setValue("https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png");
                        saveAlert();
                    }
                });
    }

    public static String  getMd5(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger no = new BigInteger(1,messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length()<32)
                hashtext = '0'+hashtext;
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}