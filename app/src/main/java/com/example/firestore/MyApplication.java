package com.example.firestore;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyApplication extends Application {

    public static FirebaseFirestore db;

    @Override
    public void onCreate() {
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        super.onCreate();

    }
}