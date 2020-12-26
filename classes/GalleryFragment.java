package com.example.cricketworld.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cricketworld.MainActivity;
import com.example.cricketworld.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private static String matchName="Match";
    private static String team_1_Name="Team 1";
    private static String team_2_Name="Team 2";
    private static int Overs=20;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);
       // final TextView textView = root.findViewById(R.id.matchName1);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
             //   textView.setHint(s);
            }
        });

        Button button = (Button) root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText match = (EditText) root.findViewById(R.id.matchName);
                EditText team1 = (EditText) root.findViewById(R.id.team1);
                EditText team2 = (EditText) root.findViewById(R.id.team2);
                EditText overs = (EditText) root.findViewById(R.id.overs);

                if (match.getText()!=null && !match.getText().toString().equals(""))
                    matchName = match.getText().toString();

                if (team1.getText()!=null && !team1.getText().toString().equals(""))
                    team_1_Name = team1.getText().toString();

                if (team2.getText()!=null && !team2.getText().toString().equals(""))
                    team_2_Name = team2.getText().toString();

                if (overs.getText().toString()!=null && overs.getText().toString()!="")
                    Overs = Integer.parseInt(overs.getText().toString());

                Intent in = new Intent(getContext(),MakeTeam.class);
                in.putExtra("matchName",matchName);
                in.putExtra("team1",team_1_Name);
                in.putExtra("team2",team_2_Name);
                MakeTeam.team_code = 1;
                Global.totalOvers=Overs;
                MakeTeam.isCreateClicked = true;
                startActivity(in);
            }
        });

        return root;
    }
}