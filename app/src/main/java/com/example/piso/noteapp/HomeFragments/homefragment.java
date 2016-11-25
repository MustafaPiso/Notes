package com.example.piso.noteapp.HomeFragments;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piso.noteapp.Adapters.HomeAdapter;
import com.example.piso.noteapp.Models.Note;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.MyOwnProfile;
import com.example.piso.noteapp.ProfilePackage.UserProfile;
import com.example.piso.noteapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
 * Created by Piso on 15/11/2016.
 */
public class homefragment extends android.support.v4.app.Fragment {
    private static Fragment fragment ;
    public static List<Note> noteList ;
    public static HomeAdapter homeAdapter ;
    RecyclerView homeNotesrecyclerView ;
    TextView whatisnew  ;
    ImageView imageProfile ;
    FirebaseDatabase firebaseDatabase ;
   public static String  userName ;
    public  static  Person user  ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     View   view = inflater.inflate(R.layout.home_fragment, container, false);
        CardView cardView = (CardView) view.findViewById(R.id.viewgonee);
        if(MyOwnProfile.viewgone)
        {
            cardView.setVisibility(View.GONE);
            //MyOwnProfile.viewgone = false ;
        }
        else
            cardView.setVisibility(View.VISIBLE);

        homeNotesrecyclerView  = (RecyclerView) view.findViewById(R.id.homenotesrecycle);

        firebaseDatabase= FirebaseDatabase.getInstance();
        imageProfile = (ImageView) view.findViewById(R.id.profilephoto);
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent( getActivity(), MyOwnProfile.class) ;
                intent.putExtra("Id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent );
            }
        });
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




      /*  if(getArguments()!=null)
        {
            Note note = new Note() ;
            note.setTitle(getArguments().getString("title"));
            note.setTopic(getArguments().getString("mainTopic"));
            note.setContent(getArguments().getString("content"));
            Calendar c = Calendar.getInstance();
            int seconds = c.get(Calendar.MINUTE);
            note.setTime(String.valueOf(seconds));
            noteList.add(0,note);

            Log.v("iteeeeeeee",String.valueOf( noteList.size()));
            homeAdapter.notifyDataSetChanged();

        }
        else
        Log.v("itemmmmmmmm","Get Arguments   Null ");*/

        whatisnew = (TextView) view.findViewById(R.id.whatisnewnote);

        whatisnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                       getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });
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
            noteList = user.getNotes_Of_MainTopic(firebaseDatabase,noteList ,str, homeAdapter,null);

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
     DatabaseReference ref = firebaseDatabase.getReference("/MyPersons").child(id) ;
     ref.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {

             userName =dataSnapshot.child("name").getValue(String.class) ;
             user.setImage(dataSnapshot.child("image").getValue(String.class));
             image(FirebaseAuth.getInstance().getCurrentUser().getUid());
         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
     });

 }
    private  void  image ( String id ){

         FirebaseStorage firebaseStorage;
        StorageReference mStorage;
         firebaseStorage = FirebaseStorage.getInstance();
        mStorage = firebaseStorage.getReference();

        mStorage.child(id+"/"+user.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                Log.v("link0", String.valueOf(uri));
                Picasso.with(getActivity().getApplicationContext()).load(uri).fit().centerCrop().into(imageProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
