package com.example.jonat.whatsapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.jonat.whatsapp.Activity.ConversaActivity;
import com.example.jonat.whatsapp.Adapter.conversaAdapter;
import com.example.jonat.whatsapp.Helper.base64Custom;
import com.example.jonat.whatsapp.Helper.preferencias;
import com.example.jonat.whatsapp.R;
import com.example.jonat.whatsapp.config.ConfiguracaoFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Conversas extends Fragment {

    private ListView listConversas;
    private ArrayList<com.example.jonat.whatsapp.model.Conversas> conversas;
    private ArrayAdapter adaptador;

    private DatabaseReference reference;
    private ValueEventListener listener;

    public Conversas() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        conversas = new ArrayList<>();
        listConversas = view.findViewById(R.id.lstConversas);
        adaptador = new conversaAdapter(getActivity(), conversas);
        listConversas.setAdapter(adaptador);

        //Recuperar dados do Usuario.
        preferencias preferencias = new preferencias(getActivity());
       String idUsuarioLogado = preferencias.getIdentificador();

        //Recuperar conversas do Firebase
        reference = ConfiguracaoFirebase.getFirebase()
                .child("conversas")
                .child(idUsuarioLogado);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                conversas.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    com.example.jonat.whatsapp.model.Conversas conversa = dados.getValue(com.example.jonat.whatsapp.model.Conversas.class);
                    conversas.add(conversa);
                }
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listConversas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                com.example.jonat.whatsapp.model.Conversas conversa = conversas.get(i);

                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                intent.putExtra("nome", conversa.getNome());
                String email = base64Custom.decodificarBase64(conversa.getIdUsuario());
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        reference.addValueEventListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        reference.removeEventListener(listener);
    }
}
