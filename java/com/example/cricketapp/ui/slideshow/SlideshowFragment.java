package com.example.cricketapp.ui.slideshow;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    public static DatabaseReference dref;

    private ArrayList<String> names,emails,imageUrls,newNames,newEmails,newImageUrls;
    private Set<Integer> indexes;

    public static String currentUserEmail,currentUserName="unknown",currentUserImageUrl="";

    RecyclerView list;
    CustomAdapter adapter;

    EditText search_item;
    ImageView imageView;

    LayoutInflater li;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dref = firebaseDatabase.getReference();
        user = mAuth.getCurrentUser();

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
       // final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        names = new ArrayList<String>();
        emails = new ArrayList<String>();
        imageUrls = new ArrayList<String>();
        newNames = new ArrayList<String>();
        newEmails = new ArrayList<String>();
        newImageUrls = new ArrayList<String>();
        indexes = new HashSet<Integer>();

        list = root.findViewById(R.id.myList);
        search_item = root.findViewById(R.id.search_item);
        imageView = root.findViewById(R.id.search_btn);

        adapter = new CustomAdapter(getActivity(),names,emails,imageUrls);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);

        currentUserEmail = user.getEmail();

        setListenerToImage();
        //setListenerToList();
        getUserList();

        getActivity().setTheme(R.style.AppTheme_NoActionBar);

        return root;
    }

    public void getUserList(){
        dref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren()){

                    HashMap<String,String> map = (HashMap<String, String>) ds.getValue();

                    String em = map.get("email");
                    if (!em.equals(currentUserEmail)){
                        emails.add(map.get("email"));
                        names.add(map.get("name"));
                        imageUrls.add(map.get("imageUrl"));
                    }else {
                        currentUserName = map.get("name");
                        currentUserImageUrl = map.get("imageUrl");
                    }
                }
                adapter = new CustomAdapter(getActivity(),names,emails,imageUrls);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setListenerToImage(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newImageUrls.clear();
                newEmails.clear();
                newNames.clear();
                String item = search_item.getText().toString();
                for (String s:names){
                    if (s.contains(item))
                        indexes.add(names.indexOf(s));
                }
                for (String s:emails){
                    if (s.contains(item))
                        indexes.add(emails.indexOf(s));
                }
                for (int i:indexes){
                    newNames.add(names.get(i));
                    newEmails.add(emails.get(i));
                    newImageUrls.add(imageUrls.get(i));
                }
                adapter = new CustomAdapter(getActivity(),newNames,newEmails,newImageUrls);
                list.setAdapter(adapter);
            }
        });
    }
//
//    public void changeActionBar(){
//       final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//       actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//       actionBar.setDisplayShowCustomEnabled(true);
//       actionBar.setTitle("Add Friend");
//
//       getActivity().setTitle("Add Friend");
//
//       final View view = LayoutInflater.from(getContext()).inflate(R.layout.add_friend_app_bar,null);
//
//        final EditText search = (EditText)view.findViewById(R.id.search_item);
//        search.setText("a");
//        search.setVisibility(View.INVISIBLE);
//
//        ((Button)view.findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().setTitle("");
//                search.setVisibility(View.VISIBLE);
//            }
//        });
//
//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Toast.makeText(getContext(),"before",Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence item, int start, int before, int count) {
//                Toast.makeText(getContext(),item.toString(),Toast.LENGTH_LONG).show();
//               if(item.length()>0){
//                   setSearchList(item.toString());
//               }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//       actionBar.setCustomView(view);
//       actionBar.setElevation(0);
//    }
//
//    public void setSearchList(String item){
//        newImageUrls.clear();
//        newEmails.clear();
//        newNames.clear();
//        for (String s:names){
//            if (s.contains(item))
//                indexes.add(names.indexOf(s));
//        }
//        for (String s:emails){
//            if (s.contains(item))
//                indexes.add(emails.indexOf(s));
//        }
//        for (int i:indexes){
//            newNames.add(names.get(i));
//            newEmails.add(emails.get(i));
//            newImageUrls.add(imageUrls.get(i));
//        }
//        adapter = new CustomAdapter(getActivity(),newNames,newEmails,newImageUrls);
//        list.setAdapter(adapter);
//    }
}