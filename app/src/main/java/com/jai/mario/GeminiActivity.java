package com.jai.mario;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.genai.GenerativeModel;
import com.google.genai.GenerativeModelConfig;
import com.google.genai.GenerativeModelResponse;
import com.google.genai.Session;

import java.util.concurrent.Executors;

public class GeminiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scrollView = new ScrollView(this);
        TextView resultView = new TextView(this);
        resultView.setPadding(32, 32, 32, 32);
        resultView.setTextSize(16f);
        scrollView.addView(resultView);
        setContentView(scrollView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        String prompt = getIntent().getStringExtra("prompt");
        if (prompt == null || prompt.trim().isEmpty()) {
            resultView.setText("No prompt provided.");
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                GenerativeModel model = GenerativeModel.builder()
                        .modelName("gemini-pro")
                        .apiKey(System.getenv("GOOGLE_API_KEY")) 
                        .build();

                GenerativeModelResponse response = model.generateContent(prompt);

                runOnUiThread(() -> resultView.setText(response.getText()));

            } catch (Exception e) {
                runOnUiThread(() -> resultView.setText("Error: " + e.getMessage()));
            }
        });
    }
}