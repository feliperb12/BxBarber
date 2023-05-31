package br.com.bxbarber.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.com.bxbarber.R;
import br.com.bxbarber.model.Barbeiro;

public class MyAdapterProfissionais extends RecyclerView.Adapter<MyAdapterProfissionais.ViewHolder>  {
    private List<Barbeiro> barbeiros;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Barbeiro barbeiro);
    }


    public MyAdapterProfissionais(List<Barbeiro> barbeiro,OnItemClickListener listener) {
        this.barbeiros = barbeiro;
        this.listener =  listener;
    }

    public MyAdapterProfissionais(List<Barbeiro> barbeiro) {
        this.barbeiros = barbeiro;
    }

    public void setBarbeiros(List<Barbeiro> barbeiros) {
        this.barbeiros = barbeiros;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.barbeiros_item,parent,false);
        return new ViewHolder(view);
    }

    //EXIBIR AS VISUALIZAÇÕES DO USUÁRIO, EXIBIR OS ITENS
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Barbeiro barbeiro = barbeiros.get(position);
        Glide.with(holder.itemView.getContext())
                .load(barbeiro.getImagemUrl())
                .into(holder.foto);
        //holder.foto.setImageResource(barbeiro.getFoto());
        holder.nome.setText(barbeiro.getNomeBarbeiro());
        holder.telefone.setText(barbeiro.getTelefoneBarbeiro());
        holder.botaoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(barbeiro);
            }
        });
    }

    @Override
    public int getItemCount() {
        return barbeiros.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome,telefone;
        ImageView foto;
        Button botaoPerfil;

        public ViewHolder(View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.img_barbeiro);
            nome = itemView.findViewById(R.id.nome_barbeiro);
            telefone = itemView.findViewById(R.id.telefone_barbeiro);
            botaoPerfil = itemView.findViewById(R.id.btn_verperfil);
        }
    }
}
