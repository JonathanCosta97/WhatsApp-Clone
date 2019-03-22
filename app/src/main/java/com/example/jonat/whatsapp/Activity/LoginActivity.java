package com.example.jonat.whatsapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jonat.whatsapp.Helper.base64Custom;
import com.example.jonat.whatsapp.Helper.permissao;
import com.example.jonat.whatsapp.Helper.preferencias;
import com.example.jonat.whatsapp.Manifest;
import com.example.jonat.whatsapp.R;
import com.example.jonat.whatsapp.config.ConfiguracaoFirebase;
import com.example.jonat.whatsapp.model.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button login;
    private String idenficadorUsuarioLogado;

    Usuario usuario;
    FirebaseAuth autenticacao;
    private DatabaseReference reference;
    private ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        email = findViewById(R.id.edtEmail);
        senha = findViewById(R.id.edtSenha);
        login = findViewById(R.id.btnLogar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                validarAutenticacaoUsuario();
            }
        });
    }

    private void validarAutenticacaoUsuario(){

       idenficadorUsuarioLogado = base64Custom.codificarBase64(usuario.getEmail());

        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    reference = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child(idenficadorUsuarioLogado);

                    listener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

                            preferencias preferencias = new preferencias(LoginActivity.this);
                            preferencias.salvarDados(idenficadorUsuarioLogado, usuarioRecuperado.getNome());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    reference.addListenerForSingleValueEvent(listener);

                    abrirMainActivity();
                    Toast.makeText(LoginActivity.this,"Usuario logado com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this,"Ocorreu um erro ao logar usuario!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verificarUsuarioLogado(){

        autenticacao = ConfiguracaoFirebase.getAutenticacao();

        if (autenticacao.getCurrentUser() !=null){
            abrirMainActivity();
        }
    }

    private void abrirMainActivity(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this,CadastroUsuario.class);
        startActivity(intent);

    }

}
