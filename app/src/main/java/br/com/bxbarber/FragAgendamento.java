package br.com.bxbarber;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragAgendamento extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText editTextDate;
    private Spinner spinnerService;
    private Spinner spinnerBarber;
    private TextView textViewDurationAndValue;
    private Button buttonSchedule;
    private View parentLayout;

    private FirebaseFirestore db;

    public FragAgendamento() {
        // Construtor padrão do fragmento
    }

    public static FragAgendamento newInstance(String param1, String param2) {
        FragAgendamento fragment = new FragAgendamento();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Infla o layout associado ao fragmento
        View view = inflater.inflate(R.layout.fragment_agendamento, container, false);

        // Inicialize as referências dos componentes
        editTextDate = view.findViewById(R.id.edit_date);
        spinnerService = view.findViewById(R.id.spin_servico);
        spinnerBarber = view.findViewById(R.id.spin_profissional);
        textViewDurationAndValue = view.findViewById(R.id.infoAgendamento);
        buttonSchedule = view.findViewById(R.id.btn_agendar);
        parentLayout = view.findViewById(android.R.id.content);


        // Inicialize o Firestore
        db = FirebaseFirestore.getInstance();

        // Preencha o Spinner de Serviços com dados do Firestore
        populateServiceSpinner();

        // Preencha o Spinner de Barbeiros com dados do Firestore
        populateBarberSpinner();

        // Defina o evento de clique do botão "Agendar"
        buttonSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleAppointment();
            }
        });

        return view;
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_spinner_item, barbers);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerBarber.setAdapter(adapter);
                }
            }
        });
    }

    private void scheduleAppointment() {
        String data = editTextDate.getText().toString();
        String  servico = spinnerService.getSelectedItem().toString();
        String barbeiro = spinnerBarber.getSelectedItem().toString();

        // Consulte o documento do serviço selecionado no Firestore
        db.collection("servicos")
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
}