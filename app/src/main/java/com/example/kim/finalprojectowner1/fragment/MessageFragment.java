package com.example.kim.finalprojectowner1.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kim.finalprojectowner1.model.Chat;
import com.example.kim.finalprojectowner1.Adapter.MyAdapter;
import com.example.kim.finalprojectowner1.model.Chat;
import com.example.kim.finalprojectowner1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class MessageFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManage;
    FirebaseDatabase database;

    List<Chat> mChat;
    EditText etText;
    Button btnSend;


    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        etText = (EditText)rootView.findViewById(R.id.etText);
        btnSend = (Button) rootView.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(view -> {
            String stText = etText.getText().toString();
            if (stText.equals("") || stText.isEmpty()){
                Toast.makeText(getActivity(), "내용을 입력을 해주세요", Toast.LENGTH_SHORT).show();
            }else{
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message").child(formattedDate);
                Hashtable<String, String> chat = new Hashtable<>();
                chat.put("text",stText);
                myRef.setValue(chat);
                etText.setText("");
            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManage = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManage);

        mChat = new ArrayList<>();

        mAdapter = new MyAdapter(mChat);
        mRecyclerView.setAdapter(mAdapter);

        DatabaseReference myRef = database.getReference("message");

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Chat chat = dataSnapshot.getValue(Chat.class);
                mChat.add(chat);
                mRecyclerView.scrollToPosition(mChat.size()-1);
                mAdapter.notifyItemInserted(mChat.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return rootView;
    }


}
