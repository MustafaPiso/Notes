package com.example.piso.noteapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.piso.noteapp.Adapters.CommentAdapter;
import com.example.piso.noteapp.HomeFragments.homefragment;
import com.example.piso.noteapp.Models.Comment;
import com.example.piso.noteapp.Models.Note;
import com.example.piso.noteapp.Models.Person;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.List;

public class NoteComments extends AppCompatActivity {
    RecyclerView listComments;
    CommentAdapter commentAdapter;
    FirebaseDatabase firebaseDatabase ;
    List<Comment> comments ;
    Note note ;
    ImageButton imageButton  ;
    EditText contentComment  ;
    DatabaseReference databaseReference ;
    TextView mName  , mtime , mMainTopic , mTiltle , mContent  ;
    ImageView mPhoto ,likeimg ;
    LinearLayout mlike , mComment ;
    Person user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_comments);
        imageButton = (ImageButton) findViewById(R.id.sendComment);
        contentComment = (EditText) findViewById(R.id.contentComment);
        listComments = (RecyclerView) findViewById(R.id.list_Comments);
        firebaseDatabase = FirebaseDatabase.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        user = new Person();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( "  Home");
        mMainTopic = (TextView)  findViewById(R.id.cardTopicmain);
        mTiltle  = (TextView)  findViewById(R.id.cardTitle);
        mContent = (TextView)  findViewById(R.id.cardcontentOfNote);
        mName=(TextView) findViewById(R.id.cardname);
        mtime=(TextView)  findViewById(R.id.cardTime);
        mPhoto = (ImageView)   findViewById(R.id.cardphoto);

        mlike = (LinearLayout)  findViewById(R.id.cardlikebtn);
        mComment = (LinearLayout) findViewById(R.id.cardcommentbtn);
        likeimg = (ImageView)  findViewById(R.id.likephoto);
        if(getIntent()!= null)
        {
            Intent intent = getIntent() ;
            note = (Note) intent.getSerializableExtra("note");
        }
        getperson(note.getPublisherId(),mPhoto);
        mTiltle.setText(note.getTitle());
        mMainTopic.setText(note.getTopic());
        mContent.setText(note.getContent());
        mName.setText(note.getPublishername());
        mtime.setText(note.getTime());
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  databaseReference = firebaseDatabase.getReference("/GeneralNotes/"+ note.getId() + "/comments");
                String pushid = databaseReference.push().getKey();
                Comment comment = new Comment ();
                //databaseReference=firebaseDatabase.getReference("/MyPersons/"+note.getId()+"/name");
                comment.setName(homefragment.userName);
                comment.setContent(contentComment.getText().toString());
                databaseReference.child(pushid).setValue(comment);
                contentComment.setText("");
            }
        });

        ShowPreviousComment( note.getId());
    }
   private void  ShowPreviousComment (String note ){

       comments = new ArrayList<Comment>();
       commentAdapter =new CommentAdapter(comments);

         getComments(note);
       LinearLayoutManager llm = new LinearLayoutManager(this);
       llm.setOrientation(LinearLayoutManager.VERTICAL);
       listComments.setLayoutManager(llm);
       listComments.setAdapter(commentAdapter);
   }

    public void getComments(String id){
    DatabaseReference databaseReference = firebaseDatabase.getReference("/GeneralNotes/"+ id + "/comments");
    databaseReference.addChildEventListener(new

    ChildEventListener() {
        @Override
        public void onChildAdded (DataSnapshot dataSnapshot, String s){
            comments.add(dataSnapshot.getValue(Comment.class));
            commentAdapter.notifyDataSetChanged();
        }
        @Override
        public void onChildChanged (DataSnapshot dataSnapshot, String s){
        }
        @Override
        public void onChildRemoved (DataSnapshot dataSnapshot){
        }
        @Override
        public void onChildMoved (DataSnapshot dataSnapshot, String s){
        }
        @Override
        public void onCancelled (DatabaseError databaseError){
        }
    }
    );
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
                Picasso.with(getApplicationContext()).load(uri).fit().centerCrop().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
