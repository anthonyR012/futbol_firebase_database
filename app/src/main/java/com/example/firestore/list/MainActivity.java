package com.example.firestore.list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.firestore.MyApplication;
import com.example.firestore.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }



    private void ejecuteDatabase() {

        Map<String,Object>  map = new HashMap<>();
        map.put("id","a1");
        map.put("name","anthony");
        map.put("email","emali@example.com");
        map.put("password","12345");

        MyApplication.db.collection("users").document("a1").set(map).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Se registro la informacion", Toast.LENGTH_SHORT).show();
                    }
                }
        );


    }
}