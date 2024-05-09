package com.bkv.tickets.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    private static final String LOG_TAG = SignInActivity.class.getName();

    private EditText fullNameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText passwordAgainET;
    private TextView errorTV;

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

        fullNameET = findViewById(R.id.fullNameEditText);
        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordAgainET = findViewById(R.id.passwordAgainEditText);
        errorTV = findViewById(R.id.errorTextView);
    }

    public void signInOnClick(View view) {
        errorTV.setText("");

        String fullName = fullNameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        if (fullName.isEmpty()) {
            setError(fullNameET, R.string.error_field_empty, true);
            return;
        }

        if (email.isEmpty()) {
            setError(emailET, R.string.error_field_empty, true);
            return;
        }

        if (password.isEmpty()) {
            setError(passwordET, R.string.error_field_empty, true);
            return;
        }

        if (passwordAgain.isEmpty()) {
            setError(passwordAgainET, R.string.error_field_empty, true);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(emailET, R.string.error_invalid_email, false);
            return;
        }

        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$");
        if (!passwordPattern.matcher(password).matches()) {
            passwordAgainET.setText("");
            setError(passwordET, R.string.error_weak_password, true);
            return;
        }

        if (!password.equals(passwordAgain)) {
            passwordAgainET.setText("");
            setError(passwordET, R.string.error_passwords_dont_match, true);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            Log.i(LOG_TAG, String.format("Registration: %s ; %s", fullName, email));

            if (task.isSuccessful()) {
                Log.d(LOG_TAG, "User created successfully");
                redirectToHome();
                return;
            }

            try {
                throw task.getException();
            } catch (FirebaseAuthWeakPasswordException e) {
                passwordAgainET.setText("");
                setError(passwordET, R.string.error_weak_password, true);
            } catch (FirebaseAuthInvalidCredentialsException e) {
                setError(emailET, R.string.error_invalid_email, false);
            } catch (FirebaseAuthUserCollisionException e) {
                setError(emailET, R.string.error_email_used, false);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
                Toast.makeText(SignInActivity.this, getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cancelOnClick(View view) {
        finish();
    }

    private void redirectToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void setError(EditText editText, int errorCode, boolean clearText) {
        if (clearText) {
            editText.setText("");
        }
        editText.requestFocus();
        errorTV.setText(errorCode);
    }
}