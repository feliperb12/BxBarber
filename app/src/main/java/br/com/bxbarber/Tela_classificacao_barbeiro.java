package br.com.bxbarber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import android.annotation.SuppressLint;

import android.view.MenuItem;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tela_classificacao_barbeiro extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "ClassificacaoBarbeiro";

    private TableLayout tabelaClassificacao;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classificacao_barbeiro);

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

        tabelaClassificacao = findViewById(R.id.tableLayout);

        // Consultar os barbeiros ordenados por pontos
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("barbeiros")
                .orderBy("pontos", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> barbeiros = task.getResult().getDocuments();
                            exibirClassificacaoBarbeiros(barbeiros);
                        } else {
                            Log.e(TAG, "Erro ao consultar barbeiros", task.getException());
                        }
                    }
                });
    }

    private void exibirClassificacaoBarbeiros(List<DocumentSnapshot> barbeiros) {
        // Ordenar a lista de barbeiros com base nos pontos em ordem decrescente
        Collections.sort(barbeiros, new Comparator<DocumentSnapshot>() {
            @Override
            public int compare(DocumentSnapshot b1, DocumentSnapshot b2) {
                long pontos1 = b1.getLong("pontos");
                long pontos2 = b2.getLong("pontos");
                return Long.compare(pontos2, pontos1);
            }
        });

        // Verificar se há linhas existentes na tabela
        int existingRows = tabelaClassificacao.getChildCount();

        // Obter o número de barbeiros
        int numBarbeiros = barbeiros.size();

        // Determinar o número de linhas a serem atualizadas (o menor entre existingRows, numBarbeiros e 5)
        int numLinhasAtualizar = Math.min(existingRows, Math.min(numBarbeiros, 5));

        // Iterar sobre as linhas existentes e atualizar com os novos dados dos barbeiros
        for (int i = 0; i < numLinhasAtualizar; i++) {
            DocumentSnapshot barbeiro = barbeiros.get(i);
            String nome = barbeiro.getString("nome");
            long pontos = barbeiro.getLong("pontos");

            TableRow row = (TableRow) tabelaClassificacao.getChildAt(i);
            TextView rankText = (TextView) row.getChildAt(0);
            TextView nomeText = (TextView) row.getChildAt(1);
            TextView pontosText = (TextView) row.getChildAt(2);

            rankText.setText(String.valueOf(i + 1));
            nomeText.setText(nome);
            pontosText.setText(String.valueOf(pontos));
        }

        // Remover linhas extras, se houver mais do que 5 barbeiros
        if (existingRows > 5) {
            tabelaClassificacao.removeViews(5, existingRows - 5);
            existingRows = 5;
        }

        // Se houver mais barbeiros do que linhas existentes, adicionar novas linhas à tabela (máximo de 5)
        if (numBarbeiros > existingRows) {
            for (int i = existingRows; i < Math.min(numBarbeiros, 5); i++) {
                DocumentSnapshot barbeiro = barbeiros.get(i);
                String nome = barbeiro.getString("nome");
                long pontos = barbeiro.getLong("pontos");

                TableRow row = new TableRow(this);
                TextView rankText = new TextView(this);
                TextView nomeText = new TextView(this);
                TextView pontosText = new TextView(this);

                rankText.setText(String.valueOf(i + 1));
                nomeText.setText(nome);
                pontosText.setText(String.valueOf(pontos));

                row.addView(rankText);
                row.addView(nomeText);
                row.addView(pontosText);

                tabelaClassificacao.addView(row,0);
            }
        }
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