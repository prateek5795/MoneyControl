package com.example.prateek.moneycontrol.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prateek.moneycontrol.Helper.CustomAlertDialog;
import com.example.prateek.moneycontrol.Helper.DateHelper;
import com.example.prateek.moneycontrol.Model.Item_model;
import com.example.prateek.moneycontrol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.llGreeting)
    LinearLayout llGreeting;
    @BindView(R.id.llAddMoney)
    LinearLayout llAddMoney;
    @BindView(R.id.llTransact)
    LinearLayout llTransact;
    @BindView(R.id.llActivity)
    LinearLayout llActivity;
    @BindView(R.id.llProfile)
    LinearLayout llProfile;

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvGreeting)
    TextView tvGreeting;
    @BindView(R.id.tvUserName)
    TextView tvUsername;
    @BindView(R.id.tvThisMonth)
    TextView tvThisMonth;
    @BindView(R.id.tvBudgetAmount)
    TextView tvBudgetAmount;
    @BindView(R.id.tvSpentAmount)
    TextView tvSpentAmount;

    @BindViews({R.id.tvAddMoney, R.id.tvTransact, R.id.tvActivity, R.id.tvProfile})
    List<TextView> tvButtons;

    @BindViews({R.id.v1, R.id.v2, R.id.v3, R.id.v4, R.id.v5, R.id.v6, R.id.v7, R.id.v8, R.id.v9, R.id.v10, R.id.v11, R.id.v12})
    List<View> dividers;

    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.iv_deposit)
    ImageView ivDeposit;
    @BindView(R.id.iv_transact)
    ImageView ivTransact;
    @BindView(R.id.iv_activity)
    ImageView ivActivity;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;

    DateHelper dh = new DateHelper();
    String currDate = dh.getCurrDate();
    String fullDate = dh.getFullDate();

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mUserIdRef, mUserActivity;

    ProgressDialog progressDialog;

    public int budgetAmount, spentAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mUserIdRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mUserIdRef.keepSynced(true);

        mUserActivity = FirebaseDatabase.getInstance().getReference().child("Activity").child(mAuth.getCurrentUser().getUid());
        mUserActivity.keepSynced(true);

        ButterKnife.bind(this);

        setAppTheme();

        mUserIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvUsername.setText(dataSnapshot.child("fname").getValue(String.class));

                budgetAmount = Integer.valueOf(dataSnapshot.child("budget").getValue(String.class));
                spentAmount = Integer.valueOf(dataSnapshot.child("spent").getValue(String.class));

                tvBudgetAmount.setText("₹ " + String.valueOf(budgetAmount));
                tvSpentAmount.setText("₹ " + String.valueOf(spentAmount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Internet Error", Toast.LENGTH_SHORT).show();
            }
        });

        llAddMoney.setOnClickListener(this);
        llTransact.setOnClickListener(this);
        llActivity.setOnClickListener(this);
        llProfile.setOnClickListener(this);
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

    private void setAppTheme() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.argb(64, 0, 0, 0));
        }

        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);

        tvDate.setText(dh.getCurrMonth(calendar.get(Calendar.MONTH)) + " " + dh.getCurrDate());

        if (time >= 0 && time < 12) {
            tvGreeting.setText("Good Morning,");
            llGreeting.setBackground(new ColorDrawable(Color.parseColor("#38B0DE")));
            ivImage.setImageDrawable(getResources().getDrawable(R.drawable.morning));

        } else if (time >= 12 && time < 16) {
            tvGreeting.setText("Good Afternoon,");
            llGreeting.setBackground(new ColorDrawable(Color.parseColor("#978864")));
            ivImage.setImageDrawable(getResources().getDrawable(R.drawable.afternoon));

        } else if (time >= 16 && time < 20) {

            tvGreeting.setText("Good Evening,");
            llGreeting.setBackground(new ColorDrawable(Color.parseColor("#7f8c8d")));
            ivImage.setImageDrawable(getResources().getDrawable(R.drawable.evening));

        } else if (time >= 20) {
            tvGreeting.setText("Good Evening,");
            llGreeting.setBackground(new ColorDrawable(Color.BLACK));
            tvThisMonth.setBackground(new ColorDrawable(Color.parseColor("#2C3E50")));

            for (View v : dividers) {
                v.setBackground(new ColorDrawable(Color.parseColor("#2C3E50")));
            }

            for (TextView tv : tvButtons) {
                tv.setTextColor(Color.parseColor("#2C3E50"));
            }

            ivImage.setImageDrawable(getResources().getDrawable(R.drawable.night));
            ivDeposit.setImageDrawable(getResources().getDrawable(R.mipmap.deposit_night));
            ivTransact.setImageDrawable(getResources().getDrawable(R.mipmap.transact_night));
            ivActivity.setImageDrawable(getResources().getDrawable(R.mipmap.list_night));
            ivProfile.setImageDrawable(getResources().getDrawable(R.mipmap.profile_night));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.llAddMoney:

                final CustomAlertDialog depositDialog = new CustomAlertDialog(MainActivity.this);
                depositDialog.setTitle("Deposit Money");
                depositDialog.setItemNameHint("Eg From Salary");
                depositDialog.setItemValueHint("Amount");
                depositDialog.setPositiveButton("Add", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String moneyType = depositDialog.getItemName();
                        String addValue = depositDialog.getItemValue();
                        int av = Integer.valueOf(addValue);

                        if (moneyType.equals("") || addValue.equals("")) {
                            Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT).show();
                        } else {
                            mUserActivity.push().setValue(new Item_model(moneyType, String.valueOf(budgetAmount + av), fullDate, "Deposit"));
                            mUserIdRef.child("budget").setValue(String.valueOf(budgetAmount + av));
                            Toast.makeText(getApplicationContext(), "Money added", Toast.LENGTH_SHORT).show();
                            depositDialog.close();
                        }
                    }
                });

                depositDialog.setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        depositDialog.close();
                    }
                });

                depositDialog.show();

                break;

            case R.id.llTransact:

                final CustomAlertDialog transactDialog = new CustomAlertDialog(MainActivity.this);
                transactDialog.setTitle("Transact Money");
                transactDialog.setItemNameHint("Eg Shoes");
                transactDialog.setItemValueHint("Amount");
                transactDialog.setPositiveButton("Transact", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String moneyType = transactDialog.getItemName();
                        String transactValue = transactDialog.getItemValue();
                        int tv = Integer.valueOf(transactValue);

                        if (moneyType.equals("") || transactValue.equals("")) {
                            Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT).show();
                        } else {
                            mUserActivity.push().setValue(new Item_model(moneyType, String.valueOf(spentAmount + tv), fullDate, "Transact"));
                            mUserIdRef.child("spent").setValue(String.valueOf(spentAmount + tv));
                            Toast.makeText(getApplicationContext(), "Money transacted", Toast.LENGTH_SHORT).show();
                            transactDialog.close();
                        }
                    }
                });

                transactDialog.setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transactDialog.close();
                    }
                });

                transactDialog.show();

                break;

            case R.id.llActivity:
                Intent activityIntent = new Intent(MainActivity.this, RecentActivity.class);
                startActivity(activityIntent);
                break;

            case R.id.llProfile:
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                break;
        }

    }
}
