package br.com.bxbarber;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Tela_historico extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatePickerDialog picker;
    EditText eText_dataIncial, eText_dataFinal;

    TextView txt_valorTotal;
    ListView listView;
    String valorTotalFormatado;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

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
        List<String> cortesList = new ArrayList<>();
        //ADAPTER PARA A LISTA
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview_historico, R.id.txt_item_corte, cortesList);

        this.listView = (ListView) findViewById(R.id.listView_historico);
        listView.setAdapter(adapter);

        txt_valorTotal = findViewById(R.id.cortes);

        //CALENDARIO JUNTO COM O EDITTEXT DA DATA INICIAL
        eText_dataIncial = (EditText) findViewById(R.id.editDataInicial);
        eText_dataIncial.setInputType(InputType.TYPE_NULL);
        eText_dataIncial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // date picker dialog
                picker = new DatePickerDialog(Tela_historico.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                final int selectedYear = year;
                                final int selectedMonth = monthOfYear;
                                final int selectedDay = dayOfMonth;

                                // TimePickerDialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(Tela_historico.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                eText_dataIncial.setText(String.format("%02d", selectedDay) + "/" + String.format("%02d", (selectedMonth + 1)) + "/" + selectedYear +
                                                        " " + String.format("%02d:%02d", hourOfDay, minute));

                                                // Chamada para realizar a consulta e atualizar o ListView
                                                //realizarConsultaNoFire();
                                            }
                                        }, hour, minutes, true);

                                timePickerDialog.show();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        //CALENDARIO JUNTO COM O EDITTEXT DA DATA FINAL
        eText_dataFinal = (EditText) findViewById(R.id.editDataFinal);
        eText_dataFinal.setInputType(InputType.TYPE_NULL);
        eText_dataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // date picker dialog
                picker = new DatePickerDialog(Tela_historico.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                final int selectedYear = year;
                                final int selectedMonth = monthOfYear;
                                final int selectedDay = dayOfMonth;

                                // TimePickerDialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(Tela_historico.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                eText_dataFinal.setText(String.format("%02d", selectedDay) + "/" + String.format("%02d", (selectedMonth + 1)) + "/" + selectedYear +
                                                        " " + String.format("%02d:%02d", hourOfDay, minute));

                                                // Chamada para realizar a consulta e atualizar o ListView
                                                //realizarConsultaNoFire();
                                            }
                                        }, hour, minutes, true);

                                timePickerDialog.show();
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }
    // Método para realizar a consulta no Firestore e atualizar o ListView com os resultados
    public void realizarConsultaNoFire(View view) {
        // Dentro do método onClick dos EditTexts de data

        // Obter datas selecionadas nos EditTexts
        String dataInicial = eText_dataIncial.getText().toString();
        String dataFinal = eText_dataFinal.getText().toString();

        // Converter as datas para o formato adequado para a consulta no Firestore
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date dateInicialObj = null;
        Date dateFinalObj = null;
        try {
            dateInicialObj = dateFormat.parse(dataInicial);
            dateFinalObj = dateFormat.parse(dataFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Criar referência à coleção no Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cortesRef = db.collection("cortes");

        // Consultar os dados com base nas datas selecionadas
        Query query = cortesRef.whereGreaterThanOrEqualTo("data", dateInicialObj)
                .whereLessThanOrEqualTo("data", dateFinalObj);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> cortesList = new ArrayList<>();
                Double valorTotal = 0.00;

                // Percorrer os documentos retornados pela consulta
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // Extrair os dados necessários do documento (serviço, data e valor)
                    String servico = document.getString("servico");
                    Date data = document.getDate("data");
                    double valor = document.getDouble("valor_servico");

                    // Formatar a data para exibição
                    String dataFormatada = dateFormat.format(data);

                    // Formatar o valor para duas casas decimais e substituir o ponto por vírgula
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setDecimalSeparator(',');
                    symbols.setGroupingSeparator('.');
                    DecimalFormat decimalFormat = new DecimalFormat("#0.00", symbols);
                    String valorFormatado = decimalFormat.format(valor);


                    // Construir a string a ser exibida no ListView
                    String item = "R$" + valorFormatado + " - " + servico + " " + dataFormatada;
                    cortesList.add(item);
                    valorTotal += valor;
                    valorTotalFormatado = decimalFormat.format(valorTotal);
                }

                // Atualizar o adaptador do ListView com os dados filtrados
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Tela_historico.this,
                        R.layout.activity_listview_historico, R.id.txt_item_corte, cortesList);
                listView.setAdapter(adapter);
                txt_valorTotal.setText("R$"+valorTotalFormatado);

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

    // Métodos para abrir as telas correspondentes ao clicar nos itens do menu lateral
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

