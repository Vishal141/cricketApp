package com.example.cricketapp.ui.matchHistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cricketapp.R;
import com.example.cricketapp.register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Scoreboard extends AppCompatActivity {
    private FirebaseStorage storage;
    private StorageReference sref,commentRef;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dref;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    TabLayout tabLayout;
    LinearLayout comments;
    EditText comment;

    TableLayout tableLayout1,tableLayout2;
    TextView team1,team2,score1,score2,over1,over2,bt1,bt2,bl1,result,title;

    private String url,commentUrl;
    private File file,commentFile;
    private String[] details,h1,h2;
    private StringTokenizer tokenizer;
    private BufferedReader reader;

    private ArrayList<String> batting_1,batting_2,bowling_1,bowling_2,commentWithEmail;
    private ArrayList<String[] > batting_11,batting_21,bowling_12,bowling_22;

    private String currentUserEmail,matchKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        tabLayout = findViewById(R.id.tablayout);
        setListener();
        comments = findViewById(R.id.comments);

        team1 = findViewById(R.id.team1);
        team2 = findViewById(R.id.team2);
        score1 = findViewById(R.id.score1);
        score2 = findViewById(R.id.score2);
        over1 = findViewById(R.id.over1);
        over2 = findViewById(R.id.over2);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bl1 = findViewById(R.id.bl1);
        result =findViewById(R.id.status);
        title = findViewById(R.id.title);

        comment = findViewById(R.id.comment);

        tableLayout1 = findViewById(R.id.battingScore1);
        tableLayout2 = findViewById(R.id.bowlingScore1);
        tableLayout1.removeAllViews();
        tableLayout2.removeAllViews();

        h1 = new String[]{"Name","R","B","4","6","SR"};
        h2 = new String[]{"Name","O","M","R","W","Eco"};

        addRow(h1,tableLayout1,0);
        addRow(h2,tableLayout2,0);

        batting_1 = new ArrayList<String>(11);
        batting_2 = new ArrayList<String>(11);
        bowling_1 = new ArrayList<String>(11);
        bowling_2 = new ArrayList<String>(11);

        batting_11 = new ArrayList<String[]>(11);
        batting_21 = new ArrayList<String[]>(11);
        bowling_12 = new ArrayList<String[]>(11);
        bowling_22 = new ArrayList<String[]>(11);

        commentWithEmail = new ArrayList<String>();


        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        commentUrl = intent.getStringExtra("commentUrl");
        currentUserEmail = intent.getStringExtra("email");
        matchKeys = intent.getStringExtra("key");
        firebaseDatabase = FirebaseDatabase.getInstance();
        dref = firebaseDatabase.getReference().child("Matches").
                child(currentUserEmail).child(matchKeys);
        mAuth = FirebaseAuth.getInstance();
        user  = mAuth.getCurrentUser();

        storage = FirebaseStorage.getInstance();
        sref = storage.getReferenceFromUrl(url);
        commentRef = storage.getReferenceFromUrl(commentUrl);

        setLayout();
        getFile();
    }


    public void setListener(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("1st Inning"))
                    setTab1();
                else
                    setTab2();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void getFile(){
        try {
            file = File.createTempFile("score",".txt");
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sref.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                System.out.println(taskSnapshot.getTotalByteCount());
                Toast.makeText(getApplicationContext(),"download successful",Toast.LENGTH_LONG).show();
                extractData();
                extractRow(batting_1,"batting_1");
                extractRow(bowling_1,"bowling_1");
                extractRow(batting_2,"batting_2");
                extractRow(bowling_2,"bowling_2");
                setTab1();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"download failed",Toast.LENGTH_LONG).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                Toast.makeText(getApplicationContext(),"download completed",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void extractData(){
        String line="";
        try {
            reader = new BufferedReader(new FileReader(file));
            line = reader.readLine();
            int i = 0;
            while (line!=null){
                if (line.equals("**********************************")){
                    ++i;
                    line = reader.readLine();
                    continue;
                }

                switch (i){
                    case 1:batting_1.add(line);break;
                    case 2:bowling_1.add(line);break;
                    case 3:batting_2.add(line);break;
                    case 4:bowling_2.add(line);break;
                }

                line = reader.readLine();
            }

            System.out.println(batting_1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void extractRow(ArrayList<String> list,String which){
        for (String line:list){
            int j=0;
            tokenizer = new StringTokenizer(line,"-*-");
            details = new String[6];
            while (tokenizer.hasMoreTokens() && j<6){
                details[j++]=tokenizer.nextToken();
            }
            switch (which)
            {
                case "batting_1":batting_11.add(details);break;
                case "bowling_1":bowling_12.add(details);break;
                case "batting_2":batting_21.add(details);break;
                case "bowling_2":bowling_22.add(details);break;
            }
        }
    }



    public void addRow(String[] details, TableLayout tableLayout, int index) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

        TextView name = new TextView(this);
        name.setText(details[0]);
        name.setWidth(120);
        name.setGravity(Gravity.CENTER);

        TextView run = new TextView(this);
        run.setText(details[1]);
        run.setWidth(50);
        run.setGravity(Gravity.CENTER);

        TextView ball = new TextView(this);
        ball.setText(details[2]);
        ball.setWidth(50);
        ball.setGravity(Gravity.CENTER);

        TextView four = new TextView(this);
        four.setText(details[3]);
        four.setWidth(50);
        four.setGravity(Gravity.CENTER);

        TextView six = new TextView(this);
        six.setText(details[4]);
        six.setWidth(40);
        six.setGravity(Gravity.CENTER);

        TextView sr = new TextView(this);
        sr.setText(details[5]);
        sr.setWidth(50);
        sr.setGravity(Gravity.CENTER);

        //  row.setLayoutParams(lp);
        row.addView(name, 0);
        row.addView(run, 1);
        row.addView(ball, 2);
        row.addView(four, 3);
        row.addView(six, 4);
        row.addView(sr, 5);

        tableLayout.setStretchAllColumns(true);
        tableLayout.addView(row, index);
    }

    public void setTab1(){
        tableLayout1.removeAllViews();
        tableLayout2.removeAllViews();
        addRow(h1,tableLayout1,0);
        addRow(h2,tableLayout2,0);
        int i=1,j=1;
        for (String[] array:batting_11)
            addRow(array,tableLayout1,i++);
        for (String[] array:bowling_12)
            addRow(array,tableLayout2,j++);
    }

    public void setTab2(){
        tableLayout1.removeAllViews();
        tableLayout2.removeAllViews();
        addRow(h1,tableLayout1,0);
        addRow(h2,tableLayout2,0);
        int i=1,j=1;
        for (String[] array:batting_21)
            addRow(array,tableLayout1,i++);
        for (String[] array:bowling_22)
            addRow(array,tableLayout2,j++);
    }

    public void setLayout(){
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> map = (HashMap<String, String>)snapshot.getValue();
                team1.setText(map.get("team 1"));
                team2.setText(map.get("team 2"));
                title.setText(team1.getText().toString()+" Vs "+team2.getText().toString());
                score1.setText(map.get("score1"));
                score2.setText(map.get("score2"));
                over1.setText(map.get("over1"));
                over2.setText(map.get("over2"));
                if (map.get("current bat 1")!=null)
                {   bt1.setText(map.get("current bat 1"));
                    bt2.setText(map.get("current bat 2"));
                    bl1.setText(map.get("current bowl 1"));
                    result.setVisibility(View.GONE);
                }else
                {
                    bt1.setVisibility(View.GONE);
                    bt2.setVisibility(View.GONE);
                    bl1.setVisibility(View.GONE);
                    result.setText(map.get("status"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dref.child("CommentUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getComments();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getComments(){
        try {
            commentFile = File.createTempFile("comment",".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        commentRef.getFile(commentFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                setComments();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setComments(){
        try {
            BufferedReader commentReader = new BufferedReader(new FileReader(commentFile));
            String line;
            comments.removeAllViews();
            while ((line=commentReader.readLine())!=null){
                commentWithEmail.add(line);
                StringTokenizer tokenizer = new StringTokenizer(line,"-*-");
                String e="",c="";
                if (tokenizer.hasMoreTokens())
                    e = tokenizer.nextToken();
                if (tokenizer.hasMoreTokens())
                    c = tokenizer.nextToken();
                View view;
                if (e.equals(user.getEmail()))
                    view = LayoutInflater.from(this).inflate(R.layout.msg_right,null);
                else
                    view = LayoutInflater.from(this).inflate(R.layout.msg_text_left,null);
                if (c.equals(""))
                    c=e;
                view.setPadding(10,10,10,10);
                TextView textView = view.findViewById(R.id.msg);
                textView.setText(c);
                comments.addView(view,0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(View view){
        String com =user.getEmail()+"-*-"+comment.getText().toString();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(commentFile));
            for (String line:commentWithEmail){
                writer.append(line+"\n");
            }
            writer.append(com+"\n");
            writer.flush();
            writer.close();

            commentRef.putFile(Uri.parse(commentFile.toURI().toString())).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_LONG).show();
                    commentRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dref.child("CommentUrl").setValue(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        comment.setText("");
    }
}