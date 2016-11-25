package com.example.piso.noteapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piso.noteapp.Adapters.Main_Topic_Adapter;
import com.example.piso.noteapp.Models.MainTopic;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserInterests extends AppCompatActivity {
    Main_Topic_Adapter mainTopicAdapter ;
    RecyclerView maintopicrecycle ;
    FirebaseDatabase firebaseDatabase ;
    List<MainTopic> topicList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interests);
        firebaseDatabase = FirebaseDatabase.getInstance();
        maintopicrecycle  = (RecyclerView) findViewById(R.id.list_Interests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("      Interests ");

        ShowNotes();
    }
    public void ShowNotes()
    {
        topicList = new ArrayList<MainTopic>();
        mainTopicAdapter=new Main_Topic_Adapter( topicList, firebaseDatabase,  this);
        topicList =  getTopicList(firebaseDatabase,topicList ,mainTopicAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        maintopicrecycle.setLayoutManager(llm);
        maintopicrecycle.setAdapter(mainTopicAdapter);
    }
    public List<MainTopic> getTopicList(final FirebaseDatabase database  ,final List<MainTopic> topicList, final Main_Topic_Adapter mainTopicAdapter){

        DatabaseReference ref = database.getReference("/Public_MainTopic");

        ChildEventListener mylistener;
        mylistener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MainTopic topic  = new MainTopic() ;
                topic.setName(dataSnapshot.child("name").getValue(String.class));
                topic.setNumber_of_notes(dataSnapshot.child("number_of_notes").getValue(int.class));
                topicList.add(topic);
                mainTopicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for(int i=0 ; i<topicList.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(topicList.get(i).getName()))
                    {
                        topicList.remove(i);
                        mainTopicAdapter.notifyDataSetChanged();
                    }
                }

                MainTopic topic  = new MainTopic() ;
                topic.setName(dataSnapshot.child("name").getValue(String.class));
                topic.setNumber_of_notes(dataSnapshot.child("number_of_notes").getValue(int.class));
                topicList.add(topic);
                mainTopicAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for(int i=0 ; i<topicList.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(topicList.get(i).getName()))
                    {
                        topicList.remove(i);
                        mainTopicAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.addChildEventListener(mylistener);

        return topicList ;
    }

}
