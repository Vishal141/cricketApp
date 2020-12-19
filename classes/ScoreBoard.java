package com.example.cricketworld.ui.gallery;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cricketworld.R;

public class ScoreBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_board);

        TableLayout tableLayout1 = (TableLayout)findViewById(R.id.battingScore1);
        TableLayout tableLayout2 = (TableLayout)findViewById(R.id.bowlingScore1);
        TableLayout tableLayout3 = (TableLayout)findViewById(R.id.battingScore2);
        TableLayout tableLayout4 = (TableLayout)findViewById(R.id.bowlingScore2);

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
}
