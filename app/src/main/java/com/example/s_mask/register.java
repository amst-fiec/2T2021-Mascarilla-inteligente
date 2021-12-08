package com.example.s_mask;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void cancelRegisterAccount(View view) {
        Intent cancelRegisterAccount = new Intent(getBaseContext(), MainActivity.class);
        startActivity(cancelRegisterAccount);
    }
}