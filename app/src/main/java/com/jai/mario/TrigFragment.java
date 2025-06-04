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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class TrigFragment extends Fragment {

    TextInputEditText inputAngle;
    MaterialButton sinButton, cosButton, tanButton,
                   secButton, cosecButton, cotButton,
                   advancedButton, askAiButton;
    TextView resultText;

    private MaterialButton selectedButton = null;
    private String lastTrigFunction = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trig, container, false);

        inputAngle = view.findViewById(R.id.inputAngle);
        sinButton = view.findViewById(R.id.sinButton);
        cosButton = view.findViewById(R.id.cosButton);
        tanButton = view.findViewById(R.id.tanButton);
        secButton = view.findViewById(R.id.secButton);
        cosecButton = view.findViewById(R.id.cosecButton);
        cotButton = view.findViewById(R.id.cotButton);
        advancedButton = view.findViewById(R.id.advancedButton);
        askAiButton = view.findViewById(R.id.askAiButton);
        resultText = view.findViewById(R.id.resultText);

        sinButton.setOnClickListener(v -> {
            setSelectedTrig("sin", sinButton);
            calculateTrig("sin");
        });
        cosButton.setOnClickListener(v -> {
            setSelectedTrig("cos", cosButton);
            calculateTrig("cos");
        });
        tanButton.setOnClickListener(v -> {
            setSelectedTrig("tan", tanButton);
            calculateTrig("tan");
        });
        secButton.setOnClickListener(v -> {
            setSelectedTrig("sec", secButton);
            calculateTrig("sec");
        });
        cosecButton.setOnClickListener(v -> {
            setSelectedTrig("cosec", cosecButton);
            calculateTrig("cosec");
        });
        cotButton.setOnClickListener(v -> {
            setSelectedTrig("cot", cotButton);
            calculateTrig("cot");
        });

        advancedButton.setOnClickListener(v -> showAdvanced());

        askAiButton.setOnClickListener(v -> {
            String result = resultText.getText().toString().trim();
            if (!result.isEmpty()) {
                String prompt = "Explain the trigonometric result below with formulas and identities:\n\n" + result;
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

    private void setSelectedTrig(String function, MaterialButton button) {
        lastTrigFunction = function;
        if (selectedButton != null) {
            selectedButton.setStrokeWidth(0);
        }
        selectedButton = button;
        selectedButton.setStrokeWidth(6);
        selectedButton.setStrokeColor(ContextCompat.getColorStateList(requireContext(), R.color.white));
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
            String display;

            switch (type) {
                case "sin":
                    result = Math.sin(radians);
                    display = "sin(" + degrees + "°) = " + format(result);
                    break;
                case "cos":
                    result = Math.cos(radians);
                    display = "cos(" + degrees + "°) = " + format(result);
                    break;
                case "tan":
                    double cosCheck = Math.cos(radians);
                    if (isAlmostZero(cosCheck)) {
                        display = "tan(" + degrees + "°) is undefined (cos = 0)";
                    } else {
                        result = Math.tan(radians);
                        display = "tan(" + degrees + "°) = " + format(result);
                    }
                    break;
                case "sec":
                    double cos = Math.cos(radians);
                    if (isAlmostZero(cos)) {
                        display = "sec(" + degrees + "°) is undefined (cos = 0)";
                    } else {
                        result = 1 / cos;
                        display = "sec(" + degrees + "°) = 1 / cos(" + degrees + ") = " + format(result);
                    }
                    break;
                case "cosec":
                    double sin = Math.sin(radians);
                    if (isAlmostZero(sin)) {
                        display = "cosec(" + degrees + "°) is undefined (sin = 0)";
                    } else {
                        result = 1 / sin;
                        display = "cosec(" + degrees + "°) = 1 / sin(" + degrees + ") = " + format(result);
                    }
                    break;
                case "cot":
                    double tan = Math.tan(radians);
                    if (isAlmostZero(tan)) {
                        display = "cot(" + degrees + "°) is undefined (tan = 0)";
                    } else {
                        result = 1 / tan;
                        display = "cot(" + degrees + "°) = 1 / tan(" + degrees + ") = " + format(result);
                    }
                    break;
                default:
                    display = "Unknown function";
            }

            resultText.setText(display);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAdvanced() {
        if (lastTrigFunction == null) {
            Toast.makeText(getContext(), "Select a function first", Toast.LENGTH_SHORT).show();
            return;
        }
        calculateTrig(lastTrigFunction);
    }

    private boolean isAlmostZero(double value) {
        return Math.abs(value) < 1e-10;
    }

    private String format(double value) {
        return String.format("%.5f", value);
    }
}
