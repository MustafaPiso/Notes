package com.example.piso.noteapp.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.piso.noteapp.Models.Note;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Piso on 25/11/2016.
 */
public class ChangePrivacy extends DialogFragment {

        RadioButton r1 , r2 ;
    TextView ok , cancel  ;
    String noteid  ;
    int position ;
   FirebaseDatabase firebase ;
    LinearLayout publicbtn  , privatebtn  ;
    Person user ;
    Note newnote ;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_privacy, container, false);
        r1 = (RadioButton) view.findViewById(R.id.radioButton);
        r2 = (RadioButton) view.findViewById(R.id.radioButton1);
        publicbtn = (LinearLayout) view.findViewById(R.id.public_chooce);
        privatebtn = (LinearLayout) view.findViewById(R.id.private_chooce);
        ok = (TextView) view.findViewById(R.id.okbtn);
        cancel = (TextView) view.findViewById(R.id.cancelbtn) ;
        firebase = FirebaseDatabase.getInstance();
        user = new Person() ;

        if(getArguments()!=null)
        {
            noteid = getArguments().getString("noteid");
            position = getArguments().getInt("position");
            newnote = (Note)getArguments().getSerializable("note");
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(r2.isChecked()) {
                    user.RemoveNote(firebase, noteid, FirebaseAuth.getInstance().getCurrentUser().getUid(), position);
                    user.CreateNote(firebase, newnote, "/MyPersons/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/PrivatesNotes");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        publicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(true);
                r2.setChecked(false);
               // getDialog().dismiss();
            }
        });
        privatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(false);
                r2.setChecked(true);
              //  getDialog().dismiss();
            }
        });

        r1.setChecked(true);

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(r1.isChecked())
                {
                    r2.setChecked(false);
                }
                else
                    r2.setChecked(true);
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(r2.isChecked())
                {
                    r1.setChecked(false);
                }
                else
                    r1.setChecked(true);
            }
        });

        return view;
    }
}
