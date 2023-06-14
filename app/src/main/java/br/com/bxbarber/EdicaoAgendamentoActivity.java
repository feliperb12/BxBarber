package br.com.bxbarber;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import br.com.bxbarber.model.Adicionar_agendamento;
import br.com.bxbarber.model.Agendamento;

public class EdicaoAgendamentoActivity extends AppCompatActivity {

    private EditText editTextDate;
    private DatePickerDialog picker;
    private Spinner spinnerService;
    private Spinner spinnerBarber;
    private TextView textViewDurationAndValue;

    private View parentLayout;

    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_agendamento);

        // Inicialize as referências dos componentes
        editTextDate = findViewById(R.id.edit_date_editar);
        //CALENDARIO JUNTO COM O EDITTEXT DA DATA INICIAL
        editTextDate.setInputType(InputType.TYPE_NULL);
        spinnerService = findViewById(R.id.spin_servico_editar);
        spinnerBarber = findViewById(R.id.spin_profissional_editar);
        textViewDurationAndValue = findViewById(R.id.infoAgendamento_editar);

        parentLayout = findViewById(android.R.id.content);
        // Inicialize o Firestore
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();


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
                picker = new DatePickerDialog(EdicaoAgendamentoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                final int selectedYear = year;
                                final int selectedMonth = monthOfYear;
                                final int selectedDay = dayOfMonth;

                                // TimePickerDialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(EdicaoAgendamentoActivity.this,
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EdicaoAgendamentoActivity.this,
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EdicaoAgendamentoActivity.this,
                            android.R.layout.simple_spinner_item, barbers);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerBarber.setAdapter(adapter);
                }
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

    private void salvarAgendamento(Agendamento agendamento) {
        String data = editTextDate.getText().toString();
        String servico = spinnerService.getSelectedItem().toString();
        String barbeiro = spinnerBarber.getSelectedItem().toString();

        if (agendamento != null && agendamento.getId() != null) {

            // Crie um novo objeto Appointment com os dados selecionados e a duração obtida
            Adicionar_agendamento appointment = new Adicionar_agendamento(data, servico, barbeiro);

            db.collection("agendamentos").document(agendamento.getId())
                    .set(appointment)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar snackbar = Snackbar.make(parentLayout, "Agendamento salvo no Firestore", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar snackbar = Snackbar.make(parentLayout, "Erro ao salvar agendamento", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    });
        }
    }

    public void salvar(View view) {
        Intent intent = getIntent();
        if (intent.hasExtra("agendamento")) {
            Agendamento agendamento = (Agendamento) intent.getParcelableExtra("agendamento");
            agendamento.getId();
            if (agendamento != null) {
                salvarAgendamento(agendamento);
            }
        }
    }

}
