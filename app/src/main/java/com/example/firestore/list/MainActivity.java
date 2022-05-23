package com.example.firestore.list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.firestore.MyApplication;
import com.example.firestore.R;
import com.example.firestore.models.PartidosModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AdapterPartidos adapter;
    private RecyclerView recyclerPartido;
    private List<PartidosModel> partidosLista;
    private com.getbase.floatingactionbutton.FloatingActionButton addPartido,addPerson ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerPartido = findViewById(R.id.recyclePartidos);
        addPartido = findViewById(R.id.addPartido);
        addPerson = findViewById(R.id.addPerson);
        MyApplication.db = FirebaseFirestore.getInstance();
        getPartidosActual();

        addPartido.setOnClickListener(v -> goToCreate("partido"));
        addPerson.setOnClickListener(v -> goToCreate("person"));
    }

    private void goToCreate(String mostrarCreate) {
        Intent intent = new Intent(MainActivity.this, Create.class);
        Bundle bundle = new Bundle();

        switch (mostrarCreate){
            case "partido":
                bundle.putSerializable("estado",mostrarCreate);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case "person":
                bundle.putSerializable("estado",mostrarCreate);
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
                break;
        }

    }

    private void getPartidosActual() {
//        Map<String,Object>  map = new HashMap<>();
//        map.put("id","b1");
//        map.put("oponente","America");
//        map.put("rival","cali");
//        map.put("fecha","05/05/2022");
//        map.put("lugar","Canchas de juanchito");

        partidosLista = new ArrayList<>();
        MyApplication.db.collection("partidos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(int i = 0; i < queryDocumentSnapshots.getDocumentChanges().size(); i++){


                    PartidosModel partidosModel = new PartidosModel(
                            queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().get("fecha").toString(),
                            queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().get("id").toString(),
                            queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().get("lugar").toString(),
                            queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().get("oponente").toString(),
                            queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().get("rival").toString(),
                            queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().get("hora").toString()
                    );

                    partidosLista.add(partidosModel);
                }
                adapterItems();
                
            }
        });

    }

    private void adapterItems() {

       recyclerPartido.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
       //ENVIAMOS BASE DE DATOS Y SOLICITAMOS LA LISTA DE CLIENTES PARA ADAPTARLA
       adapter = new AdapterPartidos(partidosLista);
       recyclerPartido.setAdapter(adapter);
        adapter.setOnclikListener(v -> clickOnItem(v));

    }
    private void clickOnItem(View v) {

        int index = recyclerPartido.getChildAdapterPosition(v);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("TICKET DE COMPRA")
                .setMessage(partidosLista.get(index).getOponente()+" VS "+partidosLista.get(index).getRival()+"\n"
                        +"Hora: "+partidosLista.get(index).getHora()+" Fecha: "+partidosLista.get(index).getFecha()+" Lugar: "+partidosLista.get(index).getLugar())
                .setPositiveButton("ACEPTAR TICKET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showAlert(partidosLista.get(index).getId());
                    }
                })
                .setNegativeButton("CANCELAR ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();

    }

    private void showAlert(String id_partido){
        final EditText inputUbicacion = new EditText(MainActivity.this);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Estas a un paso de estar registrado");
        alertDialog.setMessage("Ingresa tu cedula");
        //latitud
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        inputUbicacion.setLayoutParams(lp);
        alertDialog.setView(inputUbicacion);

        alertDialog.setIcon(R.drawable.ticket);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String idDocument = inputUbicacion.getText().toString();

                if(!idDocument.isEmpty()){
                    CollectionReference citiesRef =MyApplication.db.collection("users");

// Create a query against the collection.
                    Query query = citiesRef.whereEqualTo("id", idDocument);
                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.isEmpty()){
                                Map<String,Object>  map = new HashMap<>();

                                map.put("id_usuario",idDocument);
                                map.put("id_partido",id_partido);
                                QueryDocumentSnapshot user = queryDocumentSnapshots.getDocumentChanges().get(0).getDocument();

                                MyApplication.db.collection("matrizPartidos").document(id_partido).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        showAlertTwo("Felicidades!!",user.get("name")+" has quedado registrado para este partido");
                                    }
                                });

                                Log.i("usuario",queryDocumentSnapshots.getDocumentChanges().get(0).getDocument().get("name").toString());

                            }else{
                                showAlertTwo("Ha ocurrido un error!!","Parece que no existe este usuario, recuerda registrarte primero");

                            }

                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("usuario","error: "+e);

                        }
                    });

                }else{
                    showAlert(id_partido);
                }
            }
        });
        alertDialog.setNegativeButton("CANCELAR TRANSACCION", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        alertDialog.create();
        alertDialog.show();

    }

    private void showAlertTwo(String title,String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title)
                .setMessage(message)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();
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