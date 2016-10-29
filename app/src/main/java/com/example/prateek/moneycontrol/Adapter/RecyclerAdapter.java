package com.example.prateek.moneycontrol.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prateek.moneycontrol.Model.Item_model;
import com.example.prateek.moneycontrol.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.DateViewHolder> {

    private List<Item_model> itemsList;

    public class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvRowName, tvRowValue, tvRowDate, tvDepOrTrans;

        public DateViewHolder(View view) {
            super(view);
            tvRowName = (TextView) view.findViewById(R.id.tvRowName);
            tvRowName = (TextView) view.findViewById(R.id.tvRowValue);
            tvRowName = (TextView) view.findViewById(R.id.tvRowDate);
            tvRowName = (TextView) view.findViewById(R.id.tv_depOrTrans);
        }
    }

    public RecyclerAdapter(List<Item_model> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_row, parent, false);
        return new DateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position) {
        Item_model model = itemsList.get(position);
        holder.tvRowName.setText(model.getName());
        holder.tvRowValue.setText(model.getValue());
        holder.tvRowDate.setText(model.getDate());
        holder.tvDepOrTrans.setText(model.getType());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

}
