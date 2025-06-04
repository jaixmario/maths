package com.jai.mario.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jai.mario.GeminiActivity;
import com.jai.mario.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TrigFragment extends Fragment {

    TextInputEditText inputAngle;
    MaterialButton sinButton, cosButton, tanButton, advancedButton, askAiButton;
    TextView resultText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trig, container, false);

        inputAngle = view.findViewById(R.id.inputAngle);
        sinButton = view.findViewById(R.id.sinButton);
        cosButton = view.findViewById(R.id.cosButton);
        tanButton = view.findViewById(R.id.tanButton);
        advancedButton = view.findViewById(R.id.advancedButton);
        askAiButton = view.findViewById(R.id.askAiButton);
        resultText = view.findViewById(R.id.resultText);

        sinButton.setOnClickListener(v -> calculateTrig("sin"));
        cosButton.setOnClickListener(v -> calculateTrig("cos"));
        tanButton.setOnClickListener(v -> calculateTrig("tan"));

        advancedButton.setOnClickListener(v -> {
            String angleStr = inputAngle.getText().toString().trim();
            if (angleStr.isEmpty()) {
                Toast.makeText(getContext(), "Enter angle", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                double degrees = Double.parseDouble(angleStr);
                double radians = Math.toRadians(degrees);
                String steps = "Convert degrees to radians:\n" +
                        degrees + "° × π/180 = " + String.format("%.5f", radians) + " rad";
                resultText.setText(steps);
                askAiButton.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });

        askAiButton.setOnClickListener(v -> {
            String result = resultText.getText().toString().trim();
            if (!result.isEmpty()) {
                String prompt = "Explain how this trigonometric result is calculated:\n" + result;
                Intent intent = new Intent(getContext(), GeminiActivity.class);
                intent.putExtra("prompt", prompt);
                startActivity(intent);
            }
        });

        resultText.setOnClickListener(v -> {
            String text = resultText.getText().toString().trim();
            if (!text.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Trig Result", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void calculateTrig(String type) {
        askAiButton.setVisibility(View.GONE);
        String angleStr = inputAngle.getText().toString().trim();
        if (angleStr.isEmpty()) {
            Toast.makeText(getContext(), "Enter angle in degrees", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double degrees = Double.parseDouble(angleStr);
            double radians = Math.toRadians(degrees);
            double result;

            switch (type) {
                case "sin":
                    result = Math.sin(radians);
                    resultText.setText("sin(" + degrees + "°) = " + String.format("%.5f", result));
                    break;
                case "cos":
                    result = Math.cos(radians);
                    resultText.setText("cos(" + degrees + "°) = " + String.format("%.5f", result));
                    break;
                case "tan":
                    result = Math.tan(radians);
                    resultText.setText("tan(" + degrees + "°) = " + String.format("%.5f", result));
                    break;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }
}