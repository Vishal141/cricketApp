package com.example.cricketapp.ui.create_match;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cricketapp.R;
import com.example.cricketapp.register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Select extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Intent i = getIntent();
        String from = i.getStringExtra("from");

        //TextView select = (TextView)findViewById(R.id.select);
        final TextView p = (TextView)findViewById(R.id.textView8);
        TextView n = (TextView)findViewById(R.id.textView9);

        LayoutInflater li = LayoutInflater.from(this);
        TextView textView =(TextView) li.inflate(android.R.layout.simple_list_item_1,null);
        textView.setTextColor(Color.WHITE);


        ListView previosList = (ListView)findViewById(R.id.previosList);
        ListView newList = (ListView)findViewById(R.id.newList);

        ArrayAdapter<String> previos;
        ArrayAdapter<String> New;

        if (from.equals("Teams")) {
//            select.setText("Select Openers");
            setTitle("Select Openers");
            p.setVisibility(View.GONE);
            previosList.setVisibility(View.GONE);
            if (Global.inning==1)
            {
                if (Global.batting==1)
                    New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_1_players);
                else
                    New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_2_players);
            }
            else
            {
                if (Global.batting==2)
                    New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_1_players);
                else
                    New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_2_players);
            }
            System.out.println(Global.team_1_players.size()+" and "+Global.team_2_players.size());
            newList.setAdapter(New);
            newList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (Global.clicked==0)
                    {
                        Global.op1 = position;
                        Global.clicked=1;
                    }
                    else{
                        Global.op2=position;
                        Intent intent = new Intent(context,Select.class);
                        intent.putExtra("from","firstBowler");
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        else
        {
            if (from.equals("firstBowler"))
            {
                //select.setText("Select first bowler");
                setTitle("Select first bowler");
                p.setVisibility(View.GONE);
                previosList.setVisibility(View.GONE);

                if (Global.inning==1)
                {
                    if (Global.batting==1)
                        New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_2_players);
                    else
                        New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_1_players);
                }
                else
                {
                    if (Global.batting==2)
                        New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_2_players);
                    else
                        New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_1_players);
                }
                newList.setAdapter(New);
                newList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Global.New = position;
                        Intent intent = new Intent(context,MatchActivity.class);
                        intent.putExtra("code","opners selected");
                       // initMatch();
                        startActivity(intent);
                        finish();
                    }
                });
            }
            else
            {
                String type = i.getStringExtra("type");
                if (type.equals("batsman"))
                {
                    //select.setText("Select a new batsman");
                    setTitle("Select a new batsman");
                    p.setVisibility(View.GONE);
                    previosList.setVisibility(View.GONE);

                    if (Global.inning==1)
                    {
                        if (Global.batting==1)
                        {
                            New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_1_players);
                            Global.selectedFrom = 1;
                        }
                        else
                        {
                            New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_2_players);
                            Global.selectedFrom = 2;
                        }
                    }
                    else
                    {
                        if (Global.batting==2)
                        {
                            New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_1_players);
                            Global.selectedFrom = 1;
                        }
                        else
                        {
                            New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_2_players);
                            Global.selectedFrom = 2;
                        }
                    }
                    newList.setAdapter(New);
                    newList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Global.New = position;
                            Intent intent = new Intent(context,MatchActivity.class);
                            intent.putExtra("code","batsman selected");
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                else
                {
                    //select.setText("select a bowler");
                    setTitle("Select a bowler");
                    p.setVisibility(View.VISIBLE);
                    previosList.setVisibility(View.VISIBLE);
                    if (Global.inning==1)
                    {
                        if (Global.batting==1)
                        {
                            New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_2_players);
                            previos = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.bowlersTeam2);
                            Global.selectedFrom = 2;
                        }
                        else
                        {
                            New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_1_players);
                            previos = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.bowlersTeam1);
                            Global.selectedFrom = 1;
                        }
                    }
                    else
                    {
                        if (Global.batting==2)
                        {
                            New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_2_players);
                            previos = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.bowlersTeam2);
                            Global.selectedFrom = 2;
                        }
                        else
                        {
                            New = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.team_1_players);
                            previos = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,Global.bowlersTeam1);
                            Global.selectedFrom = 1;
                        }
                    }
                    newList.setAdapter(New);
                    previosList.setAdapter(previos);
                    newList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Global.New = position;
                            Intent intent = new Intent(context,MatchActivity.class);
                            intent.putExtra("code","new bowler selected");
                            startActivity(intent);
                            finish();
                        }
                    });
                    previosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Global.prev = position;
                            Intent intent = new Intent(context,MatchActivity.class);
                            intent.putExtra("code","previos bowler selected");
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        }
    }
}
