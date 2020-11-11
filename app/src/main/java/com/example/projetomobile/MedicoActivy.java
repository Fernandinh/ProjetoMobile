package com.example.projetomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MedicoActivy extends AppCompatActivity {

    private Button ReceberLigacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_activy);

        ReceberLigacao = findViewById(R.id.Ligacao);

        ReceberLigacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ligacao(v);
            }
        });
    }

    public void Ligacao (View view){
        Intent menu = new Intent(this, RecycleView_Medicos.class);
        startActivity(menu);
    }
}