package com.example.cricketapp.ui.fHIstory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cricketapp.R;
import com.example.cricketapp.register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link friend_match_history#newInstance} factory method to
 * create an instance of this fragment.
 */
public class friend_match_history extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<String> emails,matchKeys;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dref;

    private String currentUserEmail;

    private RecyclerView matches;
    private CustomAdapter adapter;

    public friend_match_history() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment friend_match_history.
     */
    // TODO: Rename and change types and number of parameters
    public static friend_match_history newInstance(String param1, String param2) {
        friend_match_history fragment = new friend_match_history();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        emails = new ArrayList<String>();
        matchKeys = new ArrayList<String>();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dref = firebaseDatabase.getReference().child("Matches");
        currentUserEmail = user.getEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_friend_match_history, container, false);
        matches = root.findViewById(R.id.matches);
        adapter = new CustomAdapter(getActivity(),emails,matchKeys);
        matches.setLayoutManager(new LinearLayoutManager(getActivity()));
        matches.setAdapter(adapter);

        getMatchKeys();

        return root;
    }

    public void getMatchKeys(){
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emails.clear();
                matchKeys.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    for (DataSnapshot ds1:ds.getChildren()){
                        HashMap<String,String> map = (HashMap<String, String>)ds1.getValue();
                        if (map.get("status").equals("running")){
                            if (!register.getMd5(currentUserEmail).equals(ds.getKey())){
                                emails.add(ds.getKey());
                                matchKeys.add(ds1.getKey());
                            }
                        }
                    }
                }
                adapter = new CustomAdapter(getActivity(),emails,matchKeys);
               // matches.setLayoutManager(new LinearLayoutManager(getActivity()));
                matches.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}