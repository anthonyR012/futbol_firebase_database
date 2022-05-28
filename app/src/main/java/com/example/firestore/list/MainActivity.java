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
import com.google.android.gms.tasks.Task;
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
                        Log.i("partidoRegistra",partidosLista.get(index).getId());
                        showAlert(partidosLista.get(index).getId(),partidosLista.get(index));
                    }
                })
                .setNeutralButton("ASISTENCIA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showAlertThree(partidosLista.get(index).getId());
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
    String messageShowThree = "";
    int length = 0;
    private void showAlertThree(String id) {

        Task<QuerySnapshot> partidos = MyApplication.db.collection("matrizPartidos").whereEqualTo("id_partido", id).get();
        partidos.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ArrayList<String> id_user = new ArrayList();
                length = queryDocumentSnapshots.getDocumentChanges().size();
                for (int i = 0; i <length ;i++){
                    final int count = i;

                    id_user.add(queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().get("id_usuario").toString());
                    Log.i("id_user",id_user.get(i));
                }
                if(!id_user.isEmpty()){
                    Task<QuerySnapshot> user = MyApplication.db.collection("users").whereIn("id",id_user ).get();
                    user.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



                            for(int i = 0 ; i < queryDocumentSnapshots.getDocumentChanges().size();i++){
                                messageShowThree += queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().get("name").toString();
                                messageShowThree += "\n";
                                Log.i("nombres",queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().get("name").toString());
                            }
                            showAlertTwo("ASISTENCIA PARTIDO ",messageShowThree);


                        }
                    });
                }else{
                    showAlertTwo("ASISTENCIA PARTIDO ","No hay nadie registrado en este partido");
                }


            }
        });
//        ArrayList id_usu =  new ArrayList<>();
//        MyApplication.db.collection("partidos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                for(int i = 0; i < queryDocumentSnapshots.getDocumentChanges().size(); i++){
//                    if(queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().get("id_partido").toString().equals(id)){
//                        id_usu.add(queryDocumentSnapshots.getDocumentChanges().get(i).getDocument().get("id_usuario").toString());
//                    }
//
//                }
//
//
//
//            }
//        });

    }

    private void showAlert(String id_partido, PartidosModel partidosModel){
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
                                int valorEntero = (int) Math.floor(Math.random()*(10-9000+1)+9000);
                                map.put("id_usuario",idDocument);
                                map.put("id_partido",id_partido);
                                QueryDocumentSnapshot user = queryDocumentSnapshots.getDocumentChanges().get(0).getDocument();

                                MyApplication.db.collection("matrizPartidos").document(String.valueOf(valorEntero)).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        String message = "Señor "+user.get("name")+" C.C "+user.get("id")+"\n"+
                                                "Correo: "+user.get("email")+"\n"+
                                                "Celular: "+user.get("celular")+"\n"+
                                                "Usted ha quedado registrado satisfactoriamente: \n"+
                                                partidosModel.getOponente()+" VS "+partidosModel.getRival()+"\n"+
                                                "Hora inicio: "+partidosModel.getHora()+" Fecha: "+partidosModel.getFecha();


                                        showAlertTwo("Felicidades!!",message);
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
                    showAlert(id_partido, partidosModel);
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
                        length = 0;
                        messageShowThree = "";
                    }
                })
                .setCancelable(false)
                .create().show();


    }



}