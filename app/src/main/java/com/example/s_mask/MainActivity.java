package com.example.s_mask;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonLogin;

    private String email = "";
    private String password = "";

    DatabaseReference mGDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mGDatabase = FirebaseDatabase.getInstance().getReference();

        mEditTextEmail = (EditText) findViewById(R.id.et_emailLogin);
        mEditTextPassword = (EditText) findViewById(R.id.et_passwordLogin);
        mButtonLogin = (Button) findViewById(R.id.btn_login);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditTextEmail.getText().toString();
                password = mEditTextPassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()){
                    loginUser();
                }else{
                    Toast.makeText(MainActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, dashboard.class));
            finish();
        }
    }

    private void loginUser(){
        pruebaConexion = isOnline(this);
        if(pruebaConexion == true){
            if(!email.isEmpty() && !email.isEmpty()){
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> tasklogin) {
                        if(tasklogin.isSuccessful()){
                            startActivity(new Intent(MainActivity.this, dashboard.class));
                            finish();
                        }else{
                            Toast.makeText(MainActivity.this, "Por favor compruebe que sus " +
                                    "credenciales sean correctas", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                Toast.makeText(MainActivity.this, "Por favor ingrese su usuario y contraseña", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "No hay conexión a internet", Toast.LENGTH_SHORT);
            toast1.show();
        }

    }

    boolean pruebaConexion = false;

    private static ConnectivityManager manager;

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }


    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new
            ActivityResultContracts.StartActivityForResult(), new
            ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            if (account != null) {
                                firebaseAuthWithGoogle(account);
                                Intent dashboard = new Intent(getBaseContext(), dashboard.class);
                                startActivity(dashboard);
                            }
                        } catch (ApiException e) {
                            Log.w("TAG", "Falló el inicio de sesión con google", e);
                            Toast toast2 =
                                    Toast.makeText(getApplicationContext(),
                                            "Falló el inicio de sesión con google, inténtelo de nuevo más tarde", Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                    }
                }
    });

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
            Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),
                null);
            mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    } else {
                    System.out.println("Error en el inicio de sesión");
                    updateUI(null);
                    }
                });
            }

    private void updateUI(FirebaseUser user) {
            if (user != null) {
                String name = user.getDisplayName();
                String email = user.getEmail();
                String photo = String.valueOf(user.getPhotoUrl());
                } else {
                System.out.println("Sin registrarse");
                }
    }

    public void login(View view) {
        pruebaConexion = isOnline(this);
        if(pruebaConexion == true){
            resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
        }
        else{
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "No hay conexión a internet", Toast.LENGTH_SHORT);
            toast1.show();
        }
    }

    public void register(View view) {
        Intent register = new Intent(getBaseContext(), register.class);
        startActivity(register);
    }

}