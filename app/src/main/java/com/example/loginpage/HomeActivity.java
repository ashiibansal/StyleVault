package com.example.loginpage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button logoutButton, chatbotButton, bodyMeasurementButton, moodBoardButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize buttons
        logoutButton = findViewById(R.id.logoutButton);
        chatbotButton = findViewById(R.id.chatbotButton);
        bodyMeasurementButton = findViewById(R.id.bodyMeasurementButton);
        moodBoardButton = findViewById(R.id.moodBoardButton);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Logout functionality
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Chatbot - Coming Soon
        chatbotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ComingSoonActivity.class);
                startActivity(intent);
            }
        });

        // Body Measurements
        bodyMeasurementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BodyMeasurementActivity.class);
                startActivity(intent);
            }
        });

        // Pinterest Mood Board
        moodBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MoodBoardActivity.class);
                startActivity(intent);
            }
        });
    }
}
