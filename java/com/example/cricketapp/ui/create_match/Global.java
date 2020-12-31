package com.example.cricketapp.ui.create_match;

import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Global {
    public static ArrayList<String> team_1_players,team_2_players;
    public static Map<String, List<String>> player_1_Maping ,player_2_Maping;
    public static String matchName, team1,team2,score1="0-0",score2="0-0",over1="",over2="0",matchKey,currentUserEmail;
    public static int batting=1,inning=1,op1=0,op2=1,New,prev,clicked=0,selectedFrom=1,currentBat,currentBowl,totalOvers,target,win;
    public static ArrayList<TableRow> battingRowsTeam1 = new ArrayList<TableRow>(11);
    public static ArrayList<TableRow> bowlingRowsTeam1 = new ArrayList<TableRow>(11);
    public static ArrayList<TableRow> battingRowsTeam2 = new ArrayList<TableRow>(11);
    public static ArrayList<TableRow> bowlingRowsTeam2 = new ArrayList<TableRow>(11);
    public static ArrayList<String> bowlersTeam1 = new ArrayList<>();
    public static ArrayList<String> bowlersTeam2 = new ArrayList<>();
    public static ArrayList<TextView> balls = new ArrayList<>(6);
    public static TableRow batrow1,batrow2,ballrow;

  //  public static HSSFSheet Sheet;
}
