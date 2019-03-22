package com.example.jonat.whatsapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.jonat.whatsapp.Fragments.Contatos;
import com.example.jonat.whatsapp.Fragments.Conversas;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new Conversas();
                break;

            case 1:
                fragment = new Contatos();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    //Recupera o titulo das abas
    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}
