package br.com.bxbarber;

import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//Classe Main Acitivity
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void irTelaProfissionais(View view) {
        Intent intent = new Intent(this, Tela_profissionais.class);
        Bundle bundle = new Bundle();
        startActivity(intent);
    }

    public void irTelaServico(View view) {
        Intent intent = new Intent(this, Tela_servicosBarbearia.class);
        Bundle bundle = new Bundle();
        startActivity(intent);
    }

    public void IrTelaHistorico(View view) {
        Intent intent = new Intent(this, Tela_historico.class);
        Bundle bundle = new Bundle();
        startActivity(intent);
    }

}