package com.jai.mario.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jai.mario.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SquareFragment extends Fragment {

    TextInputEditText inputNumber, inputRoot;
    MaterialButton normalButton, advancedButton;
    TextView resultText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_square, container, false);

        inputNumber = view.findViewById(R.id.inputNumber);
        inputRoot = view.findViewById(R.id.inputRoot);
        normalButton = view.findViewById(R.id.normalButton);
        advancedButton = view.findViewById(R.id.advancedButton);
        resultText = view.findViewById(R.id.resultText);

        inputNumber.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) inputRoot.setText("");
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        inputRoot.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) inputNumber.setText("");
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        normalButton.setOnClickListener(view1 -> {
            String squareText = inputNumber.getText().toString().trim();
            String rootText = inputRoot.getText().toString().trim();

            if (!squareText.isEmpty()) {
                try {
                    int number = Integer.parseInt(squareText);
                    int square = number * number;
                    resultText.setText(number + "² = " + square);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid number for square", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Invalid number for root", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please enter a value", Toast.LENGTH_SHORT).show();
            }
        });

        advancedButton.setOnClickListener(view12 -> {
            String squareText = inputNumber.getText().toString().trim();
            String rootText = inputRoot.getText().toString().trim();

            if (!squareText.isEmpty()) {
                try {
                    int number = Integer.parseInt(squareText);
                    int tens = number / 10;
                    int units = number % 10;
                    int step1 = tens * 10;
                    int a2 = step1 * step1;
                    int ab2 = 2 * step1 * units;
                    int b2 = units * units;
                    int total = a2 + ab2 + b2;

                    String steps = number + "² = (" + step1 + " + " + units + ")² =\n"
                            + step1 + "² + 2×" + step1 + "×" + units + " + " + units + "²\n"
                            + "     = " + a2 + " + " + ab2 + " + " + b2 + " = " + total;

                    resultText.setText(steps);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid number for square", Toast.LENGTH_SHORT).show();
                }
            } else if (!rootText.isEmpty()) {
                try {
                    int number = Integer.parseInt(rootText);
                    double root = Math.sqrt(number);
                    int rounded = (int) root;
                    if (rounded * rounded == number) {
                        String steps = "√" + number + " = " + rounded + "\nBecause " + rounded + "² = " + number;
                        resultText.setText(steps);
                    } else {
                        resultText.setText(number + " is not a perfect square");
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid number for root", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please enter a value", Toast.LENGTH_SHORT).show();
            }
        });

        resultText.setOnClickListener(v -> {
            String text = resultText.getText().toString().trim();
            if (!text.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Result", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}