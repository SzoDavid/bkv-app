package com.bkv.tickets.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bkv.tickets.R;
import com.bkv.tickets.Services.PropertiesService;

public class SignInActivity extends AppCompatActivity {
    private static final String LOG_TAG = SignInActivity.class.getName();

    private EditText usernameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText passwordAgainET;

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

        Log.i(LOG_TAG, String.format("Regisztrált: %s ; %s ; %s ; %s", username, email, password, passwordAgain));
        // TODO: registration

        redirectToHome();
    }

    public void cancelOnClick(View view) {
        finish();
    }

    private void redirectToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("SECRET_KEY", PropertiesService.getSecretKey());
        startActivity(intent);
    }
}