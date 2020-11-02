package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.StorageTaskScheduler;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.Key;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Usuario;

public class Perfil extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private TextView Email;
    private TextView Nome;
    private TextView Dtnsc;
    private TextView Cpf;
    private CircleImageView profileImg;
    private DatabaseReference dr;
    private DatabaseReference databaseReference;
    private FloatingActionButton fb;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    private String cameraPermissions[];
    private String storagePermissions[];
    StorageReference storageReference;
    String storagePath = "Foto de Perfil";
    Uri image_uri;
    String ProfilePhoto;

    FirebaseUser user;
    Query query;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.Perfill);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        Nome = findViewById(R.id.NomeUser);
        Cpf = findViewById(R.id.cpf);
        Dtnsc = findViewById(R.id.dtnsc);
        Email = findViewById(R.id.email);
        profileImg = findViewById(R.id.FotoUser);
        fb = findViewById(R.id.editarrr);

        dr =  FirebaseDatabase.getInstance().getReference("Usuário");
        storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuário");
        query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        RecuperarDados(query);

        cameraPermissions = new String[] {Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE};

        storagePermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};




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

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarPerfil();

            }
        });

        }
        private  boolean checkStoragePermission()
        {
            boolean result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    (PackageManager.PERMISSION_GRANTED);

            return result;
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        private void requestStoragePermission()
        {
            requestPermissions( storagePermissions, STORAGE_REQUEST_CODE);
        }

        private  boolean checkCameraPermission()
          {
        boolean result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void requestCameraPermission()
          {
        requestPermissions( cameraPermissions, CAMERA_REQUEST_CODE);
    }


    private void EditarPerfil() {
        String opcoes[] = {"Editar Foto de Perfil",
                "Editar Nome",
                "Editar Cpf",
                "Editar Data de Nascimento",
                "Editar Email"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

        builder.setTitle("Escolha alguma opção:");

        builder.setItems(opcoes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if( which == 0)
                {
                    ProfilePhoto = "image";
                    showImageDialog();

                }
                else if( which == 1)
                {
                    showUpdateDialog("Nome");

                }
                else if( which == 2)
                {

                }
                else if( which == 3)
                {

                }
                else if( which == 4)
                {

                }
            }
        });

        builder.create().show();
    }

    private void showUpdateDialog(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Atualizar" + key);

        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);

        final EditText editText = new EditText(getApplicationContext());
        editText.setHint("Enter" + key);

        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();

                if(!TextUtils.isEmpty(value))
                {
                   HashMap<String, Object> result = new HashMap<>();

                   result.put(key, value);

                   dr.child(user.getEmail()).updateChildren(result)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(getApplicationContext(), "Atualizado", Toast.LENGTH_SHORT).show();

                               }
                           }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {

                           Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                       }
                   });

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Enter" + key, Toast.LENGTH_SHORT).show();

                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create().show();;
    }

    private void showImageDialog() {
            String opcoes[] = {"Camera",
                    "Galeria"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

            builder.setTitle("Escolha alguma opção:");

            builder.setItems(opcoes, new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if( which == 0)
                    {
                      if(!checkCameraPermission())
                      {
                          requestCameraPermission();
                      }
                      else
                      {
                          pickFromCamera();
                      }

                    }
                    else if( which == 1)
                    {
                        if(!checkStoragePermission())
                        {
                            requestStoragePermission();
                        }
                        else
                        {
                            pickFromGallery();
                        }

                    }
                }
            });

            builder.create().show();
        }

    private void RecuperarDados(Query query) {

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Usuario user = snapshot.getValue(Usuario.class);
                    String link = user.getImagem();
                    String nome = user.getNome();
                    String cpf = user.getCpf();
                    String dtns = user.getDtsnc();
                    String email = user.getEmail();

                    if (!link.isEmpty()) {
                        Picasso.get().load(link).into(profileImg);
                    }

                    Nome.setText(nome);
                    Email.setText(email);
                    Cpf.setText(cpf);
                    Dtnsc.setText(dtns);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Perfil.this, "Não foi possivel recuperar os dados", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case CAMERA_REQUEST_CODE:
            {
                if(grantResults.length > 0)
                {
                    boolean CameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(CameraAccepted && writeStorageAccepted)
                    {
                        pickFromCamera();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Por favor, aceite as permissoes de camera e armazenamento", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE:
            {
                if(grantResults.length > 0)
                {
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(writeStorageAccepted)
                    {
                        pickFromGallery();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Por favor, aceite s permissão de armazenamento", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == IMAGE_PICK_GALLERY_CODE)
            {
                image_uri = data.getData();

                uploadProfilePhoto(image_uri);
            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE)
            {
                uploadProfilePhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfilePhoto(final Uri uri) {

        String filePathAndName = storagePath + "" + ProfilePhoto + "_" + user.getEmail();

        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while(!uriTask.isSuccessful());
                        Uri dowloadUri = uriTask.getResult();

                        if(uriTask.isSuccessful())
                        {
                            HashMap<String, Object> results = new HashMap<>();

                            dr.child(user.getEmail()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "Imagem Atualizada", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Erro ao Atualizar a imagem", Toast.LENGTH_SHORT).show();

                                }
                            });
                            results.put(ProfilePhoto, dowloadUri.toString());
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Ocorreu algum erro", Toast.LENGTH_SHORT).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Tempo Descrição");

        image_uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("Image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }
}
