package com.example.appcontatosroom.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.activitypdm.R;
import com.example.appcontatosroom.model.Contato;
import java.util.ArrayList;
import java.util.List;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder> {

    private List<Contato> contatos = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ContatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_contato, parent, false);
        return new ContatoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContatoViewHolder holder, int position) {
        Contato contatoAtual = contatos.get(position);
        holder.tvNome.setText(contatoAtual.nome);
        holder.tvTelefone.setText(contatoAtual.telefone);
        holder.tvEmail.setText(contatoAtual.email);
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
        notifyDataSetChanged(); // Notifica o adapter que a lista mudou
    }

    class ContatoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNome;
        private TextView tvTelefone;
        private TextView tvEmail;
        private Button btnEditar;
        private Button btnDeletar;

        public ContatoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvTelefone = itemView.findViewById(R.id.tvTelefone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnDeletar = itemView.findViewById(R.id.btnDeletar);

            btnEditar.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onEditClick(contatos.get(position));
                }
            });

            btnDeletar.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(contatos.get(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onEditClick(Contato contato);
        void onDeleteClick(Contato contato);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}