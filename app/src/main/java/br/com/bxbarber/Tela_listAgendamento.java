package br.com.bxbarber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
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

import android.annotation.SuppressLint;

import android.view.MenuItem;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import br.com.bxbarber.adpter.AgendamentoAdapter;
import br.com.bxbarber.model.Agendamento;

public class Tela_listAgendamento extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerViewAgendamentos;
    private AgendamentoAdapter agendamentoAdapter;
    private List<Agendamento> agendamentos;
    private FirebaseFirestore db;
    private View parentLayout;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_list_agendamento);

        //CONFIGURAÇÕES NAVBAR E BOTTOMBAR
        // Referenciar os componentes do layout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);


        // Configurar a Toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon); // Ícone dos três pontinhos
        }

        // Configurar o Navigation Drawer
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Configurar o Listener do menu lateral
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Verifique qual item do menu foi selecionado e abra a tela correspondente
                switch (item.getItemId()) {
                    case R.id.profissionais:
                        openScreen9();
                        return true;
                    case R.id.home:
                        openScreen7();
                        return true;
                    case R.id.servicos:
                        openScreen8();
                        return true;
                }
                return false;
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }


    //MENUBAR LATERAL
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Verifique qual item do menu foi selecionado e abra a tela correspondente
        switch (item.getItemId()) {
            case R.id.agendamento:
                openScreen1();
                break;
            case R.id.minhaAgenda:
                openScreen2();
                break;
            case R.id.historico:
                openScreen3();
                break;
            case R.id.noticias:
                openScreen4();
                break;
            case R.id.ajuda:
                openScreen5();
                break;
            case R.id.sair:
                openScreen6();
                break;

        }
        // Fecha o Navigation Drawer após o clique no item do menu
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openScreen1() {
        Intent intent = new Intent(this, Tela_Agendamento.class);
        startActivity(intent);
    }

    private void openScreen2() {
        Intent intent = new Intent(this, Tela_listAgendamento.class);
        startActivity(intent);
    }

    private void openScreen3() {
        Intent intent = new Intent(this, Tela_historico.class);
        startActivity(intent);
    }

    private void openScreen4() {
        Intent intent = new Intent(this, Tela_noticias.class);
        startActivity(intent);
    }

    private void openScreen5() {
        Intent intent = new Intent(this, Tela_ajuda.class);
        startActivity(intent);
    }

    private void openScreen6() {
        Intent intent = new Intent(this, tela_login.class);
        startActivity(intent);
    }
    private void openScreen7() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }
    private void openScreen8() {
        Intent intent = new Intent(this, Tela_servicosBarbearia.class);
        startActivity(intent);
    }

    private void openScreen9() {
        Intent intent = new Intent(this, Tela_profissionais.class);
        startActivity(intent);
    }
}