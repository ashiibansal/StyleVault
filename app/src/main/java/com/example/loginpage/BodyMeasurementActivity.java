package com.example.loginpage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BodyMeasurementActivity extends AppCompatActivity {

    EditText heightInput, weightInput, shoulderInput, chestInput, waistInput;
    Button calculateBodyTypeButton, backButton, viewLastMeasurementButton;
    TextView resultText;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_measurement);

        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
        shoulderInput = findViewById(R.id.shoulderInput);
        chestInput = findViewById(R.id.chestInput);
        waistInput = findViewById(R.id.waistInput);
        calculateBodyTypeButton = findViewById(R.id.calculateBodyTypeButton);
        backButton = findViewById(R.id.backButton);
        resultText = findViewById(R.id.resultText);
        viewLastMeasurementButton = findViewById(R.id.viewLastMeasurementButton);  // New button for viewing last input

        dbHelper = new DatabaseHelper(this);

        calculateBodyTypeButton.setOnClickListener(v -> {
            String heightStr = heightInput.getText().toString().trim();
            String weightStr = weightInput.getText().toString().trim();
            String shoulderStr = shoulderInput.getText().toString().trim();
            String chestStr = chestInput.getText().toString().trim();
            String waistStr = waistInput.getText().toString().trim();

            if (heightStr.isEmpty() || weightStr.isEmpty() || shoulderStr.isEmpty() || chestStr.isEmpty() || waistStr.isEmpty()) {
                Toast.makeText(BodyMeasurementActivity.this, "Please enter all values", Toast.LENGTH_SHORT).show();
                return;
            }

            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);
            double shoulder = Double.parseDouble(shoulderStr);
            double chest = Double.parseDouble(chestStr);
            double waist = Double.parseDouble(waistStr);

            String bodyType = determineBodyType(height, weight, shoulder, chest, waist);
            String recommendations = getStyleRecommendations(bodyType);

            resultText.setText("Your Body Type: " + bodyType + "\n\n" + recommendations);

            // Save the measurements in the database
            dbHelper.saveBodyMeasurement("user@example.com", height, weight, shoulder, chest, waist, bodyType); // Replace with actual email
        });

        backButton.setOnClickListener(v -> finish());

        // View last body measurement button
        viewLastMeasurementButton.setOnClickListener(v -> {
            Cursor cursor = dbHelper.getLastBodyMeasurement("user@example.com"); // Replace with actual email
            if (cursor != null && cursor.moveToFirst()) {
                String lastHeight = cursor.getString(cursor.getColumnIndex("height"));
                String lastWeight = cursor.getString(cursor.getColumnIndex("weight"));
                String lastBodyType = cursor.getString(cursor.getColumnIndex("body_type"));
                resultText.setText("Last Body Measurements:\nHeight: " + lastHeight + "\nWeight: " + lastWeight + "\nBody Type: " + lastBodyType);
            } else {
                Toast.makeText(this, "No previous measurements found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String determineBodyType(double height, double weight, double shoulder, double chest, double waist) {
        double bmi = weight / ((height / 100) * (height / 100));
        double waistToHeightRatio = waist / height;
        double shoulderToWaistRatio = shoulder / waist;

        if (bmi < 18.5 && shoulderToWaistRatio <= 1.2) {
            return "Ectomorph (Lean, Slender)";
        } else if (bmi >= 18.5 && bmi < 24.9 && shoulderToWaistRatio > 1.2 && waist < chest) {
            return "Mesomorph (Athletic, Broad Shoulders)";
        } else if (bmi >= 25 && waist > chest) {
            return "Endomorph (Broad, Stocky)";
        } else if (Math.abs(shoulder - waist) < 7 && Math.abs(chest - waist) < 7) {
            return "Rectangle (Evenly Proportioned)";
        } else if (shoulder > chest + 5 && chest > waist + 10) {
            return "Inverted Triangle (V-Shaped)";
        } else if (waistToHeightRatio > 0.55) {
            return "Oval (Round, Larger Midsection)";
        } else {
            return "Balanced (Mixed Proportions)";
        }
    }

    private String getStyleRecommendations(String bodyType) {
        switch (bodyType) {
            case "Ectomorph (Lean, Slender)":
                return "Recommended: Layered outfits, structured blazers, slim-fit trousers.";
            case "Mesomorph (Athletic, Broad Shoulders)":
                return "Recommended: Fitted clothes, V-neck shirts, well-tailored suits.";
            case "Endomorph (Broad, Stocky)":
                return "Recommended: Dark colors, vertical stripes, single-breasted blazers.";
            case "Rectangle (Evenly Proportioned)":
                return "Recommended: Structured jackets, patterns, and layering to add definition.";
            case "Inverted Triangle (V-Shaped)":
                return "Recommended: Straight-cut pants, subtle patterns, balanced proportions.";
            case "Oval (Round, Larger Midsection)":
                return "Recommended: V-neck shirts, dark colors, and clothes that elongate the silhouette.";
            case "Balanced (Mixed Proportions)":
                return "Recommended: Experiment with fits, patterns, and layering to find your best look.";
            default:
                return "Recommended: Wear well-fitted clothing to enhance your look.";
        }
    }
}
