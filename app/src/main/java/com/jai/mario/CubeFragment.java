package com.jai.mario.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jai.mario.GeminiActivity;
import com.jai.mario.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CubeFragment extends Fragment {

    TextInputEditText inputCube, inputCubeRoot;
    MaterialButton normalButton, advancedButton, askAiButton;
    TextView resultText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cube, container, false);

        inputCube = view.findViewById(R.id.inputCube);
        inputCubeRoot = view.findViewById(R.id.inputCubeRoot);
        normalButton = view.findViewById(R.id.normalButton);
        advancedButton = view.findViewById(R.id.advancedButton);
        askAiButton = view.findViewById(R.id.askAiButton);
        resultText = view.findViewById(R.id.resultText);

        inputCube.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) inputCubeRoot.setText("");
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        inputCubeRoot.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) inputCube.setText("");
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        normalButton.setOnClickListener(view1 -> {
            askAiButton.setVisibility(View.GONE); // Hide AI button on normal
            String cubeText = inputCube.getText().toString().trim();
            String rootText = inputCubeRoot.getText().toString().trim();

            if (!cubeText.isEmpty()) {
                try {
                    int number = Integer.parseInt(cubeText);
                    int cube = number * number * number;
                    resultText.setText(number + "³ = " + cube);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid number for cube", Toast.LENGTH_SHORT).show();
                }
            } else if (!rootText.isEmpty()) {
                try {
                    int number = Integer.parseInt(rootText);
                    double root = Math.cbrt(number);
                    int rounded = (int) root;
                    if (rounded * rounded * rounded == number) {
                        resultText.setText(number + " = " + rounded + "³");
                    } else {
                        resultText.setText(number + " is not a perfect cube");
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid number for cube root", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please enter a value", Toast.LENGTH_SHORT).show();
            }
        });

        advancedButton.setOnClickListener(view12 -> {
            askAiButton.setVisibility(View.GONE);
            String cubeText = inputCube.getText().toString().trim();
            String rootText = inputCubeRoot.getText().toString().trim();

            if (!cubeText.isEmpty()) {
                try {
                    int number = Integer.parseInt(cubeText);
                    int cube = number * number * number;
                    String steps = number + "³ = " + number + " × " + number + " × " + number + " = " + cube;
                    resultText.setText(steps);
                    askAiButton.setVisibility(View.VISIBLE); // Show Ask AI
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid number for cube", Toast.LENGTH_SHORT).show();
                }
            } else if (!rootText.isEmpty()) {
                try {
                    int number = Integer.parseInt(rootText);
                    double root = Math.cbrt(number);
                    int rounded = (int) root;
                    if (rounded * rounded * rounded == number) {
                        String steps = "∛" + number + " = " + rounded + "\nBecause " + rounded + "³ = " + number;
                        resultText.setText(steps);
                    } else {
                        resultText.setText(number + " is not a perfect cube");
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid number for cube root", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please enter a value", Toast.LENGTH_SHORT).show();
            }
        });

        askAiButton.setOnClickListener(v -> {
            String result = resultText.getText().toString().trim();
            if (!result.isEmpty()) {
                String prompt = "Please explain step-by-step how this result was calculated as a cube operation.\n\nResult:\n" + result;
                Intent intent = new Intent(getContext(), GeminiActivity.class);
                intent.putExtra("prompt", prompt);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "No result to send to AI", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}