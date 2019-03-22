package com.example.jonat.whatsapp.model;

public class Contato {

    private String idenficadorContato;
    private String nomeContato;
    private String emailContato;

    public Contato() {
    }

    public String getIdenficadorContato() {
        return idenficadorContato;
    }

    public void setIdenficadorContato(String idenficadorContato) {
        this.idenficadorContato = idenficadorContato;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }
}
