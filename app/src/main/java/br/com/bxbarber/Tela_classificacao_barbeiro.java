package br.com.bxbarber;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tela_classificacao_barbeiro extends AppCompatActivity {
    private static final String TAG = "ClassificacaoBarbeiro";

    private TableLayout tabelaClassificacao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classificacao_barbeiro);

        tabelaClassificacao = findViewById(R.id.tableLayout);

        // Consultar os barbeiros ordenados por pontos
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("barbeiros")
                .orderBy("pontos", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> barbeiros = task.getResult().getDocuments();
                            exibirClassificacaoBarbeiros(barbeiros);
                        } else {
                            Log.e(TAG, "Erro ao consultar barbeiros", task.getException());
                        }
                    }
                });
    }

    private void exibirClassificacaoBarbeiros(List<DocumentSnapshot> barbeiros) {
        // Ordenar a lista de barbeiros com base nos pontos em ordem decrescente
        Collections.sort(barbeiros, new Comparator<DocumentSnapshot>() {
            @Override
            public int compare(DocumentSnapshot b1, DocumentSnapshot b2) {
                long pontos1 = b1.getLong("pontos");
                long pontos2 = b2.getLong("pontos");
                return Long.compare(pontos2, pontos1);
            }
        });

        // Verificar se há linhas existentes na tabela
        int existingRows = tabelaClassificacao.getChildCount();

        // Obter o número de barbeiros
        int numBarbeiros = barbeiros.size();

        // Determinar o número de linhas a serem atualizadas (o menor entre existingRows, numBarbeiros e 5)
        int numLinhasAtualizar = Math.min(existingRows, Math.min(numBarbeiros, 5));

        // Iterar sobre as linhas existentes e atualizar com os novos dados dos barbeiros
        for (int i = 0; i < numLinhasAtualizar; i++) {
            DocumentSnapshot barbeiro = barbeiros.get(i);
            String nome = barbeiro.getString("nome");
            long pontos = barbeiro.getLong("pontos");

            TableRow row = (TableRow) tabelaClassificacao.getChildAt(i);
            TextView rankText = (TextView) row.getChildAt(0);
            TextView nomeText = (TextView) row.getChildAt(1);
            TextView pontosText = (TextView) row.getChildAt(2);

            rankText.setText(String.valueOf(i + 1));
            nomeText.setText(nome);
            pontosText.setText(String.valueOf(pontos));
        }

        // Remover linhas extras, se houver mais do que 5 barbeiros
        if (existingRows > 5) {
            tabelaClassificacao.removeViews(5, existingRows - 5);
            existingRows = 5;
        }

        // Se houver mais barbeiros do que linhas existentes, adicionar novas linhas à tabela (máximo de 5)
        if (numBarbeiros > existingRows) {
            for (int i = existingRows; i < Math.min(numBarbeiros, 5); i++) {
                DocumentSnapshot barbeiro = barbeiros.get(i);
                String nome = barbeiro.getString("nome");
                long pontos = barbeiro.getLong("pontos");

                TableRow row = new TableRow(this);
                TextView rankText = new TextView(this);
                TextView nomeText = new TextView(this);
                TextView pontosText = new TextView(this);

                rankText.setText(String.valueOf(i + 1));
                nomeText.setText(nome);
                pontosText.setText(String.valueOf(pontos));

                row.addView(rankText);
                row.addView(nomeText);
                row.addView(pontosText);

                tabelaClassificacao.addView(row,0);
            }
        }
    }

}