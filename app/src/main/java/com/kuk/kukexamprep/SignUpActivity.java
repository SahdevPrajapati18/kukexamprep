package com.kuk.kukexamprep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    EditText emailInput, passwordInput, usernameInput;
    Button signupBtn, signinRedirectBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        usernameInput = findViewById(R.id.username);
        signupBtn = findViewById(R.id.signupBtn);
        signinRedirectBtn = findViewById(R.id.signinRedirectBtn);

        mAuth = FirebaseAuth.getInstance();

        signupBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String username = usernameInput.getText().toString().trim();

            if (username.isEmpty()) {
                usernameInput.setError("Username is required");
                usernameInput.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter a valid email");
                emailInput.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordInput.setError("Password must be at least 6 characters");
                passwordInput.requestFocus();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();

                        user.updateProfile(profileUpdates).addOnCompleteListener(profileTask -> {
                            if (profileTask.isSuccessful()) {
                                user.reload().addOnCompleteListener(reloadTask -> {
                                    Toast.makeText(SignUpActivity.this, "Signup successful! Please log in.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                    finish();
                                });
                            } else {
                                Toast.makeText(SignUpActivity.this, "Failed to set display name", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        signinRedirectBtn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }
}
