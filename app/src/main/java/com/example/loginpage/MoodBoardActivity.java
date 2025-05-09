package com.example.loginpage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MoodBoardActivity extends AppCompatActivity {

    Button smartCasualButton, businessAttireButton, streetwearButton, athleisureButton;
    Button minimalistButton, vintageButton, edgyButton, weekendRelaxedButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_board);

        smartCasualButton = findViewById(R.id.smartCasualButton);
        businessAttireButton = findViewById(R.id.businessAttireButton);
        streetwearButton = findViewById(R.id.streetwearButton);
        athleisureButton = findViewById(R.id.athleisureButton);
        minimalistButton = findViewById(R.id.minimalistButton);
        vintageButton = findViewById(R.id.vintageButton);
        edgyButton = findViewById(R.id.edgyButton);
        weekendRelaxedButton = findViewById(R.id.weekendRelaxedButton);
        backButton = findViewById(R.id.backButton);

        smartCasualButton.setOnClickListener(v -> openPinterest("smart casual men's outfits"));
        businessAttireButton.setOnClickListener(v -> openPinterest("business attire men"));
        streetwearButton.setOnClickListener(v -> openPinterest("modern streetwear outfits for men"));
        athleisureButton.setOnClickListener(v -> openPinterest("athleisure fashion for men"));
        minimalistButton.setOnClickListener(v -> openPinterest("minimalist men's fashion"));
        vintageButton.setOnClickListener(v -> openPinterest("vintage aesthetic outfits for men"));
        edgyButton.setOnClickListener(v -> openPinterest("edgy fashion styles for men"));
        weekendRelaxedButton.setOnClickListener(v -> openPinterest("weekend relaxed outfits for men"));

        backButton.setOnClickListener(v -> finish()); // Close activity and go back
    }

    private void openPinterest(String query) {
        try {
            String url = "https://www.pinterest.com/search/pins/?q=" + Uri.encode(query);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setPackage("com.pinterest"); // Open Pinterest app if installed
            startActivity(intent);
        } catch (Exception e) {
            String webUrl = "https://www.pinterest.com/search/pins/?q=" + Uri.encode(query);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
            startActivity(webIntent);
        }
    }
}
