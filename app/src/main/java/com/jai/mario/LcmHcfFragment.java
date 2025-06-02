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
    MaterialButton calculateBtn;
    TextView resultText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lcm_hcf, container, false);

        inputA = view.findViewById(R.id.inputA);
        inputB = view.findViewById(R.id.inputB);
        calculateBtn = view.findViewById(R.id.calculateBtn);
        resultText = view.findViewById(R.id.resultText);

        calculateBtn.setOnClickListener(v -> {
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