package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    public String email;
    private TextView Nome;
    public String UID;
    private FusedLocationProviderClient fusedLocationClient;

    private CircleImageView profileImg;
    private DatabaseReference databaseReference;
    private DatabaseReference d1;
    DatabaseReference dr;
    FirebaseUser user;
    Query query;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    private TextView e;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.Perfill);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dr = database.getReference();
        user = mAuth.getCurrentUser();


        profileImg = findViewById(R.id.Foto);
        Nome = findViewById(R.id.aa);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        Intent uid = getIntent();
        UID = uid.getStringExtra("UID");

        e = findViewById(R.id.vacinnn);

      

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuário");
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());




        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case  R.id.Menuu:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return;

                    case R.id.Perfill:


                    case R.id.Configuracoess:
                }
            }
        });

        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.perfiltela);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });





        }
    private void Colocarfotinha() {
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0)
                {
                    if(dataSnapshot.hasChild("Usuário"))
                    {
                        String imagem = dataSnapshot.child("imagem").getValue().toString();
                        Picasso.get().load(imagem).into(profileImg);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}