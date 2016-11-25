package com.example.piso.noteapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piso.noteapp.R;

import java.util.ArrayList;

/**
 * Created by Piso on 25/11/2016.
 */
public class NoteSittingAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private ArrayList data;
    String tempValues = null;
    LayoutInflater inflater;
    boolean owner  ;

    /*************
     * CustomAdapter Constructor
     *****************/
    public NoteSittingAdapter(
            Activity activitySpinner,
            int textViewResourceId,
            ArrayList objects,
            boolean owner

    ) {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data = objects;
        this.owner = owner ;


        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_sitting_own_card, parent, false);
        TextView label = (TextView) row.findViewById(R.id.txtown);
        ImageView companyLogo = (ImageView) row.findViewById(R.id.imageown);
      if(owner) {
          if (position == 1) {

              label.setText("Edit Note");
              companyLogo.setImageResource(R.drawable.editicon);
          } else if (position == 2) {
              label.setText("Change Privacy");
              companyLogo.setImageResource(R.drawable.publicicon2);
          } else if(position == 3 ) {
              label.setText("Delete");
              companyLogo.setImageResource(R.drawable.deleteicon);
          }
      }
        else
      {
          if (position == 1) {
          label.setText("Save Note");
          companyLogo.setImageResource(R.drawable.saveicon);
       } else if (position == 2) {
          label.setText("Follow The Person");
          companyLogo.setImageResource(R.drawable.followicon);
      }

      }
        return row;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/

        View row = inflater.inflate(R.layout.spinner_empty, parent, false);



        return row ;

    }
}
