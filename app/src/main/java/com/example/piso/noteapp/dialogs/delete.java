package com.example.piso.noteapp.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piso.noteapp.Adapters.HomeAdapter;
import com.example.piso.noteapp.HomeFragments.homefragment;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by Piso on 25/11/2016.
 */
public class delete extends DialogFragment {


    TextView yes , no  ;
    FirebaseDatabase firebase ;
    String noteid ;
    int position  ;
    private Person user ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View  view = inflater.inflate(R.layout.delete_note,container,false);
       yes = (TextView) view.findViewById(R.id.yesbtn);
        no = (TextView) view.findViewById(R.id.nobtn);
        firebase = FirebaseDatabase.getInstance();
        user = new Person ();


        if(getArguments()!=null)
        {
            noteid = getArguments().getString("noteid");
            position = getArguments().getInt("position");
        }
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "yes", Toast.LENGTH_SHORT).show();
                //getDialog().hide();
                user.RemoveNote(firebase , noteid , FirebaseAuth.getInstance().getCurrentUser().getUid() , position );
                Toast.makeText(getActivity() , String.valueOf(position) , Toast.LENGTH_LONG).show();

                getDialog().cancel();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "no", Toast.LENGTH_SHORT).show();
              getDialog().dismiss();
            }
        });

        return  view;
    }
}
