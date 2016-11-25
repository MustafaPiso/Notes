package com.example.piso.noteapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piso.noteapp.Adapters.HomeAdapter;
import com.example.piso.noteapp.HomeFragments.NewNoteFragment;
import com.example.piso.noteapp.Models.Note;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.ProfilePackage.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MyOwnProfileFragment extends Fragment {
    private static Fragment fragment ;
    public static List<Note> noteList ;
    public static HomeAdapter homeAdapter ;
    RecyclerView homeNotesrecyclerView ;
    TextView whatisnew  ;
    ImageView imageProfile ;
    FirebaseDatabase firebaseDatabase ;
    public static String  userName ;
    public  static Person user  ;

    public MyOwnProfileFragment() {
    }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View   view = inflater.inflate(R.layout.fragment_my_own_profile, container, false);
            homeNotesrecyclerView  = (RecyclerView) view.findViewById(R.id.prorfilecardlist);
            firebaseDatabase= FirebaseDatabase.getInstance();
            user = new Person() ;
            fragment = new NewNoteFragment();
            Bundle bundle =  new Bundle();
            getperson(FirebaseAuth.getInstance().getCurrentUser().getUid(),fragment,bundle);

            if(getArguments()!=null)
            {
                String maintopic=  getArguments().getString("mainTopic") ;
                ShowNotes(maintopic);
            }
            else
                ShowNotes("Public");

            return view ;
        }
        private Note addnote (){
            Note note  = new Note() ;
            note.setContent("1- Mustafa Gamal abass \n 2-MustafaGamal Abass \n 3-MustafaGamalAbass\n");
            note.setPublishername("Mustafa Gamal Abass ");
            Calendar c = Calendar.getInstance();
            int minute = c.get(Calendar.MINUTE);
            int hour  = c.get(Calendar.HOUR);
            String Time  = hour + " : "+  minute ;
            note.setTime(Time);
            note.setTitle("RecycleView .. ");
            note.setTopic("Android");
            return  note ;
        }
        public void ShowNotes(String str ) {
            Log.v("hey " , " I'm here ");
            //  DatabaseReference ref = database.getReference().child("Public_MainTopic").child(maintopic).child("Notes");
            noteList = new ArrayList<Note>();
            homeAdapter=new HomeAdapter( noteList, firebaseDatabase , getContext(), (AppCompatActivity) getActivity());
            if(str.equals("Public"))
                noteList = user.getTasks(firebaseDatabase,noteList ,"/GeneralNotes" ,homeAdapter,null);
            else
                noteList = user.getNotes_Of_MainTopic(firebaseDatabase,noteList ,"/Public_MainTopic/"+str+"/Notes", homeAdapter,null);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            homeNotesrecyclerView.setLayoutManager(llm);
            homeNotesrecyclerView.setAdapter(homeAdapter);
        }
        public  static void set_new_note (String maintopic , String  title ,String content ){
            Note note = new Note() ;

            note.setContent(content);
            note.setTopic(maintopic);
            note.setTitle(title);

            noteList.add(0,note);
        }
        private void getperson (String id  , final  Fragment fragment , final Bundle bundle)
        {
            DatabaseReference ref = firebaseDatabase.getReference("/MyPersons").child(id).child("name");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    userName =dataSnapshot.getValue(String.class) ;

                    // bundle.putSerializable("user" , userr);
                    // fragment.setArguments(bundle);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }



