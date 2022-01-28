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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Vibrator;
import android.app.NotificationManager;

import org.w3c.dom.Text;

public class dashboard extends AppCompatActivity {

    //Variables

    private TableLayout tableLayout;
    private TextView txtHora;
    private TextView txtDistancia;
    private TextView txtTiempo;
    private String hora;
    private String distancia;
    private String tiempo;
    private String[] celdas = new String[3];

    int cont = 1;
    /*String hora = "";
    String distancia = "";
    String tiempo = "";*/

    private String[]header = {"Hora", "Distancia", "Tiempo"};

    private ArrayList<String[]> rows =  new ArrayList<>();
    private ArrayList<String[]> rowsP =  new ArrayList<>();
    private ArrayList<String[]> rowsC =  new ArrayList<>();

    int rand;


    private TableDynamic tableDynamic;

    ArrayList<String> listaPersonas = new ArrayList<String>();
    ArrayList<String> listaPersonasPrueba = new ArrayList<String>();
    private EditText etCantidadLista;
    private int cantidadLista;
    private double tiempoUso;
    private static final int idUnica = 51623;
    private String channelID = "channelID";
    private String channelName = "channelName";
    private ImageButton mImageButtonSignOut;
    private FirebaseAuth mAuth;

    private TextView mTituloNombre;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Autenticacion para firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        tableLayout = (TableLayout) findViewById(R.id.tl_dashboard);

        rowsC.add(new String[]{"17:14XD", "5 metros", "1m 18s"});

        //Tabla dinamica de datos mascarlla
        tableDynamic = new TableDynamic(tableLayout, getApplicationContext());
        tableDynamic.addData(getClients());
        tableDynamic.backgroundData(Color.YELLOW);
        tableDynamic.lineColor(Color.BLACK);

        mTituloNombre = (TextView) findViewById(R.id.tv_titleTable);

        mImageButtonSignOut = (ImageButton) findViewById(R.id.btn_signOut);

