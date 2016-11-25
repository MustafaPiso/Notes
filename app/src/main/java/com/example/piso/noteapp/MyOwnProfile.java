package com.example.piso.noteapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.piso.noteapp.HomeFragments.MainTopicFragment;
import com.example.piso.noteapp.HomeFragments.NewNoteFragment;
import com.example.piso.noteapp.HomeFragments.homefragment;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.ProfilePackage.UserProfileFragment;
import com.google.android.gms.nearby.messages.PublishCallback;
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


public class MyOwnProfile extends AppCompatActivity {
  Fragment mfragment ;
    public static  boolean   viewgone = false ;
    TextView   followers , interests  , about , numberOfFollowers   ;
  LinearLayout follow ;
    Person user  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_own_profile);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(homefragment.userName);

       user = new Person();
         viewgone = false ;
        ImageView imageView = (ImageView) findViewById(R.id.backPhoto1);
        getperson(FirebaseAuth.getInstance().getCurrentUser().getUid(),imageView);
        follow = (LinearLayout) findViewById(R.id.folowgone);
        follow.setVisibility(View.GONE);
             followers  = (TextView)  findViewById(R.id.followersprofile);
             interests  = (TextView)  findViewById(R.id.interestprofile);
             about  = (TextView) findViewById(R.id.aboutprofile);
             numberOfFollowers = (TextView) findViewById(R.id.number_followers);
       followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MyOwnProfile.this, UserFollowers.class);
                intent.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
            }
        });
            interests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MyOwnProfile.this, UserInterests.class) ;
                startActivity(intent);


            }
        });
         about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(MyOwnProfile.this, UserAbout.class) ;
                startActivity(intent);

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
        mfragment = new MainTopicFragment();
         viewgone = true ;

        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentcontainer, mfragment);
        fragmentTransaction.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewNoteFragment newNoteFragment =new  NewNoteFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("from_home",false);
                newNoteFragment.setArguments(bundle);
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentcontainer,newNoteFragment);
                fragmentTransaction.commit();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        viewgone = true ;
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewgone = false ;
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
