package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Usuario;


public class Cadastro extends AppCompatActivity {

    private Button Cadastrar;
    private Button Voltar;
    private Button addFoto;
    private EditText Nome;
    private EditText Senha;
    private EditText Email;
    private EditText Cpf;
    private EditText Dtns;
    private CircleImageView profileImg;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference dr;
    private FirebaseAuth mAuth;
    private StorageReference storageProfilePicsRef;
    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private static final int Gallery_Intent = 1;
    private Usuario usuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Foto de Perfil");
        mAuth = FirebaseAuth.getInstance();
        dr = FirebaseDatabase.getInstance().getReference().child("Usuário");


        usuario = new Usuario();

        Nome = findViewById(R.id.NOME);
        Email = findViewById(R.id.EMAIL);
        Senha = findViewById(R.id.SENHA);
        Cpf = findViewById(R.id.CPF);
        Dtns = findViewById(R.id.DTNS);
        Cadastrar = findViewById(R.id.CADASTRAR);
        Voltar= findViewById(R.id.VOLTAR);
        addFoto = findViewById(R.id.addFoto);
        profileImg = findViewById(R.id.FotoPerfil);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(Cpf, smf);
        Cpf.addTextChangedListener(mtw);

        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(Dtns, simpleMaskFormatter);
        Dtns.addTextChangedListener(maskTextWatcher);



        Cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadProfileImage();



            }
        });

        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.faddcadastro);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TelaInicio.class));

            }
        });

        addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CropImage.activity().setAspectRatio(1,1).start(Cadastro.this);

            }
        });

        getUserinfo();

    }

    private void getUserinfo() {
        dr.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0)
                {
                    if(dataSnapshot.hasChild("Imagem"))
                    {
                        String imagem = dataSnapshot.child("Imagem").getValue().toString();
                        Picasso.get().load(imagem).into(profileImg);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImg.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Não foi possivel adicionar a imagem", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadProfileImage() {

        if(imageUri != null)
        {
            final StorageReference fileRef;

            fileRef = storageProfilePicsRef
                    .child(mAuth.getCurrentUser() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri dowloadUrl = task.getResult();
                        myUri = dowloadUrl.toString();

                        CreateUser(myUri);
                    }
                }
            });
        }
        else
        {
            Toast.makeText(Cadastro.this, "Nenhuma Imagem foi selecionada", Toast.LENGTH_SHORT).show();
        }
    }


    public void voltarteladeinicio (View view){

        Intent intent = new Intent(this, TelaInicio.class);
        startActivity(intent);
    }



    private void CreateUser(String myUri)
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

        CadastrarUsuario(Nome.getText().toString(), Email.getText().toString(), Senha.getText().toString(), Cpf.getText().toString(), Dtns.getText().toString(), myUri);


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

    private void CadastrarUsuario (String nome, String email, String senha, String cpf, String dtnsc, String myUri)
    {
      String key = myRef.child("Usuário").push().getKey();

      usuario.setNome(nome);
      usuario.setImagem(myUri);
      usuario.setEmail(email);
      usuario.setSenha(senha);
      usuario.setCpf(cpf);
      usuario.setDtsnc(dtnsc);

      myRef.child("Usuário").child(key).setValue(usuario);

      Toast.makeText(this,"Usuário inserido", Toast.LENGTH_SHORT).show();
    }


}