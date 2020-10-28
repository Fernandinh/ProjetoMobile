package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapters.AdapterConsultasAntigas;
import adapters.AdapterConsultasMarcadas;
import model.ConsultasAntigas;
import model.ConsultasMarcadas;

public class recycle_view_consultas_antigas extends AppCompatActivity {

    RecyclerView recview;
    DatabaseReference dr;
    String email;
    ArrayList<ConsultasAntigas> list;
    AdapterConsultasAntigas adapter;
    FloatingActionButton fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_consultas_antigas);

        setTitle("Consultas Finalizadas");

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        recview = (RecyclerView)findViewById(R.id.reciview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        list= new ArrayList<ConsultasAntigas>();

        dr = FirebaseDatabase.getInstance().getReference().child("Consultas Finalizadas");
        Query query = dr.orderByChild("Email").equalTo(email);

        final ValueEventListener erro = query.addValueEventListener(new ValueEventListener() {
            @Override


            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ConsultasAntigas c = dataSnapshot.getValue(ConsultasAntigas.class);
                    list.add(c);
                }
                adapter = new AdapterConsultasAntigas(recycle_view_consultas_antigas.this, list);
                recview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(recycle_view_consultas_antigas.this, "Erro", Toast.LENGTH_SHORT).show();


            }

        });


        fb = (FloatingActionButton)findViewById(R.id.fadd);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), add_consultas_antigas.class));

            }
        });

    }
}