package com.example.piso.noteapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piso.noteapp.Home;
import com.example.piso.noteapp.HomeFragments.homefragment;
import com.example.piso.noteapp.Models.MainTopic;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.MyOwnProfile;
import com.example.piso.noteapp.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Piso on 22/11/2016.
 */
public class Main_Topic_Adapter  extends RecyclerView.Adapter<Main_Topic_Adapter.TopicHolder>{

    List<MainTopic> topicList;
    FirebaseDatabase firebase;
    AppCompatActivity activity  ;
    public Main_Topic_Adapter(List<MainTopic> taskList, FirebaseDatabase firebase , AppCompatActivity activity)
    {
        this.topicList=taskList;
        this.firebase = firebase;
        this.activity  =activity ;
    }
    @Override
    public TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.main_topic_item, parent,false);
        return new TopicHolder (itemView);
    }

    @Override
    public void onBindViewHolder(final TopicHolder holder, int position) {

        final MainTopic user = topicList.get(position);
        holder.mainTopic.setText(user.getName());
        holder.num_notes.setText(String.valueOf(user.getNumber_of_notes()) + " Notes");

        holder.cardViewmaintopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homefragment f = new homefragment();
                            Bundle args = new Bundle();
                            args.putString("mainTopic", holder.mainTopic.getText().toString());
                            f.setArguments(args);
                if(MyOwnProfile.viewgone) {
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentcontainer, f);
                    fragmentTransaction.commit();
                }
                else
                {
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, f);
                    fragmentTransaction.commit();

                }
            }
        });



    }
    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public static class TopicHolder extends RecyclerView.ViewHolder {
        protected TextView mainTopic  , num_notes ;
        CardView cardViewmaintopic  ;
        public TopicHolder(View itemView) {
            super(itemView);

            mainTopic=(TextView) itemView.findViewById(R.id.name_maintopic);
            num_notes=(TextView) itemView.findViewById(R.id.numberofnotes);
            cardViewmaintopic  = (CardView) itemView.findViewById(R.id.cardmaintopic);
        }
    }
}
