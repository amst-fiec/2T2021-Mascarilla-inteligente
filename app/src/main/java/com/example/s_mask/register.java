package com.example.s_mask;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    private EditText mEditTextNames;
    private EditText mEditTextLastNames;
    private EditText mEditTextEmail;
    private EditText mEditTextUser;
    private EditText mEditTextPassword;
    private EditText mEditTextConfPassword;

    //VARIABLES DE DATOS A REGISTRAR
    private String names = "";
    private String lastNames = "";
    private String email = "";
    private String user = "";
    private String password = "";
    private String confPassword = "";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mEditTextNames = (EditText) findViewById(R.id.et_firstNames);
        mEditTextLastNames = (EditText) findViewById(R.id.et_lastNames);
        mEditTextEmail = (EditText) findViewById(R.id.et_email);
        mEditTextUser = (EditText) findViewById(R.id.et_registerUser);
        mEditTextPassword = (EditText) findViewById(R.id.et_password);
        mEditTextConfPassword = (EditText) findViewById(R.id.et_cPassword);

    }

    public void cancelRegisterAccount(View view) {
        Intent cancelRegisterAccount = new Intent(getBaseContext(), MainActivity.class);
        startActivity(cancelRegisterAccount);
    }

    public void registerAccount(View view){
        names = mEditTextNames.getText().toString();
        lastNames = mEditTextLastNames.getText().toString();
        email = mEditTextEmail.getText().toString();
        user = mEditTextUser.getText().toString();
        password = mEditTextPassword.getText().toString();
        confPassword = mEditTextConfPassword.getText().toString();

        if(!names.isEmpty() && !lastNames.isEmpty() && !email.isEmpty() && !user.isEmpty()
                && !password.isEmpty() && !confPassword.isEmpty()){
            if(password.length() >= 6){
                if(password.equals(confPassword)){
                    registerUser();
                }else{
                    Toast.makeText(register.this, "La contraseña no coincide", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(register.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(register.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String, Object> map = new HashMap<>();
                    Map<String, Object> mapTable = new HashMap<>();

                    int rand = (int)(Math.random()*30 + 1);
                    int cont;
                    for( cont = 1; cont <= rand; cont++){
                        int sec = (int)(Math.random()*59 + 1);
                        int min = (int)(Math.random()*59 + 1);
                        int distance = (int)(Math.random()*8 + 1);
                        int minDt = (int)(Math.random()*59 + 1);
                        int hourDt = (int)(Math.random()*23 + 1);
                        mapTable.put("time ID "+cont, min +"m "+ sec +"s");
                        mapTable.put("distance ID "+cont, distance +" mtrs");
                        mapTable.put("dayTime ID "+cont, hourDt + ":" +minDt);
                    }

                    map.put("names", names);
                    map.put("lastNames", lastNames);
                    map.put("email", email);
                    map.put("user", user);
                    map.put("password", password);
                    map.put("table", mapTable);

                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("Usuarios Mascarilla Inteligente SS-Mask").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                startActivity(new Intent(register.this, dashboard.class));
                                finish();
                            }else{
                                Toast.makeText(register.this, "Hubo un error al crear los datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(register.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}