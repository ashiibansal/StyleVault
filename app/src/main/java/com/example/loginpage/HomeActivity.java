package com.example.loginpage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button  chatbotButton, bodyMeasurementButton, moodBoardButton;
    ImageView userIcon;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        chatbotButton = findViewById(R.id.chatbotButton);
        bodyMeasurementButton = findViewById(R.id.bodyMeasurementButton);
        moodBoardButton = findViewById(R.id.moodBoardButton);
        userIcon = findViewById(R.id.userIcon);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);



        chatbotButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ComingSoonActivity.class));
        });

        bodyMeasurementButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, BodyMeasurementActivity.class));
        });

        moodBoardButton.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MoodBoardActivity.class));
        });

        userIcon.setOnClickListener(v -> showUserInfoDialog());
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
        finish();
    }

    private void showUserInfoDialog() {
        String email = sharedPreferences.getString("email", "N/A");
        String username = sharedPreferences.getString("username", "N/A");

        new AlertDialog.Builder(this)
                .setTitle("User Info")
                .setMessage("Username: " + username + "\nEmail: " + email)
                .setPositiveButton("Logout", (dialog, which) -> logout())
                .setNegativeButton("Close", null)
                .show();
    }
}
