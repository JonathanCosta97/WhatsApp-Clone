package com.example.jonat.whatsapp.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jonat.whatsapp.Helper.preferencias;
import com.example.jonat.whatsapp.R;
import com.example.jonat.whatsapp.model.Mensagem;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class mensagemAdapter extends ArrayAdapter<Mensagem> {

    private ArrayList<Mensagem> conversas;
    private Context context;


    public mensagemAdapter(Context c, ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.conversas = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        //Verificar se a lista está vazia
        if (conversas != null){

            //Recuperar dados do remetente
            preferencias preferencias = new preferencias(context);
            String idUsuarioRemetente = preferencias.getIdentificador();

            //Inicializar o objeto para a montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Recuperar a mensagem
            Mensagem mensagem = conversas.get(position);

            // Montar a view apartir do xlm
            if (idUsuarioRemetente.equals(mensagem.getIdUsuario())){
                view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
            }else {
            view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }

            //Recuperar elemento para exibição
            TextView msgm = view.findViewById(R.id.mensagem_direita);
            msgm.setText(mensagem.getMensagem());
        }

        return view;
    }
}
