package com.example.cricketapp.ui.create_match;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MatchActivity extends AppCompatActivity {

    //layout variables
    private TableLayout tableLayout1, tableLayout2;
    private LinearLayout ll;
    private View outLayout;
    private CheckBox rotate, strikeOrNot;
    private LayoutInflater li;

    private static int Strick = 1, nonStrick = 2, bowler = 0;
    private static int run = 0, wicket = 0, over = 0, ball = 0, ballOver = 0;
    private int overWicket = 0, overRun = 0;

    private static boolean isOverFinish = false;

    //firebase variables
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;
    private DatabaseReference dref;
    private StorageReference sref;

    private Date date;
    private SimpleDateFormat format;
    private String d;
    private static String update;

    private Context context = this;

    private File FILE=null;

    private TextView matchName, team1, team2, score1, score2, over1, over2, newBall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        tableLayout1 = (TableLayout) findViewById(R.id.batting);
        tableLayout2 = (TableLayout) findViewById(R.id.bowling);
        ll = (LinearLayout) findViewById(R.id.thisOver);
        ll.setPadding(5, 5, 5, 5);

        li = LayoutInflater.from(this);
        outLayout = li.inflate(R.layout.catch_out, null);
        rotate = (CheckBox) outLayout.findViewById(R.id.strikeRotate);
        strikeOrNot = (CheckBox) outLayout.findViewById(R.id.striker);

        matchName = (TextView) findViewById(R.id.matchName);
        team1 = (TextView) findViewById(R.id.team1);
        team2 = (TextView) findViewById(R.id.team2);
        score1 = (TextView) findViewById(R.id.score1);
        score2 = (TextView) findViewById(R.id.score2);
        over1 = (TextView) findViewById(R.id.over1);
        over2 = (TextView) findViewById(R.id.over2);

        //Initialize firebase variables
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dref = firebaseDatabase.getReference().child("Matches");
        sref = FirebaseStorage.getInstance().getReference();

        format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        date = new Date();

        setLayoutInitially("initial");

        //Handling intent from activities
        Intent intent = getIntent();
        String from = intent.getStringExtra("code");
        if (from.equals("opners selected"))
            openers();
        else {
            if (from.equals("new bowler selected"))
                BowlerSelected("new");
            else {
                if (from.equals("previos bowler selected"))
                    BowlerSelected("previos");
                else {
                    if (from.equals("batsman selected"))
                        batsmanSelected();
                    else{
                        if (from.equals("scoreboard"))
                            setLayoutInitially("scoreboard");
                    }
                }
            }
        }

        if (isOverFinish){
            changeOver();
            isOverFinish = false;
        }

    }

    //function to setLayout initially
    public void setLayoutInitially(String when) {
        if (when.equals("initial"))
        {
            matchName.setText(Global.matchName);
            team1.setText(Global.team1);
            team2.setText(Global.team2);

            if (Global.inning == 1) {
                if (Global.batting == 1) {
//                Global.score1=score;
//                Global.over1 = O;
                    score2.setText("Yet to");
                    over2.setText("bat");
                    score1.setText(Global.score1 + "");
                    over1.setText(Global.over1 + "");
                } else {
//                Global.score2 = score;
//                Global.over2 = O;
                    score1.setText("Yet to");
                    over1.setText("bat");
                    score2.setText(Global.score2);
                    over2.setText(Global.over2 + "");
                }
            } else {
                if (Global.batting == 2) {
//                Global.score1=score;
//                Global.over1 = O;
                    score1.setText(Global.score1);
                    over1.setText(Global.over1);
                    score2.setText(Global.score2 + "");
                    over2.setText(Global.over2 + "");
                } else {
//                Global.score2 = score;
//                Global.over2 = O;
                    score2.setText(Global.score2 + "");
                    over2.setText(Global.over2 + "");
                    score1.setText(Global.score1);
                    over1.setText(Global.over1);
                }
            }
        }else{
            reCopy();
            tableLayout1.addView(Global.batrow2,2);
        }
    }

    //Initialize match in firebase database
    public void initMatch(String bt1,String bt2,String bl1,String bl2,String when){
        dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                child("status").setValue("running");
        dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                child("team 1").setValue(Global.team1);
        dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                child("team 2").setValue(Global.team2);
        dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                child("score1").setValue(Global.score1);
        dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                child("score2").setValue(Global.score2);
        dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                child("over1").setValue(Global.over1);
        dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                child("over2").setValue(Global.over2);
        if (when.equals("initial"))
        {
            dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                    child("current bat 1").setValue(bt1);
            dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                    child("current bat 2").setValue(bt2);
            dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                    child("current bowl 1").setValue(bl1);
            dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                    child("current bowl 2").setValue(bl2);
        }else
        {
            if (when.equals("bt1"))
                dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                        child("current bat 1").setValue(bt1);
            else {
                if (when.equals("bt2"))
                    dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                            child("current bat 2").setValue(bt1);
                else
                    dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                            child("current bowl 1").setValue(bt1);
            }
        }
    }

    //set layout after each ball
    public void setLayout() {
        matchName.setText(Global.matchName);
        team1.setText(Global.team1);
        team2.setText(Global.team2);

        String score = run + "-" + wicket;

        if (ball == 6) {
            over++;
            ball = 0;
        }
        String O = over + "." + ball;

        if (Global.inning == 1) {
            if (Global.batting == 1) {
                Global.score1 = score;
                Global.over1 = O;
                score2.setText("Yet to");
                over2.setText("bat");
                score1.setText(Global.score1 + "");
                over1.setText(Global.over1 + "");
            } else {
                Global.score2 = score;
                Global.over2 = O;
                score1.setText("Yet to");
                over1.setText("bat");
                score2.setText(Global.score2);
                over2.setText(Global.over2 + "");
            }
        } else {
            if (Global.batting == 2) {
                Global.score1 = score;
                Global.over1 = O;
                score1.setText(Global.score1);
                over1.setText(Global.over1);
                score2.setText(Global.score2 + "");
                over2.setText(Global.over2 + "");
            } else {
                Global.score2 = score;
                Global.over2 = O;
                score2.setText(Global.score2 + "");
                over2.setText(Global.over2 + "");
                score1.setText(Global.score1);
                over1.setText(Global.over1);
            }
        }
    }

    //creating textview for this over layout
    public TextView newTextview(String text) {
        newBall = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        newBall.setText(text);
        newBall.setGravity(Gravity.CENTER);
        newBall.setTextColor(Color.WHITE);
        newBall.setWidth(100);
        newBall.setHeight(100);
        newBall.setBackgroundColor(Color.parseColor("#651FFF"));
        newBall.setLayoutParams(params);
        return newBall;
    }

    //creating Table Row to add in table layout
    public TableRow addRow(String Name, TableLayout tableLayout, int index) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

        TextView name = new TextView(this);
        name.setText(Name);
        name.setWidth(120);
        name.setGravity(Gravity.CENTER);

        TextView run = new TextView(this);
        run.setText("0");
        run.setWidth(50);
        run.setGravity(Gravity.CENTER);

        TextView ball = new TextView(this);
        ball.setText("0");
        ball.setWidth(50);
        ball.setGravity(Gravity.CENTER);

        TextView four = new TextView(this);
        four.setText("0");
        four.setWidth(50);
        four.setGravity(Gravity.CENTER);

        TextView six = new TextView(this);
        six.setText("0");
        six.setWidth(40);
        six.setGravity(Gravity.CENTER);

        TextView sr = new TextView(this);
        sr.setText("0.0");
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
        return row;
    }

    //swap the strick
    public void swap() {
        int temp = Strick;
        Strick = nonStrick;
        nonStrick = temp;
    }

    // handle out event for different cases
    public void Out(View view) {
        Button button = (Button) view;
        String text = button.getText().toString();
        if(ballOver==5)
            isOverFinish = true;
        System.out.println(isOverFinish);
        wicket++;
        switch (text) {
            case "catch":
                catchOut("catch");
                break;
            case "LBW":
                lbwOrBold("lbw");
                break;
            case "bold":
                lbwOrBold("b");
                break;
            case "RunOut":
                catchOut("RunOut");
                break;
            case "stumps":
                catchOut("stumps");
                break;
        }
    }

    //lbw and bold event
    public void lbwOrBold(String type) {
        overWicket++;
        ball++;
        ballOver++;
        updateBowlingTable(0);
        setLayout();
        ll.addView(newTextview("w"));

        TableRow rowBat = (TableRow) tableLayout1.getChildAt(Strick);
        TableRow rowBall = (TableRow) tableLayout2.getChildAt(1);

        TextView bats = (TextView) rowBat.getChildAt(0);
        TextView bowls = (TextView) rowBall.getChildAt(0);

        String name = bats.getText().toString();
        bats.setText(name + "\n" + type + " " + bowls.getText().toString());
        tableLayout1.removeViewAt(Strick);
        update = "bt1";
        if (wicket == Global.team_1_players.size() - 1) {
            if (Global.inning == 1)
                alertInning(1);
            else {
               if (Global.target==run){
                   win(-1);
               }else{
                   if (Global.batting == 1)
                       win(1);
                   else
                       win(2);
               }
            }
        } else {
            copyRows();
            selectBatsman();
//            if(isOverFinish){
//                changeOver();
//                isOverFinish = false;
//            }
        }
    }

    //copy rows for retrieving after back
    public void copyRows() {
        Global.batrow1 = (TableRow) tableLayout1.getChildAt(1);
        Global.ballrow = (TableRow) tableLayout2.getChildAt(1);
        Global.balls.clear();
        for (int i = 0; i < ball; i++) {
            TextView view = (TextView) ll.getChildAt(i);
            Global.balls.add(view);
        }
        ll.removeAllViews();
        tableLayout1.removeViewAt(1);
        tableLayout2.removeViewAt(1);
    }

    //selecting batsman after out
    public void selectBatsman() {
        Intent i = new Intent(this, Select.class);
        i.putExtra("from", "match");
        i.putExtra("type", "batsman");
        startActivity(i);
    }

    //setting layout after batsman selected
    public void batsmanSelected() {
        String name;
        reCopy();
        if (Global.selectedFrom == 1) {
            name = Global.team_1_players.get(Global.New);
            TableRow row = addRow(name, tableLayout1, 2);
            Global.battingRowsTeam1.add(row);
        } else {
            name = Global.team_2_players.get(Global.New);
            TableRow row = addRow(name, tableLayout1, 2);
            Global.battingRowsTeam2.add(row);
        }
        initMatch(name+": 0/0","","","",update);
    }

    //copy rows after back
    public void reCopy() {
        tableLayout1.addView(Global.batrow1);
        tableLayout2.addView(Global.ballrow);
        for (TextView view : Global.balls)
            ll.addView(view);
    }


    public void Undo(View view) {

    }

    //handling lag bie event
    public void lb(View view) {
        Button button = (Button) view;
        String text = button.getText().toString();
        ballOver++;
        ball++;
        switch (text) {
            case "1+lb":
                run++;
                ll.addView(newTextview("1+lb"));
                setLayout();
                updateBowlingTable(1);
                swap();
                break;
            case "2+lb":
                run += 2;
                ll.addView(newTextview("lb+2"));
                setLayout();
                updateBowlingTable(2);
                swap();
                break;
            case "3+lb":
                run += 3;
                ll.addView(newTextview("lb+3"));
                setLayout();
                updateBowlingTable(3);
                swap();
                break;
            case "4+lb":
                run += 4;
                ll.addView(newTextview("lb+4"));
                setLayout();
                updateBowlingTable(4);
                break;
            case "6+lb":
                run += 6;
                ll.addView(newTextview("lb+6"));
                setLayout();
                updateBowlingTable(6);
                break;
        }
        int result = checkForWin();
        if (result == 1)
            win(1);
        if (result == 2)
            win(2);
        if (ballOver == 0)
            changeOver();
    }

    //handling score event
    public void increaseScore(View view) {
        Button button = (Button) view;
        String r = button.getText().toString();
        run += Integer.parseInt(r);
        overRun += Integer.parseInt(r);
        ++ball;
        ++ballOver;
        setLayout();
        ll.addView(newTextview(r));
        updateBattingTable(Integer.parseInt(r));
        updateBowlingTable(Integer.parseInt(r));
        int result = checkForWin();
        if (result == -1) {
            if (Integer.parseInt(r) % 2 == 1)
                swap();
            if (ballOver == 0)
                changeOver();
        } else {
          win(result);
        }
    }

    //event after over finish
    public void changeOver() {
        swap();
        ll.removeAllViews();
        Global.batrow1 = (TableRow) tableLayout1.getChildAt(1);
        Global.batrow2 = (TableRow) tableLayout1.getChildAt(2);
        tableLayout2.removeViewAt(1);
        tableLayout1.removeViewAt(1);
        tableLayout1.removeViewAt(1);

        if (Global.inning == 1) {
            if (checkInning())
                alertInning(1);
        } else {
            if (checkInning()) {
                alertInning(2);
            }
        }
        if (!checkInning()) {                                          //checking for inning after over finish
            Intent i = new Intent(this, Select.class);
            i.putExtra("from", "selectBowler");
            i.putExtra("type", "bowler");
            startActivity(i);
        }
    }

    //wide event
    public void wide(View view) {
        TextView Wide = (TextView) view;
        String text = Wide.getText().toString();
        switch (text) {
            case "wide":
                run++;
                ll.addView(newTextview("wd"));
                setLayout();
                updateBowlingTable(1);
                break;
            case "1+wide":
                run += 2;
                ll.addView(newTextview("wd+1"));
                setLayout();
                updateBowlingTable(2);
                swap();
                break;
            case "2+wide":
                run += 3;
                ll.addView(newTextview("wd+2"));
                setLayout();
                updateBowlingTable(3);
                break;
            case "3+wide":
                run += 4;
                ll.addView(newTextview("wd+3"));
                setLayout();
                updateBowlingTable(4);
                swap();
                break;
            case "4+wide":
                run += 5;
                ll.addView(newTextview("wd+4"));
                setLayout();
                updateBowlingTable(5);
                break;
        }
        int result = checkForWin();   //check for win in second inning
        if (result == 1)
            win(1);
        if (result == 2)
            win(2);
    }

    //No Ball event
    public void noBall(View view) {
        TextView NoBall = (TextView) view;
        String text = NoBall.getText().toString();
        //Toast.makeText(this,text,Toast.LENGTH_LONG).show();
        switch (text) {
            case "No Ball":
                run++;
                ll.addView(newTextview("nb"));
                setLayout();
                updateBowlingTable(1);
                break;
            case "1+No Ball":
                run += 2;
                ll.addView(newTextview("nb+1"));
                setLayout();
                updateBowlingTable(2);
                updateBattingTable(1);
                swap();
                break;
            case "2+No Ball":
                run += 3;
                ll.addView(newTextview("nb+2"));
                setLayout();
                updateBowlingTable(3);
                updateBattingTable(2);
                break;
            case "3+No Ball":
                run += 4;
                ll.addView(newTextview("nb+3"));
                setLayout();
                updateBowlingTable(4);
                updateBattingTable(3);
                swap();
                break;
            case "4+No Ball":
                run += 5;
                ll.addView(newTextview("nb+4"));
                setLayout();
                updateBowlingTable(5);
                updateBattingTable(4);
                break;
            case "6+No Ball":
                run += 7;
                ll.addView(newTextview("nb+6"));
                setLayout();
                updateBowlingTable(7);
                updateBattingTable(6);
                break;
        }
        int result = checkForWin(); //check for win
        if (result == 1)
            win(1);
        if (result == 2)
            win(2);
    }

    //handling event after openers selected
    public void openers() {
        String bt1,bt2,bl1;
        if (Global.inning == 1) {
            d = format.format(date);
            Global.currentUserEmail = user.getEmail();
            Global.matchKey = d+" "+Global.team1+" Vs "+Global.team2;
            FILE = initFile();
            putFile(FILE);     //putting empty file in database
            putCommentFile();  //putting empty file for comment in database
            if (Global.batting == 1) {
                bt1 = Global.team_1_players.get(Global.op1);
                bt2 = Global.team_1_players.get(Global.op2);
                bl1 = Global.team_2_players.get(Global.New);                        // setting batting and bowling layout
                TableRow op1 = addRow(bt1, tableLayout1, 1);
                TableRow op2 = addRow(bt2, tableLayout1, 2);
                TableRow bow = addRow(bl1, tableLayout2, 1);
                Global.battingRowsTeam1.add(op1);
                Global.battingRowsTeam1.add(op2);
                Global.bowlingRowsTeam2.add(bow);
                Global.bowlersTeam2.add(Global.team_2_players.get(Global.New));
                initMatch(bt1+": 0/0",bt2+": 0/0",bl1+": 0/0","None","initial");
            } else {
                bt1 = Global.team_2_players.get(Global.op1);
                bt2 = Global.team_2_players.get(Global.op2);
                bl1 = Global.team_1_players.get(Global.New);
                TableRow op1 = addRow(bt1, tableLayout1, 1);
                TableRow op2 = addRow(bt2, tableLayout1, 2);
                TableRow bow = addRow(bl1, tableLayout2, 1);
                Global.battingRowsTeam2.add(op1);
                Global.battingRowsTeam2.add(op2);
                Global.bowlingRowsTeam1.add(bow);
                Global.bowlersTeam1.add(Global.team_1_players.get(Global.New));
                initMatch(bt1+": 0/0",bt2+": 0/0",bl1+": 0/0","None","initial");
            }
        } else {
            if (Global.batting == 2) {
                bt1 = Global.team_1_players.get(Global.op1);
                bt2 = Global.team_1_players.get(Global.op2);
                bl1 = Global.team_2_players.get(Global.New);
                TableRow op1 = addRow(Global.team_1_players.get(Global.op1), tableLayout1, 1);
                TableRow op2 = addRow(Global.team_1_players.get(Global.op2), tableLayout1, 2);
                TableRow bow = addRow(Global.team_2_players.get(Global.New), tableLayout2, 1);
                Global.battingRowsTeam1.add(op1);
                Global.battingRowsTeam1.add(op2);
                Global.bowlingRowsTeam2.add(bow);
                Global.bowlersTeam2.add(Global.team_2_players.get(Global.New));
                initMatch(bt1+": 0/0",bt2+": 0/0",bl1+": 0/0","None","initial");
            } else {
                bt1 = Global.team_2_players.get(Global.op1);
                bt2 = Global.team_2_players.get(Global.op2);
                bl1 = Global.team_1_players.get(Global.New);
                TableRow op1 = addRow(Global.team_2_players.get(Global.op1), tableLayout1, 1);
                TableRow op2 = addRow(Global.team_2_players.get(Global.op2), tableLayout1, 2);
                TableRow bow = addRow(Global.team_1_players.get(Global.New), tableLayout2, 1);
                Global.battingRowsTeam2.add(op1);
                Global.battingRowsTeam2.add(op2);
                Global.bowlingRowsTeam1.add(bow);
                Global.bowlersTeam1.add(Global.team_1_players.get(Global.New));
                initMatch(bt1+": 0/0",bt2+": 0/0",bl1+": 0/0","None","initial");
            }
        }
    }

    // handling update in batting table like increasing batsman's score
    public void updateBattingTable(int r) {
        TableRow row = (TableRow) tableLayout1.getChildAt(Strick);
        TextView batN = (TextView)row.getChildAt(0);
        TextView batR = (TextView) row.getChildAt(1);
        TextView batB = (TextView) row.getChildAt(2);
        TextView batF = (TextView) row.getChildAt(3);
        TextView batS = (TextView) row.getChildAt(4);
        TextView batSR = (TextView) row.getChildAt(5);
        int batsmanRun = Integer.parseInt(batR.getText().toString());
        int batsmanBall = Integer.parseInt(batB.getText().toString());
        int batsmanFour = Integer.parseInt(batF.getText().toString());
        int batsmanSix = Integer.parseInt(batS.getText().toString());
        float batsmanSR = Float.parseFloat(batSR.getText().toString());

        batsmanRun += r;
        batsmanBall++;
        if (r == 4)
            batsmanFour++;
        if (r == 6)
            batsmanSix++;
        batsmanSR = (float) batsmanRun * 100 / batsmanBall;
        batR.setText(batsmanRun + "");
        batB.setText(batsmanBall + "");
        batF.setText(batsmanFour + "");
        batS.setText(batsmanSix + "");
        batSR.setText(String.format("%.2f",batsmanSR));

        String bt = batN.getText().toString()+": "+batsmanRun+"/"+batsmanBall;
        if (Strick==1)
            initMatch(bt,"","","","bt1");
        else
            initMatch(bt,"","","","bt2");
        uploadFile();
    }

    //handle updates in bowling table like ball or wicket
    public void updateBowlingTable(int r) {
        TableRow row = (TableRow) tableLayout2.getChildAt(1);
        TextView bowN = (TextView) row.getChildAt(0);
        TextView bowO = (TextView) row.getChildAt(1);
        TextView bowM = (TextView) row.getChildAt(2);
        TextView bowR = (TextView) row.getChildAt(3);
        TextView bowW = (TextView) row.getChildAt(4);
        TextView bowEco = (TextView) row.getChildAt(5);

        float o = Float.parseFloat(bowO.getText().toString());
        int M = Integer.parseInt(bowM.getText().toString());
        int R = Integer.parseInt(bowR.getText().toString());
        int w = Integer.parseInt(bowW.getText().toString());
        float eco = Float.parseFloat(bowEco.getText().toString());
        String O;
        int numBall;
        if (ballOver == 6) {
            int Ov = (int) o;
            ++Ov;
            O = Ov + "";
           // o = Float.parseFloat(O);
            ballOver = 0;
            if (overRun == 0)
                M += 1;
            numBall = Ov*6 + ballOver;
        } else {
            int Ov = (int) o;
            O = Ov + "." + ballOver;
           // o = Float.parseFloat(O);
            numBall = Ov*6 + ballOver;
        }
        R += r;
        w += overWicket;

        eco = (float) (R*6)/ numBall;

        bowEco.setText(String.format("%.2f",eco));
        bowO.setText(O);
        bowM.setText(M + "");
        bowR.setText(R + "");
        bowW.setText("" + w);

        String bl = bowN.getText().toString()+": "+w+"/"+O;
        initMatch(bl,"","","","bl1");
        uploadFile();
    }

    //handling event after bowler selected for over
    public void BowlerSelected(String type) {
        if (type.equals("new")) {                   //if new bowler selected
            if (Global.selectedFrom == 1) {
                TableRow row = addRow(Global.team_1_players.get(Global.New), tableLayout2, 1);
                Global.bowlingRowsTeam1.add(row);
                Global.bowlersTeam1.add(Global.team_1_players.get(Global.New));
            } else {
                TableRow row = addRow(Global.team_2_players.get(Global.New), tableLayout2, 1);
                Global.bowlingRowsTeam2.add(row);
                Global.bowlersTeam2.add(Global.team_2_players.get(Global.New));
            }
        } else {                                      //if previos bowler selected
            if (Global.selectedFrom == 1) {
                TableRow row = Global.bowlingRowsTeam1.get(Global.prev);
                tableLayout2.addView(row, 1);
            } else {
                TableRow row = Global.bowlingRowsTeam2.get(Global.prev);
                tableLayout2.addView(row, 1);
            }
        }
        tableLayout1.addView(Global.batrow1, 1);
        tableLayout1.addView(Global.batrow2, 2);
    }

    //event for catch out
    public void catchOut(final String type) {
        TableRow row = (TableRow) tableLayout1.getChildAt(Strick);
        TableRow row1 = (TableRow) tableLayout2.getChildAt(1);
        final TextView name = (TextView) row.getChildAt(0);
        final TextView nameBow = (TextView) row1.getChildAt(0);
        TextView heading = (TextView) outLayout.findViewById(R.id.catch_or_run);
        final EditText Name = (EditText) outLayout.findViewById(R.id.name);

        if (type.equals("catch")) {
            update = "bt1";
            heading.setText("Name of the player who take catch");
            strikeOrNot.setVisibility(View.INVISIBLE);
        } else {
            if (type.equals("stumps")){
                update = "bt1";
                heading.setText("Name of the wicket keeper");
                strikeOrNot.setVisibility(View.INVISIBLE);
            }else {
                heading.setText("name of the player who hit the wicket");
                strikeOrNot.setVisibility(View.VISIBLE);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (outLayout.getParent() != null)
            ((ViewGroup) outLayout.getParent()).removeView(outLayout);

        builder.setView(outLayout);
        builder.setCancelable(true);
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Name.getText().toString() == null || Name.getText().toString() == "")
                    errorAlert("Name is mondatory", type);
                else {
                    if (rotate.isChecked())
                        swap();
                    if(!type.equals("catch")){
                        if (strikeOrNot.isChecked())
                            update = "bt1";
                        else
                            update = "bt2";
                    }
                    overWicket++;
                    ball++;
                    ballOver++;
                    updateBowlingTable(0);
                    setLayout();
                    ll.addView(newTextview("w"));
                    if (type.equals("catch"))
                        name.setText(name.getText().toString() + "\n" + "c " + Name.getText().toString() + " b " + nameBow.getText().toString());
                    else
                    {
                        if (type.equals("stumps"))
                            name.setText(name.getText().toString()+"\n"+"st "+Name.getText().toString());
                        else
                            name.setText(name.getText().toString() + "\n" + "run out " + Name.getText().toString());
                    }
                    tableLayout1.removeViewAt(Strick);
                    if (wicket == Global.team_1_players.size() - 1) {    //checking for all out
                        if (Global.inning == 1)
                            alertInning(1);
                        else {
                           if (Global.target==run)              //checking for run chase
                               win(-1);
                           else {
                               if (Global.batting == 1)
                                   win(1);
                               else
                                   win(2);
                           }
                        }
                    } else {
                        copyRows();
                        selectBatsman();
                    }
                }
            }
        });
        builder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void errorAlert(String msg, final String type) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("warning");
        alert.setMessage(msg);
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                catchOut(type);
            }
        });
        alert.show();
    }

    public boolean checkInning() {
        return (Global.totalOvers==over);
    }

    //alerting after inning finished or match over
    public void alertInning(int inning) {
        if (inning == 1) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Inning 1 has finished");
            alert.setMessage("Click below to go for inning 2");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(context, Select.class);
                    i.putExtra("from", "Teams");
                    Global.inning = 2;
                    int temp = Global.currentBat;
                    Global.currentBowl = Global.currentBat;
                    Global.currentBat = temp;
                    Global.clicked = 0;
                    Global.target = run;
                    run = 0;
                    wicket = 0;
                    over = 0;ballOver=0;ball=0;
                    startActivity(i);
                }
            });
            alert.show();
        } else {
            if (Global.target < run) {
                if (Global.batting == 2)
                    win(1);
                else
                    win(2);
            } else {
                if (Global.target > run) {
                    if (Global.batting == 2)
                        win(2);
                    else
                        win(1);
                } else {
                    win(-1);
                }
            }
        }
    }

    //checking for win
    public int checkForWin() {
        if (Global.inning==2)
        {
            if (Global.target < run) {
                if (Global.batting == 1)
                    return 2;
                else
                    return 1;
            }
        }
        return -1;
    }

    //finish match and goto scoreboard
    public void win(int who) {
        Global.win=who;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Match finish");
        if (who == 1)
        {
            dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                    child("status").setValue(Global.team1+" won the match");           //change status of match in database
            alert.setMessage(Global.team1 + " won the match");
        }
        else
        {
            if (who==2){
                dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                        child("status").setValue(Global.team2+" won the match");
                alert.setMessage(Global.team2 + " won the match");
            }else {
                dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                        child("status").setValue("Match Tied");
                alert.setMessage("Match Tied");
            }
        }
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                        child("current bat 1").removeValue();
                dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                        child("current bat 2").removeValue();
                dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                        child("current bowl 1").removeValue();
                gotoScoreBoard();
            }
        });
        alert.show();
    }

    //go to scoreboard activity after match or clicking of scoreboard
    public void gotoScoreBoard(){
        Intent i = new Intent(context,ScoreBoard.class);
        i.putExtra("code","finish");
        tableLayout1.removeAllViews();                     //removing views such that we add it's child in scoreboard
        tableLayout2.removeAllViews();
        startActivity(i);
        finish();
    }

    public void scoreBoard(View view){
        Intent i = new Intent(context,ScoreBoard.class);
        i.putExtra("code","notFinish");
        copyRows();
        Global.batrow2 = (TableRow)tableLayout1.getChildAt(1);
        tableLayout1.removeViewAt(1);
        startActivity(i);
    }

    // adding each row of table in file
    public void setFromRow(TableRow row,String text,BufferedWriter writer){
        TextView name = (TextView)row.getChildAt(0);
        TextView run = (TextView)row.getChildAt(1);
        TextView ball = (TextView)row.getChildAt(2);
        TextView four = (TextView)row.getChildAt(3);
        TextView six = (TextView)row.getChildAt(4);
        TextView sr = (TextView)row.getChildAt(5);

        text = text+name.getText().toString()+"-*-"+run.getText().toString()+"-*-"+ball.getText().toString()+             //creating line for file
                        "-*-"+four.getText().toString()+"-*-"+six.getText().toString()+"-*-"+sr.getText().toString()+"-*-"+"\n";

        try {
            writer.append(text);     // appending line in file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //putting file in database after updating it's lines
    public void putFile(File file){
        Uri uri = Uri.parse(file.toURI().toString());
        final String path = register.getMd5(Global.currentUserEmail)+Global.matchKey.replaceAll(" ","");
        sref.child(path).putFile(uri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sref.child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println(uri);
                        dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).
                                child("scoreboard").setValue(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();

            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(getApplicationContext(),"Completed",Toast.LENGTH_LONG).show();
            }
        });
    }

    //initialize file in database
    public File initFile(){
        File file=null;
        try {
            file = File.createTempFile("score",".txt");
            file.deleteOnExit();
            Writer writer = new BufferedWriter(new FileWriter(file));
            writer.append("************************* Cricket App Match ***************************\n");
            writer.flush();
            writer.close();
            return file;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    //write in file
    public void writeInFile(){
        BufferedWriter writer=null;
        try {
            writer = new BufferedWriter(new FileWriter(FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Global.batting==1)
        {
            addStar(writer);
            for (TableRow row:Global.battingRowsTeam1)        //adding line from table
                setFromRow(row,"",writer);
            addStar(writer);                                  //adding starts for separating line in file
            for (TableRow row:Global.bowlingRowsTeam2)
                setFromRow(row,"",writer);
            addStar(writer);
            for (TableRow row:Global.battingRowsTeam2)
                setFromRow(row,"",writer);
            addStar(writer);
            for (TableRow row:Global.bowlingRowsTeam1)
                setFromRow(row,"",writer);
            addStar(writer);
        }
        else{
            addStar(writer);
            for (TableRow row:Global.battingRowsTeam2)
                setFromRow(row,"",writer);
            addStar(writer);
            for (TableRow row:Global.bowlingRowsTeam1)
                setFromRow(row,"",writer);
            addStar(writer);
            for (TableRow row:Global.battingRowsTeam1)
                setFromRow(row,"",writer);
            addStar(writer);
            for (TableRow row:Global.bowlingRowsTeam2)
                setFromRow(row,"",writer);
            addStar(writer);
        }

        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //function that add stars in file
    public void addStar(BufferedWriter writer){
        try {
            writer.append("**********************************\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(){
        FILE=initFile();
        writeInFile();
        putFile(FILE);
    }

   //putting comment file so user can comment on match
    public void putCommentFile(){
        final String path = register.getMd5(Global.currentUserEmail)+Global.matchKey.replaceAll(" ","");
        try {
            File file = File.createTempFile("comment",".txt");    //creating temp file for putting
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.append("***************************************************");
            writer.flush();
            writer.close();
            sref.child("Comments").child(path).putFile(Uri.parse(file.toURI().toString())).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sref.child("Comments").child(path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dref.child(register.getMd5(user.getEmail())).child(Global.matchKey).child("CommentUrl").setValue(uri.toString());
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}