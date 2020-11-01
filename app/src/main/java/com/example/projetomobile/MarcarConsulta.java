package com.example.projetomobile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import model.Medicos;

import static java.util.Calendar.HOUR;

public class MarcarConsulta extends AppCompatActivity {

    EditText Data;
    EditText Hora;
    Button btnDia;
    Button btnHora;
    Button btnConsulta;
    EditText Nome;
    EditText Especialidade;
    EditText Medico;
    EditText Local;
    EditText Cpf;
    String email;
    String UID;
    String local;
    String medico;
    String especialidade;
    DatabaseReference dr;
    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_consulta);

        Intent uid = getIntent();
        UID = uid.getStringExtra("UID");

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        Intent intent1 = getIntent();
        medico = intent1.getStringExtra("medico");

        Intent intent2 = getIntent();
        especialidade = intent2.getStringExtra("especialidade");

        Intent intent3 = getIntent();
        local = intent3.getStringExtra("local");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dr = database.getReference("usuario");




        context = this;
        Data = findViewById(R.id.DATA);
        Hora = findViewById(R.id.HORA);
        Nome = findViewById(R.id.NOME);
        Cpf = findViewById(R.id.CPF);
        Especialidade = findViewById(R.id.ESPECIALIDADE);
        Medico = findViewById(R.id.MEDICO);
        Local = findViewById(R.id.LOCAL);
        btnDia = findViewById(R.id.BTNdata);
        btnHora = findViewById(R.id.BTNhora);
        btnConsulta = findViewById(R.id.btnMarcarConsulta);

        Nome.setText(email);
        Medico.setText(medico);
        Especialidade.setText(especialidade);
        Local.setText(local);

        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validação();

            }
        });

        btnDia.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                ButtonData();
            }
        });

        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonHora();
            }
        });

        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.marcar);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecycleView_Medicos.class));

            }
        });

    }
    public void voltartelainici (View view){

        Intent intent = new Intent(this, RecycleView_Medicos.class);
        startActivity(intent);
    }

    private void Validação() {
        String d = Data.getText().toString();
        String h = Hora.getText().toString();
        String n = Nome.getText().toString();
        String e = Especialidade.getText().toString();
        String m = Medico.getText().toString();
        String l = Local.getText().toString();

        if(n .isEmpty())
        {
            Nome.setError("Insira seu nome");
            Nome.requestFocus();

        }
        else if(e.isEmpty())
        {
            Especialidade.setError("Insira a especialidade do médico");
            Especialidade.requestFocus();
        }

        else if(m.isEmpty())
        {
            Medico.setError("Insira o nome do médico");
            Medico.requestFocus();
        }

        else if(l.isEmpty())
        {
            Local.setError("Insira o local da consulta");
            Local.requestFocus();

        }
        else if(d.isEmpty())
        {
            Data.setError("Coloque a data");
            Data.requestFocus();
        }
        else if(h.isEmpty())
        {
            Hora.setError("Coloque a hora");
            Hora.requestFocus();
        }
        else{
            processinsert();
        }

    }

    private void ButtonHora() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        boolean formato24horas = DateFormat.is24HourFormat(context);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String time = + hour +":" + minute;
                Hora.setText(time);


            }
        }, HOUR, MINUTE, formato24horas);
        timePickerDialog.show();

    }

    private void ButtonData() {

        Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                String dateString= date + " " + month + " " + year;
                Data.setText(dateString);

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);

                CharSequence dateCharSequence = DateFormat.format("EEEE, dd MMM yyyy", calendar1);
                Data.setText(dateCharSequence);

            }
        },YEAR,MONTH,DATE);
        datePickerDialog.show();
    }

    private void processinsert() {

        Map<String,Object> map = new HashMap<>();

        map.put("Nome",Nome.getText().toString());
        map.put("Email", user.getEmail());
        map.put("Medico", Medico.getText().toString());
        map.put("Especialidade", Especialidade.getText().toString());
        map.put("Data", Data.getText().toString());
        map.put("Hora", Hora.getText().toString());
        map.put("Local", Local.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Consultas Marcadas").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Nome.setText("");
                        Medico.setText("");
                        Especialidade.setText("");
                        Data.setText("");
                        Hora.setText("");
                        Local.setText("");





                    }
                });
    }
}