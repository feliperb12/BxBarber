package br.com.bxbarber;

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

public class Tela_ajuda extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Button whatsappButton;
    private Button emailButton;
    private Button instagramButton;
    private TextView textView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);

        // Inicializar os elementos do layout
        whatsappButton = findViewById(R.id.whatsappButton);
        emailButton = findViewById(R.id.emailButton);
        instagramButton = findViewById(R.id.instagramButton);
        textView = findViewById(R.id.txt_teste);

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


        // Configurar o listener de clique para o botão do WhatsApp
        whatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirecionar para o número de telefone do WhatsApp
                Uri uri = Uri.parse("tel:62994427291");
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });

        // Configurar o listener de clique para o botão de e-mail
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirecionar para a aplicação de e-mail com o endereço de e-mail preenchido
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:bxbarber123@gmail.com"));
                startActivity(Intent.createChooser(intent, "Enviar e-mail"));
            }
        });

        // Configurar o listener de clique para o botão do Instagram
        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirecionar para o perfil do Instagram
                Uri uri = Uri.parse("https://www.instagram.com/feliipe__ribeiro/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // Configurar o listener de clique para o TextView
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implemente aqui a ação desejada para o TextView
            }
        });

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
