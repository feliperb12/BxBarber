package br.com.bxbarber;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.bxbarber.adpter.MyAdapterProfissionais;
import br.com.bxbarber.model.Barbeiro;

public class Tela_profissionais extends AppCompatActivity implements MyAdapterProfissionais.OnItemClickListener   {
    //private List<Barbeiro> barbeiros;
    private RecyclerView recyclerViewBarbeiros;
    private MyAdapterProfissionais barbeiroAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profissionais);
        Intent intent = getIntent();

        recyclerViewBarbeiros = findViewById(R.id.recycler_profissionais);

        // Configurar o layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewBarbeiros.setLayoutManager(layoutManager);

        // Adicionar dados de exemplo
        List<Barbeiro> barbeiros = getListaDeBarbeiros();

        // Configurar o adaptador
        barbeiroAdapter = new MyAdapterProfissionais(barbeiros, (MyAdapterProfissionais.OnItemClickListener) this);
        recyclerViewBarbeiros.setAdapter(barbeiroAdapter);

    }
    @Override
    public void onItemClick(Barbeiro barbeiro) {
        // Abrir a tela de detalhes do barbeiro
        Intent intent = new Intent(this, Tela_DetalheBarbeiro.class);
        intent.putExtra("imagemResId", barbeiro.getFoto());
        intent.putExtra("nome", barbeiro.getNomeBarbeiro());
        intent.putExtra("telefone", barbeiro.getTelefoneBarbeiro());
        startActivity(intent);
    }
    // Método de exemplo para obter a lista de barbeiros
    private List<Barbeiro> getListaDeBarbeiros() {
        List<Barbeiro> barbeiros = new ArrayList<>();
        barbeiros.add(new Barbeiro(R.drawable.barbeiro, "Cleiton", "64984518533"));
        barbeiros.add(new Barbeiro(R.drawable.barbeiro2, "Jose Henrique", "649932141"));
        barbeiros.add(new Barbeiro(R.drawable.barbeiro3, "Tulho Araujo", "649932141"));
        barbeiros.add(new Barbeiro(R.drawable.barbeiro3, "Max Tavares", "649932141"));
        barbeiros.add(new Barbeiro(R.drawable.barbeiro3, "Ricardo Silva", "649932141"));
        barbeiros.add(new Barbeiro(R.drawable.barbeiro3, "Bernado Araujo", "649932141"));
        barbeiros.add(new Barbeiro(R.drawable.barbeiro3, "Tulho Araujo", "649932141"));
        barbeiros.add(new Barbeiro(R.drawable.barbeiro3, "Tulho Araujo", "649932141"));
        return barbeiros;
    }


}
