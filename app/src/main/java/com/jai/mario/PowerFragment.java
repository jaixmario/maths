package com.jai.mario.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jai.mario.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigInteger;

public class PowerFragment extends Fragment {

    TextInputEditText inputA, inputB, inputN;
    MaterialButton solveBtn;
    TextView resultText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_power, container, false);

        inputA = view.findViewById(R.id.inputA);
        inputB = view.findViewById(R.id.inputB);
        inputN = view.findViewById(R.id.inputN);
        solveBtn = view.findViewById(R.id.solveButton);
        resultText = view.findViewById(R.id.resultText);

        solveBtn.setOnClickListener(v -> {
            String aStr = inputA.getText().toString().trim();
            String bStr = inputB.getText().toString().trim();
            String nStr = inputN.getText().toString().trim();

            if (TextUtils.isEmpty(aStr) || TextUtils.isEmpty(bStr) || TextUtils.isEmpty(nStr)) {
                resultText.setText("Please enter values for a, b and n.");
                return;
            }

            try {
                int a = Integer.parseInt(aStr);
                int b = Integer.parseInt(bStr);
                int n = Integer.parseInt(nStr);

                BigInteger result = calculateBinomialPower(a, b, n);
                resultText.setText("(" + a + " + " + b + ")^" + n + " =\n\n" + result.toString());

            } catch (Exception e) {
                resultText.setText("Error: Invalid input.");
            }
        });

        return view;
    }

    private BigInteger calculateBinomialPower(int a, int b, int n) {
        BigInteger sum = BigInteger.ZERO;

        for (int k = 0; k <= n; k++) {
            BigInteger coeff = binomial(n, k);
            BigInteger aTerm = BigInteger.valueOf(a).pow(n - k);
            BigInteger bTerm = BigInteger.valueOf(b).pow(k);
            sum = sum.add(coeff.multiply(aTerm).multiply(bTerm));
        }

        return sum;
    }

    private BigInteger binomial(int n, int k) {
        BigInteger res = BigInteger.ONE;
        for (int i = 1; i <= k; i++) {
            res = res.multiply(BigInteger.valueOf(n - i + 1)).divide(BigInteger.valueOf(i));
        }
        return res;
    }
}