package br.com.bxbarber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Tela_DetalheBarbeiro extends AppCompatActivity {
    private RatingBar ratingBar;
    private ImageView imagemBarbeiro;
    private TextView nomeBarbeiro, telefoneBarbeiro;

    private Button btn_avaliarBarbeiro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_barbeiro);

        imagemBarbeiro = findViewById(R.id.imagem_barbeiro_detalhes);
        nomeBarbeiro = findViewById(R.id.nome_barbeiro_detalhes);
        telefoneBarbeiro = findViewById(R.id.telefone_barbeiro_detalhes);
        ratingBar = findViewById(R.id.ratingBar);
        btn_avaliarBarbeiro = findViewById(R.id.btn_verperfil);

        Intent intent = getIntent();
        if (intent != null) {
            String nome = intent.getStringExtra("nome");
            String telefone = intent.getStringExtra("telefone");
            int imagemResId = intent.getIntExtra("imagemResId", 0);

            // Definir os valores nos elementos de layout
            imagemBarbeiro.setImageResource(imagemResId);
            nomeBarbeiro.setText(nome);
            telefoneBarbeiro.setText(telefone);
        }

        btn_avaliarBarbeiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                Toast.makeText(Tela_DetalheBarbeiro.this, "Classificação: "+ rating, Toast.LENGTH_SHORT).show();
            }
        });
    }
}