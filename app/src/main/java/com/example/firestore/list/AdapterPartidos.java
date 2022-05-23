package com.example.firestore.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.firestore.R;
import com.example.firestore.models.PartidosModel;
import com.example.firestore.models.UsersModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class AdapterPartidos extends RecyclerView.Adapter<AdapterPartidos.ViewHolderClient> implements View.OnClickListener {
    List<PartidosModel> partidos;
    List<PartidosModel> partidosOriginal;
    private View.OnClickListener listener;

    public AdapterPartidos(List<PartidosModel> partidos) {
        //CONTRUCTOR QUE RECIBE LISTA Y LA GUARDA EN DOS LISTAS DISTINTAS
        this.partidos = partidos;
        partidosOriginal = new ArrayList<>();
        partidosOriginal.addAll(partidos);
    }

    @NonNull
    @Override
    public ViewHolderClient onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //INFLAMOS VISTA DEL LAYOUT
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_partido,null,false);
        view.setOnClickListener(this);
        return new ViewHolderClient(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClient holder, int position) {
        //EN VISTA CREADA ASIGNAMOS DATOS
        holder.AsignarDatos(partidos.get(position));
    }

    public void filtrado(String txtSearch){
        verifiedString(txtSearch);

    }

    public void setOnclikListener(View.OnClickListener listener){
        this.listener=listener;
    }

    private void verifiedString(String txtSearch) {
        //MIDE LONGITUD DE CADENA Y REALIZA BUSQUEDA EN RECYCLE
        int longitud = txtSearch.length();
        if (longitud == 0){

            //SI ES CERO LIMPIA DATOS DE LISTA Y UTILIZA LA SEGUNDA LISTA RESERVADA
            partidos.clear();
            partidos.addAll(partidosOriginal);

        }else{
            //CUANDO SEA MAYOR SE FILTRA POR ESTE METODO EL CONTENIDO
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                List<PartidosModel> collecion
//                        = partidos.stream()
//                        .filter(i -> i.partidos.getNombre_cliente().toLowerCase().contains(txtSearch.toLowerCase(Locale.ROOT)))
//                        .collect(Collectors.toList());
//                partidos.clear();
//                partidos.addAll(collecion);

            }else{
                //SI ES SDK MAYOR A LA VERSION N, EJECUTA ESTE METODO
                for (PartidosModel p:partidosOriginal ) {
//                    if (p.cliente.getNombre_cliente().toLowerCase(Locale.ROOT).contains(txtSearch.toLowerCase(Locale.ROOT))){
//                        clientes.add(p);
//                    }
                }
            }
        }
        //NOFICAMOS CAMBIOS PARA EL RECYCLE
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return partidos.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }


    public class ViewHolderClient extends RecyclerView.ViewHolder {
        TextView oponente;
        TextView placeId;
        TextView rival;


        public ViewHolderClient(@NonNull View itemView) {
            super(itemView);
            oponente = itemView.findViewById(R.id.oponente);
            placeId = itemView.findViewById(R.id.placeId);
            rival = itemView.findViewById(R.id.rival);



        }

        public void AsignarDatos(PartidosModel partidos) {
            rival.setText(partidos.getRival());
            oponente.setText(partidos.getOponente());
            placeId.setText(partidos.getHora()+" "+partidos.getFecha()+" en "+partidos.getLugar());


        }
    }
}
