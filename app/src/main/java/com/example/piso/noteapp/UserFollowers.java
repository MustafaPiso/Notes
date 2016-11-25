package com.example.piso.noteapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.piso.noteapp.Adapters.List_people_Adapter;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.ProfilePackage.UserProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserFollowers extends AppCompatActivity {
    public List<Person> personList ;
    public FirebaseDatabase mFirebase  ;
    RecyclerView ListMembersrecycleview ;
    List_people_Adapter listPeopleAdapter  ;
  String id  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_followers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(UserProfileFragment.user.getName());
          if(getIntent()!= null)
          {
              id = getIntent().getExtras().getString("userid");
          }
        else
          id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        personList = new ArrayList<>();
        mFirebase = FirebaseDatabase.getInstance();

        listPeopleAdapter=new List_people_Adapter(personList, mFirebase,  this);

        personList  =  getMembers(mFirebase,"Android",personList ,listPeopleAdapter) ;

        ListMembersrecycleview = (RecyclerView)  findViewById(R.id.list_Followers);
        ListMembersrecycleview.setAdapter(listPeopleAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ListMembersrecycleview.setLayoutManager(linearLayoutManager);


    }
    public List<Person> getMembers(final FirebaseDatabase database, String committeeName,
                                   final List<Person> members, final List_people_Adapter listPeopleAdapter){

        DatabaseReference ref = database.getReference("/MyPersons/"+id+"/Followers");

        ChildEventListener mylistener;
        mylistener=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String member =dataSnapshot.getValue(String.class);
                getmember(database,member,members,listPeopleAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for(int i=0 ; i<members.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(members.get(i).getId()))
                    {
                        members.remove(i);
                        listPeopleAdapter.notifyDataSetChanged();
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

        return members ;
    }// get all tasks of a committee

    private  void getmember(final FirebaseDatabase database,String member  ,
                            final List<Person> list, final List_people_Adapter listPeopleAdapter){

        DatabaseReference ref = database.getReference("/MyPersons/"+member);
     ref.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
             Person user = dataSnapshot.getValue(Person.class);
             list.add(user);
             listPeopleAdapter.notifyDataSetChanged();
             Log.v("list ", String.valueOf(list.size()));
             Log.v("lissst ", user.getName());

         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });

    }

}





