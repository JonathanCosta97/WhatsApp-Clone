    package com.example.jonat.whatsapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jonat.whatsapp.Fragments.Contatos;
import com.example.jonat.whatsapp.R;
import com.example.jonat.whatsapp.model.Contato;

import java.util.ArrayList;
import java.util.List;

public class contatoAdapter extends ArrayAdapter<Contato> {

    private ArrayList<Contato> contatos;
    private Context context;

    public contatoAdapter(Context c, ArrayList<Contato> objects) {
        super(c, 0, objects);
        this.contatos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if( contatos != null ){

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_contatos, parent, false);

            // recupera elemento para exibição
            TextView nomeContato = (TextView) view.findViewById(R.id.ltContato);
            TextView emailContato = (TextView) view.findViewById(R.id.ltEmail);

            Contato contato = contatos.get( position );
            nomeContato.setText( contato.getNomeContato());
            emailContato.setText( contato.getEmailContato());

        }
        return view;
    }

}
