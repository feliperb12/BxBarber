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
    // Declaração dos componentes da interface
    private EditText nomeUsuarioCadastro, emailUsuarioCadastro, senhaCadastro, dataNascUsuario, cpfCadastro, confirmSenhaCadastro;
    private Button cadastroCliente;
    private Button voltarLogin;
    // Mensagens exibidas ao usuário
    String[] mensagens = {"Preencha todos os campos", "Cadastro efetuado com sucesso"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
        IniciarComponentes(); // Inicializa os componentes da interface

        // Configura o clique do botão "voltarLogin"
        voltarLogin.setOnClickListener(view -> {
            // Navega para a tela de login
            Intent intent = new Intent(this, tela_login.class);
            startActivity(intent);
        });

        // Configura o clique do botão "cadastroCliente"
        cadastroCliente.setOnClickListener(v -> {
            // Obtém os valores dos campos de entrada
            String nome = nomeUsuarioCadastro.getText().toString();
            String email = emailUsuarioCadastro.getText().toString();
            String senha = senhaCadastro.getText().toString();
            String dataNasc = dataNascUsuario.getText().toString();
            String cpf = cpfCadastro.getText().toString();
            String confirmarSenha = confirmSenhaCadastro.getText().toString();

            // Verifica se todos os campos foram preenchidos
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || dataNasc.isEmpty() || cpf.isEmpty() || confirmarSenha.isEmpty()) {
                // Exibe uma mensagem de erro ao usuário usando o Snackbar
                Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                CadastrarUsuario(v); // Chama o método para realizar o cadastro do usuário
            }
        });
    }

    // Método para realizar o cadastro do usuário
    private void CadastrarUsuario(View v) {
        String email = emailUsuarioCadastro.getText().toString();
        String senha = senhaCadastro.getText().toString();
        String confirmarSenha = confirmSenhaCadastro.getText().toString();

        // Verifica se as senhas coincidem
        if (!senha.equals(confirmarSenha)) {
            // Exibe uma mensagem de erro ao usuário usando o Snackbar
            Snackbar snackbar = Snackbar.make(v, "As senhas não coincidem", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            return;
        }

        // Cria um novo usuário no Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SalvarDadosUsuario(); // Salva os dados do usuário no Firestore
                // Exibe uma mensagem de sucesso ao usuário usando o Snackbar
                Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();

                // Navega para a tela de login
                Intent intent = new Intent(this, tela_login.class);
                startActivity(intent);
            } else {
                String erro;

                try {
                    throw task.getException();
                } catch (FirebaseAuthWeakPasswordException e) {
                    erro = "Inisira uma senha com no mínimo 6 carácteres";
                } catch (FirebaseAuthUserCollisionException e) {
                    erro = "Uma conta com esse e-mail já existe";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    erro = "E-mail inválido";
                } catch (Exception e) {
                    erro = "Erro ao cadastrar usuário";
                }

                // Exibe uma mensagem de erro ao usuário usando o Snackbar
                Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });
    }

    // Salva os dados do usuário no Firestore
    private void SalvarDadosUsuario() {
        String nome = nomeUsuarioCadastro.getText().toString();
        String cpf = cpfCadastro.getText().toString();
        String dataNasc = dataNascUsuario.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Cria um mapa com os dados do usuário
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);
        usuarios.put("cpf", cpf);
        usuarios.put("dataNasc", dataNasc);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Salva os dados do usuário no Firestore
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(unused -> {
            Log.d("db", "Sucesso ao salvar os dados");
        }).addOnFailureListener(e -> {
            Log.d("db_error", "Erro ao salvar os dados" + e.toString());
        });
    }

    // Inicializa os componentes da interface
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
