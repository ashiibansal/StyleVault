package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText username, email, password, confirmPassword;
    Button signupButton, backToLoginButton;
    DatabaseHelper databaseHelper; // Database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        username = findViewById(R.id.signupUsername);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        signupButton = findViewById(R.id.signupButton);
        backToLoginButton = findViewById(R.id.backToLoginButton);

        // Initialize database
        databaseHelper = new DatabaseHelper(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                if (!isValidUsername(user)) {
                    Toast.makeText(SignupActivity.this, "Username must be at least 3 characters and contain only letters and numbers!", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(userEmail)) {
                    Toast.makeText(SignupActivity.this, "Enter a valid email address!", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(pass)) {
                    Toast.makeText(SignupActivity.this, "Password must be at least 6 characters, include an uppercase letter, a lowercase letter, a digit, and a special character!", Toast.LENGTH_LONG).show();
                } else if (!pass.equals(confirmPass)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isRegistered = databaseHelper.registerUser(user, userEmail, pass);
                    if (isRegistered) {
                        Toast.makeText(SignupActivity.this, "Signup Successful! Please log in.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]{3,}$");
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[@#$%^&+=!].*");
    }
}
