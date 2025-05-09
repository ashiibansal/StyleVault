package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button loginButton, signupButton;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        databaseHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(view -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (!isValidUsername(user)) {
                Toast.makeText(MainActivity.this, "Invalid username!", Toast.LENGTH_SHORT).show();
            } else if (!isValidPassword(pass)) {
                Toast.makeText(MainActivity.this, "Invalid password!", Toast.LENGTH_SHORT).show();
            } else if (databaseHelper.validateUser(user, pass)) {
                // Fetch email
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT email FROM users WHERE username=?", new String[]{user});
                String email = "";
                if (cursor.moveToFirst()) {
                    email = cursor.getString(cursor.getColumnIndex("email"));
                }
                cursor.close();

                // Save to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("username", user);
                editor.putString("email", email);
                editor.apply();

                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
            }
        });

        signupButton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
        });
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]{3,}$");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[@#$%^&+=!].*");
    }
}
