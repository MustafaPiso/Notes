package com.example.piso.noteapp.ProfilePackage;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.piso.noteapp.Adapters.HomeAdapter;
import com.example.piso.noteapp.Adapters.ProfileAdapter;
import com.example.piso.noteapp.HomeFragments.NewNoteFragment;
import com.example.piso.noteapp.Models.Note;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.MyOwnProfile;
import com.example.piso.noteapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserProfileFragment extends Fragment {


    public UserProfileFragment() {
    }
    RecyclerView ListPrivateNotes ;
    private FirebaseDatabase firebaseDatabase ;
    private List<Note> noteList ;
    private ProfileAdapter homeAdapter ;
    public  static  Person user ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View root =  inflater.inflate(R.layout.default_profile, container, false);

        if(getActivity().getIntent()!=null ) {
            Intent intent = getActivity().getIntent();
            user = (Person) intent.getSerializableExtra("user");
//            Log.v("ffffffggggf",String.valueOf(user.getFollowers_Number()));
        }
        ImageView view  = (ImageView) root.findViewById(R.id.backPhoto);
        getperson(user.getId(),view);
        ListPrivateNotes = (RecyclerView) root.findViewById(R.id.profilenotesrecycle);
        firebaseDatabase= FirebaseDatabase.getInstance();
        noteList= new ArrayList<Note>();
//        Log.v("aaaadsfdaaaaa",String.valueOf( user.getFollowers_Number()));


        ShowNotes ();

        return root ;
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
    public void ShowNotes()
    {

        noteList = new ArrayList<Note>();
        homeAdapter = new ProfileAdapter(noteList,firebaseDatabase,getContext(), (AppCompatActivity) getActivity());
        homeAdapter.notifyDataSetChanged();



        FirebaseUser firebaseUser  ;
        firebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        String  str  = user.getId();

        noteList = getNotes_Of_Main(firebaseDatabase,noteList ,"/MyPersons/"+str+"/PublicNotes",null , homeAdapter);
        //noteList = user.getTasks(firebaseDatabase,noteList ,"/MyPersons/" + str+"/PrivatesNotes" ,null,homeAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ListPrivateNotes.setLayoutManager(llm);
        ListPrivateNotes.setAdapter(homeAdapter);
    }
    public  List<Note> getNotes_Of_Main(final FirebaseDatabase database, final List<Note> notes,
                                              final String maintopic ,
                                              final HomeAdapter mainTopicAdapter , final ProfileAdapter profileAdapter)
    {
        DatabaseReference ref ;
            ref = database.getReference(maintopic);
        ChildEventListener mylistener;
        mylistener=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String  str  = dataSnapshot.getValue(String.class);
                user.getnote(database , notes ,str ,mainTopicAdapter,profileAdapter);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        ref.addChildEventListener(mylistener);

        return notes ;
    }
    private void getperson (final String id , final ImageView imageView)
    {
        FirebaseDatabase firebaseDatabase  = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("/MyPersons").child(id) ;
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                user.setImage(dataSnapshot.child("image").getValue(String.class));
                image(id , imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private  void  image (String id   , final ImageView imageView){

        FirebaseStorage firebaseStorage;
        StorageReference mStorage;
        firebaseStorage = FirebaseStorage.getInstance();
        mStorage = firebaseStorage.getReference();

        mStorage.child(id+"/"+user.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                Log.v("link0", String.valueOf(uri));
                Picasso.with(getActivity().getApplicationContext()).load(uri).fit().centerCrop().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
