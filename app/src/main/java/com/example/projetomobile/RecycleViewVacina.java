package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import adapters.AdapterVacinas;
import model.Vacinas;

public class RecycleViewVacina extends AppCompatActivity {

    RecyclerView recview;
    DatabaseReference dr;
    ArrayList<Vacinas> list;
    AdapterVacinas adapter;
    FloatingActionButton fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_vacina);

        recview = (RecyclerView) findViewById(R.id.reciview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Vacinas>();

        dr = FirebaseDatabase.getInstance().getReference().child("Vacinas");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshotl: dataSnapshot.getChildren()) {
                    Vacinas r = dataSnapshotl.getValue(Vacinas.class);
                    list.add(r);
                }
                adapter = new AdapterVacinas(RecycleViewVacina.this, list);
                recview.setAdapter(adapter);

             //   LinearLayoutManager layoutManager = new LinearLayoutManager(RecycleViewVacina.this, LinearLayoutManager.HORIZONTAL, false);
              //  recview.setLayoutManager(layoutManager);]

                GridLayoutManager gridLayoutManager = new GridLayoutManager(RecycleViewVacina.this, 2);
                recview.setLayoutManager(gridLayoutManager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(RecycleViewVacina.this, "Erro", Toast.LENGTH_SHORT).show();

            }
        });

        fb = (FloatingActionButton)findViewById(R.id.remedio);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
    }
}