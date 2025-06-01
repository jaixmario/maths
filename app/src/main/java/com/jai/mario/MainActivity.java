package com.jai.mario;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextInputEditText inputNumber, inputRoot;
    MaterialButton calculateButton;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputNumber = findViewById(R.id.inputNumber);
        inputRoot = findViewById(R.id.inputRoot);
        calculateButton = findViewById(R.id.calculateButton);
        resultText = findViewById(R.id.resultText);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String squareText = inputNumber.getText().toString().trim();
                String rootText = inputRoot.getText().toString().trim();

                if (!squareText.isEmpty()) {
                    try {
                        int number = Integer.parseInt(squareText);
                        int square = number * number;
                        resultText.setText(number + "² = " + square);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Invalid number for square", Toast.LENGTH_SHORT).show();
                    }
                } else if (!rootText.isEmpty()) {
                    try {
                        int number = Integer.parseInt(rootText);
                        double root = Math.sqrt(number);
                        int rounded = (int) root;
                        if (rounded * rounded == number) {
                            resultText.setText(number + " = " + rounded + "²");
                        } else {
                            resultText.setText(number + " is not a perfect square");
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Invalid number for root", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}