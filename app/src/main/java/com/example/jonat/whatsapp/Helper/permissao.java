package com.example.jonat.whatsapp.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class permissao {

    public static boolean validaPermicoes(int requestCode, Activity activity, String[] permissoes){

        if (Build.VERSION.SDK_INT >=23){

            List<String> listaPermissoes = new ArrayList<String>();

            //Percorrer as permissoes passadas verificando se ha alguma permissao liberada.
            for (String permissao : permissoes){
              Boolean validaPermissao = ContextCompat.checkSelfPermission(activity,permissao) ==
                      PackageManager.PERMISSION_GRANTED;

              if (!validaPermissao){
                  listaPermissoes.add(permissao);
              }
            }

                //Se a lista tiver vazia não é necessario pedir permissao

            if (listaPermissoes.isEmpty()){
                return true;
            }
                String[] novasPermissoes = new String[listaPermissoes.size()];
                listaPermissoes.toArray(novasPermissoes);

                //Solicitar Permissao

            ActivityCompat.requestPermissions(activity,novasPermissoes,requestCode);
        }

        return true;
    }
}
