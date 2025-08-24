package com.example.appcontatosroom.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

// A anotação @Entity define que esta classe será uma tabela no banco de dados.
@Entity
public class Contato implements Serializable { // Serializable para passar o objeto entre activities

    // @PrimaryKey define a chave primária da tabela.
    // autoGenerate = true faz com que o ID seja gerado automaticamente.
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nome;
    public String telefone;
    public String email;
}