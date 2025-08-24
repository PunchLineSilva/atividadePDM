package com.example.activitypdm.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.activitypdm.model.Contato;

import java.util.List;

// @Dao informa ao Room que esta é uma interface de acesso a dados.
@Dao
public interface ContatoDAO {

    // @Query permite escrever uma consulta SQL personalizada.
    // Aqui, selecionamos todos os contatos, ordenados pelo nome.
    @Query("SELECT * FROM Contato ORDER BY nome ASC")
    List<Contato> getAll();

    // @Insert gera o código para inserir um ou mais contatos no banco.
    @Insert
    void insert(Contato... contatos);

    // @Update gera o código para atualizar um contato.
    @Update
    void update(Contato contato);

    // @Delete gera o código para deletar um contato.
    @Delete
    void delete(Contato contato);
}