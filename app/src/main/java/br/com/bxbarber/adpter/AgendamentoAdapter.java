package br.com.bxbarber.adpter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.bxbarber.R;
import br.com.bxbarber.model.Agendamento;

public class AgendamentoAdapter extends RecyclerView.Adapter<AgendamentoViewHolder> {
    private List<Agendamento> agendamentos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditarClick(int position);
        void onExcluirClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AgendamentoAdapter(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

    @NonNull
    @Override
    public AgendamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agendamento, parent, false);
        return new AgendamentoViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendamentoViewHolder holder, int position) {
        Agendamento agendamento = agendamentos.get(position);
        holder.textViewBarbeiro.setText(agendamento.getBarbeiro());
        holder.textViewData.setText(agendamento.getData());
        holder.textViewDuracao.setText(agendamento.getDuracao());
        holder.textViewServico.setText(agendamento.getServico());
    }

    @Override
    public int getItemCount() {
        return agendamentos.size();
    }
}