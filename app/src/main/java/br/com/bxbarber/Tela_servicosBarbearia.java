package br.com.bxbarber;

import android.content.Intent;
import android.os.Bundle;
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


public class Tela_servicosBarbearia extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // Declaração dos componentes da interface
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);

        // CONFIGURAÇÕES NAVBAR E BOTTOMBAR

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

    // MENUBAR LATERAL

    // Método chamado quando um item do menu lateral é selecionado
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

    // Métodos para abrir as telas correspondentes

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


