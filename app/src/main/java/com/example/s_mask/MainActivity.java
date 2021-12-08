package com.example.s_mask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
                            Log.w("TAG", "Falló el inicio de sesión con google.", e);
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
                    System.out.println("error");
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
                } }

    public void login(View view) {
        //Intent dashboard = new Intent(getBaseContext(), dashboard.class);
        //startActivity(dashboard);
        resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
    }

    public void register(View view) {
        Intent register = new Intent(getBaseContext(), register.class);
        startActivity(register);
    }

}