package com.example.cricketapp.ui.gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cricketapp.R;
import com.example.cricketapp.chatActivity;
import com.example.cricketapp.register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class post extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dref;
    private StorageReference sref;

    TextView email;
    ImageView imageView;

    private Uri url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        email = findViewById(R.id.email);
        imageView = findViewById(R.id.image);

        mAuth = FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dref = firebaseDatabase.getReference().child("Posts");
        sref = FirebaseStorage.getInstance().getReference().child("Posts");

        Intent intent = getIntent();
        url = intent.getData();

        email.setText(user.getEmail());
        imageView.setImageURI(url);

    }

    public void send(View view){
        EditText comment = findViewById(R.id.comment);
        Toast.makeText(getApplicationContext(),"Sending",Toast.LENGTH_LONG).show();
        if (comment.getText()!=null && !comment.getText().toString().equals("")){
            final String com = comment.getText().toString();
            final String id = UUID.randomUUID().toString();
            sref.child(id).putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("sending");
                    sref.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dref.child(id).child("email").setValue(user.getEmail());
                            dref.child(id).child("url").setValue(uri.toString());
                            dref.child(id).child("comment").setValue(com);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Failed\n"+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(getApplicationContext(),"Post added",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), chatActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Add a Comment");
            alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();
        }
    }
}