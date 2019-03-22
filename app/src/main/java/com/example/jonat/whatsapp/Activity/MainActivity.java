package com.example.jonat.whatsapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jonat.whatsapp.Adapter.TabAdapter;
import com.example.jonat.whatsapp.Helper.SlidingTabLayout;
import com.example.jonat.whatsapp.Helper.base64Custom;
import com.example.jonat.whatsapp.Helper.preferencias;
import com.example.jonat.whatsapp.R;
import com.example.jonat.whatsapp.config.ConfiguracaoFirebase;
import com.example.jonat.whatsapp.model.Contato;
import com.example.jonat.whatsapp.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseAuth autenticacao;
    private DatabaseReference reference;

    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;

    private String identificadorContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.vp_pagina);

        tabLayout.setDistributeEvenly(true);
        tabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));

        //Configurando o adapter
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_tela_principal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.actionSair:
                deslogarUsuario();
                return true;

            case R.id.actionConfiguracoes:
                return true;

            case R.id.actionAdicionar:
                abrirCadastrarContato();
                return true;

            default:return super.onOptionsItemSelected(item);
        }
    }

    public void deslogarUsuario(){

        autenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirCadastrarContato(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Novo contato");
        dialog.setMessage("e-mail do usuario");
        dialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        dialog.setView(editText);

        dialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Recuperar o email do contato
                String novoContato = editText.getText().toString();

                //Validar Campo Contato
                if (novoContato.isEmpty()){
                    Toast.makeText(MainActivity.this,"Preencha o e-mail", Toast.LENGTH_SHORT).show();
                }else {

                    //Verificar se o usuario ja está cadastrado

                    identificadorContato = base64Custom.codificarBase64(novoContato);

                    reference = ConfiguracaoFirebase.getFirebase();
                    reference = reference.child("usuarios").child(identificadorContato);

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null){

                                //Recuperar dados do contato
                                Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);

                                //Recuperar o identificador do usuario logado.
                                preferencias preferencias = new preferencias(MainActivity.this);
                                String identificadorUsuarioLogado  = preferencias.getIdentificador();

                                reference = ConfiguracaoFirebase.getFirebase();
                                reference = reference.child("contatos").child(identificadorUsuarioLogado)
                                                                        .child(identificadorContato);

                                Contato contato = new Contato();
                                contato.setIdenficadorContato(identificadorContato);
                                contato.setNomeContato(usuarioContato.getNome());
                                contato.setEmailContato(usuarioContato.getEmail());

                                reference.setValue(contato);

                            }else{
                                Toast.makeText(MainActivity.this,"Usuario não possui cadastro", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        });

        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.create();
        dialog.show();
    }
}
