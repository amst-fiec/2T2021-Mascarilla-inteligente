package com.example.s_mask;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import android.os.Vibrator;
import android.app.NotificationManager;

public class dashboard extends AppCompatActivity {
    ArrayList<String> listaPersonas = new ArrayList<String>();
    ArrayList<String> listaPersonasPrueba = new ArrayList<String>();
    private EditText etCantidadLista;
    private int cantidadLista;
    private FirebaseAuth mAuth;
    private double tiempoUso;
    private static final int idUnica = 51623;
    private String channelID = "channelID";
    private String channelName = "channelName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        etCantidadLista = (EditText)findViewById(R.id.et_peopleAmountNumber);

        pruebaCantidadListaPersonasPrueba();

        cantidadLista = listaPersonasPrueba.size();
        etCantidadLista.setText(String.valueOf(cantidadLista));

        createNotificationChannel();

        Intent intent = new Intent(this, dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.mascarilla)
                .setContentTitle("SS-Mask")
                .setContentText("Debe cambiar la mascarilla")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Debe cambiar la mascarilla porque la ha utilizado por mas 4 horas o mas de 50 personas han estado cerca de usted"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        if (cantidadLista > 5 || tiempoUso > 14400){
            Toast.makeText(getApplicationContext(),
                    "Debe cambiar la mascarilla", Toast.LENGTH_SHORT).show();


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, builder.build());
        }
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setDescription("Debe cambiar la mascarilla");
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public void logout(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Toast.makeText(getApplicationContext(),
                "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(dashboard.this, MainActivity.class));
        finish();

        /*FirebaseAuth.getInstance().signOut();

        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(dashboard.this, MainActivity.class));
                        Toast.makeText(getApplicationContext(),
                                "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Falló el cierre de sesión", Toast.LENGTH_SHORT).show();
                    }
                });*/
    }

    private void pruebaCantidadListaPersonasPrueba(){
        listaPersonasPrueba.add("Alex");
        listaPersonasPrueba.add("Dayana");
        listaPersonasPrueba.add("Oscar");
        listaPersonasPrueba.add("Andres");
        listaPersonasPrueba.add("Aracelly");
        listaPersonasPrueba.add("Justhyn");
        listaPersonasPrueba.add("Juan");
    }

}