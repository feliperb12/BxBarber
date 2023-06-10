package br.com.bxbarber;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


public class tela_login extends AppCompatActivity {

    private EditText emailUsuarioLogin;
    private EditText senhaLogin;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> signInLauncher;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        emailUsuarioLogin = findViewById(R.id.emailUsuarioLogin);

        senhaLogin = findViewById(R.id.senhaLogin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();


        // Ir tela Cadastro
        Button telaCadastro = findViewById(R.id.telaCadastro);
        telaCadastro.setOnClickListener(view -> {
            Intent intent = new Intent(tela_login.this, TelaCadastro.class);
            startActivity(intent);
        });

        // Validacao personalizada
        Button login = findViewById(R.id.login);
        login.setOnClickListener(v -> validarLogin());

        //Validacao Google
        Button loginGoogle = findViewById(R.id.loginGoogle);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);



        loginGoogle.setOnClickListener(v -> validarGoogle());
        signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                onActivityResult(RC_SIGN_IN, result.getResultCode(), data);
            }
        });

    }

    private void validarLogin() {
        String email = emailUsuarioLogin.getText().toString();
        String senha = senhaLogin.getText().toString();

        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(senha)) {
            emailUsuarioLogin.setError("Insira seu E-mail");
            senhaLogin.setError("Insira sua Senha");
            return;
        }else if(TextUtils.isEmpty(email)) {
            emailUsuarioLogin.setError("Insira seu e-mail");
            return;
        }else if(TextUtils.isEmpty(senha)){
            senhaLogin.setError("Insira sua senha");
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

    int RC_SIGN_IN = 40;

    private void validarGoogle() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuth(account.getIdToken());

            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void firebaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser usuario = mAuth.getCurrentUser();

                        Usuarios usuarios = new Usuarios();
                        usuarios.setUsuarioId(usuario.getUid());
                        usuarios.setNome(usuario.getDisplayName());
                        usuarios.setPerfil(usuario.getPhotoUrl().toString());

                        db.getReference().child("Usuários").child(usuario.getUid()).setValue(usuarios);

                        Intent intent = new Intent(tela_login.this, HomeScreen.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Erro ao tentar logar com a sua conta Google", Toast.LENGTH_SHORT).show();
                    }
                });
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