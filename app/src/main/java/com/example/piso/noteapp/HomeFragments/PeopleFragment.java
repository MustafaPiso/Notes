package com.example.piso.noteapp.HomeFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.piso.noteapp.Adapters.List_people_Adapter;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.R;
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
public class PeopleFragment extends Fragment {
    public List <Person>  personList ;
     public FirebaseDatabase mFirebase  ;
    RecyclerView  ListMembersrecycleview ;
    List_people_Adapter listPeopleAdapter  ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view  = inflater.inflate(R.layout.peoplefragment , container , false ) ;
        personList = new ArrayList<>();
        mFirebase = FirebaseDatabase.getInstance();

        listPeopleAdapter=new List_people_Adapter(personList, mFirebase, getContext());

        personList  =  getMembers(mFirebase,"Android",personList ,listPeopleAdapter) ;

        ListMembersrecycleview = (RecyclerView) view.findViewById(R.id.list_people);
        ListMembersrecycleview.setAdapter(listPeopleAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ListMembersrecycleview.setLayoutManager(linearLayoutManager);

        return  view  ;
    }
    public List<Person> getMembers(final FirebaseDatabase database, String committeeName,
                                   final List<Person> members, final List_people_Adapter listPeopleAdapter){

        DatabaseReference ref = database.getReference("/MyPersons");

        ChildEventListener mylistener;
        mylistener=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Person member =dataSnapshot.getValue(Person.class);
                member.setId(dataSnapshot.getKey());
                members.add(member);
                listPeopleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for(int i=0 ; i<members.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(members.get(i).getId()))
                    {
                        members.remove(i);
                        listPeopleAdapter.notifyDataSetChanged();
                    }
                }
                Person member =dataSnapshot.getValue(Person.class); // cast child to member
                member.setId(dataSnapshot.getKey());
                members.add(member);
                listPeopleAdapter.notifyDataSetChanged();
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

}
