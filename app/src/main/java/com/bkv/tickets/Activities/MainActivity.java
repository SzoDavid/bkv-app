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

import com.bkv.tickets.Models.User;
import com.bkv.tickets.R;
import com.bkv.tickets.Services.FirestoreServices.FirestoreUserService;
import com.bkv.tickets.Services.Interfaces.IUserService;
import com.bkv.tickets.Services.PropertiesService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();

    private EditText emailET;
    private EditText passwordET;
    private TextView errorTV;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private IUserService userService;

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

        db = FirebaseFirestore.getInstance();

        userService = new FirestoreUserService(db);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            userService.getByAuthId(FirebaseAuth.getInstance().getCurrentUser().getUid(), this::getByAuthIdCallback);
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

                userService.getByAuthId(FirebaseAuth.getInstance().getCurrentUser().getUid(), this::getByAuthIdCallback);
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

    public void getByAuthIdCallback(Task<User> task) {
        if (task.isSuccessful()) {
            redirectToHome(task.getResult());
            return;
        }

        Exception exception = task.getException();
        if (exception == null || exception.getMessage() == null) {
            exception = new Exception("Failed to query user");
        }
        Log.e(LOG_TAG, exception.getMessage());
        Toast.makeText(MainActivity.this, getString(R.string.error_unexpected), Toast.LENGTH_SHORT).show();
    }

    public void signInOnClick(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("SECRET_KEY", PropertiesService.getSecretKey());
        startActivity(intent);
    }

    private void redirectToHome(User currentUser) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("USER_ID", currentUser.getId());
        intent.putExtra("USER_AUTH_ID", currentUser.getAuthId());
        intent.putExtra("USER_EMAIL", currentUser.getEmail());
        intent.putExtra("USER_NAME", currentUser.getName());
        startActivity(intent);
    }
}