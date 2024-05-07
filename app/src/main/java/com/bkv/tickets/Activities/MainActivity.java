package com.bkv.tickets.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bkv.tickets.R;
import com.bkv.tickets.Services.PropertiesService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    private EditText emailET;
    private EditText passwordET;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);
    }

    public void loginOnClick(View view) {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i(LOG_TAG, String.format("Bejelentkezett: %s ; %s", email, password));

                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "User logged in successfully");
                    redirectToReservations();
                    return;
                }

                Log.d(LOG_TAG, "User auth error");
                Toast.makeText(MainActivity.this, "User auth error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signInOnClick(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("SECRET_KEY", PropertiesService.getSecretKey());
        startActivity(intent);
    }

    private void redirectToReservations() {
        Intent intent = new Intent(this, ReservationsActivity.class);
        startActivity(intent);
    }


}