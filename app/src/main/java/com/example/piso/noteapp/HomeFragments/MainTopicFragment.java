package com.example.piso.noteapp.HomeFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piso.noteapp.Adapters.HomeAdapter;
import com.example.piso.noteapp.Adapters.List_people_Adapter;
import com.example.piso.noteapp.Adapters.Main_Topic_Adapter;
import com.example.piso.noteapp.Models.MainTopic;
import com.example.piso.noteapp.Models.Note;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.MyOwnProfile;
import com.example.piso.noteapp.MyOwnProfileFragment;
import com.example.piso.noteapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piso on 22/11/2016.
 */
public class MainTopicFragment extends Fragment {
    Main_Topic_Adapter mainTopicAdapter ;
    RecyclerView maintopicrecycle ;
    TextView whatisnew  ;
    ImageView imageProfile ;
    FirebaseDatabase firebaseDatabase ;
    List<MainTopic> topicList ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_topic_fragment, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        maintopicrecycle  = (RecyclerView) view.findViewById(R.id.maintopiclist);
        ShowNotes();
       return view ;
    }
    public void ShowNotes()
    {
        topicList = new ArrayList<MainTopic>();
        mainTopicAdapter=new Main_Topic_Adapter( topicList, firebaseDatabase, (AppCompatActivity) getActivity());
        topicList =  getTopicList(firebaseDatabase,topicList ,mainTopicAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        maintopicrecycle.setLayoutManager(llm);
        maintopicrecycle.setAdapter(mainTopicAdapter);
    }
   public List<MainTopic> getTopicList(final FirebaseDatabase database  ,final List<MainTopic> topicList, final Main_Topic_Adapter mainTopicAdapter){
       DatabaseReference ref ;
       if(!MyOwnProfile.viewgone)
          ref = database.getReference("/Public_MainTopic");
       else
           ref = database.getReference("/MyPersons/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/MyTopic");

       ChildEventListener mylistener;
        mylistener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MainTopic topic  = new MainTopic() ;
                if(dataSnapshot.child("name").exists()) {
                    topic.setName(dataSnapshot.child("name").getValue(String.class));
                    topic.setNumber_of_notes(dataSnapshot.child("number_of_notes").getValue(int.class));
                    topicList.add(topic);
                    mainTopicAdapter.notifyDataSetChanged();
                }
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
