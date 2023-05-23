package br.com.bxbarber;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Tela_historico extends AppCompatActivity {
    DatePickerDialog picker;
    EditText eText_dataIncial,eText_dataFinal;
    String[] items = {"R$25,00 - Cabelo 18/08/2023", "R$15,00 - Barba 10/04/2023", "R$35,00 - Cabelo e Barba 20/12/2023", "R$05,00 - Sobrancelha 07/05/2023"};
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        Intent intent = getIntent();

        //ADAPTER PARA A LISTTTA
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview_historico, R.id.textView, items);

        ListView listView = (ListView) findViewById(R.id.listView_historico);
        listView.setAdapter(adapter);

        //CALENDARIO JUNTO COM O EDITTEXT DA DATA INICIAL
        eText_dataIncial=(EditText) findViewById(R.id.editDataInicial);
        eText_dataIncial.setInputType(InputType.TYPE_NULL);
        eText_dataIncial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Tela_historico.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText_dataIncial.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        //CALENDARIO JUNTO COM O EDITTEXT DA DATA FINAL
        eText_dataFinal=(EditText) findViewById(R.id.editDataFinal);
        eText_dataFinal.setInputType(InputType.TYPE_NULL);
        eText_dataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Tela_historico.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText_dataFinal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }
}
