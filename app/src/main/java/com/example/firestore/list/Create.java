package com.example.firestore.list;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firestore.MyApplication;
import com.example.firestore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Create extends AppCompatActivity {

    EditText rival,oponente,fecha,lugar,hora;
    EditText nombres,celular,cedula,email;
    Button crearPartido,crearPersona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        String estado = (String) bundle.getSerializable("estado");
        initialiceVariables(estado);
        MyApplication.db = FirebaseFirestore.getInstance();
        findViewById(R.id.returnPrincipal).setOnClickListener(v -> goToInit());
    }
    private void goToInit(){
        startActivity(new Intent(this,MainActivity.class));
    }

    private void initialiceVariables(String estado) {
        if(estado.equals("partido")){
            setContentView(R.layout.create_partido);
            rival = findViewById(R.id.rival);
            oponente = findViewById(R.id.oponente);
            fecha = findViewById(R.id.fecha);
            lugar = findViewById(R.id.lugar);
            hora = findViewById(R.id.hora);
            crearPartido = findViewById(R.id.crearPartido);
            crearPartido.setOnClickListener(v -> crearPartido());
        }else{
            setContentView(R.layout.crear_persona);
            nombres = findViewById(R.id.nombres);
            celular = findViewById(R.id.celular);
            cedula = findViewById(R.id.cedula);
            email = findViewById(R.id.correo);
            crearPersona = findViewById(R.id.crearPersona);
            crearPersona.setOnClickListener(v -> crearPersona());
        }


    }

    private void crearPersona() {

        Map<String,Object> map = new HashMap<>();
        map.put("id",cedula.getText().toString());
        map.put("name",nombres.getText().toString());
        map.put("email",email.getText().toString());
        map.put("celular",celular.getText().toString());

        MyApplication.db.collection("users").document(cedula.getText().toString()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                alertShow("Has registrado un nuevo usuario");
            }
        });
    }

    private void crearPartido() {
        int valorEntero = (int) Math.floor(Math.random()*(10-9000+1)+9000);

        Map<String,Object>  map = new HashMap<>();
        String id = hora.getText().toString()+""+valorEntero;
        map.put("id",id);
        map.put("lugar",lugar.getText().toString());
        map.put("oponente",oponente.getText().toString());
        map.put("rival",rival.getText().toString());
        map.put("hora",hora.getText().toString());
        map.put("fecha",fecha.getText().toString());


        MyApplication.db.collection("partidos").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                alertShow("Has registrado un nuevo partido");
            }
        });

    }

    private void alertShow(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Felicidades!!")
                .setIcon(R.drawable.campo)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!
                        goToInit();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
}