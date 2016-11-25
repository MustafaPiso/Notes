package com.example.piso.noteapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piso.noteapp.EditNote;
import com.example.piso.noteapp.HomeFragments.homefragment;
import com.example.piso.noteapp.Models.Note;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.NoteComments;
import com.example.piso.noteapp.R;
import com.example.piso.noteapp.UserAbout;
import com.example.piso.noteapp.dialogs.ChangePrivacy;
import com.example.piso.noteapp.dialogs.delete;
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
import java.util.List;

/**
 * Created by Piso on 16/11/2016.
 */
public class HomeAdapter extends   RecyclerView.Adapter<HomeAdapter.NoteHolder>{

   public List<Note> NoteList;
    Person user  ;
    FirebaseDatabase firebase;
    private Context mcontext  ;
    NoteHolder mholder ;
    AppCompatActivity appCompatActivity ;
    NoteSittingAdapter sittingAdapter ;
    public  static boolean deleteNote = false ;
    public HomeAdapter(List<Note> noteList, FirebaseDatabase firebase, Context context , AppCompatActivity appCompatActivityِ)
    {
        this.NoteList=noteList;
        this.firebase = firebase;
        this.mcontext = context ;
        this.appCompatActivity = appCompatActivityِ ;
        user  = new Person();
    }
    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.home_note_item, parent,false);
        mholder =new NoteHolder (itemView);
        Log.v("Adapter","Createeed");
        return  mholder ;
    }

    @Override
    public void onBindViewHolder(final NoteHolder holder, final int position) {
        final Note note = NoteList.get(position);
        holder.mContent.setText(note.getContent());
        holder.mName.setText(note.getPublishername());
        holder.mtime.setText(note.getTime());
        holder.mTiltle.setText(note.getTitle());
        holder.mMainTopic.setText(note.getTopic());
        getperson(note.getPublisherId(),holder.mPhoto);
        holder.mMainTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homefragment f = new homefragment();
                Bundle args = new Bundle();
                args.putString("mainTopic", holder.mMainTopic.getText().toString());
                f.setArguments(args);

                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        appCompatActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, f);
                fragmentTransaction.commit();
            }
        });
       // Log.v("sadssasa",note.getPublisherId()) ;

        holder.mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(appCompatActivity , NoteComments.class) ;
                intent.putExtra("note", note);
                appCompatActivity.startActivity(intent);
                // Toast.makeText(mcontext, "My "+String.valueOf(position)+" Comment", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(mcontext, "My "+String.valueOf(position)+" Like", Toast.LENGTH_SHORT).show();
               holder.likeimg.setImageResource(R.drawable.likeiconblue);
            }
        });
        holder.mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(mcontext, "My "+String.valueOf(position)+" Profile", Toast.LENGTH_SHORT).show();
                //mPhoto.setImageResource(R.drawable.likeiconblue);
            }
        });

//        image(FirebaseAuth.getInstance().getCurrentUser().getUid(),holder.mPhoto) ;
        holder.sitting.setSelection(3);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(note.getPublisherId()))
        {
            spinnerAdapter(holder.sitting ,true , note.getId() , position );
        }
        else
        {
            spinnerAdapter(holder.sitting ,false , note.getId() , position );
        }

    }


    @Override
    public int getItemCount() {
        return NoteList.size();
    }
    private  void spinnerAdapter (Spinner spinner , final boolean owner  ,final  String noteid ,final int positioon ){

    List<String>  ListSpinnerData = new ArrayList<>();
       ListSpinnerData.add("Edit");
        ListSpinnerData.add("Privacy");
        ListSpinnerData.add("Privacy");
        if(owner)
        ListSpinnerData.add("Delete");

        sittingAdapter = new NoteSittingAdapter(appCompatActivity, R.layout.spinner_sitting_own_card,
                (ArrayList) ListSpinnerData , owner);

        // Set adapter to spinner
        spinner.setAdapter(sittingAdapter  );

        // Listener called when spinner item selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                // your code here

                // Get selected row data to show on screen
              //  Toast.makeText( appCompatActivity, String.valueOf(position), Toast.LENGTH_SHORT).show();
                if(owner) {
                    if (position == 1) {

                        // appCompatActivity.startActivity(new Intent(appCompatActivity, EditNote.class));

                    } else if (position == 2) {
                        android.app.FragmentManager manager = appCompatActivity.getFragmentManager();
                        ChangePrivacy deletedialog = new ChangePrivacy();

                        // paas if the currnet note public or private
                        Bundle bundle = new Bundle();
                        bundle.putString("noteid", noteid );
                        bundle.putInt("position",positioon);
                        bundle.putSerializable("note",NoteList.get(positioon));
                        deletedialog.setArguments(bundle);
                        deletedialog.show(manager, null);
                    }
                    else if(position == 3) {
                        android.app.FragmentManager manager = appCompatActivity.getFragmentManager();
                        delete deletedialog = new delete();
                        Bundle bundle = new Bundle();
                        bundle.putString("noteid", noteid );
                        bundle.putInt("position",positioon);
                        deletedialog.setArguments(bundle);
                        deletedialog.show(manager, null);
                        Toast.makeText(appCompatActivity , "passed" , Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(position==1)
                    {
                        user.CreateNote(firebase, NoteList.get(positioon),"/MyPersons/" +FirebaseAuth.getInstance().getCurrentUser().getUid()+"/PrivatesNotes");
                        Toast.makeText(appCompatActivity, "Saved " , Toast.LENGTH_LONG).show();
                    }
                    else  if(position==2)
                    {
                        user.follow_someone(firebase, NoteList.get(positioon).getPublisherId() , FirebaseAuth.getInstance().getCurrentUser().getUid());
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    public static class NoteHolder extends RecyclerView.ViewHolder {
        protected TextView mName  , mtime , mMainTopic , mTiltle , mContent  ;
        ImageView mPhoto ,likeimg ;
        LinearLayout mlike , mComment ;
        Spinner sitting ;

        public NoteHolder(View itemView) {
            super(itemView);
            mMainTopic = (TextView) itemView.findViewById(R.id.cardTopicmain);
            mTiltle  = (TextView) itemView.findViewById(R.id.cardTitle);
            mContent = (TextView) itemView.findViewById(R.id.cardcontentOfNote);
            mName=(TextView) itemView.findViewById(R.id.cardname);
            mtime=(TextView) itemView.findViewById(R.id.cardTime);
            mPhoto = (ImageView)  itemView.findViewById(R.id.cardphoto);
            mlike = (LinearLayout) itemView.findViewById(R.id.cardlikebtn);
            mComment = (LinearLayout)itemView.findViewById(R.id.cardcommentbtn);
            likeimg = (ImageView) itemView.findViewById(R.id.likephoto);
            sitting = (Spinner) itemView.findViewById(R.id.spinnersitting);
            Log.v("Adapter","Linkeeed");
        }
    }
    private void getperson (final String id , final ImageView imageView)
    {
        DatabaseReference ref = firebase.getReference("/MyPersons").child(id) ;
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
                Picasso.with(appCompatActivity.getApplicationContext()).load(uri).fit().centerCrop().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
