package com.example.projetomobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public String email;
    private FusedLocationProviderClient fusedLocationClient;
    private int  MY_PERMISSION_REQUEST_CONTACTS;
    DatabaseReference dr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        dr = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");




    }

    private void LatLongFirebase() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.e("Latitude:", + location.getLatitude() + "Longitude" + location.getLongitude());

                            Map<String,Object> latlang = new HashMap<>();

                            latlang.put("Latitude", location.getLatitude());
                            latlang.put("Longitude", location.getLongitude());

                            dr.child("Ubs").push().setValue(latlang);

                        }
                    }
                });
    }

    public void Mapa (View view){
        Intent menu = new Intent(this, MapaActivity.class);
        startActivity(menu);
    }

    public void Médicos (View view){

        Intent menu = new Intent(this, RecycleView_Medicos.class);
        menu.putExtra("email", email);
        startActivity(menu);
    }


    public void proximaTela(View view){

        Intent intent = new Intent(this, RecycleView_Remedios.class);
        startActivity(intent);
    }


    public void ConsultasFinalizadas(View view){

        Intent menu = new Intent(this, recycle_view_consultas_antigas.class);
        menu.putExtra("email", email);
        startActivity(menu);
    }

    public void ConsultasMarcadas(View view){

        Intent menu = new Intent(this, RecyclerView_Consultas_Marcadas.class);
        menu.putExtra("email", email);
        startActivity(menu);
    }


}
