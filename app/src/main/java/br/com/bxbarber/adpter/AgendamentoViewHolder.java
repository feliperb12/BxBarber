package br.com.bxbarber.adpter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.bxbarber.R;

public class AgendamentoViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewBarbeiro;
    public TextView textViewData;
    public TextView textViewDuracao;
    public TextView textViewServico;
    public Button buttonExcluir;
    public Button buttonEditar;

    public AgendamentoViewHolder(@NonNull View itemView, final AgendamentoAdapter.OnItemClickListener listener) {
        super(itemView);
        textViewBarbeiro = itemView.findViewById(R.id.textViewBarbeiro);
        textViewData = itemView.findViewById(R.id.textViewData);
        textViewDuracao = itemView.findViewById(R.id.textViewDuracao);
        textViewServico = itemView.findViewById(R.id.textViewServico);
        buttonExcluir = itemView.findViewById(R.id.buttonExcluir);
        buttonEditar = itemView.findViewById(R.id.buttonEditar);

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onExcluirClick(position);
                    }
                }
            }
        });

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onEditarClick(position);
                    }
                }
            }
        });
    }
}
