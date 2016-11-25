package com.example.piso.noteapp.HomeFragments;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piso.noteapp.Adapters.CustomAdapter;
import com.example.piso.noteapp.Models.MainTopic;
import com.example.piso.noteapp.Models.Note;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.ProfilePackage.UserProfileFragment;
import com.example.piso.noteapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Piso on 16/11/2016.
 */
/*this.overridePendingTransition(R.anim.animation_enter,
R.anim.animation_leave);*/
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NewNoteFragment extends Fragment {
    Person user ;
    Note  newnote ;
    private Fragment fragment ;
    Button post ;
    EditText  maintopic  , title , content  ;
    TextView finalTopic  , finalTitle  , name;
    LinearLayout finaltitlelayout , finalTopicalyout ;
    boolean fromHome  = true ;
    FirebaseDatabase mfirebaseDatabase ;

    public ArrayList<String> ListSpinnerData = new ArrayList<String>();
    TextView output = null;
    CustomAdapter adapter;
    Spinner SpinnerExample ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View   view = inflater.inflate(R.layout.new_note_fragment, container, false);

        post = (Button) view.findViewById(R.id.postbtn);
        maintopic = (EditText) view.findViewById(R.id.newNoteMaintopic);
        finalTitle = (TextView) view.findViewById(R.id.final_title);
        finalTopic = (TextView) view.findViewById(R.id.final_main_topic);
        finaltitlelayout = (LinearLayout) view.findViewById(R.id.finaltitlelayout);
        finalTopicalyout = (LinearLayout) view.findViewById(R.id.finaltopicLayout);
        finaltitlelayout.setVisibility(View.GONE);
        finalTopicalyout.setVisibility(View.GONE);
        title = (EditText) view.findViewById(R.id.newNoteTitle);
        name = (TextView) view .findViewById(R.id.nameofProfile);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView2);
      //  image(FirebaseAuth.getInstance().getCurrentUser().getUid(),imageView);
        getpersonnn(FirebaseAuth.getInstance().getCurrentUser().getUid(),imageView);
        getperson(FirebaseAuth.getInstance().getCurrentUser().getUid());
        content = (EditText) view.findViewById(R.id.newnotecontent);
        final Animation interLeft = AnimationUtils.loadAnimation(
                getActivity().getApplicationContext(), R.anim.inter_left);
        final Animation interRight = AnimationUtils.loadAnimation(
                getActivity().getApplicationContext(), R.anim.inter_right);
        final Animation outLeft = AnimationUtils.loadAnimation(
                getActivity().getApplicationContext(), R.anim.out_left);
        final Animation outright = AnimationUtils.loadAnimation(
                getActivity().getApplicationContext(), R.anim.out_right);
        finaltitlelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finaltitlelayout.getVisibility()==View.VISIBLE) {

                    finaltitlelayout.startAnimation(outLeft);
                    finaltitlelayout.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.startAnimation(interLeft);

                }
            }
        });
        user = new Person();
        newnote  = new Note();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
       if(getArguments()!=null)
        {
            fromHome  = getArguments().getBoolean("from_home");
            //user = (Person) getArguments().getSerializable("user");
        }

                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /*
                        if (title.getVisibility() == View.VISIBLE && (!title.getText().toString().equals(" ")
                                || title.getText()!=null) ) {
                            title.startAnimation(outright);
                            title.setVisibility(View.GONE);
                            finaltitlelayout.startAnimation(interRight);
                            finalTitle.setText(title.getText().toString());
                            finaltitlelayout.setVisibility(View.VISIBLE);
                              }*/
                        FirebaseUser   firebaseUser  ;
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String  str  = firebaseUser.getUid();
                        Log.v("rrrrrrrrr",str);

                        // String main = maintopic.getText().toString() ;
                         final DatabaseReference databaseReference = mfirebaseDatabase.getReference();
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("Public_MainTopic").child(maintopic.getText().toString()).exists()) {
                                    // TODO: handle the case where the data already exists
                                  //  Toast.makeText(getActivity(), "is exist ", Toast.LENGTH_SHORT).show();

                                        int number =   dataSnapshot.child("Public_MainTopic").child(maintopic.getText().toString()).child("number_of_notes").getValue(int.class);
                                        number++;
                                        databaseReference.child("Public_MainTopic").child(maintopic.getText().toString()).child("number_of_notes").setValue(number);
                                    MainTopic nmainTopic = new MainTopic() ;
                                    nmainTopic.setName(maintopic.getText().toString());
                                    nmainTopic.setNumber_of_notes(1);
                                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    if(! dataSnapshot.child("MyPersons").child(id).child("MyTopic")
                                            .child(maintopic.getText().toString()).exists())
                                    { user.CreateNewTopic( databaseReference.child("MyPersons")
                                            .child(id).child("MyTopic").child(maintopic.getText().toString())
                                            ,nmainTopic);}
                                    else
                                    {
                                      int num =  dataSnapshot.child("MyPersons").child(id).child("MyTopic")
                                              .child(maintopic.getText().toString()).child("number_of_notes").getValue(int.class);
                                        num++;
                                        databaseReference.child("MyPersons").child(id).child("MyTopic")
                                                .child(maintopic.getText().toString()).child("number_of_notes").setValue(num);
                                     }

                                    setNote();
                                    Log.v("hhhhhhh","hena");
                                }
                                else {
                                    // TODO: handle the case where the data does not yet exist
                                    MainTopic nmainTopic = new MainTopic() ;
                                    nmainTopic.setName(maintopic.getText().toString());
                                    nmainTopic.setNumber_of_notes(1);
                                    user.CreateNewTopic(databaseReference.child("Public_MainTopic").child(maintopic.getText().toString()),nmainTopic);
                                    setNote();
                                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    user.CreateNewTopic( databaseReference.child("MyPersons")
                                            .child(id).child("MyTopic").child(maintopic.getText().toString()),nmainTopic);

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                      //  List<String> stringList = new ArrayList<String>();
                      /*  MainTopic nmainTopic  =  new MainTopic() ;
                        nmainTopic.setName();*/


                        if(!fromHome)
                        {
                            android.support.v4.app.FragmentTransaction fragmentTransaction =
                                    getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentcontainer, new MainTopicFragment());
                            fragmentTransaction.commit();
                        }
                  else {
                            Log.v("heeeeeeeey" ,"contener" );

                            homefragment f = new homefragment();
                           /* Bundle args = new Bundle();
                            args.putString("mainTopic", maintopic.getText().toString());
                            args.putString("title", title.getText().toString());
                            args.putString("content", content.getText().toString());
                            f.setArguments(args);*/

                            android.support.v4.app.FragmentTransaction fragmentTransaction =
                                    getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, f);
                            fragmentTransaction.commit();



                        }
                        }
                    });



       // CustomSpinner activity = null;
          SpinnerExample = (Spinner)view.findViewById(R.id.spinner);

        // Resources passed to adapter to get image
        Resources res = getResources();

        ListSpinnerData.add("Public");
        ListSpinnerData.add("Private");

        adapter = new CustomAdapter(getActivity(), R.layout.spinner_rows, ListSpinnerData);

        // Set adapter to spinner
        SpinnerExample.setAdapter(adapter);

        // Listener called when spinner item selected
        SpinnerExample.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                // your code here

                // Get selected row data to show on screen
                Toast.makeText( getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        return  view ;
    }
    private  void setNote (){
        newnote.setTitle(title.getText().toString());
        newnote.setContent(content.getText().toString());
        newnote.setTopic(maintopic.getText().toString());
        newnote.setPublisherId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        newnote.setPublishername(name.getText().toString());
        String time  =DateFormat.getDateTimeInstance().format(new Date());
        newnote.setTime(time);
        if(SpinnerExample.getSelectedItemPosition()==0) {
            // Toast.makeText(getActivity(), "Public", Toast.LENGTH_SHORT).show();
            user.CreateNote(mfirebaseDatabase, newnote, "/GeneralNotes");
        }
        else
        {
            // Toast.makeText(getActivity(), "Private", Toast.LENGTH_SHORT).show();
            user.CreateNote(mfirebaseDatabase, newnote,"/MyPersons/" +FirebaseAuth.getInstance().getCurrentUser().getUid()+"/PrivatesNotes");
        }
    }
    private void getperson (String id   )
    {
        DatabaseReference ref = mfirebaseDatabase.getReference("/MyPersons").child(id).child("name");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name.setText(dataSnapshot.getValue(String.class) );

                // bundle.putSerializable("user" , userr);
                // fragment.setArguments(bundle);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void getpersonnn (final String id , final ImageView imageView)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
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
                Picasso.with( getActivity().getApplicationContext()).load(uri).fit().centerCrop().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
