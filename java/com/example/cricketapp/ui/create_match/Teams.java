package com.example.cricketapp.ui.create_match;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cricketapp.R;

//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Teams extends AppCompatActivity {

    private static ArrayList<String> team_1_players,team_2_players;
    private  ArrayList<String> players1 = new ArrayList<String>(11) ;
    private  ArrayList<String> players2 = new ArrayList<String>(11) ;
    private static Map<String, List<String>> player_1_Maping ;
    private static Map<String, List<String> > player_2_Maping;

    private InputStream is;
    private AssetManager asset;

    public static boolean fromMake=true;
    private Context context = this;
    private Context context1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        setTitle("Both Teams");

        TextView matchName = (TextView)findViewById(R.id.matchName);
        TextView team1 = (TextView)findViewById(R.id.team1);
        TextView team2 = (TextView)findViewById(R.id.team2);

        RadioButton button1 = (RadioButton)findViewById(R.id.tButton_1);
        RadioButton button2 = (RadioButton)findViewById(R.id.tButton_2);
       // Button button = (Button)findViewById(R.id.back);

        matchName.setText(MakeTeam.matchName);
        team1.setText(MakeTeam.team_1_Name);
        team2.setText(MakeTeam.team_2_Name);

        button1.setText(team1.getText());
        button2.setText(team2.getText());

        ListView team1List = (ListView)findViewById(R.id.team_1_list);
        ListView team2List = (ListView)findViewById(R.id.team_2_list);

        team_1_players = Global.team_1_players;
        team_2_players = Global.team_2_players;
        player_2_Maping = Global.player_2_Maping;
        player_1_Maping = Global.player_1_Maping;

        // Toast.makeText(context,""+team_1_players.size()+" "+team_2_players.size(),Toast.LENGTH_LONG).show();
        Intent i = getIntent();
        String from = i.getStringExtra("from");
        context1 = i.getParcelableExtra("context");

        if (!from.equals("makeTeam")){
            button1.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);
            //button.setVisibility(View.VISIBLE);
        }else{
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
           // button.setVisibility(View.INVISIBLE);
        }


        for (String p:team_1_players)
            players1.add(p+"\t\t"+player_1_Maping.get(p));
        for (String p:team_2_players)
            players2.add(p+"\t\t"+player_2_Maping.get(p));

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,players1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,players2);

        team1List.setAdapter(adapter1);
        team2List.setAdapter(adapter2);

        team1List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,"list1",Toast.LENGTH_LONG).show();
            }
        });

        team2List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,"list2",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void start(View view){
        Intent intent = new Intent(context,Select.class);
        intent.putExtra("from","Teams");
        RadioGroup batting = (RadioGroup)findViewById(R.id.batting);
        RadioButton toss = (RadioButton)findViewById(batting.getCheckedRadioButtonId());
        if (toss.getText().equals(Global.team1))
        {
            Global.batting = 1;
            Global.currentBat = 1;
            Global.currentBowl=2;
        }
        else
        {
            Global.batting = 2;
            Global.currentBowl=1;
            Global.currentBat = 2;
        }
        startActivity(intent);
        finish();
    }

}