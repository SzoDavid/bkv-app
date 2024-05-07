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

public class SignInActivity extends AppCompatActivity {
    private static final String LOG_TAG = SignInActivity.class.getName();

    private EditText usernameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText passwordAgainET;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String key = getIntent().getStringExtra("SECRET_KEY");
        if (!PropertiesService.getSecretKey().equals(key)) {
            finish();
        }

        mAuth = FirebaseAuth.getInstance();

        usernameET = findViewById(R.id.usernameEditText);
        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordAgainET = findViewById(R.id.passwordAgainEditText);
    }

    public void signInOnClick(View view) {
        String username = usernameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        if (!password.equals(passwordAgain)) {
            Log.e(LOG_TAG, "A ket jelszo nem egyezik");
            return;
        }

        // TODO: validate

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i(LOG_TAG, String.format("Regisztrált: %s ; %s ; %s ; %s", username, email, password, passwordAgain));

                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "User created successfully");
                    redirectToReservations();
                    return;
                }

                Log.d(LOG_TAG, "User creation error");
                Toast.makeText(SignInActivity.this, "User creation error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void cancelOnClick(View view) {
        finish();
    }

    private void redirectToReservations() {
        Intent intent = new Intent(this, ReservationsActivity.class);
        intent.putExtra("SECRET_KEY", PropertiesService.getSecretKey());
        startActivity(intent);
    }
}