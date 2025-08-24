package com.example.appcontatosroom.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.appcontatosroom.model.Contato;

// A anotação @Database define a classe do banco de dados.
// 'entities' lista todas as tabelas (Entidades).
// 'version' é a versão do banco, importante para migrações.
@Database(entities = {Contato.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // Este método abstrato retorna uma instância do nosso DAO.
    public abstract ContatoDAO contatoDAO();
}