package com.example.prateek.moneycontrol.Helper;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prateek.moneycontrol.R;

public class CustomAlertDialog extends AppCompatDialog {

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private TextView tv_title;
    private EditText et_itemName;
    private EditText et_itemValue;
    private TextView positiveButton;
    private TextView negativeButton;

    public CustomAlertDialog(Context context) {
        super(context);

        builder = new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.custom_dialog, null);

        positiveButton = (TextView) customView.findViewById(R.id.positiveButton);
        negativeButton = (TextView) customView.findViewById(R.id.negativeButton);
        tv_title = (TextView) customView.findViewById(R.id.tv_title);
        et_itemName = (EditText) customView.findViewById(R.id.et_itemName);
        et_itemValue = (EditText) customView.findViewById(R.id.et_itemValue);

        alertDialog = builder.setView(customView).create();
    }

    public void hideNegativeButton() {
        negativeButton.setVisibility(View.GONE);
    }

    public void show() {
        if (alertDialog != null) alertDialog.show();
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void hideTitle() {
        tv_title.setVisibility(View.GONE);
    }

    public void setItemNameHint(String hint) {
        et_itemName.setHint(hint);
    }

    public String getItemName() {
        return et_itemName.getText().toString();
    }

    public void setItemValueHint(String hint) {
        et_itemValue.setHint(hint);
    }

    public String getItemValue() {
        return et_itemValue.getText().toString();
    }

    public void setPositiveButton(String positive_text, View.OnClickListener listener) {
        positiveButton.setText(positive_text);
        positiveButton.setOnClickListener(listener);
    }

    public void setNegativeButton(String negative_text, View.OnClickListener listener) {
        negativeButton.setText((negative_text));
        negativeButton.setOnClickListener(listener);
    }

    public void close() {
        alertDialog.dismiss();
    }

    public void setMandatory() {
        alertDialog.setCancelable(false);
    }
}
