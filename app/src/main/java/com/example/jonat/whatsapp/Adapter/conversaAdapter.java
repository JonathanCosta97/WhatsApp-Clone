package com.example.jonat.whatsapp.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jonat.whatsapp.R;
import com.example.jonat.whatsapp.model.Conversas;

import java.util.ArrayList;
import java.util.List;

public class conversaAdapter extends ArrayAdapter<Conversas> {

    private ArrayList<Conversas> conversas;
    private Context context;

    public conversaAdapter(@NonNull Context c, @NonNull ArrayList<Conversas> objects) {
        super(c, 0, objects);
        this.context = c;
        this.conversas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        //Verificar se a lista está vazia.
        if (conversas != null){

            //Inicializa o objeto para a montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta a view a partir do XML
            view = inflater.inflate(R.layout.lista_conversa, parent, false);

            //Recupera elemento para a exibição
            TextView nomeUsuario = view.findViewById(R.id.ltNome);
            TextView ultimaConversa = view.findViewById(R.id.ltUltimaConversa);

            Conversas conversa = conversas.get(position);
            nomeUsuario.setText(conversa.getNome());
            ultimaConversa.setText(conversa.getMensagens());
        }

        return view;
    }
}
