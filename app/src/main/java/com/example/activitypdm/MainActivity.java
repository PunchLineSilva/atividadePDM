package com.example.appcontatosroom.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcontatosroom.R;
import com.example.appcontatosroom.database.AppDatabase;
import com.example.appcontatosroom.database.DatabaseProvider;
import com.example.appcontatosroom.model.Contato;

import java.util.List;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private EditText etNome, etTelefone, etEmail;
    private Button btnSalvar;
    private RecyclerView recyclerView;
    private ContatoAdapter adapter;

    private AppDatabase db;
    private Executor ioExecutor;
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private Contato contatoParaEditar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inicializa a UI
        etNome = findViewById(R.id.etNome);
        etTelefone = findViewById(R.id.etTelefone);
        etEmail = findViewById(R.id.etEmail);
        btnSalvar = findViewById(R.id.btnSalvar);
        recyclerView = findViewById(R.id.recyclerView);

        // 2. Pega a instância do banco e o executor
        db = DatabaseProvider.getDatabase(getApplicationContext());
        ioExecutor = DatabaseProvider.getIoExecutor();

        // 3. Configura o RecyclerView
        configurarRecyclerView();

        // 4. Carrega os contatos iniciais
        carregarContatos();

        // 5. Configura o clique do botão salvar
        btnSalvar.setOnClickListener(v -> salvarContato());
    }

    private void configurarRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContatoAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ContatoAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Contato contato) {
                // Preenche os campos para edição
                contatoParaEditar = contato;
                etNome.setText(contato.nome);
                etTelefone.setText(contato.telefone);
                etEmail.setText(contato.email);
                btnSalvar.setText("Atualizar Contato");
            }

            @Override
            public void onDeleteClick(Contato contato) {
                // Deleta o contato em uma thread separada
                ioExecutor.execute(() -> {
                    db.contatoDAO().delete(contato);
                    carregarContatos(); // Recarrega a lista
                    mainThreadHandler.post(() -> {
                        Toast.makeText(MainActivity.this, "Contato deletado", Toast.LENGTH_SHORT).show();
                    });
                });
            }
        });
    }

    private void salvarContato() {
        String nome = etNome.getText().toString().trim();
        String telefone = etTelefone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (nome.isEmpty()) {
            etNome.setError("O nome é obrigatório");
            etNome.requestFocus();
            return;
        }

        // Roda a operação de salvar/atualizar fora da thread principal
        ioExecutor.execute(() -> {
            if (contatoParaEditar == null) {
                // Criando um novo contato
                Contato novoContato = new Contato();
                novoContato.nome = nome;
                novoContato.telefone = telefone;
                novoContato.email = email;
                db.contatoDAO().insert(novoContato);
                mainThreadHandler.post(() -> {
                    Toast.makeText(this, "Contato salvo!", Toast.LENGTH_SHORT).show();
                });
            } else {
                // Atualizando um contato existente
                contatoParaEditar.nome = nome;
                contatoParaEditar.telefone = telefone;
                contatoParaEditar.email = email;
                db.contatoDAO().update(contatoParaEditar);
                mainThreadHandler.post(() -> {
                    Toast.makeText(this, "Contato atualizado!", Toast.LENGTH_SHORT).show();
                });
            }

            // Limpa os campos e recarrega a lista na thread principal
            mainThreadHandler.post(() -> {
                limparCampos();
                carregarContatos();
            });
        });
    }

    private void carregarContatos() {
        ioExecutor.execute(() -> {
            List<Contato> contatos = db.contatoDAO().getAll();
            // Atualiza a UI na thread principal
            mainThreadHandler.post(() -> {
                adapter.setContatos(contatos);
            });
        });
    }

    private void limparCampos() {
        etNome.setText("");
        etTelefone.setText("");
        etEmail.setText("");
        btnSalvar.setText("Salvar Contato");
        contatoParaEditar = null;
        etNome.requestFocus();
    }
}