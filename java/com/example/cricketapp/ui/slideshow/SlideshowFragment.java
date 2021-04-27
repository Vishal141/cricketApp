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
    private Set<Integer> indices;

    public static String currentUserEmail,currentUserName="unknown",currentUserImageUrl="";

    RecyclerView list;
    CustomAdapter adapter;

    EditText search_item;

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

        names = new ArrayList<String>();
        emails = new ArrayList<String>();
        imageUrls = new ArrayList<String>();
        newNames = new ArrayList<String>();
        newEmails = new ArrayList<String>();
        newImageUrls = new ArrayList<String>();
        indices = new HashSet<Integer>();

        list = root.findViewById(R.id.myList);
        search_item = root.findViewById(R.id.search_item);

        adapter = new CustomAdapter(getActivity(),names,emails,imageUrls);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);

        currentUserEmail = user.getEmail();

        search_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence item, int start, int before, int count) {
                //System.out.println(item);
                newImageUrls.clear();
                newEmails.clear();
                newNames.clear();
                indices.clear();
                for (String s:names){
                    if (s.contains(item.toString()))
                        indices.add(names.indexOf(s));
                }
                for (String s:emails){
                    if (s.contains(item.toString()))
                        indices.add(emails.indexOf(s));
                }
                for (int i:indices){
                    newNames.add(names.get(i));
                    newEmails.add(emails.get(i));
                    newImageUrls.add(imageUrls.get(i));
                }
                adapter = new CustomAdapter(getActivity(),newNames,newEmails,newImageUrls);
                list.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
}