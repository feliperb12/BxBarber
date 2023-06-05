package br.com.bxbarber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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
            String imagemUrl = intent.getStringExtra("imagemUrl");
            Glide.with(this)
                    .load(imagemUrl)
                    .into(imagemBarbeiro);

            // Definir os valores nos elementos de layout
            //imagemBarbeiro.setImageResource(imagemResId);
            nomeBarbeiro.setText(nome);
            telefoneBarbeiro.setText(telefone);
        }
        btn_avaliarBarbeiro.setOnClickListener(new View.OnClickListener() {
            String nome = intent.getStringExtra("nome");
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();

                // Consultar o documento do Firestore com base no nome do usuário
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                CollectionReference barbeirosRef = firestore.collection("barbeiros");
                Query query = barbeirosRef.whereEqualTo("nome", nome);

                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String userId = documentSnapshot.getId();
                            DocumentReference userRef = barbeirosRef.document(userId);
                            int pontosAtuais = documentSnapshot.getLong("pontos").intValue();
                            int novosPontos = pontosAtuais + Math.round(rating);
                            // Salvar a quantidade de pontos no Firestore

                            userRef.update("pontos", novosPontos)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Tela_DetalheBarbeiro.this, "Pontos salvos com sucesso!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Tela_DetalheBarbeiro.this, "Erro ao salvar os pontos.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(Tela_DetalheBarbeiro.this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}