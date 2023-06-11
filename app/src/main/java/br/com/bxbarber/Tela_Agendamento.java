package br.com.bxbarber;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Tela_Agendamento extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private EditText editTextDate;
    private DatePickerDialog picker;
    private Spinner spinnerService;
    private Spinner spinnerBarber;
    private TextView textViewDurationAndValue;
    private Button buttonSchedule;
    private View parentLayout;

    private FirebaseFirestore db;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_agendamento);

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

        // Inicialize as referências dos componentes
        editTextDate = findViewById(R.id.edit_date_tela);
        //CALENDARIO JUNTO COM O EDITTEXT DA DATA INICIAL
        editTextDate.setInputType(InputType.TYPE_NULL);
        spinnerService = findViewById(R.id.spin_servico_tela);
        spinnerBarber = findViewById(R.id.spin_profissional_tela);
        textViewDurationAndValue = findViewById(R.id.infoAgendamento_tela);
        buttonSchedule = findViewById(R.id.btn_agendar_tela);
        parentLayout = findViewById(android.R.id.content);
        Intent intent = getIntent();
        // Inicialize o Firestore
        db = FirebaseFirestore.getInstance();

        // Preencha o Spinner de Serviços com dados do Firestore
        populateServiceSpinner();

        // Preencha o Spinner de Barbeiros com dados do Firestore
        populateBarberSpinner();

        // Defina o evento de seleção do Spinner de Serviço
        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateDurationAndValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não é necessário implementar esse método neste caso
            }
        });

        // Defina o evento de seleção do Spinner de Profissional
        spinnerBarber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateDurationAndValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não é necessário implementar esse método neste caso
            }
        });

        // Defina o evento de seleção do EditText da Data
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // date picker dialog
                picker = new DatePickerDialog(Tela_Agendamento.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                final int selectedYear = year;
                                final int selectedMonth = monthOfYear;
                                final int selectedDay = dayOfMonth;

                                // TimePickerDialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(Tela_Agendamento.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                editTextDate.setText(String.format("%02d", selectedDay) + "/" + String.format("%02d", (selectedMonth + 1)) + "/" + selectedYear +
                                                        " " + String.format("%02d:%02d", hourOfDay, minute));

                                                // Chamada para realizar a consulta e atualizar o ListView
                                                //realizarConsultaNoFire();
                                                calculateDurationAndValue();
                                            }
                                        }, hour, minutes, true);

                                timePickerDialog.show();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        // Defina o evento de clique do botão "Agendar"
        buttonSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleAppointment();
            }
        });
    }

    private void populateServiceSpinner() {
        // Consulte os serviços no Firestore e preencha o Spinner de Serviços com os dados
        // Use a coleção "servicos" como exemplo
        db.collection("servico").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> services = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String serviceName = document.getString("nome");
                        services.add(serviceName);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Tela_Agendamento.this,
                            android.R.layout.simple_spinner_item, services);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerService.setAdapter(adapter);
                }
            }
        });
    }

    private void populateBarberSpinner() {
        // Consulte os barbeiros no Firestore e preencha o Spinner de Barbeiros com os dados
        // Use a coleção "barbeiros" como exemplo
        db.collection("barbeiros").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> barbers = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String barberName = document.getString("nome");
                        barbers.add(barberName);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Tela_Agendamento.this,
                            android.R.layout.simple_spinner_item, barbers);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerBarber.setAdapter(adapter);
                }
            }
        });
    }

    private void scheduleAppointment() {
        String data = editTextDate.getText().toString();
        String servico = spinnerService.getSelectedItem().toString();
        String barbeiro = spinnerBarber.getSelectedItem().toString();

        // Consulte o documento do serviço selecionado no Firestore
        db.collection("servico")
                .whereEqualTo("nome", servico)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String duracao = document.getString("duracao");

                        // Crie um novo objeto Appointment com os dados selecionados e a duração obtida
                        Adicionar_agendamento appointment = new Adicionar_agendamento(data, servico, barbeiro, duracao);

                        // Salve o objeto Appointment no Firestore
                        db.collection("agendamentos")
                                .add(appointment)
                                .addOnSuccessListener(documentReference -> {
                                    Snackbar snackbar = Snackbar.make(parentLayout, "Agendamento salvo no Firestore", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                })
                                .addOnFailureListener(e -> {
                                    Snackbar snackbar = Snackbar.make(parentLayout, "Erro ao salvar agendamento", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                });
                    }
                });
    }

    private void calculateDurationAndValue() {
        String servico = spinnerService.getSelectedItem().toString();

        // Consulte o documento do serviço selecionado no Firestore
        db.collection("servico")
                .whereEqualTo("nome", servico)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        if (document != null) {
                            String duracao = document.getString("duracao");
                            Double valor = document.getDouble("valor");

                            // Formatar o valor para duas casas decimais e substituir o ponto por vírgula
                            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                            symbols.setDecimalSeparator(',');
                            symbols.setGroupingSeparator('.');
                            DecimalFormat decimalFormat = new DecimalFormat("#0.00", symbols);
                            String valorFormatado = decimalFormat.format(valor);

                            // Atualize o TextView com a duração e o valor do agendamento
                            textViewDurationAndValue.setText("Duração: " + duracao +" minutos" + "\n  Valor: R$ " + valorFormatado);
                        }
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