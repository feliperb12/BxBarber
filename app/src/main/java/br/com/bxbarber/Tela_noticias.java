package br.com.bxbarber;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

public class Tela_noticias extends AppCompatActivity {

    private ImageView likeImage;
    private ImageView deslikeImage;

    private View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        likeImage = findViewById(R.id.like);
        deslikeImage = findViewById(R.id.deslike);
        parentLayout = findViewById(android.R.id.content);
        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackbar("Que bom que você gostou do nosso conteúdo!");
            }
        });

        deslikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackbar("As próximas notícias temos certeza que vão agradar você!!!");
            }
        });
    }
    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}