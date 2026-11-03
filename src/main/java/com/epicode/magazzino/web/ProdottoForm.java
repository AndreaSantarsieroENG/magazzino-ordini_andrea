package com.epicode.magazzino.web;

import java.math.BigDecimal;

public class ProdottoForm {
    private String codice;
    private String nome;
    private BigDecimal prezzo;
    private int disponibili;
    private int scortaMinima;

    // Getters e setters per Spring
    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public int getDisponibili() {
        return disponibili;
    }

    public void setDisponibili(int disponibili) {
        this.disponibili = disponibili;
    }

    public int getScortaMinima() {
        return scortaMinima;
    }

    public void setScortaMinima(int scortaMinima) {
        this.scortaMinima = scortaMinima;
    }
}

