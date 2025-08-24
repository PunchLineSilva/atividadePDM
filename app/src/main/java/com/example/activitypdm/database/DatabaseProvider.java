package com.example.activitypdm.database;

import android.content.Context;
import androidx.room.Room;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Esta classe gerencia a criação de uma única instância do banco de dados (padrão Singleton).
public final class DatabaseProvider {

    private static volatile com.example.appcontatosroom.database.AppDatabase INSTANCE;
    // Executor para rodar as operações do banco em uma thread separada, evitando travar a UI.
    private static final Executor IO_EXECUTOR = Executors.newSingleThreadExecutor();

    private DatabaseProvider() {}

    public static com.example.appcontatosroom.database.AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseProvider.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            com.example.appcontatosroom.database.AppDatabase.class,
                            "contatos.db" // Nome do arquivo do banco de dados
                    ).build();
                }
            }
        }
        return INSTANCE;
    }

    // Retorna o executor para ser usado na MainActivity
    public static Executor getIoExecutor() {
        return IO_EXECUTOR;
    }
}