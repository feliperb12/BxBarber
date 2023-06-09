package br.com.bxbarber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.annotation.SuppressLint;

import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Tela_DetalheBarbeiro extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Declaração dos componentes de interface
    private RatingBar ratingBar; // Barra de avaliação
    private ImageView imagemBarbeiro; // Imagem do barbeiro
    private TextView nomeBarbeiro, telefoneBarbeiro; // Nome e telefone do barbeiro

    private Button btn_avaliarBarbeiro; // Botão de avaliação

    private DrawerLayout drawerLayout; // Layout do drawer (menu lateral)
    private ActionBarDrawerToggle drawerToggle; // Toggle para abrir/fechar o drawer
    private NavigationView navigationView; // Menu lateral de navegação
    private Toolbar toolbar; // Barra de ferramentas

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_barbeiro);

        // CONFIGURAÇÕES NAVBAR E BOTTOMBAR

        // Referenciar os componentes do layout
        drawerLayout = findViewById(R.id.drawer_layout); // Layout do drawer
        navigationView = findViewById(R.id.navigation_view); // Menu lateral
        toolbar = findViewById(R.id.toolbar); // Barra de ferramentas

        // Configurar a Toolbar
        setSupportActionBar(toolbar); // Define a toolbar como a action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Habilita o ícone de voltar
            actionBar.setHomeAsUpIndicator(R.drawable.icon); // Define o ícone dos três pontinhos
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

        // Configurar o Bottom Navigation View
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

        // Inicialização dos componentes de interface
        imagemBarbeiro = findViewById(R.id.imagem_barbeiro_detalhes);
        nomeBarbeiro = findViewById(R.id.nome_barbeiro_detalhes);
        telefoneBarbeiro = findViewById(R.id.telefone_barbeiro_detalhes);
        ratingBar = findViewById(R.id.ratingBar);
        btn_avaliarBarbeiro = findViewById(R.id.btn_verperfil);

        // Obter os dados passados pela Intent
        Intent intent = getIntent();
        if (intent != null) {
            String nome = intent.getStringExtra("nome");
            String telefone = intent.getStringExtra("telefone");
            String imagemUrl = intent.getStringExtra("imagemUrl");

            // Carregar a imagem do barbeiro usando a biblioteca Glide
            Glide.with(this)
                    .load(imagemUrl)
                    .into(imagemBarbeiro);

            // Definir os valores nos elementos de layout
            nomeBarbeiro.setText(nome);
            telefoneBarbeiro.setText(telefone);
        }

        // Configurar o listener do botão de avaliação do barbeiro
        btn_avaliarBarbeiro.setOnClickListener(new View.OnClickListener() {
            String nome = intent.getStringExtra("nome");

            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();

                // Consultar o documento do Firestore com base no nome do usuário
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                CollectionReference barbeirosRef = firestore.collection("barbeiros");
                Query query = barbeirosRef.whereEqualTo("nome", nome);

                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String userId = documentSnapshot.getId();
                            DocumentReference userRef = barbeirosRef.document(userId);
                            int pontosAtuais = documentSnapshot.getLong("pontos").intValue();
                            int novosPontos = pontosAtuais + Math.round(rating);
                            // Salvar a quantidade de pontos no Firestore

                            userRef.update("pontos", novosPontos)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Tela_DetalheBarbeiro.this, "Pontos salvos com sucesso!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Tela_DetalheBarbeiro.this, "Erro ao salvar os pontos.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(Tela_DetalheBarbeiro.this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    // Métodos para lidar com eventos do menu lateral

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

    // Implementação do NavigationView.OnNavigationItemSelectedListener
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

    // Métodos para abrir telas

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
