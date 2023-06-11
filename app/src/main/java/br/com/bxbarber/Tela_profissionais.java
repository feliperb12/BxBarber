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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import br.com.bxbarber.adpter.MyAdapterProfissionais;
import br.com.bxbarber.model.Barbeiro;

public class Tela_profissionais extends AppCompatActivity implements MyAdapterProfissionais.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerViewBarbeiros;
    private MyAdapterProfissionais barbeiroAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profissionais);

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
                        openScreen6();
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

}
