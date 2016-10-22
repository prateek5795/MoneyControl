package com.example.prateek.moneycontrol.Helper;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prateek.moneycontrol.R;

public class CustomAlertDialog extends AppCompatDialog {

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private TextView tv_title;
    private EditText et_field1;
    private EditText et_field2;
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
        et_field1 = (EditText) customView.findViewById(R.id.et_field1);
        et_field2 = (EditText) customView.findViewById(R.id.et_field2);

        alertDialog = builder.setView(customView).create();
    }

    public void show() {
        if (alertDialog != null) alertDialog.show();
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setField1Hint(String hint) {
        et_field1.setHint(hint);
    }

    public String getField1() {
        return et_field1.getText().toString();
    }

    public void setField2Hint(String hint) {
        et_field2.setHint(hint);
    }

    public String getField2() {
        return et_field2.getText().toString();
    }

    public void setField2InputNum() {
        et_field2.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void hideField2() {
        et_field2.setVisibility(View.GONE);
    }

    public void setField2InputPass() {
        et_field2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
}
