package com.example.loginpage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BodyMeasurementActivity extends AppCompatActivity {

    EditText heightInput, weightInput, shoulderInput, chestInput, waistInput;
    Button calculateBodyTypeButton, backButton;
    TextView resultText;

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

        calculateBodyTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String determineBodyType(double height, double weight, double shoulder, double chest, double waist) {
        double bmi = weight / ((height / 100) * (height / 100));

        if (bmi < 18.5 && waist < chest - 10) {
            return "Ectomorph (Lean, Slender)";
        } else if (bmi >= 18.5 && bmi < 24.9 && shoulder > waist + 10) {
            return "Mesomorph (Athletic, Broad Shoulders)";
        } else {
            return "Endomorph (Broad, Stocky)";
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
            default:
                return "Recommended: Wear well-fitted clothing to enhance your look.";
        }
    }
}
