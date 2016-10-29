package com.example.prateek.moneycontrol.UI;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

import com.example.prateek.moneycontrol.Adapter.RecyclerAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateItemsActivity extends AppCompatActivity {

    @BindView(R.id.activity_date_items)
    LinearLayout activity_date_items;

    @BindView(R.id.rvDate)
    RecyclerView rvDate;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserActivity;
    FirebaseRecyclerAdapter<Item_model, DateViewHolder> firebaseRecyclerAdapter;
    Query dateQuery;

    DateHelper dh = new DateHelper();

    Calendar calendar = Calendar.getInstance();
    String datePick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_items);

        ButterKnife.bind(this);
        setActivityTheme();
        rvDate.setHasFixedSize(true);
        LinearLayoutManager lManager = new LinearLayoutManager(this);
        lManager.setReverseLayout(true);
        lManager.setStackFromEnd(true);
        rvDate.setLayoutManager(lManager);

        Bundle extras = getIntent().getExtras();
        datePick = extras.getString("cDate");

        mAuth = FirebaseAuth.getInstance();
        mUserActivity = FirebaseDatabase.getInstance().getReference().child("Activity").child(mAuth.getCurrentUser().getUid());
        dateQuery = mUserActivity.orderByChild("date").equalTo(datePick);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Item_model, DateViewHolder>(
                Item_model.class,
                R.layout.item_list_row,
                DateViewHolder.class,
                dateQuery
        ) {
            @Override
            protected void populateViewHolder(DateViewHolder viewHolder, Item_model model, int position) {
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
            }
        };
        rvDate.setAdapter(firebaseRecyclerAdapter);

    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvRowName, tvRowValue, tvRowDate, tvDepOrTrans;

        public DateViewHolder(View itemView) {
            super(itemView);
            tvRowName = (TextView) itemView.findViewById(R.id.tvRowName);
            tvRowValue = (TextView) itemView.findViewById(R.id.tvRowValue);
            tvRowDate = (TextView) itemView.findViewById(R.id.tvRowDate);
            tvDepOrTrans = (TextView) itemView.findViewById(R.id.tv_depOrTrans);
        }
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
            activity_date_items.setBackground(new ColorDrawable(Color.parseColor("#38B0DE")));
        } else if (time >= 12 && time < 16) {
            activity_date_items.setBackground(new ColorDrawable(Color.parseColor("#978864")));
        } else if (time >= 16 && time < 20) {
            activity_date_items.setBackground(new ColorDrawable(Color.parseColor("#7f8c8d")));
        } else if (time >= 20) {
            activity_date_items.setBackground(new ColorDrawable(Color.BLACK));
        }
    }
}
