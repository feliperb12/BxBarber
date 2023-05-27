package br.com.bxbarber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class tela_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        Button telaCadastro;
        telaCadastro = findViewById(R.id.telaCadastro);

        telaCadastro.setOnClickListener(view -> {
            Intent intent = new Intent(tela_login.this, TelaCadastro.class);
            startActivity(intent);
        });
    }
}