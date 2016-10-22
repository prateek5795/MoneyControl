package com.example.prateek.moneycontrol.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prateek.moneycontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.et_firstName)
    EditText et_firstName;

    @BindView(R.id.et_lastName)
    EditText et_lastName;

    @BindView(R.id.et_emailInput)
    EditText et_emailInput;

    @BindView(R.id.et_passwordInput1)
    EditText et_passwordInput1;

    @BindView(R.id.et_passwordInput2)
    EditText et_passwordInput2;

    @BindView(R.id.bSignup)
    Button bSignup;

    private ProgressDialog mProgress;

    FirebaseAuth mAuth;
    DatabaseReference mDatabseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mProgress = new ProgressDialog(this);
        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignup();
            }
        });
    }

    private void startSignup() {
        final String fName = et_firstName.getText().toString();
        final String lName = et_lastName.getText().toString();
        String email = et_emailInput.getText().toString();
        String pass1 = et_passwordInput1.getText().toString();
        String pass2 = et_passwordInput2.getText().toString();
        if (fName.equals("") || lName.equals("") || email.equals("") || pass1.equals("") || pass2.equals("")) {
            Toast.makeText(SignupActivity.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
        } else {
            if (!pass1.equals(pass2)) {
                Toast.makeText(SignupActivity.this, "Passwords dont match", Toast.LENGTH_SHORT).show();
            } else {
                mProgress.setMessage("Creating new account");
                mProgress.show();
                mAuth.createUserWithEmailAndPassword(email, pass1)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference currUserRef = mDatabseUsers.child(user_id);
                                    currUserRef.child("fname").setValue(fName);
                                    currUserRef.child("lname").setValue(lName);
                                    currUserRef.child("budget").setValue("0");
                                    currUserRef.child("spent").setValue("0");
                                    Toast.makeText(SignupActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                    mProgress.dismiss();
                                    Intent mainIntent = new Intent(SignupActivity.this, MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);
                                } else {
                                    Toast.makeText(SignupActivity.this, "Error creating account", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
}
