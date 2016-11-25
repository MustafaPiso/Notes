package com.example.piso.noteapp.AuthenticationSystem;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.piso.noteapp.Home;
import com.example.piso.noteapp.Models.Person;
import com.example.piso.noteapp.R;
import com.firebase.client.Firebase;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.internal.overlay.zzo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    StorageReference mStorage;
   ImageView  myphoto ;
    EditText mfirst_name,mLast_name ,mphone  , mFaculty , mId;
    Spinner day, month, year, gender;
    private String marray_gender[] , mArray_days[] ,mArray_months[] , mArray_years[];
    Button mDone_btn ;
    Person  mNew_person ;
    FirebaseUser user;
    private boolean Data_valid = true ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Firebase.setAndroidContext(this);
        set_Date_Data();
        mfirst_name = (EditText) findViewById(R.id.first_name);
        mLast_name  = (EditText) findViewById(R.id.last_name);
        mphone  = (EditText) findViewById(R.id.phone);
         day = (Spinner) findViewById(R.id.days);
        month = (Spinner)findViewById(R.id.months);
        year = (Spinner)findViewById(R.id.years);
        gender = (Spinner)findViewById(R.id.gender);
        mDone_btn  = (Button) findViewById(R.id.done);
        user = FirebaseAuth.getInstance().getCurrentUser();
        myphoto = (ImageView) findViewById(R.id.myprofilephoto);

        firebaseDatabase= FirebaseDatabase.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();
        mStorage = firebaseStorage.getReference();

        mDone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Data_valid){
                    Instantiate_person();
                    SaveSharedPreference.setUserName(InfoActivity.this, mfirst_name.getText().toString().toLowerCase());
                    //String  id =FirebaseAuth.getInstance().getCurrentUser().getUid() ;
                    DatabaseReference databaseReference = firebaseDatabase.getReference("/MyPersons/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    databaseReference.setValue(mNew_person);
                    startActivity(new Intent(InfoActivity.this, Home.class));

                }
            }
        });
        myphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });
    }
    private  void  Instantiate_person(){

        mNew_person = new Person();
        List<String> inter = new ArrayList<>();
        inter.add("Android");
        inter.add("ios");
        inter.add("algo");
        inter.add("Prolog");
        mNew_person.setId("tmp");
        mNew_person.setEmail(user.getEmail());
        mNew_person.setName(mfirst_name.getText().toString()+" "+mLast_name.getText().toString());
        mNew_person.setPhone(mphone.getText().toString());
        mNew_person.setGender(gender.getSelectedItem().toString());
        mNew_person.setFollowers_Number(0);
        mNew_person.setInterests(inter);
        FirebaseAuth  user  = FirebaseAuth.getInstance();



        String daystr  = day.getSelectedItem().toString() ;
        String monthstr  = month.getSelectedItem().toString() ;
        String yearstr  = year.getSelectedItem().toString() ;
        if(daystr.equals("Day")||monthstr.equals("Month")||yearstr.equals("Year"))
        {
            Toast.makeText(this, "Invaild Birthdata ! " , Toast.LENGTH_LONG).show();
            Data_valid = false ;
            return;
        }
        mNew_person.setBirthDate(daystr+"/"+monthstr+"/"+yearstr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null)
        {
            final Uri uri = data.getData();
            StorageReference filePath = mStorage.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  //  mStorage.child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+member.user.getImage()).delete();
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Log.v("link", String.valueOf(downloadUri));
                    mNew_person.setImage(uri.getLastPathSegment());
                    Picasso.with(InfoActivity.this).load(downloadUri).fit().centerCrop().into(myphoto);
                    DatabaseReference DB = firebaseDatabase.getReference
                            ("/MyPersons/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/image") ;

                    DB.setValue(uri.getLastPathSegment());
                }
            });
        }
    }

    void set_Date_Data(){
        marray_gender = new String[2];
        marray_gender[0]="Male";
        marray_gender[1]="Female";
        Spinner s = (Spinner) findViewById(R.id.gender);
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,marray_gender);
        s.setAdapter(adapter);



        mArray_days = new String[31];
        mArray_days[0]="Day";
        for(int i=1 ; i<31 ; i++)
            mArray_days[i] = String.valueOf(i);


        Spinner mDays_spinner = (Spinner) findViewById(R.id.days);
        final ArrayAdapter mDays_adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,mArray_days);
        mDays_spinner.setAdapter(mDays_adapter);



        mArray_months = new String[13];
        mArray_months[0] = "Month";mArray_months[1] = "Jan";mArray_months[2] = "Fab";mArray_months[3] = "Mar";
        mArray_months[4] = "Apr";mArray_months[5] = "May";mArray_months[6] = "Jun";mArray_months[7] = "Jul";
        mArray_months[8] = "Aug";mArray_months[9] = "Sept";mArray_months[10] = "Oct";mArray_months[11] = "Nov";
        mArray_months[12] = "Dec";
        Spinner mMonths_spinner = (Spinner) findViewById(R.id.months);
        final ArrayAdapter mMonths_adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,mArray_months);
        mMonths_spinner.setAdapter(mMonths_adapter);
        mArray_years = new String[50];
        mArray_years[0] = "Year";
        for(int i=1 ; i<50 ; i++)
        {
            mArray_years[i]= String.valueOf((i+1970));
        }
        Spinner mYears_spinner = (Spinner) findViewById(R.id.years);
        final ArrayAdapter mYears_adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,mArray_years);
        mYears_spinner.setAdapter(mYears_adapter);

    }

}