package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import adapters.AdapterMedicos;
import model.Medicos;
public class RecycleView_Medicos extends AppCompatActivity {

    private RecyclerView myMedicoList;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String userNome = "";
    private String userEspecialidade  = "";
    private String userLocal = "";
    private String userImagem = "";
    private Button Ligacao;
    private String profileImage;
    private DatabaseReference MedicoRef;
    private DatabaseReference MedicoReference;
    private String calledBy = "";
    private String NomeUser = "";

    RecyclerView mycontactList;
    RecyclerView recview;
    DatabaseReference dr;
    ArrayList<Medicos> list;
    AdapterMedicos adapter;
    FloatingActionButton fb;
    String email;
    String UID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view__medicos);
        setTitle("Médicos");

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        MedicoReference = FirebaseDatabase.getInstance().getReference().child("Usuário");
        userRef = FirebaseDatabase.getInstance().getReference().child("Usuário");



        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        Intent uid = getIntent();
        UID = uid.getStringExtra("UID");

        recview = (RecyclerView) findViewById(R.id.reciview);
        recview.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Medicos>();


        MedicoRef = FirebaseDatabase.getInstance().getReference().child("Usuário");


        dr = FirebaseDatabase.getInstance().getReference().child("Usuário");
        Query query = dr.orderByChild("tipo").equalTo("doctor");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshotl: snapshot.getChildren()) {

                    Medicos r = dataSnapshotl.getValue(Medicos.class);
                    list.add(r);
                }
                adapter = new AdapterMedicos(RecycleView_Medicos.this, list, email);
                recview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecycleView_Medicos.this, "Erro", Toast.LENGTH_SHORT).show();

            }

        });
        fb = (FloatingActionButton)findViewById(R.id.fadd);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getApplicationContext(), MarcarConsulta.class);
                menu.putExtra("email", email);
                startActivity(menu);

            }
        });
        fb = (FloatingActionButton)findViewById(R.id.VOLTAR);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_remedio,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s.toString());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        RecebendoLigacao();
    }

    private void RecebendoLigacao()
    {

        userRef.child(mAuth.getCurrentUser().getUid())
                .child("Tocando")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild("tocando"))
                        {
                            calledBy = dataSnapshot.child("tocando").getValue().toString();

                            Intent callingIntent = new Intent(getApplicationContext(), CallingActivity.class);
                            callingIntent.putExtra("UID_MEDICO", calledBy);
                            startActivity(callingIntent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        //RecebendoLigacao();

        FirebaseRecyclerOptions<Medicos> options
                = new FirebaseRecyclerOptions.Builder<Medicos>()
                .setQuery(MedicoReference.child(currentUserId), Medicos.class)
                .build();

        FirebaseRecyclerAdapter<Medicos,MedicosViewHolder> firebaseRecyclerAdapter
                 = new FirebaseRecyclerAdapter<Medicos, MedicosViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MedicosViewHolder holder, int position, @NonNull Medicos model) {
                final String listUserId = getRef(position).getKey();

                MedicoReference.child(listUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {
                            userNome = dataSnapshot.child("nome").getValue().toString();
                            userImagem = dataSnapshot.child("imagem").getValue().toString();
                            userEspecialidade = dataSnapshot.child("especialidade").getValue().toString();
                            userLocal = dataSnapshot.child("local").getValue().toString();

                            holder.Nome.setText(userNome);
                            Picasso.get().load(userImagem).into(holder.Img);
                            holder.Especialidade.setText(userEspecialidade);
                            holder.Hospital.setText(userLocal);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public MedicosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_medicos, parent,false);
                MedicosViewHolder viewHolder = new MedicosViewHolder(view);
                return  viewHolder;
            }
        };
        myMedicoList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void ValidateUser()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Usuário").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Intent settingIntent = new Intent(RecycleView_Medicos.this, MainActivity.class);
                startActivity(settingIntent);
                finish();

            }
        });
    }

    private void RecebendoLigacao()
    {

        MedicoRef.child(mAuth.getCurrentUser().getUid())
                .child("Tocando")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild("tocando"))
                        {
                            calledBy = dataSnapshot.child("tocando").getValue().toString();

                            Intent callingIntent = new Intent(getApplicationContext(), CallingActivity.class);
                            callingIntent.putExtra("UID_MEDICO", calledBy);
                            startActivity(callingIntent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    class MedicosViewHolder extends RecyclerView.ViewHolder {
        TextView Nome;
        TextView Especialidade;
        TextView Hospital;
        ImageView Img;
        Button Consultaa;
        Button Video;

        public MedicosViewHolder(@NonNull View itemView) {
            super(itemView);

            Nome = (TextView) itemView.findViewById(R.id.Nome);
            Especialidade = (TextView) itemView.findViewById(R.id.Especialidade);
            Hospital = (TextView) itemView.findViewById(R.id.Hospital);
            Img = (ImageView) itemView.findViewById(R.id.FotO);
            Consultaa = (Button) itemView.findViewById(R.id.BtnMarcarConsulta);
            Video = (Button) itemView.findViewById(R.id.VideoCall);

        }
    }

     */
}