package com.jai.mario.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.jai.mario.R;

public class SettingsFragment extends Fragment {

    private TextInputEditText apiKeyInput;
    private MaterialButton saveBtn;
    private SwitchCompat updateSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        apiKeyInput = view.findViewById(R.id.apiKeyInput);
        saveBtn = view.findViewById(R.id.saveBtn);
        updateSwitch = view.findViewById(R.id.updateSwitch);

        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
        String savedKey = prefs.getString("apiKey", "");
        boolean updatesEnabled = prefs.getBoolean("checkForUpdates", true);

        apiKeyInput.setText(savedKey);
        updateSwitch.setChecked(updatesEnabled);

        saveBtn.setOnClickListener(v -> {
            String key = apiKeyInput.getText().toString();
            prefs.edit().putString("apiKey", key).apply();
            Toast.makeText(getContext(), "API Key saved!", Toast.LENGTH_SHORT).show();
        });

        updateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("checkForUpdates", isChecked).apply();
            Toast.makeText(getContext(), "Check for Updates " + (isChecked ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}