        //Cerrar sesion
        mImageButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(dashboard.this, "Se cerro la sesión", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(dashboard.this, MainActivity.class));
                finish();
            }
        });

        etCantidadLista = (EditText)findViewById(R.id.et_peopleAmountNumber);

        //Contar cantidad de personas en tabla mascarilla
        cantidadLista = getClients().size()/2;
        etCantidadLista.setText(String.valueOf(cantidadLista));

        createNotificationChannel();

        Intent intent = new Intent(this, dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        //Creacion y alerta de la notificacion
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.mascarilla)
                .setContentTitle("SS-Mask")
                .setContentText("Debe cambiar la mascarilla")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Debe cambiar la mascarilla porque la ha utilizado por mas 4 horas o mas de 50 personas han estado cerca de usted"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        //Validacion para la notificacion
        if (rand == 1 || tiempoUso > 14400){
            Toast.makeText(getApplicationContext(),
                    "Debe cambiar la mascarilla", Toast.LENGTH_SHORT).show();


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, builder.build());
        }

        getUserInfo();
    }

    private ArrayList<String[]>getClients(){

        rand = (int)(Math.random()*5 + 1);
        if(rand == 1){
            rows.add(new String[]{"11:57", "4 metros", "7m 45s"});
            rows.add(new String[]{"16:30", "3 metros", "6m 41s"});
            rows.add(new String[]{"15:40", "7 metros", "9m 27s"});
            rows.add(new String[]{"16:27", "4 metros", "6m 47s"});
            rows.add(new String[]{"15:41", "5 metros", "9m 20s"});
            rows.add(new String[]{"17:24", "5 metros", "23m 18s"});
            rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
            rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
            rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
            rows.add(new String[]{"18:56", "4 metros", "13m 45s"});
            rows.add(new String[]{"09:48", "4 metros", "22m 38s"});
            rows.add(new String[]{"08:24", "5 metros", "10m 15s"});
            rows.add(new String[]{"19:45", "7 metros", "12m 37s"});
            rows.add(new String[]{"20:44", "5 metros", "11m 28s"});
            rows.add(new String[]{"16:56", "4 metros", "3m 45s"});
            rows.add(new String[]{"12:45", "7 metros", "2m 37s"});
            rows.add(new String[]{"13:24", "3 metros", "2m 18s"});
            rows.add(new String[]{"11:56", "4 metros", "7m 45s"});
            rows.add(new String[]{"22:45", "7 metros", "4m 39s"});
            rows.add(new String[]{"07:14", "5 metros", "3m 19s"});
            rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
            rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
            rows.add(new String[]{"19:16", "4 metros", "3m 46s"});
            rows.add(new String[]{"12:55", "7 metros", "26m 37s"});
            rows.add(new String[]{"16:27", "4 metros", "6m 47s"});
            rows.add(new String[]{"15:40", "7 metros", "9m 27s"});
            rows.add(new String[]{"17:24", "5 metros", "23m 18s"});
            rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
            rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
            rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
            rows.add(new String[]{"18:56", "4 metros", "12m 45s"});
            rows.add(new String[]{"09:45", "7 metros", "22m 38s"});
            rows.add(new String[]{"08:24", "5 metros", "10m 15s"});
            rows.add(new String[]{"17:56", "4 metros", "7m 454"});
            rows.add(new String[]{"19:45", "7 metros", "12m 37s"});
            rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
            rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
            rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
            rows.add(new String[]{"18:56", "4 metros", "12m 45s"});
            rows.add(new String[]{"17:56", "4 metros", "7m 454"});
            rows.add(new String[]{"19:45", "7 metros", "12m 37s"});
            rows.add(new String[]{"20:44", "5 metros", "11m 28s"});
            rows.add(new String[]{"19:16", "4 metros", "3m 46s"});
            rows.add(new String[]{"12:55", "7 metros", "26m 37s"});
            rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
            rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
            rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
            rows.add(new String[]{"18:56", "4 metros", "12m 45s"});
            rows.add(new String[]{"13:24", "3 metros", "2m 18s"});
            rows.add(new String[]{"11:56", "4 metros", "7m 45s"});
            rows.add(new String[]{"22:45", "7 metros", "4m 39s"});
            rows.add(new String[]{"07:14", "5 metros", "3m 19s"});
            rows.add(new String[]{"16:27", "4 metros", "6m 47s"});
        }else{
            if(rand == 2){
                rows.add(new String[]{"22:45", "7 metros", "4m 39s"});
                rows.add(new String[]{"07:14", "5 metros", "3m 19s"});
                rows.add(new String[]{"16:27", "4 metros", "6m 47s"});
                rows.add(new String[]{"15:40", "7 metros", "9m 27s"});
                rows.add(new String[]{"17:24", "5 metros", "23m 18s"});
                rows.add(new String[]{"19:45", "7 metros", "12m 37s"});
                rows.add(new String[]{"20:44", "5 metros", "11m 28s"});
                rows.add(new String[]{"19:16", "4 metros", "3m 46s"});
                rows.add(new String[]{"12:55", "7 metros", "26m 37s"});
                rows.add(new String[]{"17:14", "5 metros", "1m 18s"});
                rows.add(new String[]{"16:56", "4 metros", "3m 45s"});
                rows.add(new String[]{"12:45", "7 metros", "2m 37s"});
                rows.add(new String[]{"13:24", "3 metros", "2m 18s"});
                rows.add(new String[]{"11:56", "4 metros", "7m 45s"});
                rows.add(new String[]{"22:45", "7 metros", "4m 39s"});
                rows.add(new String[]{"07:14", "5 metros", "3m 19s"});
                rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
                rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
                rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
                rows.add(new String[]{"18:56", "4 metros", "12m 45s"});
            }else{
                if(rand == 3){
                    rows.add(new String[]{"22:45", "7 metros", "4m 39s"});
                    rows.add(new String[]{"07:14", "5 metros", "3m 19s"});
                    rows.add(new String[]{"16:27", "4 metros", "6m 47s"});
                    rows.add(new String[]{"15:40", "7 metros", "9m 27s"});
                    rows.add(new String[]{"12:45", "7 metros", "2m 37s"});
                    rows.add(new String[]{"13:24", "3 metros", "2m 18s"});
                    rows.add(new String[]{"11:56", "4 metros", "7m 45s"});
                    rows.add(new String[]{"22:45", "7 metros", "4m 39s"});
                    rows.add(new String[]{"07:14", "5 metros", "3m 19s"});
                    rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
                    rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
                    rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
                    rows.add(new String[]{"18:56", "4 metros", "12m 45s"});
                }else{
                    if(rand == 4){
                        rows.add(new String[]{"11:56", "4 metros", "7m 45s"});
                        rows.add(new String[]{"22:45", "7 metros", "4m 39s"});
                        rows.add(new String[]{"16:56", "4 metros", "3m 45s"});
                        rows.add(new String[]{"12:45", "7 metros", "2m 37s"});
                        rows.add(new String[]{"13:24", "3 metros", "2m 18s"});
                        rows.add(new String[]{"11:56", "4 metros", "7m 45s"});
                        rows.add(new String[]{"16:27", "4 metros", "6m 47s"});
                        rows.add(new String[]{"15:40", "7 metros", "9m 27s"});
                        rows.add(new String[]{"17:24", "5 metros", "23m 18s"});
                        rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
                        rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
                        rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
                        rows.add(new String[]{"18:56", "4 metros", "12m 45s"});
                        rows.add(new String[]{"09:45", "7 metros", "22m 38s"});
                        rows.add(new String[]{"08:24", "5 metros", "10m 15s"});
                        rows.add(new String[]{"17:56", "4 metros", "7m 454"});
                        rows.add(new String[]{"19:45", "7 metros", "12m 37s"});
                        rows.add(new String[]{"20:44", "5 metros", "11m 28s"});
                        rows.add(new String[]{"19:16", "4 metros", "3m 46s"});
                        rows.add(new String[]{"12:55", "7 metros", "26m 37s"});
                        rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
                        rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
                        rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
                        rows.add(new String[]{"18:56", "4 metros", "12m 45s"});
                    }
                    else{
                        rows.add(new String[]{"11:57", "4 metros", "7m 45s"});
                        rows.add(new String[]{"16:30", "3 metros", "6m 41s"});
                        rows.add(new String[]{"15:40", "7 metros", "9m 27s"});
                        rows.add(new String[]{"16:27", "4 metros", "6m 47s"});
                        rows.add(new String[]{"15:41", "5 metros", "9m 20s"});
                        rows.add(new String[]{"17:24", "5 metros", "23m 18s"});
                        rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
                        rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
                        rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
                        rows.add(new String[]{"18:56", "4 metros", "13m 45s"});
                        rows.add(new String[]{"09:48", "4 metros", "22m 38s"});
                        rows.add(new String[]{"08:24", "5 metros", "10m 15s"});
                        rows.add(new String[]{"19:45", "7 metros", "12m 37s"});
                        rows.add(new String[]{"20:44", "5 metros", "11m 28s"});
                        rows.add(new String[]{"16:56", "4 metros", "3m 45s"});
                        rows.add(new String[]{"12:45", "7 metros", "2m 37s"});
                        rows.add(new String[]{"13:24", "3 metros", "2m 18s"});
                        rows.add(new String[]{"11:56", "4 metros", "7m 45s"});
                        rows.add(new String[]{"22:45", "7 metros", "4m 39s"});
                        rows.add(new String[]{"07:14", "5 metros", "3m 19s"});
                        rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
                        rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
                        rows.add(new String[]{"19:16", "4 metros", "3m 46s"});
                        rows.add(new String[]{"12:55", "7 metros", "26m 37s"});
                        rows.add(new String[]{"16:27", "4 metros", "6m 47s"});
                        rows.add(new String[]{"15:40", "7 metros", "9m 27s"});
                        rows.add(new String[]{"17:24", "5 metros", "23m 18s"});
                        rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
                        rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
                        rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
                        rows.add(new String[]{"18:56", "4 metros", "12m 45s"});
                        rows.add(new String[]{"09:45", "7 metros", "22m 38s"});
                        rows.add(new String[]{"08:24", "5 metros", "10m 15s"});
                        rows.add(new String[]{"17:56", "4 metros", "7m 454"});
                        rows.add(new String[]{"19:45", "7 metros", "12m 37s"});
                        rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
                        rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
                        rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
                        rows.add(new String[]{"18:56", "4 metros", "12m 45s"});
                        rows.add(new String[]{"17:56", "4 metros", "7m 454"});
                        rows.add(new String[]{"19:45", "7 metros", "12m 37s"});
                        rows.add(new String[]{"20:44", "5 metros", "11m 28s"});
                        rows.add(new String[]{"19:16", "4 metros", "3m 46s"});
                        rows.add(new String[]{"12:55", "7 metros", "26m 37s"});
                        rows.add(new String[]{"16:36", "4 metros", "4m 45s"});
                        rows.add(new String[]{"13:15", "7 metros", "9m 37s"});
                        rows.add(new String[]{"19:24", "5 metros", "33m 18s"});
                        rows.add(new String[]{"18:56", "4 metros", "12m 45s"});
                        rows.add(new String[]{"13:24", "3 metros", "2m 18s"});
                        rows.add(new String[]{"11:56", "4 metros", "7m 45s"});
                        rows.add(new String[]{"22:45", "7 metros", "4m 39s"});
                        rows.add(new String[]{"07:14", "5 metros", "3m 19s"});
                        rows.add(new String[]{"16:27", "4 metros", "6m 47s"});
                    }
                }
            }
        }
        return rows;
    }

    //Conseguir Nombres del usuario y presentarlo en parte superior de pantalla
    private void getUserInfo(){
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Usuarios Mascarilla Inteligente SS-Mask").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String names = dataSnapshot.child("names").getValue().toString();
                    String lastNames = dataSnapshot.child("lastNames").getValue().toString();

                    mTituloNombre.setText(names +" "+ lastNames);
                }else{
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //Creacion canal para notificacion
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
}