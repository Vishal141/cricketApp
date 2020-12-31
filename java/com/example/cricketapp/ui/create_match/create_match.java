package com.example.cricketapp.ui.create_match;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link create_match#newInstance} factory method to
 * create an instance of this fragment.
 */
public class create_match extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText match,team1,team2,overs;
    String matchName = "Match Name", team_1_Name = "Team 1", team_2_Name = "Team 2";

    public create_match() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment create_match.
     */
    // TODO: Rename and change types and number of parameters
    public static create_match newInstance(String param1, String param2) {
        create_match fragment = new create_match();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        //System.out.println("create_match");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //System.out.println("onView");
        View root =  inflater.inflate(R.layout.fragment_create_match, container, false);
        Button button = (Button) root.findViewById(R.id.button);
        System.out.println(button.getText().toString());
        match = (EditText) root.findViewById(R.id.matchName);
        team1 = (EditText) root.findViewById(R.id.team1);
        team2 = (EditText) root.findViewById(R.id.team2);
        overs = (EditText) root.findViewById(R.id.overs);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("abc");
                create();
            }
        });

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

        if (overs.getText().toString() != null && overs.getText().toString() != "")
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