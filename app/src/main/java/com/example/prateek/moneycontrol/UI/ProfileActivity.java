package com.example.prateek.moneycontrol.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prateek.moneycontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_fname)
    EditText et_fname;
    @BindView(R.id.et_lname)
    EditText et_lname;
    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.tv_password)
    TextView tv_password;

    @BindView(R.id.bSaveChanges)
    Button bSaveChanges;
    @BindView(R.id.bSignout)
    Button bSignout;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    DatabaseReference mUserIdRef;

    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserIdRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());

        ButterKnife.bind(this);

        et_email.setText(mUser.getEmail());

        mUserIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                et_fname.setText(dataSnapshot.child("fname").getValue(String.class));
                et_lname.setText(dataSnapshot.child("lname").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProgress = new ProgressDialog(this);
        bSaveChanges.setOnClickListener(this);
        bSignout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSaveChanges:
                String fname = et_fname.getText().toString();
                String lname = et_lname.getText().toString();
                String email = et_email.getText().toString();

                if (fname.equals("") || lname.equals("") || email.equals("")) {
                    Toast.makeText(ProfileActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.setMessage("Saving Changes");
                    mProgress.show();
                    mUserIdRef.child("fname").setValue(fname);
                    mUserIdRef.child("lname").setValue(lname);

                    mUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Email not updated", Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            }
                        }
                    });
                }

            case R.id.bSignout:
                mAuth.signOut();
                Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                break;
        }
    }
}
