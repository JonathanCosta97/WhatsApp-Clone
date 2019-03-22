package com.example.jonat.whatsapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jonat.whatsapp.Activity.ConversaActivity;
import com.example.jonat.whatsapp.Adapter.contatoAdapter;
import com.example.jonat.whatsapp.Helper.preferencias;
import com.example.jonat.whatsapp.R;
import com.example.jonat.whatsapp.config.ConfiguracaoFirebase;
import com.example.jonat.whatsapp.model.Contato;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Contatos extends Fragment {

    private ListView listContatos;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference reference;
    private ValueEventListener listenerContatos;

    public Contatos() {

    }

    @Override
    public void onStart() {
        super.onStart();

        reference.addValueEventListener(listenerContatos);
    }

    @Override
    public void onStop() {
        super.onStop();

        reference.removeEventListener(listenerContatos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contatos = new ArrayList<>();

       View view = inflater.inflate(R.layout.fragment_contatos, container, false);

       listContatos = view.findViewById(R.id.lstContatos);
       /*adapter = new ArrayAdapter(
               getActivity(),
               R.layout.lista_contatos,
               contatos
       );*/

       adapter = new contatoAdapter(getActivity(),contatos);
       listContatos.setAdapter(adapter);

       //Recuperar contatos do firebase
        preferencias preferencias = new preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        reference = ConfiguracaoFirebase.getFirebase()
                .child("contatos").child(identificadorUsuarioLogado);

        //Listener para recuperar os contatos
        listenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                contatos.clear();

                //Listar contatos
                for (DataSnapshot dados:dataSnapshot.getChildren() ){

                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);
                }
                //Atualiza a lista
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                //Recuperando dandos a serem passados
                Contato contato = contatos.get(i);

                //Enviando dados para a conversa
                intent.putExtra("nome", contato.getNomeContato());
                intent.putExtra("email", contato.getEmailContato());
                startActivity(intent);

            }
        });


        return view;
    }


}
