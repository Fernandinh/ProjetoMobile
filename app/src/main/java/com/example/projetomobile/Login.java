package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okhttp3.internal.Util;


public class Login extends AppCompatActivity {

    private EditText Email;
    private EditText Senha;
    private EditText Voltar;
    private Button Logar;
    private Button Forgot;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        Email = findViewById(R.id.EMAIL);
        Senha = findViewById(R.id.SENHA);

        Logar = findViewById(R.id.LOGAR);
        Forgot = findViewById(R.id.FORGOT);

        Logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Email.getText().toString();
                String senha = Senha.getText().toString().trim();
                User();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {

                    Email.setError("Email Inválido");
                    Email.setFocusable(true);

                }
                else
                {
                    UserLogin(email,senha);
                }

                FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.login);
                fb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), TelaInicio.class));

                    }
                });

            }

        });


        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecuperarSenha();
            }
        });

    }
    public void voltartelainicio (View view){

        Intent intent = new Intent(this, TelaInicio.class);
        startActivity(intent);
    }


    private void User() {
        String email = Email.getText().toString();
        String senha = Senha.getText().toString();

        if (email == null || email.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
            Toast.makeText(this, "Todos os dados devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private void UserLogin(String email, String senha) {

        String msg ="Iniciando a tela principal do app";
        Toast.makeText(Login.this, msg, Toast.LENGTH_LONG).show();

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUi(user);




                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Falha ao realizar o login",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, ""  + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {


        }
        else
        {
            String msg = "Erro ao autenticar o usúario";
            Toast.makeText(Login.this, msg, Toast.LENGTH_LONG).show();
        }
    }



    public void updateUi(FirebaseUser currentUser) {

        Intent menu = new Intent(this, MainActivity.class);
        menu.putExtra("UID", currentUser.getUid());
        menu.putExtra("email", currentUser.getEmail());
        startActivity(menu);
    }
    private void RecuperarSenha() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Redefinir Senha");

        LinearLayout constraintLayout = new LinearLayout(this);

        final EditText emailEd = new EditText(this);
        emailEd.setHint("Email");
        emailEd.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailEd.setMinEms(10);

        constraintLayout.addView(emailEd);
        constraintLayout.setPadding(10,10,10,10);

        builder.setView(constraintLayout);


        builder.setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEd.getText().toString().trim();
                beginRecuperar(email);

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecuperar(String email) {

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(Login.this, "O link de Redefinição de Senha foi enviado para o seu Email", Toast.LENGTH_SHORT).show();
                }
                else
                {
                        Toast.makeText(Login.this, "Não foi possível enviar o link para o seu Email", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}