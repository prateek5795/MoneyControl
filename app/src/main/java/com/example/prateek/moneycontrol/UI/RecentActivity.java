package com.example.prateek.moneycontrol.UI;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.prateek.moneycontrol.Model.Item_model;
import com.example.prateek.moneycontrol.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentActivity extends AppCompatActivity {

    @BindView(R.id.rvActivity)
    RecyclerView rvActivity;

    private FirebaseAuth mAuth;

    private DatabaseReference mUserActivity, mUserIdRef;

    FirebaseRecyclerAdapter<Item_model, ActivityViewHolder> firebaseRecyclerAdapter;

    int ba, sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        mAuth = FirebaseAuth.getInstance();

        mUserIdRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mUserActivity = FirebaseDatabase.getInstance().getReference().child("Activity").child(mAuth.getCurrentUser().getUid());
        mUserActivity.keepSynced(true);

        ButterKnife.bind(this);
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

                    viewHolder.tvRowValue.setTextColor(Color.GREEN);
                    viewHolder.tvDepOrTrans.setTextColor(Color.GREEN);
                }

                if (model.getType().equals("Transact")) {
                    viewHolder.tvDepOrTrans.setText("Transaction");
                    viewHolder.tvRowName.setText(model.getName());
                    viewHolder.tvRowValue.setText("₹ " + model.getValue());
                    viewHolder.tvRowDate.setText(model.getDate());

                    viewHolder.tvRowValue.setTextColor(Color.RED);
                    viewHolder.tvDepOrTrans.setTextColor(Color.RED);
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
}
