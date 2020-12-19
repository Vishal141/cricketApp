package com.example.cricketworld.ui.gallery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.cricketworld.R;

public class MatchActivity extends AppCompatActivity {

    private TableLayout tableLayout1, tableLayout2;
    private LinearLayout ll;
    private View outLayout;
    private CheckBox rotate, strikeOrNot;
    private LayoutInflater li;
    private static int Strick = 1, nonStrick = 2, bowler = 0;
    private static int run = 0, wicket = 0, over = 0, ball = 0, ballOver = 0;
    private int overWicket = 0, overRun = 0;

    private Context context = this;

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

        setLayoutInitially();

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
                }
            }
        }

    }

    public void setLayoutInitially() {
        matchName.setText(Global.matchName);
        team1.setText(Global.team1);
        team2.setText(Global.team2);

//        String score = run+"-"+wicket;
//
//        if (ball==6){
//            over++;
//            ball=0;
//        }
//        String O = over+"."+ball;

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
    }

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

    public void swap() {
        int temp = Strick;
        Strick = nonStrick;
        nonStrick = temp;
    }

    public void Out(View view) {
        Button button = (Button) view;
        String text = button.getText().toString();
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
        }
    }

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
        if (wicket == Global.team_1_players.size() - 1) {
            if (Global.inning == 1)
                alertInning(1);
            else {
                if (Global.batting == 1)
                    win(1);
            }
        } else {
            copyRows();
            selectBatsman();
        }
    }

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

    public void selectBatsman() {
        Intent i = new Intent(this, Select.class);
        i.putExtra("from", "match");
        i.putExtra("type", "batsman");
        startActivity(i);
    }

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

    }

    public void reCopy() {
        tableLayout1.addView(Global.batrow1);
        tableLayout2.addView(Global.ballrow);
        for (TextView view : Global.balls)
            ll.addView(view);
    }


    public void Undo(View view) {

    }


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
            if (result == 1)
                win(1);
            else
                win(2);
        }
    }

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
        if (!checkInning()) {
            Intent i = new Intent(this, Select.class);
            i.putExtra("from", "selectBowler");
            i.putExtra("type", "bowler");
            startActivity(i);
        }
    }

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
        int result = checkForWin();
        if (result == 1)
            win(1);
        if (result == 2)
            win(2);
    }

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
        int result = checkForWin();
        if (result == 1)
            win(1);
        if (result == 2)
            win(2);
    }

    public void openers() {
        if (Global.inning == 1) {
            if (Global.batting == 1) {
                TableRow op1 = addRow(Global.team_1_players.get(Global.op1), tableLayout1, 1);
                TableRow op2 = addRow(Global.team_1_players.get(Global.op2), tableLayout1, 2);
                TableRow bow = addRow(Global.team_2_players.get(Global.New), tableLayout2, 1);
                Global.battingRowsTeam1.add(op1);
                Global.battingRowsTeam1.add(op2);
                Global.bowlingRowsTeam2.add(bow);
                Global.bowlersTeam2.add(Global.team_2_players.get(Global.New));
            } else {
                TableRow op1 = addRow(Global.team_2_players.get(Global.op1), tableLayout1, 1);
                TableRow op2 = addRow(Global.team_2_players.get(Global.op2), tableLayout1, 2);
                TableRow bow = addRow(Global.team_1_players.get(Global.New), tableLayout2, 1);
                Global.battingRowsTeam2.add(op1);
                Global.battingRowsTeam2.add(op2);
                Global.bowlingRowsTeam1.add(bow);
                Global.bowlersTeam1.add(Global.team_1_players.get(Global.New));
            }
        } else {
            if (Global.batting == 2) {
                TableRow op1 = addRow(Global.team_1_players.get(Global.op1), tableLayout1, 1);
                TableRow op2 = addRow(Global.team_1_players.get(Global.op2), tableLayout1, 2);
                TableRow bow = addRow(Global.team_2_players.get(Global.New), tableLayout2, 1);
                Global.battingRowsTeam1.add(op1);
                Global.battingRowsTeam1.add(op2);
                Global.bowlingRowsTeam2.add(bow);
                Global.bowlersTeam2.add(Global.team_2_players.get(Global.New));
            } else {
                TableRow op1 = addRow(Global.team_2_players.get(Global.op1), tableLayout1, 1);
                TableRow op2 = addRow(Global.team_2_players.get(Global.op2), tableLayout1, 2);
                TableRow bow = addRow(Global.team_1_players.get(Global.New), tableLayout2, 1);
                Global.battingRowsTeam2.add(op1);
                Global.battingRowsTeam2.add(op2);
                Global.bowlingRowsTeam1.add(bow);
                Global.bowlersTeam1.add(Global.team_1_players.get(Global.New));
            }
        }
    }

    public void updateBattingTable(int r) {
        TableRow row = (TableRow) tableLayout1.getChildAt(Strick);
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
        batSR.setText(batsmanSR + "");
    }

    public void updateBowlingTable(int r) {
        TableRow row = (TableRow) tableLayout2.getChildAt(1);
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
        if (ballOver == 6) {
            int Ov = (int) o;
            ++Ov;
            O = Ov + "";
            o = Float.parseFloat(O);
            ballOver = 0;
            if (overRun == 0)
                M += 1;
        } else {
            int Ov = (int) o;
            O = Ov + "." + ballOver;
            o = Float.parseFloat(O);
        }
        R += r;
        w += overWicket;
        eco = (float) R / o;

        bowEco.setText(eco + "");
        bowO.setText(O);
        bowM.setText(M + "");
        bowR.setText(R + "");
        bowW.setText("" + w);
    }

    public void BowlerSelected(String type) {
        if (type.equals("new")) {
            if (Global.selectedFrom == 1) {
                TableRow row = addRow(Global.team_1_players.get(Global.New), tableLayout2, 1);
                Global.bowlingRowsTeam1.add(row);
                Global.bowlersTeam1.add(Global.team_1_players.get(Global.New));
            } else {
                TableRow row = addRow(Global.team_2_players.get(Global.New), tableLayout2, 1);
                Global.bowlingRowsTeam2.add(row);
                Global.bowlersTeam2.add(Global.team_2_players.get(Global.New));
            }
        } else {
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

    public void catchOut(final String type) {
        TableRow row = (TableRow) tableLayout1.getChildAt(Strick);
        TableRow row1 = (TableRow) tableLayout2.getChildAt(1);
        final TextView name = (TextView) row.getChildAt(0);
        final TextView nameBow = (TextView) row1.getChildAt(0);
        TextView heading = (TextView) outLayout.findViewById(R.id.catch_or_run);
        final EditText Name = (EditText) outLayout.findViewById(R.id.name);

        if (type.equals("catch")) {
            heading.setText("Name of the player who take catch");
            strikeOrNot.setVisibility(View.INVISIBLE);
        } else {
            heading.setText("name of the player who hit the wicket");
            strikeOrNot.setVisibility(View.VISIBLE);
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
                    overWicket++;
                    ball++;
                    ballOver++;
                    updateBowlingTable(0);
                    setLayout();
                    ll.addView(newTextview("w"));
                    if (type.equals("catch"))
                        name.setText(name.getText().toString() + "\n" + "c " + Name.getText().toString() + " b " + nameBow.getText().toString());
                    else
                        name.setText(name.getText().toString() + "\n" + "run out " + Name.getText().toString());
                    tableLayout1.removeViewAt(Strick);
                    if (wicket == Global.team_1_players.size() - 1) {
                        if (Global.inning == 1)
                            alertInning(1);
                        else {
                            if (Global.batting == 1) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Match finish");
                                alert.setMessage(Global.team1 + " won the match");
                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                alert.show();
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
        if (Global.currentBat == 1) {
            if (Global.totalOvers == over)
                return true;
        } else {
            if (Global.totalOvers == over)
                return true;
        }
        return false;
    }

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
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Match finish");
                if (Global.batting == 2)
                    alert.setMessage(Global.team1 + " won the match");
                else
                    alert.setMessage(Global.team2 + " won the match");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoScoreBoard();
                    }
                });
                alert.show();
            } else {
                if (Global.target > run) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Match finish");
                    if (Global.batting == 2)
                        alert.setMessage(Global.team2 + " won the match");
                    else
                        alert.setMessage(Global.team1 + " won the match");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gotoScoreBoard();
                        }
                    });
                    alert.show();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Match finish");
                    alert.setMessage("Match Tied");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gotoScoreBoard();
                        }
                    });
                    alert.show();
                }
            }
        }
    }

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

    public void win(int who) {
        if (Global.batting == 1) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Match finish");
            if (who == 1)
                alert.setMessage(Global.team1 + " won the match");
            else
                alert.setMessage(Global.team2 + " won the match");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gotoScoreBoard();
                }
            });
            alert.show();
        }
    }

    public void gotoScoreBoard(){
        Intent i = new Intent(context,ScoreBoard.class);
        startActivity(i);
    }
}