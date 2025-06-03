package com.jai.mario.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jai.mario.R;
import com.google.android.material.button.MaterialButton;

public class SettingsFragment extends Fragment {

    private EditText apiKeyInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        apiKeyInput = view.findViewById(R.id.apiKeyInput);
        MaterialButton saveBtn = view.findViewById(R.id.saveBtn);

        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", 0);
        String currentKey = prefs.getString("gemini_key", "");
        apiKeyInput.setText(currentKey);

        saveBtn.setOnClickListener(v -> {
            String key = apiKeyInput.getText().toString().trim();
            prefs.edit().putString("gemini_key", key).apply();
            Toast.makeText(getContext(), "Key saved", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}