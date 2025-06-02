package com.jai.mario.fragments;

import android.os.Bundle;
import android.text.TextUtils;
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

public class LcmHcfFragment extends Fragment {

    TextInputEditText inputA, inputB;
    MaterialButton normalBtn, advancedBtn;
    TextView resultText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lcm_hcf, container, false);

        inputA = view.findViewById(R.id.inputA);
        inputB = view.findViewById(R.id.inputB);
        normalBtn = view.findViewById(R.id.normalBtn);
        advancedBtn = view.findViewById(R.id.advancedBtn);
        resultText = view.findViewById(R.id.resultText);

        normalBtn.setOnClickListener(v -> {
            String aText = inputA.getText().toString().trim();
            String bText = inputB.getText().toString().trim();

            if (TextUtils.isEmpty(aText) || TextUtils.isEmpty(bText)) {
                Toast.makeText(getContext(), "Enter both numbers", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int a = Integer.parseInt(aText);
                int b = Integer.parseInt(bText);
                int hcf = calculateHCF(a, b);
                int lcm = (a * b) / hcf;

                String result = "HCF(" + a + ", " + b + ") = " + hcf + "\nLCM(" + a + ", " + b + ") = " + lcm;
                resultText.setText(result);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });

        advancedBtn.setOnClickListener(v -> {
            String aText = inputA.getText().toString().trim();
            String bText = inputB.getText().toString().trim();

            if (TextUtils.isEmpty(aText) || TextUtils.isEmpty(bText)) {
                Toast.makeText(getContext(), "Enter both numbers", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int a = Integer.parseInt(aText);
                int b = Integer.parseInt(bText);
                int originalA = a, originalB = b;

                StringBuilder steps = new StringBuilder("Euclidean Method:\n");

                while (b != 0) {
                    steps.append("HCF(").append(a).append(", ").append(b).append(") → ");
                    int temp = b;
                    b = a % b;
                    a = temp;
                    steps.append("Now HCF(").append(a).append(", ").append(b).append(")\n");
                }

                int hcf = Math.abs(a);
                int lcm = (originalA * originalB) / hcf;

                steps.append("\nFinal HCF = ").append(hcf);
                steps.append("\nLCM = (").append(originalA).append(" × ").append(originalB).append(") / ").append(hcf)
                     .append(" = ").append(lcm);

                resultText.setText(steps.toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private int calculateHCF(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }
}