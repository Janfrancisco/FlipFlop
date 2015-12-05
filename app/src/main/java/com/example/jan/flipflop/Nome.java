package com.example.jan.flipflop;

import java.io.Serializable;

/**
 * Created by Jan on 19/10/2015.
 */
public class Nome implements Serializable {
    private String nome;
    private long id;

    public Nome(String nome) {
        this.nome = nome;
        this.id = 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return nome;
    }

    public void setId(long id) {
        this.id = id;
    }

}
