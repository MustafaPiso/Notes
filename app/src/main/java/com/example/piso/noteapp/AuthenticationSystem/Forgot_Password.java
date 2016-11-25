package com.example.piso.noteapp.AuthenticationSystem;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.piso.noteapp.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    private EditText mInputEmail;
    private Button  mReset_btn, mBack_btn;
    private FirebaseAuth mAuth;
    private ProgressBar mprogressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);
        mInputEmail = (EditText) findViewById(R.id.email);
        mReset_btn = (Button) findViewById(R.id.btn_reset_password);
        mBack_btn = (Button) findViewById(R.id.btn_back);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        final Firebase firebase ;
        mBack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mReset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String email = mInputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                mprogressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Forgot_Password.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Forgot_Password.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                mprogressBar.setVisibility(View.GONE);
                            }
                        });


            }
        });
    }

}
