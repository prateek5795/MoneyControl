package com.example.prateek.moneycontrol.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prateek.moneycontrol.Helper.CustomAlertDialog;
import com.example.prateek.moneycontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_emailInput)
    EditText et_emailInput;
    @BindView(R.id.et_passwordInput)
    EditText et_passwordInput;
    @BindView(R.id.tv_signup)
    TextView tv_signup;
    @BindView(R.id.tv_forgotPassword)
    TextView tv_forgotPassword;
    @BindView(R.id.bLogin)
    Button bLogin;

    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "Authenticated", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }
        };
        mProgress = new ProgressDialog(this);
        bLogin.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
        tv_forgotPassword.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bLogin:
                String email = et_emailInput.getText().toString();
                String password = et_passwordInput.getText().toString();
                if (email.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.setMessage("Verifying");
                    mProgress.show();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mProgress.dismiss();
                                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(mainIntent);
                                    } else {
                                        mProgress.dismiss();
                                        Toast.makeText(LoginActivity.this, "Authentication error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;

            case R.id.tv_signup:
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                break;

            case R.id.tv_forgotPassword:
                final CustomAlertDialog alertDialog = new CustomAlertDialog(this);
                alertDialog.setTitle("");
                alertDialog.setField1Hint("Email");
                alertDialog.hideField2();
                alertDialog.setPositiveButton("Enter", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgress.setMessage("Sending Email");
                        mProgress.show();
                        mAuth.sendPasswordResetEmail(alertDialog.getField1()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mProgress.dismiss();
                                    alertDialog.close();
                                    Toast.makeText(LoginActivity.this, "Check email for password", Toast.LENGTH_SHORT).show();
                                } else {
                                    mProgress.dismiss();
                                    alertDialog.close();
                                    Toast.makeText(LoginActivity.this, "Error sending email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                alertDialog.setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.close();
                    }
                });
                alertDialog.show();
        }
    }
}
