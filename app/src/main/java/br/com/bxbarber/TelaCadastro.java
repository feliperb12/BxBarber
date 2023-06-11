package br.com.bxbarber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class TelaCadastro extends AppCompatActivity {
    private EditText nomeUsuarioCadastro, emailUsuarioCadastro, senhaCadastro, dataNascUsuario, cpfCadastro, confirmSenhaCadastro;
    private Button cadastroCliente;
    private Button voltarLogin;
    String[] mensagens = {"Preencha todos os campos", "Cadastro efetuado com sucesso"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
        IniciarComponentes();

        voltarLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, tela_login.class);
            startActivity(intent);
        });

        cadastroCliente.setOnClickListener(v -> {
            String nome = nomeUsuarioCadastro.getText().toString();
            String email = emailUsuarioCadastro.getText().toString();
            String senha = senhaCadastro.getText().toString();
            String dataNasc = dataNascUsuario.getText().toString();
            String cpf = cpfCadastro.getText().toString();
            String confirmarSenha = confirmSenhaCadastro.getText().toString();

            if(nome.isEmpty() || email.isEmpty() || senha.isEmpty() || dataNasc.isEmpty() || cpf.isEmpty() || confirmarSenha.isEmpty()) {
                Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                CadastrarUsuario(v);
            }
        });
    }

    private void CadastrarUsuario(View v) {
        String email = emailUsuarioCadastro.getText().toString();
        String senha = senhaCadastro.getText().toString();
        String confirmarSenha = confirmSenhaCadastro.getText().toString();

        if(!senha.equals(confirmarSenha)) {
            Snackbar snackbar = Snackbar.make(v, "As senhas não coincidem", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            return;
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {

                SalvarDadosUsuario();
                Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();

                Intent intent = new Intent(this, tela_login.class);
                startActivity(intent);
            } else {
                String erro;

                try {
                    throw task.getException();

                } catch (FirebaseAuthWeakPasswordException e){
                    erro = "Inisira uma senha com no mínimo 6 carácteres";
                } catch (FirebaseAuthUserCollisionException e) {
                    erro = "Uma conta com esse e-mail já existe";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    erro = "E-mail inválido";
                } catch (Exception e) {
                    erro = "Erro ao cadastrar usuário";
                }

                Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }

    private void SalvarDadosUsuario() {
        String nome = nomeUsuarioCadastro.getText().toString();
        String cpf = cpfCadastro.getText().toString();
        String dataNasc = dataNascUsuario.getText().toString();


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);
        usuarios.put("cpf", cpf);
        usuarios.put("dataNasc", dataNasc);

         usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(unused -> {
                    Log.d("db", "Sucesso ao salvar os dados");
        })
                .addOnFailureListener(e -> {
                    Log.d("db_error", "Erro ao salvar os dados" + e.toString());
                });
    }

    private void IniciarComponentes() {
        nomeUsuarioCadastro = findViewById(R.id.nomeUsuarioCadastro);

        emailUsuarioCadastro = findViewById(R.id.emailUsuarioCadastro);

        senhaCadastro = findViewById(R.id.senhaCadastro);

        cadastroCliente = findViewById(R.id.cadastrodeCliente);

        dataNascUsuario = findViewById(R.id.dataNascUsuario);

        cpfCadastro = findViewById(R.id.cpfCadastro);

        confirmSenhaCadastro = findViewById(R.id.confirmSenhaCadastro);

        voltarLogin = findViewById(R.id.voltarLogin);
    }
}