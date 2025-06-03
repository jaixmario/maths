package com.jai.mario.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jai.mario.R;

public class SettingsFragment extends Fragment {

    private EditText apiKeyInput;
    private Button saveBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        apiKeyInput = view.findViewById(R.id.apiKeyInput);
        saveBtn = view.findViewById(R.id.saveBtn);

        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String savedKey = prefs.getString("apiKey", "");
        apiKeyInput.setText(savedKey);

        saveBtn.setOnClickListener(v -> {
            String key = apiKeyInput.getText().toString();
            prefs.edit().putString("apiKey", key).apply();
            Toast.makeText(getContext(), "API Key saved!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}