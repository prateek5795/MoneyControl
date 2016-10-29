package com.example.prateek.moneycontrol.UI;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prateek.moneycontrol.Helper.CustomAlertDialog;
import com.example.prateek.moneycontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_fname)
    EditText et_fname;
    @BindView(R.id.et_lname)
    EditText et_lname;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_newPass1)
    EditText et_newPass1;
    @BindView(R.id.et_newPass2)
    EditText et_newPass2;

    @BindView(R.id.tv_changePassword)
    TextView tv_changePassword;

    @BindView(R.id.bSaveChanges)
    Button bSaveChanges;
    @BindView(R.id.bClearData)
    Button bClearData;
    @BindView(R.id.bSignout)
    Button bSignout;
    @BindView(R.id.activity_profile)
    ScrollView activity_profile;

    @BindView(R.id.ll_newPass)
    LinearLayout ll_newPass;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mUserIdRef, mUserActivityRef;
    ProgressDialog mProgress;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setActivityTheme();
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserIdRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        mUserActivityRef = FirebaseDatabase.getInstance().getReference().child("Activity").child(mUser.getUid());
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
        bClearData.setOnClickListener(this);
        bSaveChanges.setOnClickListener(this);
        bSignout.setOnClickListener(this);
        tv_changePassword.setOnClickListener(this);
    }

    private void setActivityTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.argb(64, 0, 0, 0));
        }
        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        if (time >= 0 && time < 12) {
            activity_profile.setBackground(new ColorDrawable(Color.parseColor("#38B0DE")));
        } else if (time >= 12 && time < 16) {
            activity_profile.setBackground(new ColorDrawable(Color.parseColor("#978864")));
        } else if (time >= 16 && time < 20) {
            activity_profile.setBackground(new ColorDrawable(Color.parseColor("#7f8c8d")));
        } else if (time >= 20) {
            activity_profile.setBackground(new ColorDrawable(Color.BLACK));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSaveChanges:
                String fname = et_fname.getText().toString();
                String lname = et_lname.getText().toString();
                String email = et_email.getText().toString();
                String pass1 = et_newPass1.getText().toString();
                String pass2 = et_newPass2.getText().toString();
                if (fname.equals("") || lname.equals("") || email.equals("")) {
                    Toast.makeText(ProfileActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    mUserIdRef.child("fname").setValue(fname);
                    mUserIdRef.child("lname").setValue(lname);
                    mProgress.setMessage("Updating Email");
                    mProgress.show();
                    mUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Email Updated", Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Email not updated", Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            }
                        }
                    });
                    if (flag == 1) {
                        mProgress.setMessage("Updating Password");
                        mProgress.show();
                        if (pass1.equals(pass2) && !pass1.equals("") && !pass2.equals("")) {
                            mUser.updatePassword(pass1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mProgress.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mProgress.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Password Not Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }

            case R.id.bSignout:
                mAuth.signOut();
                Intent loginIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(loginIntent);
                break;

            case R.id.tv_changePassword:
                reauthenticateUser();
                break;

            case R.id.bClearData:
                mUserIdRef.child("budget").setValue("0");
                mUserIdRef.child("spent").setValue("0");
                if (mUserActivityRef != null) {
                    mUserActivityRef.removeValue();
                }
                Toast.makeText(ProfileActivity.this, "Cleared!", Toast.LENGTH_SHORT).show();
        }
    }

    private void reauthenticateUser() {

        final CustomAlertDialog credentialDialog = new CustomAlertDialog(ProfileActivity.this);
        credentialDialog.setTitle("Verify Credentials");
        credentialDialog.setField1Hint("Email");
        credentialDialog.setField2Hint("Password");
        credentialDialog.setField2InputPass();
        credentialDialog.setPositiveButton("Enter", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Verifying");
                mProgress.show();
                String email = credentialDialog.getField1();
                String password = credentialDialog.getField2();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgress.dismiss();
                            credentialDialog.close();
                            Toast.makeText(ProfileActivity.this, "Authenticated", Toast.LENGTH_SHORT).show();
                            tv_changePassword.setVisibility(View.GONE);
                            ll_newPass.setVisibility(View.VISIBLE);
                            flag = 1;
                        } else {
                            Toast.makeText(ProfileActivity.this, "Invalid email/password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        credentialDialog.setNegativeButton("Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                credentialDialog.close();
            }
        });
        credentialDialog.show();
    }
}
