package com.example.piso.noteapp.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.piso.noteapp.Adapters.HomeAdapter;
import com.example.piso.noteapp.Adapters.Main_Topic_Adapter;
import com.example.piso.noteapp.Adapters.ProfileAdapter;
import com.example.piso.noteapp.Home;
import com.example.piso.noteapp.HomeFragments.homefragment;
import com.example.piso.noteapp.MyOwnProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Piso on 12/11/2016.
 */
public class Person implements Serializable {

    private String Name;
    private String Id;
    private String Email;
    private String Phone;
    private String birthDate;
    private String Gender;
    private String Image;
    private  int Followers_Number ;
    FirebaseDatabase mfirebaseDatabase ;

    public List<String> getInterests() {
        return interests;
    }


    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    List<String> interests ;

    public int getFollowers_Number() {
        return Followers_Number;
    }

    public void setFollowers_Number(int followers_Number) {
        Followers_Number = followers_Number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


    public void CreateNote ( FirebaseDatabase database,Note note, String URL  ) {

            DatabaseReference databaseReference = database.getReference(URL);
            URL = URL +"/"+ databaseReference.push().getKey() ;
            databaseReference = database.getReference(URL);
            databaseReference.setValue(note);
            Log.v("ffffffffffffffffff", databaseReference.getKey());
            String idofnote = databaseReference.getKey();
            setMytopic(idofnote,database,note.getTopic());
            database =  FirebaseDatabase.getInstance();

           databaseReference = database.getReference("/Public_MainTopic/"+note.getTopic()+"/Notes") ;
           databaseReference.child(idofnote).setValue(idofnote);

           FirebaseUser firebaseUser  ;
           firebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
           String  str  = firebaseUser.getUid();

         /*  database =  FirebaseDatabase.getInstance();
           databaseReference = database.getReference("/MyPersons/"+str+"/MyTopic/"+note.getTopic()) ;
           databaseReference.child(idofnote).setValue(idofnote);*/
        database =  FirebaseDatabase.getInstance();
        databaseReference = database.getReference("/MyPersons/"+str+"/MyTopic/"+note.getTopic()) ;
       // Log.v("tttttttt", databaseReference.getRef().toString()) ;
        databaseReference.child("Notes").child(idofnote).setValue(idofnote);


    }
    private  void setMytopic (String noteid , FirebaseDatabase database , String Topic)
    {
        String  str  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database =  FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("/MyPersons/"+str+"/MyTopic/"+Topic) ;
        // Log.v("tttttttt", databaseReference.getRef().toString()) ;
        databaseReference.child("Notes").child(noteid).setValue(noteid);
    }
    public void CreateNewTopic (  DatabaseReference databaseReference ,MainTopic mainTopic  ) {
       Log.v("geeeet", databaseReference.getRef().toString() ) ;
         databaseReference.setValue(mainTopic);


    }

    public void RemoveNote (final  FirebaseDatabase  database , String note  , String person , int position ){

        DatabaseReference ref = database.getReference("/GeneralNotes").child(note) ;
                ref.removeValue();
          ref = database.getReference("/MyPersons").child(person).child("PublicNotes").child(note) ;
         ref.removeValue();
        homefragment.noteList.remove(position);
        homefragment.homeAdapter.notifyDataSetChanged();


    }
    public void UpdataNote (){
    }
    public void   makeLike (){
    }
    public void  sendComment(){
    }
     public  void follow_someone  (final  FirebaseDatabase  database  , final String source , final String Dist )
     {
         final DatabaseReference ref = database.getReference("/MyPersons/"+Dist);
         ref.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 if (dataSnapshot.child("Followers").child(source).exists()) {
                     // TODO: handle the case where the data already exists

                     /*
                       Here I should remove  the follower
                      */
                     int number =   dataSnapshot.child("followers_Number").getValue(int.class);
                     number--;
                     ref.child("followers_Number").setValue(number);
                     DatabaseReference ref = database.getReference("/MyPersons/"+Dist+"/Followers");
                     ref.child(source).removeValue();

                 }
                 else {
                     // TODO: handle the case where the data does not yet exist
                    // dataSnapshot.child("Followers").child(source).
                     Log.v("sasasasa", dataSnapshot.child("followers_Number").getRef().toString());
                     int number =   dataSnapshot.child("followers_Number").getValue(int.class);
                     number++;
                     ref.child("followers_Number").setValue(number);

                     DatabaseReference ref = database.getReference("/MyPersons/"+Dist+"/Followers");
                     ref.child(source).setValue(source);
                 }

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

         //ref =  database.getReference("/MyPersons/"+Dist)

     }
    public  List<Note> getNotes_Of_MainTopic (final FirebaseDatabase database, final List<Note> notes,
                                              final String maintopic ,
                                              final HomeAdapter mainTopicAdapter , final ProfileAdapter profileAdapter)
    {
        DatabaseReference ref ;
        if(!MyOwnProfile.viewgone)
        {
            ///MyPersons/"+str+"/PublicNotes

            ref = database.getReference("/Public_MainTopic/"+maintopic+"/Notes");

        }
        else
        {
            ref = database.getReference("/MyPersons/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/MyTopic/"+maintopic+"/Notes");
            Log.v("ssssss",ref.getRef().toString());
        }


        ChildEventListener mylistener;
        mylistener=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String  str  = dataSnapshot.getValue(String.class);
                 getnote(database , notes ,str ,mainTopicAdapter,profileAdapter);

               // Note note =dataSnapshot.child("GeneralNotes").getValue(Note.class); // cast child to task
//                note.setId(dataSnapshot.getKey());
                //notes.add(note);

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Log.v("qqqqq",  dataSnapshot.getKey());
              /*  for(int i=0 ; i<notes.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(notes.get(i).getId()))
                    {
                        notes.remove(i);
                        homeAdapter.notifyDataSetChanged();
                    }
                }*/
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
   /* public Person getCurrentPreson (final FirebaseDatabase database,String userid )
    {
        DatabaseReference ref = database.getReference("/MyPersons").child(userid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Note note =dataSnapshot.getValue(Note.class) ;
                note.setId(dataSnapshot.getKey());
                notes.add(note);
                if(profileAdapter==null)
                    mainTopicAdapter.notifyDataSetChanged();
                else
                    profileAdapter.notifyDataSetChanged();

                Log.v("aaaaaaaaaaaa", String.valueOf(notes.size()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }*/
   public void getnote(final FirebaseDatabase database, final List<Note> notes, final String url
           , final HomeAdapter mainTopicAdapter, final ProfileAdapter profileAdapter)
    {
        DatabaseReference ref = database.getReference("/GeneralNotes").child(url);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Note note =dataSnapshot.getValue(Note.class) ;
                if(note!=null)
                {
                    note.setId(dataSnapshot.getKey());
                    notes.add(note);
                    if (profileAdapter == null)
                        mainTopicAdapter.notifyDataSetChanged();
                    else
                        profileAdapter.notifyDataSetChanged();

                    Log.v("aaaaaaaaaaaa", String.valueOf(notes.size()));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public List<Note> getTasks(final FirebaseDatabase database, final List<Note> notes, String Url,
                               final HomeAdapter homeAdapter , final ProfileAdapter profileAdapter ){

        DatabaseReference ref = database.getReference(Url);
        ChildEventListener mylistener;
        mylistener=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Note note =dataSnapshot.getValue(Note.class); // cast child to task
                note.setId(dataSnapshot.getKey());
                notes.add(note);
                Log.v("gggggg" , String.valueOf(notes.size()));
                if(profileAdapter==null)
                {
                    homeAdapter.notifyDataSetChanged();
                    Log.v("Noited " , String.valueOf(notes.size()));

                }
                else
                    profileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Log.v("qqqqq",  dataSnapshot.getKey());
              /*  for(int i=0 ; i<notes.size() ; i++)
                {
                    if(dataSnapshot.getKey().equals(notes.get(i).getId()))
                    {
                        notes.remove(i);
                        homeAdapter.notifyDataSetChanged();
                    }
                }*/
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref.addChildEventListener(mylistener);

        return notes;
    }// get all tasks of a committee

}
