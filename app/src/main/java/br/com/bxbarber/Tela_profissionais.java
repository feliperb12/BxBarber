package br.com.bxbarber;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.bxbarber.adpter.MyAdapterProfissionais;
import br.com.bxbarber.model.Barbeiro;

public class Tela_profissionais extends AppCompatActivity implements MyAdapterProfissionais.OnItemClickListener   {
    private RecyclerView recyclerViewBarbeiros;
    private MyAdapterProfissionais barbeiroAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        getListaDeBarbeiros();

        // Configurar o adaptador
        barbeiroAdapter = new MyAdapterProfissionais(new ArrayList<>(),this);
        recyclerViewBarbeiros.setAdapter(barbeiroAdapter);

    }
    @Override
    public void onItemClick(Barbeiro barbeiro) {
        // Abrir a tela de detalhes do barbeiro
        Intent intent = new Intent(this, Tela_DetalheBarbeiro.class);
        intent.putExtra("imagemUrl", barbeiro.getImagemUrl());
        intent.putExtra("nome", barbeiro.getNomeBarbeiro());
        intent.putExtra("telefone", barbeiro.getTelefoneBarbeiro());
        startActivity(intent);
    }
    private void getListaDeBarbeiros() {
        CollectionReference usuariosRef = db.collection("barbeiros");

        usuariosRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Barbeiro> userList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String nome = document.getString("nome");
                        String telefone = document.getString("telefone");
                        String foto = document.getString("imagemUrl");

                        Barbeiro user = new Barbeiro(nome, telefone,foto);
                        userList.add(user);
                    }

                    // Atualizar a lista de barbeiros no adaptador
                    barbeiroAdapter.setBarbeiros(userList);
                    barbeiroAdapter.notifyDataSetChanged();
                } else {
                    String Tag = "erro";
                    Log.d(Tag, "Erro ao obter os dados: ", task.getException());
                }
            }
        });
    }


    public void onClickRanking(View view) {
        // Abrir a tela de detalhes do barbeiro
        Intent intent = new Intent(this, Tela_classificacao_barbeiro.class);
        startActivity(intent);
    }


}
