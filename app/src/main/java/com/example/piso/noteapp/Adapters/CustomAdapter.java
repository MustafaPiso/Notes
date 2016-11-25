package com.example.piso.noteapp.Adapters;

/**
 * Created by Piso on 22/11/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piso.noteapp.R;

import java.util.ArrayList;

/***** Adapter class extends with ArrayAdapter ******/
public class CustomAdapter extends ArrayAdapter<String>{

    private Activity activity;
    private ArrayList data;
    String tempValues=null;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(
            Activity activitySpinner,
            int textViewResourceId,
            ArrayList objects

    )
    {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;


        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_rows, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues =   data.get(position).toString();

        TextView label        = (TextView)row.findViewById(R.id.company);
        TextView sub          = (TextView)row.findViewById(R.id.sub);
        ImageView companyLogo = (ImageView)row.findViewById(R.id.image);

        if(position==0){

            label.setText("Public");
            sub.setText("Any one can see this note");
            companyLogo.setImageResource(R.drawable.publicicon2);
        }
        else
        {
            label.setText("Private");
            sub.setText("Only you can see this note ");
            companyLogo.setImageResource(R.drawable.privateicon);

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

            View row = inflater.inflate(R.layout.onspinnerdown, parent, false);

        ImageView imageView = (ImageView)row.findViewById(R.id.imagedown);
        if(position==0)

            imageView.setImageResource(R.drawable.publicicon);
        else
            imageView.setImageResource(R.drawable.privateicon);

        return imageView;

    }
}