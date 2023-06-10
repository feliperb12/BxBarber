package br.com.bxbarber;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class tela_login extends AppCompatActivity {

    private EditText emailUsuarioLogin;
    private EditText senhaLogin;
    private FirebaseAuth mAuth;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        emailUsuarioLogin = findViewById(R.id.emailUsuarioLogin);

        senhaLogin = findViewById(R.id.senhaLogin);

        mAuth = FirebaseAuth.getInstance();

        Button telaCadastro = findViewById(R.id.telaCadastro);

        telaCadastro.setOnClickListener(view -> {
            Intent intent = new Intent(tela_login.this, TelaCadastro.class);
            startActivity(intent);
        });


        Button login = findViewById(R.id.login);

        login.setOnClickListener(v -> validarLogin());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        Button loginGoogle = findViewById(R.id.loginGoogle);
        loginGoogle.setOnClickListener(view -> signIn());

    }

    private void validarLogin() {
        String email = emailUsuarioLogin.getText().toString();
        String senha = senhaLogin.getText().toString();

        if(TextUtils.isEmpty(email)) {
            emailUsuarioLogin.setError("Insira seu E-mail");
            return;
        }else if(TextUtils.isEmpty(senha)) {
            senhaLogin.setError("Insira sua Senha");
            return;
        }


        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(tela_login.this, task -> {

                    if (task.isSuccessful()) {
                        // Login bem-sucedido
                        Toast.makeText(tela_login.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(intent);
                    } else {
                        // Login falhou
                        Toast.makeText(tela_login.this, "Erro ao efetuar login", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
            startActivity(intent);

            try {
                task.getResult(ApiException.class);

            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Erro ao efetuar login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // if(currentUser != null){
            //startActivity(new Intent(getApplicationContext(), HomeScreen.class));
        //}

    }
}