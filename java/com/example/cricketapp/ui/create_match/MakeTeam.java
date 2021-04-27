package com.example.cricketapp.ui.create_match;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cricketapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeTeam extends AppCompatActivity {

    public static ArrayList<String> team_1_players;
    public static ArrayList<String> team_2_players;

    private ArrayAdapter<String> adapter;

    private Context context = this;

    public ListView playerList;

    public static Map<String, List<String> > player_1_Maping = new HashMap<String, List<String>>(11);;
    public static Map<String, List<String> > player_2_Maping = new HashMap<String, List<String>>(11);

    private List<String> playerProperty = new ArrayList<String>();

    public static int team_code=1;
    public static int playerCount_1=0;
    public static int playerCount_2=0;

    private boolean ADD = true;
    public static boolean isCreateClicked = true;

    public static String matchName="Match Name";
    public static String team_1_Name="Team 1";
    public static String team_2_Name="Team 2";

    public static int i=0,j=0;

    LayoutInflater li;
    View popUp;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team2);

        setTitle("Add Players to Team");

        if (isCreateClicked)
        {
            isCreateClicked = false;
            team_1_players = new ArrayList<String>(11);
            team_2_players = new ArrayList<String>(11);
            Intent intent = getIntent();
            matchName = intent.getStringExtra("matchName");
            team_1_Name = intent.getStringExtra("team1");
            team_2_Name = intent.getStringExtra("team2");
            Global.team1 = team_1_Name;
            Global.team2 = team_2_Name;
        }

        li = LayoutInflater.from(this);
        popUp = li.inflate(R.layout.addplayer,null);

        TextView textView = (TextView)findViewById(R.id.teamHead);
        playerList = (ListView)findViewById(R.id.playerList);

        //team_1_players.add("vishal");

        switch (team_code)
        {
            case 1:textView.setText("select players for team: "+team_1_Name);
                adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,team_1_players);
                break;
            case 2:textView.setText("select players for team: "+team_2_Name);
                adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,team_2_players);
                break;

        }
        playerList.setAdapter(adapter);

        if ((team_code==1 && playerCount_1==0))
            captainInfo();

    }

    public void captainInfo()
    {
        AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
        dialog1.setMessage("The first player you add should be captain of the team.");
        dialog1.setTitle("Information");
        dialog1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog1.show();
    }

    public void addPlayer(View view){
        if (playerCount_1<11 && playerCount_2<11){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (popUp.getParent()!=null)
                ((ViewGroup)popUp.getParent()).removeView(popUp);
            builder.setView(popUp);
            builder.setCancelable(true)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            add();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog = builder.create();
            dialog.show();
        }else{
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
            dialog1.setMessage("A team can't have more than 11 players");
            dialog1.setTitle("Warning");
            dialog1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog1.show();
        }
    }

    public void add(){
        RadioGroup group1 = (RadioGroup)popUp.findViewById(R.id.group_1);
        EditText name = (EditText)popUp.findViewById(R.id.playerName);

        RadioButton bats = (RadioButton)popUp.findViewById(R.id.batsman);
        RadioButton bowl = (RadioButton)popUp.findViewById(R.id.bowler);
        RadioButton allR = (RadioButton)popUp.findViewById(R.id.allRounder);

        if (group1.getCheckedRadioButtonId()== -1)
        {
            ADD = false;
            AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
            dialog1.setMessage("select a player type.");
            dialog1.setTitle("Warning");
            dialog1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog1.show();
        }

        if (team_code==1)
        {
            playerProperty.clear();
            if (ADD)
            {
                playerCount_1++;
                if (i==0)
                    playerProperty.add("captain");
                i++;
                if (bats.isChecked())
                    playerProperty.add("Batsman");
                if (bowl.isChecked())
                    playerProperty.add("Bowler");
                if (allR.isChecked())
                    playerProperty.add("All Rounder");
                player_1_Maping.put(name.getText().toString(),new ArrayList<String>());
                player_1_Maping.get(name.getText().toString()).addAll(playerProperty);
                team_1_players.add(name.getText().toString());
                adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,team_1_players);
            }
            else
                ADD = true;
        }
        else
        {
            playerProperty.clear();
            if (ADD)
            {
                playerCount_2++;
                if (j==0)
                    playerProperty.add("captain");
                j++;
                if (bats.isChecked())
                    playerProperty.add("Batsman");
                if (bowl.isChecked())
                    playerProperty.add("Bowler");
                if (allR.isChecked())
                    playerProperty.add("All Rounder");
                player_2_Maping.put(name.getText().toString(),new ArrayList<String>());
                player_2_Maping.get(name.getText().toString()).addAll(playerProperty);
                team_2_players.add(name.getText().toString());
                adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,team_2_players);
            }
            else
                ADD = true;
        }
        name.setText("");
        playerList.setAdapter(adapter);
    }

    public void next(View view){
        if (team_code==1)
        {
            if (playerCount_1<3)
            {
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
                dialog1.setMessage("A team must have more than 2 players.");
                dialog1.setTitle("Warning");
                dialog1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog1.show();
            }
            else {
                //player_1_Maping.get(team_1_players.get(0)).add("Captain");
                team_code=2;
                Intent i = new Intent(this,MakeTeam.class);
                startActivity(i);
                finish();
            }
        }
        else {
            if (team_1_players.size()!=team_2_players.size() && team_1_players.size()>0)
            {
                ADD = false;
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(this);
                dialog1.setMessage("Both the teams have same number of players.");
                dialog1.setTitle("Warning");
                dialog1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        team_code = 1;
                        Intent i = new Intent(context,MakeTeam.class);
                        startActivity(i);
                    }
                });
                dialog1.show();
            }
            else
            {
                // player_2_Maping.get(team_2_players.get(0)).add("Captain");
                Intent i = new Intent(context,Teams.class);
                i.putExtra("from","makeTeam");
                Global.player_1_Maping = player_1_Maping;
                Global.player_2_Maping = player_2_Maping;
                Global.team_1_players = team_1_players;
                Global.team_2_players= team_2_players;
                Global.matchName = matchName;
                startActivity(i);
                finish();
            }
        }
    }
}