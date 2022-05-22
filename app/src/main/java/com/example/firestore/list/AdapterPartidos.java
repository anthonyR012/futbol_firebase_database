package com.example.firestore.list;

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


public class AdapterPartidos extends RecyclerView.Adapter<AdapterPartidos.ViewHolderClient> {
    List<PartidosModel> partidos;
    List<PartidosModel> partidosOriginal;

    public AdapterRecycleClient(List<PartidosModel> partidos) {
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



    public class ViewHolderClient extends RecyclerView.ViewHolder {
        TextView idItem;
        TextView countItem;
        TextView nameItem;
        TextView phoneItem;

        public ViewHolderClient(@NonNull View itemView) {
            super(itemView);
//            idItem = itemView.findViewById(R.id.IdClientDetail);
//            countItem = itemView.findViewById(R.id.countLender);
//            nameItem = itemView.findViewById(R.id.nameClientDetail);
//            phoneItem  = itemView.findViewById(R.id.phoneClientDetail);

        }

        public void AsignarDatos(ClientePrestamos cliente) {

            int count = cliente.prestamos.size();
            idItem.setText(""+cliente.cliente.id_cliente_pk);
            countItem.setText("Total Prestamos "+count);
            nameItem.setText(cliente.cliente.getNombre_cliente()+" "+cliente.cliente.getApellido_cliente());
            phoneItem.setText(cliente.cliente.getTelefono_cliente());


        }
    }
}
