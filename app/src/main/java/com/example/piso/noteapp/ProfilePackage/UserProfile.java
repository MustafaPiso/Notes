package com.example.piso.noteapp.ProfilePackage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.piso.noteapp.Adapters.ProfileAdapter;
import com.example.piso.noteapp.Models.Note;
import com.example.piso.noteapp.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

public class UserProfile extends AppCompatActivity {

    RecyclerView ListPrivateNotes ;
    private FirebaseDatabase firebaseDatabase ;
    private List<Note> noteList ;
    private ProfileAdapter homeAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentcontainer, new UserProfileFragment());
        fragmentTransaction.commit();
         /*ListPrivateNotes = (RecyclerView) findViewById(R.id.profilenotesrecycle);
        firebaseDatabase= FirebaseDatabase.getInstance();

        noteList= new ArrayList<Note>();
        for(int i=0 ; i <10 ;i++)
        {
            noteList.add(addnote());
            Log.v("Count " , String.valueOf(i));
        }
        homeAdapter = new ProfileAdapter(noteList,firebaseDatabase,this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ListPrivateNotes.setLayoutManager(llm);
        ListPrivateNotes.setAdapter(homeAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                       getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentcontainer, new NewNoteFragment());
                fragmentTransaction.commit();
            }
        });*/
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

}
