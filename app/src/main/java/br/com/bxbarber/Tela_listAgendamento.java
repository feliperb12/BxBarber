package br.com.bxbarber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.bxbarber.adpter.AgendamentoAdapter;
import br.com.bxbarber.model.Agendamento;

public class Tela_listAgendamento extends AppCompatActivity {
    private RecyclerView recyclerViewAgendamentos;
    private AgendamentoAdapter agendamentoAdapter;
    private List<Agendamento> agendamentos;
    private FirebaseFirestore db;
    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_list_agendamento);
        Intent intent = getIntent();

        recyclerViewAgendamentos = findViewById(R.id.recyclerViewAgendamentos);
        recyclerViewAgendamentos.setLayoutManager(new LinearLayoutManager(this));
        agendamentos = new ArrayList<>();
        agendamentoAdapter = new AgendamentoAdapter(agendamentos);
        recyclerViewAgendamentos.setAdapter(agendamentoAdapter);
        parentLayout = findViewById(android.R.id.content);

        db = FirebaseFirestore.getInstance();
        carregarAgendamentos();

        agendamentoAdapter.setOnItemClickListener(new AgendamentoAdapter.OnItemClickListener() {
            @Override
            public void onEditarClick(int position) {
                Agendamento agendamento = agendamentos.get(position);
                abrirTelaEdicao(agendamento);
            }

            @Override
            public void onExcluirClick(int position) {
                Agendamento agendamento = agendamentos.get(position);
                excluirAgendamento(agendamento);
            }
        });
    }

    private void carregarAgendamentos() {
        db.collection("agendamentos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                agendamentos.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String agendamentoId = documentSnapshot.getId();
                    Agendamento agendamento = documentSnapshot.toObject(Agendamento.class);
                    agendamento.setId(agendamentoId); // Definir o ID do agendamento
                    agendamentos.add(agendamento);
                }
                agendamentoAdapter.notifyDataSetChanged();
            }
        });
    }

    private void excluirAgendamento(Agendamento agendamento) {
        if (agendamento != null && agendamento.getId() != null) {
            db.collection("agendamentos").document(agendamento.getId()).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar snackbar = Snackbar.make(parentLayout, "Agendamento foi excluido no Firestore", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar snackbar = Snackbar.make(parentLayout, "Erro ao excluir", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    });
        }
    }

    private void abrirTelaEdicao(Agendamento agendamento) {
//        Intent intent = new Intent(Tela_listAgendamento.this, EdicaoAgendamentoActivity.class);
//        intent.putExtra("agendamento", agendamento);
//        startActivity(intent);
    }
}