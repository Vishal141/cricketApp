package com.example.cricketapp.ui.create_match;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cricketapp.R;
import com.example.cricketapp.register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class create_match extends Fragment {

    EditText match,team1,team2,overs;
    String matchName = "Match Name", team_1_Name = "Team 1", team_2_Name = "Team 2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //System.out.println("onView");
        View root =  inflater.inflate(R.layout.fragment_create_match, container, false);
        Button button = (Button) root.findViewById(R.id.button);
        System.out.println(button.getText().toString());
        match = root.findViewById(R.id.matchName);
        team1 = root.findViewById(R.id.team1);
        team2 = root.findViewById(R.id.team2);
        overs = root.findViewById(R.id.overs);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("abc");
                create();
            }
        });

        getActivity().setTitle("Enter Match Details");

        return root;
    }

    public void create() {
        System.out.println("listener");

        int Overs = 20;

        if (match.getText() != null && !match.getText().toString().equals(""))
            matchName = match.getText().toString();

        if (team1.getText() != null && !team1.getText().toString().equals(""))
            team_1_Name = team1.getText().toString();

        if (team2.getText() != null && !team2.getText().toString().equals(""))
            team_2_Name = team2.getText().toString();

        if (!TextUtils.isEmpty(overs.getText()))
            Overs = Integer.parseInt(overs.getText().toString());

        Intent in = new Intent(getContext(), MakeTeam.class);
        in.putExtra("matchName", matchName);
        in.putExtra("team1", team_1_Name);
        in.putExtra("team2", team_2_Name);
        MakeTeam.team_code = 1;
        Global.totalOvers = Overs;
        MakeTeam.isCreateClicked = true;
        //initMatch();
        startActivity(in);
    }

}