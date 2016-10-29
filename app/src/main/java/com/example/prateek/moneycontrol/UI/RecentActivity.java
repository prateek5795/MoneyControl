package com.example.prateek.moneycontrol.UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prateek.moneycontrol.Helper.DateHelper;
import com.example.prateek.moneycontrol.Model.Item_model;
import com.example.prateek.moneycontrol.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentActivity extends AppCompatActivity {

    @BindView(R.id.bGoToDate)
    Button bGoToDate;

    @BindView(R.id.rvActivity)
    RecyclerView rvActivity;

    @BindView(R.id.activity_recent)
    LinearLayout activity_recent;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserActivity, mUserIdRef;
    FirebaseRecyclerAdapter<Item_model, ActivityViewHolder> firebaseRecyclerAdapter;
    int ba, sa;

    Calendar calendar = Calendar.getInstance();

    DateHelper dh = new DateHelper();
    String datePick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        ButterKnife.bind(this);
        setActivityTheme();
        mAuth = FirebaseAuth.getInstance();
        mUserIdRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mUserActivity = FirebaseDatabase.getInstance().getReference().child("Activity").child(mAuth.getCurrentUser().getUid());
        mUserActivity.keepSynced(true);
        rvActivity.setHasFixedSize(true);
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        lManager.setReverseLayout(true);
        lManager.setStackFromEnd(true);
        rvActivity.setLayoutManager(lManager);
        mUserIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ba = Integer.valueOf(dataSnapshot.child("budget").getValue(String.class));
                sa = Integer.valueOf(dataSnapshot.child("spent").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        bGoToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(RecentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        datePick = dayOfMonth + " " + dh.getCurrMonth(Integer.valueOf(monthOfYear)) + " " + year;
                        Intent dateItemsIntent = new Intent(RecentActivity.this, DateItemsActivity.class);
                        dateItemsIntent.putExtra("cDate", datePick);
                        startActivity(dateItemsIntent);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();

            }
        });

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
            activity_recent.setBackground(new ColorDrawable(Color.parseColor("#38B0DE")));
        } else if (time >= 12 && time < 16) {
            activity_recent.setBackground(new ColorDrawable(Color.parseColor("#978864")));
        } else if (time >= 16 && time < 20) {
            activity_recent.setBackground(new ColorDrawable(Color.parseColor("#7f8c8d")));
        } else if (time >= 20) {
            activity_recent.setBackground(new ColorDrawable(Color.BLACK));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Item_model, ActivityViewHolder>(
                Item_model.class,
                R.layout.item_list_row,
                ActivityViewHolder.class,
                mUserActivity
        ) {
            @Override
            protected void populateViewHolder(ActivityViewHolder viewHolder, Item_model model, final int position) {

                if (model.getType().equals("Deposit")) {
                    viewHolder.tvDepOrTrans.setText("Deposit");
                    viewHolder.tvRowName.setText(model.getName());
                    viewHolder.tvRowValue.setText("₹ " + model.getValue());
                    viewHolder.tvRowDate.setText(model.getDate());
                    viewHolder.tvRowValue.setTextColor(Color.parseColor("#4CAF50"));
                    viewHolder.tvDepOrTrans.setTextColor(Color.parseColor("#4CAF50"));
                }

                if (model.getType().equals("Transact")) {
                    viewHolder.tvDepOrTrans.setText("Transaction");
                    viewHolder.tvRowName.setText(model.getName());
                    viewHolder.tvRowValue.setText("₹ " + model.getValue());
                    viewHolder.tvRowDate.setText(model.getDate());
                    viewHolder.tvRowValue.setTextColor(Color.parseColor("#B71C1C"));
                    viewHolder.tvDepOrTrans.setTextColor(Color.parseColor("#B71C1C"));
                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RecentActivity.this);
                        builder.setTitle("Delete item?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int value = Integer.valueOf(firebaseRecyclerAdapter.getItem(position).getValue());
                                String type = firebaseRecyclerAdapter.getItem(position).getType();
                                if (type.equals("Deposit")) {
                                    mUserIdRef.child("budget").setValue(String.valueOf(ba - value));
                                }
                                if (type.equals("Transact")) {
                                    mUserIdRef.child("spent").setValue(String.valueOf(sa - value));
                                }
                                firebaseRecyclerAdapter.getRef(position).removeValue();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        };
        rvActivity.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView tvRowName, tvRowValue, tvRowDate, tvDepOrTrans;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tvRowName = (TextView) itemView.findViewById(R.id.tvRowName);
            tvRowValue = (TextView) itemView.findViewById(R.id.tvRowValue);
            tvRowDate = (TextView) itemView.findViewById(R.id.tvRowDate);
            tvDepOrTrans = (TextView) itemView.findViewById(R.id.tv_depOrTrans);
        }
    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            datePick = dayOfMonth + " " + dh.getCurrMonth(Integer.valueOf(monthOfYear)) + " " + year;
            Toast.makeText(RecentActivity.this, datePick, Toast.LENGTH_SHORT).show();
        }
    };
}
