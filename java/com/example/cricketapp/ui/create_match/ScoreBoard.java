package com.example.cricketapp.ui.create_match;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cricketapp.MainActivity;
import com.example.cricketapp.R;
import com.example.cricketapp.chatActivity;

public class ScoreBoard extends AppCompatActivity {

    String code;
    TextView result;

    TableLayout tableLayout1,tableLayout2,tableLayout3,tableLayout4;
    TextView score1,score2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        tableLayout1 = (TableLayout)findViewById(R.id.battingScore1);
        tableLayout2 = (TableLayout)findViewById(R.id.bowlingScore1);
        tableLayout3 = (TableLayout)findViewById(R.id.battingScore2);
        tableLayout4 = (TableLayout)findViewById(R.id.bowlingScore2);

        score1 = (TextView)findViewById(R.id.score1);
        score2 = (TextView)findViewById(R.id.score2);
        result = (TextView)findViewById(R.id.result);

        if (Global.batting==1){
            score1.setText(Global.team1+":"+Global.score1);
            score2.setText(Global.team2+":"+Global.score2);
        }else{
            score1.setText(Global.team2+":"+Global.score2);
            score2.setText(Global.team1+":"+Global.score1);
        }

        Intent intent = getIntent();
        code = intent.getStringExtra("code");

        if (code.equals("finish")){
            if (Global.win==1)
                result.setText(Global.team1+" won the match");
            else
                result.setText(Global.team2+" won the match");
        }

        int i=1,j=1,k=1,l=1;
        if (Global.batting==1)
        {
            for (TableRow row:Global.battingRowsTeam1)
                tableLayout1.addView(row,i++);
            for (TableRow row:Global.bowlingRowsTeam2)
                tableLayout2.addView(row,j++);
            for (TableRow row:Global.battingRowsTeam2)
                tableLayout3.addView(row,k++);
            for (TableRow row:Global.bowlingRowsTeam1)
                tableLayout4.addView(row,l++);
        }
        else{
            for (TableRow row:Global.battingRowsTeam2)
                tableLayout1.addView(row,i++);
            for (TableRow row:Global.bowlingRowsTeam1)
                tableLayout2.addView(row,j++);
            for (TableRow row:Global.battingRowsTeam1)
                tableLayout3.addView(row,k++);
            for (TableRow row:Global.bowlingRowsTeam2)
                tableLayout4.addView(row,l++);
        }

    }

    public void backTomenu(View view){
        if (code.equals("finish")){
            Intent i = new Intent(this, chatActivity.class);
            startActivity(i);
        }
        else{
            Intent i = new Intent(this,MatchActivity.class);
            i.putExtra("code","scoreboard");
            tableLayout1.removeAllViews();
            tableLayout2.removeAllViews();
            tableLayout3.removeAllViews();
            tableLayout4.removeAllViews();
            startActivity(i);
        }
    }

}
