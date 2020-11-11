package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CallingActivity extends AppCompatActivity {

    private TextView Nome;
    private ImageView Perfil;
    private ImageView BtnAtender;
    private ImageView BtnCancelar;
    private String IdMedico = "";
    private String ImagemMedico = "";
    private String callingId = "";
    private String ringningId = "";
    private String NomeMedico = "";
    private String IdUser = "";
    private String Checker = "";
    private String ImagemUser = "";
    private String NomeUser = "";
    private DatabaseReference MedicoRef;
    private MediaPlayer mediaPlayer;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        mAuth = FirebaseAuth.getInstance();

        IdUser = mAuth.getCurrentUser().getUid();


        IdMedico = getIntent().getExtras().get("UID_MEDICO").toString();
        MedicoRef = FirebaseDatabase.getInstance().getReference().child("Usuário");
        mediaPlayer = MediaPlayer.create(this, R.raw.toque);

        Nome = findViewById(R.id.nome_calling);
        Perfil = findViewById(R.id.profile_image_calling);
        BtnAtender = findViewById(R.id.make_call);
        BtnCancelar = findViewById(R.id.cancel_call);

        BtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                Checker = "clicked";

                cancelarCallingUser();
            }
        });

        BtnAtender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                final HashMap<String, Object> callingPickUp= new HashMap<>();
                callingPickUp.put("selecionado", "selecionado");

                MedicoRef.child(IdUser).child("Tocando")
                        .updateChildren(callingPickUp)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Intent VideoChamada = new Intent(CallingActivity.this, VideoChamadaActivity.class);
                                startActivity(VideoChamada);

                            }
                        });
            }
        });



        getAndSetMedicoProfile();

    }

    private void getAndSetMedicoProfile() {

        MedicoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(IdMedico).exists())
                {
                    ImagemMedico = dataSnapshot.child(IdMedico).child("imagem").getValue().toString();
                    NomeMedico = dataSnapshot.child(IdMedico).child("nome").getValue().toString();

                    Nome.setText(NomeMedico);
                    Picasso.get().load(ImagemMedico).placeholder(R.drawable.profile_image).into(Perfil);
                }

                if(dataSnapshot.child(IdUser).exists())
                {
                    ImagemUser = dataSnapshot.child(IdUser).child("imagem").getValue().toString();
                    NomeUser = dataSnapshot.child(IdUser).child("nome").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mediaPlayer.start();

        MedicoRef.child(IdMedico)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(!Checker.equals("clicked") && !dataSnapshot.hasChild("Ligando") && !dataSnapshot.hasChild("Tocando"))
                        {
                            final HashMap<String, Object> callingInfo = new HashMap<>();

                            callingInfo.put("ligando", IdMedico);
                            callingInfo.put("nome", NomeMedico);


                            MedicoRef.child(IdUser)
                                    .child("Ligando")
                                    .updateChildren(callingInfo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                final HashMap<String, Object> tocandoInfo = new HashMap<>();

                                                tocandoInfo.put("tocando", IdUser);
                                                tocandoInfo.put("nome", NomeUser);

                                                MedicoRef.child(IdMedico)
                                                        .child("Tocando")
                                                        .updateChildren(tocandoInfo);
                                            }
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        MedicoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(IdUser).hasChild("Tocando") && !dataSnapshot.child(IdUser).hasChild("Ligando"))
                {
                    BtnAtender.setVisibility(View.VISIBLE);
                }

                if(dataSnapshot.child(IdMedico).child("Tocando").hasChild("selecionado"))
                {
                    mediaPlayer.stop();
                    Intent VideoChamada = new Intent(CallingActivity.this, VideoChamadaActivity.class);
                    startActivity(VideoChamada);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void cancelarCallingUser() {

        //Usuario ligando
        MedicoRef.child(IdUser)
                .child("Ligando")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.hasChild("ligando"))
                        {
                            callingId = dataSnapshot.child("ligando").getValue().toString();

                            MedicoRef.child(callingId)
                                    .child("Tocando")
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                MedicoRef.child(IdUser)
                                                        .child("Ligando")
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                startActivity(new Intent(CallingActivity.this, RecycleView_Medicos.class));
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }else{
                            startActivity(new Intent(CallingActivity.this, RecycleView_Medicos.class));
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //Medico recebendo
        MedicoRef.child(IdUser)
                .child("Tocando")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.hasChild("tocando"))
                        {

                            ringningId = dataSnapshot.child("tocando").getValue().toString();

                            MedicoRef.child(ringningId)
                                    .child("Ligando")
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                MedicoRef.child(IdUser)
                                                        .child("Tocando")
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                startActivity(new Intent(CallingActivity.this, RecycleView_Medicos.class));
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }else{
                            startActivity(new Intent(CallingActivity.this, RecycleView_Medicos.class));
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}