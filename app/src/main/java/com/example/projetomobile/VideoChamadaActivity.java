package com.example.projetomobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoChamadaActivity extends AppCompatActivity

        implements  Session.SessionListener,
        PublisherKit.PublisherListener
{


    private static String API_Key = "46980034";
    private static String SESSION_ID = "1_MX40Njk4MDAzNH5-MTYwNDgyMTUyMzAyMX5mU2NzN2ZuWGtYMFFMQWVjcDRlYUdGejV-fg";
    private static  String TOKEN = "T1==cGFydG5lcl9pZD00Njk4MDAzNCZzaWc9NzVhMzAyYzBlNmFhZTc5NTUyMDE2YjFhNmU3ZmJmYmNiYjI3N2Y1YTpzZXNzaW9uX2lkPTFfTVg0ME5qazRNREF6Tkg1LU1UWXdORGd5TVRVeU16QXlNWDVtVTJOek4yWnVXR3RZTUZGTVFXVmpjRFJsWVVkR2VqVi1mZyZjcmVhdGVfdGltZT0xNjA0ODIxNjI4Jm5vbmNlPTAuNDg5NjE2NzE1OTI2ODU0MSZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNjA3NDEzNjI3JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static final String TAG = VideoChamadaActivity.class.getSimpleName();
    private static final int RC_VIDEO_APP_PERM = 124;

    private FrameLayout PubController;
    private FrameLayout SubController;

    private Session mSession;
    private Publisher mPublusher;
    private Subscriber mSubscriber;


    private ImageView BtnFecharVideo;
    private DatabaseReference dr;
    FirebaseAuth mAuth;
    private String UserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chamada);

        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();
        dr = FirebaseDatabase.getInstance().getReference().child("Usuário");

        BtnFecharVideo = findViewById(R.id.BtnEncerrarVideo);

        BtnFecharVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(UserId).hasChild("Tocando"))
                        {
                            dr.child(UserId).child("Tocando").removeValue();

                            if(mPublusher != null)
                            {
                                mPublusher.destroy();
                            }

                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }

                            startActivity(new Intent(VideoChamadaActivity.this, Cadastro.class));
                            finish();
                        }
                        if(dataSnapshot.child(UserId).hasChild("Ligando"))
                        {
                            dr.child(UserId).child("Ligando").removeValue();

                            if(mPublusher != null)
                            {
                                mPublusher.destroy();
                            }

                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }

                            startActivity(new Intent(VideoChamadaActivity.this, Cadastro.class));
                            finish();
                        }
                        else
                        {
                            if(mPublusher != null)
                            {
                                mPublusher.destroy();
                            }

                            if(mSubscriber != null)
                            {
                                mSubscriber.destroy();
                            }

                            startActivity(new Intent(VideoChamadaActivity.this, Cadastro.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, VideoChamadaActivity.this);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions()
    {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

        if(EasyPermissions.hasPermissions(this, perms))
        {
            SubController = findViewById(R.id.sub_container);
            PubController = findViewById(R.id.pub_container);

            mSession = new Session.Builder(this, API_Key,SESSION_ID).build();

            mSession.setSessionListener(VideoChamadaActivity.this);

            mSession.connect(TOKEN);
        }
        else
        {
            EasyPermissions.requestPermissions(this,"Precisamos da permissão da sua Camera e Microfone",RC_VIDEO_APP_PERM);
        }

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    @Override
    public void onConnected(Session session) {

        Log.i(TAG, "Sessão Conectada");

        mPublusher = new Publisher.Builder(this).build();
        mPublusher.setPublisherListener(VideoChamadaActivity.this);

        PubController.addView(mPublusher.getView());

        if(mPublusher.getView()  instanceof GLSurfaceView)
        {
            ((GLSurfaceView) mPublusher.getView()).setZOrderOnTop(true);
        }
        mSession.publish(mPublusher);

    }

    @Override
    public void onDisconnected(Session session) {

        Log.i(TAG, "Desconectado\n");
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        Log.i(TAG, "Conectado\n");

        if(mSubscriber == null)
        {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSession.subscribe(mSubscriber);

            SubController.addView(mSubscriber.getView());

        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        Log.i(TAG, "Sua Conexão caiu\n");

        if(mSubscriber != null)
        {
            mSubscriber = null;
            SubController.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

        Log.i(TAG, "Ocorreu algum erro na sua Conexão\n");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}