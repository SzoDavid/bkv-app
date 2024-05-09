package com.bkv.tickets.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bkv.tickets.R;
import com.bkv.tickets.Services.PropertiesService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    private EditText emailET;
    private EditText passwordET;
    private TextView errorTV;

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

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            redirectToHome();
            finish();
        }

        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        errorTV = findViewById(R.id.errorTextView);
    }

    public void loginOnClick(View view) {
        errorTV.setText("");

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            Log.d(LOG_TAG, String.format("Logging in: %s ; %s", email, password));

            if (task.isSuccessful()) {
                Log.d(LOG_TAG, "User logged in successfully");
                redirectToHome();
                return;
            }

            try {
                throw task.getException();
            } catch (FirebaseAuthInvalidCredentialsException e) {
                passwordET.setText("");
                passwordET.requestFocus();
                errorTV.setText(R.string.error_invalid_credentials);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
                Toast.makeText(MainActivity.this, getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signInOnClick(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("SECRET_KEY", PropertiesService.getSecretKey());
        startActivity(intent);
    }

    private void redirectToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


}