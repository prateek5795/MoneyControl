package com.example.prateek.moneycontrol;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MoneyControl extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
