package com.example.piso.noteapp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.List;

/**
 * Created by Piso on 11/11/2016.
 */
public class List_people_Adapter extends RecyclerView.Adapter<List_people_Adapter.MemberHolder>{


    List<Person> memberList;
    FirebaseDatabase firebase;
    Context context ;
    Person user ;
    public List_people_Adapter(List<Person> taskList, FirebaseDatabase firebase , Context context)
    {
        this.context= context ;
        this.memberList=taskList;
        this.firebase = firebase;
        user = new Person();
    }
    @Override
    public MemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_people_item, parent,false);
        return new MemberHolder (itemView);
     }

    @Override
    public void onBindViewHolder(MemberHolder holder, final int position) {
        final Person user = memberList.get(position);
        holder.mName.setText(user.getName());
        holder.followers.setText(user.getFollowers_Number() + " Followers");
        getpersonnn(user.getId(),holder.mPhoto);
        holder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent ;
                if(memberList.get(position).getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    intent  = new Intent(context, MyOwnProfile.class);
                    else
                     intent  = new Intent(context, UserProfile.class);
                Log.v("ffffffffff",String.valueOf(user.getId()));
                intent.putExtra("user" , user);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public static class MemberHolder extends RecyclerView.ViewHolder {
        protected TextView  mName  , followers ;
         ImageView  mPhoto ;
        LinearLayout user ;

        public MemberHolder(View itemView) {
            super(itemView);
            mName=(TextView) itemView.findViewById(R.id.cardview_name);
            followers=(TextView) itemView.findViewById(R.id.cardview_followers);
            mPhoto = (ImageView)  itemView.findViewById(R.id.cardview_imgmember);
            user = (LinearLayout) itemView.findViewById(R.id.usercard);
        }
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
                Picasso.with( context.getApplicationContext()).load(uri).fit().centerCrop().into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
