package com.example.jonat.whatsapp.Activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jonat.whatsapp.Adapter.mensagemAdapter;
import com.example.jonat.whatsapp.Fragments.Conversas;
import com.example.jonat.whatsapp.Helper.base64Custom;
import com.example.jonat.whatsapp.Helper.preferencias;
import com.example.jonat.whatsapp.R;
import com.example.jonat.whatsapp.config.ConfiguracaoFirebase;
import com.example.jonat.whatsapp.model.Mensagem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;

    private  String idUsuarioRemetente;
    private String nomeUsuarioRemetente;
    private EditText editMensagem;

    private ImageButton botaoEnviar;
    private DatabaseReference reference;

    private ListView listView;
    private ArrayList<Mensagem> conversas;
    private ArrayAdapter<Mensagem> adaptador;
    private ValueEventListener listenerConversas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = findViewById(R.id.tb_conversa);
        editMensagem = findViewById(R.id.EditConversa);
        botaoEnviar = findViewById(R.id.btnEnviar);
        listView = findViewById(R.id.lv_Conversas);

        //Recuperar dados do usuario logado!
        preferencias preferencias = new preferencias(ConversaActivity.this);
         idUsuarioRemetente = preferencias.getIdentificador();
         nomeUsuarioRemetente = preferencias.getNome();

        //Recuperando dados entre as activitys
        Bundle extra = getIntent().getExtras();

        //Verificar se existe dados a serem passados!
        if (extra != null){
            nomeUsuarioDestinatario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idUsuarioDestinatario = base64Custom.codificarBase64(emailDestinatario);
        }

        //Configurando a toolbar
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //Montando a listView
        conversas = new ArrayList<>();
        /*adaptador = new ArrayAdapter(
                ConversaActivity.this,
                android.R.layout.simple_list_item_1,
                conversas
        );*/
         adaptador = new mensagemAdapter(ConversaActivity.this, conversas);
        listView.setAdapter(adaptador);

        //Recuperar mensagens do Firebase;
        reference = ConfiguracaoFirebase.getFirebase()
                .child("mensagens")
                .child(idUsuarioRemetente)
                .child(idUsuarioDestinatario);

        listenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                conversas.clear();

                //Recuperar os dados
                for (DataSnapshot dados : dataSnapshot.getChildren()){

                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    conversas.add(mensagem);
                }

                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(listenerConversas);

        //Enviando uma mensagem
        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoMensagem = editMensagem.getText().toString();

                //Verificar se o campo estar vazio
                if (textoMensagem.isEmpty()){

                    Toast.makeText(ConversaActivity.this,"Digite uma mensagem para enviar",Toast.LENGTH_SHORT).show();

                }else{

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(textoMensagem);

                    //Salvar converça do remetente
                    boolean returnMensagemRemetente = salvarMensagens(idUsuarioRemetente,idUsuarioDestinatario, mensagem);
                    if (!returnMensagemRemetente){
                        Toast.makeText(ConversaActivity.this,"Não foi possivel enviar mensageem!", Toast.LENGTH_LONG).show();

                    }else{

                        //Salvar conversa do destinatario
                        boolean returnMensagemDestinatario = salvarMensagens(idUsuarioDestinatario, idUsuarioRemetente, mensagem);
                        if (!returnMensagemDestinatario){
                            Toast.makeText(ConversaActivity.this,"Não foi possivel enviar mensageem!", Toast.LENGTH_LONG).show();
                        }
                    }

                    com.example.jonat.whatsapp.model.Conversas conversas = new com.example.jonat.whatsapp.model.Conversas();
                    conversas.setIdUsuario(idUsuarioDestinatario);
                    conversas.setNome(nomeUsuarioDestinatario);
                    conversas.setMensagens(textoMensagem);

                    //Salvar conversa para o remetente
                    Boolean returnConversaRemetente = salvarConversas(idUsuarioRemetente, idUsuarioDestinatario, conversas);
                    if (!returnConversaRemetente){
                        Toast.makeText(ConversaActivity.this,"Erro ao salvar a Conversa",Toast.LENGTH_LONG).show();
                    }else{
                        conversas = new com.example.jonat.whatsapp.model.Conversas();
                        conversas.setIdUsuario(idUsuarioRemetente);
                        conversas.setNome(nomeUsuarioRemetente);
                        conversas.setMensagens(textoMensagem);

                        //Salvar conversa para o destinatario
                        Boolean returnConversaDestinatario = salvarConversas(idUsuarioDestinatario, idUsuarioRemetente, conversas);
                        if (!returnConversaDestinatario){
                            Toast.makeText(ConversaActivity.this,"Erro ao salvar a Conversa", Toast.LENGTH_LONG).show();
                        }
                    }

                    editMensagem.setText("");

                }
            }
        });
    }

    private boolean salvarMensagens(String idUsuarioRemetente, String idUsuarioDestinatario, Mensagem mensagem){

        try {

            reference = ConfiguracaoFirebase.getFirebase().child("mensagens");
            reference.child(idUsuarioRemetente).
                    child(idUsuarioDestinatario).
                    push().
                    setValue(mensagem);

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    private boolean salvarConversas(String idUsuarioRemetente, String idUsuarioDestinatario, com.example.jonat.whatsapp.model.Conversas conversas){
        try {

            reference = ConfiguracaoFirebase.getFirebase().child("conversas");
            reference.child(idUsuarioRemetente)
                    .child(idUsuarioDestinatario)
                    .setValue(conversas);

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        reference.removeEventListener(listenerConversas);
    }
}
