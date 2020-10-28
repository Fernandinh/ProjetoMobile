package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Usuario;


public class Cadastro extends AppCompatActivity {

    private Button Cadastrar;
    private Button Voltar;
    private EditText Nome;
    private EditText Senha;
    private EditText Email;
    private EditText Cpf;
    private EditText Dtns;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        usuario = new Usuario();

        Nome = findViewById(R.id.NOME);
        Email = findViewById(R.id.EMAIL);
        Senha = findViewById(R.id.SENHA);
        Cpf = findViewById(R.id.CPF);
        Dtns = findViewById(R.id.DTNS);
        Cadastrar = findViewById(R.id.CADASTRAR);
        Voltar= findViewById(R.id.VOLTAR);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(Cpf, smf);
        Cpf.addTextChangedListener(mtw);

        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(Dtns, simpleMaskFormatter);
        Dtns.addTextChangedListener(maskTextWatcher);



        Cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateUser();

            }
        });

        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.faddcadastro);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TelaInicio.class));

            }
        });

    }
    public void voltarteladeinicio (View view){

        Intent intent = new Intent(this, TelaInicio.class);
        startActivity(intent);
    }



    private void CreateUser()
    {
        String nome = Nome.getText().toString();
        String email = Email.getText().toString();
        String senha = Senha.getText().toString();
        String cpf = Cpf.getText().toString();
        String dtns = Dtns.getText().toString();

        if(nome == null || nome.isEmpty() || email == null || email.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty() || cpf == null || senha.isEmpty() || dtns == null || dtns.isEmpty())
        {
            Toast.makeText(this, "Todos os dados devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("Teste", task.getResult().getUser().getUid());
                        }
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.i("Teste", e.getMessage());

                    }
                });

        CadastrarUsuario(Nome.getText().toString(), Email.getText().toString(), Senha.getText().toString(), Cpf.getText().toString(), Dtns.getText().toString());


    }

    private void finishRegister(FirebaseUser user) {
        if(user == null)
        {
            Toast.makeText(Cadastro.this, "Erro ao criar o usuário", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(Cadastro.this, "Usuário Cadastrado", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void CadastrarUsuario (String nome, String email, String senha, String cpf, String dtnsc)
    {
      String key = myRef.child("usuario").push().getKey();

      usuario.setNome(nome);
      usuario.setEmail(email);
      usuario.setSenha(senha);
      usuario.setCpf(cpf);
      usuario.setDtsnc(dtnsc);

      myRef.child("usuario").child(key).setValue(usuario);

      Toast.makeText(this,"Usuário inserido", Toast.LENGTH_SHORT).show();
    }


}