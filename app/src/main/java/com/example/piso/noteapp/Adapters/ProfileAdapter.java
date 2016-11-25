package com.example.piso.noteapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piso.noteapp.Models.Note;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.NoteComments;
import com.example.piso.noteapp.ProfilePackage.UserProfile;
import com.example.piso.noteapp.ProfilePackage.UserProfileFragment;
import com.example.piso.noteapp.R;
import com.example.piso.noteapp.UserAbout;
import com.example.piso.noteapp.UserFollowers;
import com.example.piso.noteapp.UserInterests;
import com.example.piso.noteapp.dialogs.ChangePrivacy;
import com.example.piso.noteapp.dialogs.delete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piso on 20/11/2016.
 */
public class ProfileAdapter    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    List<Note> NoteList;
    FirebaseDatabase firebase;
    private Context mcontext  ;
    String[] data;
    NoteSittingAdapter sittingAdapter ;
    Person user  ;
    AppCompatActivity appCompatActivity ;

    public ProfileAdapter(List<Note> noteList, FirebaseDatabase firebase, Context context  , AppCompatActivity appCompatActivity) {
        this.NoteList=noteList;
        this.firebase = firebase;
        this.mcontext = context ;
        this.appCompatActivity = appCompatActivity ;
        if(UserProfileFragment.user!=null)
        {
            this.user = UserProfileFragment.user ;
        }
     }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView ;
        if (viewType == TYPE_ITEM) {
              itemView= LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.home_note_item, parent,false);
            //inflate your layout and pass it to view holder
            return new NoteHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            itemView= LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.about_profile, parent,false);
            //inflate your layout and pass it to view holder
            return new VHHeader(itemView);
         }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Note note = NoteList.get(position);
        if (holder instanceof NoteHolder) {

            ((NoteHolder) holder).mContent.setText(note.getContent());
            ((NoteHolder) holder).mName.setText(note.getPublishername());
            ((NoteHolder) holder).mtime.setText(note.getTime());
            ((NoteHolder) holder).mTiltle.setText(note.getTitle());
            ((NoteHolder) holder).mMainTopic.setText(note.getTopic());
            ((NoteHolder) holder). mComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent  = new Intent(appCompatActivity , NoteComments.class) ;
                    intent.putExtra("note", note);
                    appCompatActivity.startActivity(intent);
                    // Toast.makeText(mcontext, "My "+String.valueOf(position)+" Comment", Toast.LENGTH_SHORT).show();
                }
            });
            ((NoteHolder) holder).mlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  Toast.makeText(mcontext, "My "+String.valueOf(position)+" Like", Toast.LENGTH_SHORT).show();
                    ((NoteHolder) holder).likeimg.setImageResource(R.drawable.likeiconblue);
                }
            });
            ((NoteHolder) holder).mPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(mcontext, "My "+String.valueOf(position)+" Profile", Toast.LENGTH_SHORT).show();
                    //mPhoto.setImageResource(R.drawable.likeiconblue);
                }
            });

            if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(note.getPublisherId()))
            {
                spinnerAdapter(((NoteHolder) holder).sitting ,true , note.getId() , position );
            }
            else
            {
                spinnerAdapter(((NoteHolder) holder).sitting ,false , note.getId() , position );
            }
        }
        else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header
            ((VHHeader) holder).follow.setText("Follow");
            ((VHHeader) holder).numberOfFollowers.setText(String.valueOf(user.getFollowers_Number()));
            ((VHHeader) holder).follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(((VHHeader) holder).follow.getText().equals("Follow")) {
                        ((VHHeader) holder).follow.setText("Following");
                        ((VHHeader) holder).follow.setTextColor(Color.CYAN);
                        ((VHHeader) holder).numberOfFollowers.setText(String.valueOf(user.getFollowers_Number()+1));
                    }
                    else
                    {
                        ((VHHeader) holder).follow.setText("Follow");
                        ((VHHeader) holder).numberOfFollowers.setText(String.valueOf(user.getFollowers_Number()));
                    }
                    if(user.getId()==null)
                    { Log.v("Sikooo", " null"); }
                    user.follow_someone(firebase, user.getId() , FirebaseAuth.getInstance().getCurrentUser().getUid());
                }
            });
            ((VHHeader) holder).followers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent  = new Intent(mcontext, UserFollowers.class);
                    intent.putExtra("userid", UserProfileFragment.user.getId());
                    mcontext.startActivity(intent);

                }
            });
            ((VHHeader) holder).interests.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(mcontext, UserInterests.class) ;
                    mcontext.startActivity(intent);

                }
            });
            ((VHHeader) holder).about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent  = new Intent(mcontext, UserAbout.class) ;
                    mcontext.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return NoteList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private String getItem(int position) {
        return data[position - 1];
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

    class VHHeader extends RecyclerView.ViewHolder {
       TextView follow , followers , interests  , about , numberOfFollowers   ;

        public VHHeader(View itemView) {
            super(itemView);
            follow = (TextView) itemView.findViewById(R.id.followprofile);
            followers  = (TextView) itemView.findViewById(R.id.followersprofile);
            interests  = (TextView) itemView.findViewById(R.id.interestprofile);
            about  = (TextView) itemView.findViewById(R.id.aboutprofile);
            numberOfFollowers = (TextView) itemView.findViewById(R.id.number_followers);

        }
    }
    private  void spinnerAdapter (Spinner spinner , final boolean owner  , final  String noteid , final int positioon ){

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
}