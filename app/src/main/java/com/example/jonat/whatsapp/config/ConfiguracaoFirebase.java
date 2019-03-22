package com.example.jonat.whatsapp.config;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenceFirebase;
    private static FirebaseAuth autenticacao;


    public static DatabaseReference getFirebase(){

        //Caso não exista um referencia
        if (referenceFirebase == null){
            referenceFirebase = FirebaseDatabase.getInstance().getReference();

        }return referenceFirebase;
    }


    public static FirebaseAuth getAutenticacao(){

        if (autenticacao == null){
           autenticacao = FirebaseAuth.getInstance();

        }return autenticacao;
    }

}
