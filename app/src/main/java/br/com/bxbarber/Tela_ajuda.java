package br.com.bxbarber;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Tela_ajuda extends AppCompatActivity {
    private Button whatsappButton;
    private Button emailButton;
    private Button instagramButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);

        // Inicializar os elementos do layout
        whatsappButton = findViewById(R.id.whatsappButton);
        emailButton = findViewById(R.id.emailButton);
        instagramButton = findViewById(R.id.instagramButton);
        textView = findViewById(R.id.txt_teste);

        // Configurar o listener de clique para o botão do WhatsApp
        whatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirecionar para o número de telefone do WhatsApp
                Uri uri = Uri.parse("tel:62994427291");
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });

        // Configurar o listener de clique para o botão de e-mail
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirecionar para a aplicação de e-mail com o endereço de e-mail preenchido
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:bxbarber123@gmail.com"));
                startActivity(Intent.createChooser(intent, "Enviar e-mail"));
            }
        });

        // Configurar o listener de clique para o botão do Instagram
        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirecionar para o perfil do Instagram
                Uri uri = Uri.parse("https://www.instagram.com/feliipe__ribeiro/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        // Configurar o listener de clique para o TextView
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implemente aqui a ação desejada para o TextView
            }
        });
    }
}
