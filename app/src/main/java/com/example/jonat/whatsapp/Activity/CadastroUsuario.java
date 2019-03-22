package com.example.jonat.whatsapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jonat.whatsapp.Helper.base64Custom;
import com.example.jonat.whatsapp.Helper.preferencias;
import com.example.jonat.whatsapp.R;
import com.example.jonat.whatsapp.config.ConfiguracaoFirebase;
import com.example.jonat.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class CadastroUsuario extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button cadastrar;

    private Usuario usuario;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = findViewById(R.id.edtNome);
        email = findViewById(R.id.edtCad_Email);
        senha = findViewById(R.id.edtCad_Senha);
        cadastrar = findViewById(R.id.btnCadastrar);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                CadastrarUsuario();
            }
        });
    }

    private void CadastrarUsuario(){

        auth = ConfiguracaoFirebase.getAutenticacao();
        auth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuario.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Toast.makeText(CadastroUsuario.this, "Sucesso ao cadastrar usuario!",Toast.LENGTH_SHORT).show();

                    String identificadorUsuario = base64Custom.codificarBase64( usuario.getEmail() );
                    usuario.setId( identificadorUsuario );
                    usuario.Salvar();

                    preferencias preferencias = new preferencias(CadastroUsuario.this);
                    preferencias.salvarDados(identificadorUsuario, usuario.getNome());

                    abrirLoginUsuario();

                }else{
                  String erro = "";
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite um senha mais forte que contenha no minimo 8 caracteres!";

                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "O e-mail digitado é invalido. Digite um novo e-mail";

                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "O e-mail digitado ja existe!";

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuario.this, "Erro: "+ erro,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUsuario.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
