package com.example.cricketapp.ui.home;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cricketapp.R;
import com.example.cricketapp.register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dref;
    private FirebaseUser user;

    private ArrayList<String> names,emails,imageUrls,newNames,newEmails,newImageUrls;

    private String currentUserEmail,currentUserName;

    RecyclerView friendList;
    CustomAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
       // final TextView textView = root.findViewById(R.id.text_home);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dref = firebaseDatabase.getReference();
        user = mAuth.getCurrentUser();

        currentUserEmail = user.getEmail();

        names = new ArrayList<String>();
        emails = new ArrayList<String>();
        imageUrls = new ArrayList<String>();

        friendList = root.findViewById(R.id.friendList);
        adapter = new CustomAdapter(getActivity(),names,emails,imageUrls);
        friendList.setLayoutManager(new LinearLayoutManager(getActivity()));
        friendList.setAdapter(adapter);

        getFriendList();
        return root;
    }

    public void getFriendList(){
        dref.child("Friends").child(register.getMd5(currentUserEmail)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){
                    HashMap<String,String> map = (HashMap<String, String>) ds.getValue();
                    System.out.println(map);
                    emails.add(map.get("email"));
                    names.add(map.get("name"));
                    imageUrls.add(map.get("imageUrl"));
                }
                adapter = new CustomAdapter(getActivity(),names,emails,imageUrls);
                friendList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}