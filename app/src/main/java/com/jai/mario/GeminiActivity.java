package com.jai.mario;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.concurrent.Executors;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(0xFF121212);

        TextView textView = new TextView(this);
        textView.setTextColor(0xFFFFFFFF);
        textView.setPadding(32, 32, 32, 32);
        textView.setTextSize(16);
        textView.setLineSpacing(1.4f, 1.4f);
        textView.setTypeface(Typeface.MONOSPACE);
        textView.setMovementMethod(new ScrollingMovementMethod());
        scrollView.addView(textView, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(scrollView);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String API_KEY = prefs.getString("gemini_key", null);

        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            textView.setText("\u26A0\uFE0F Please enter key in Settings.");
            return;
        }

        String prompt = getIntent().getStringExtra("prompt");
        if (prompt == null || prompt.isEmpty()) {
            textView.setText("\u26A0\uFE0F No prompt received.");
            return;
        }

        String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                String json = buildCorrectJson(prompt);

                Request request = new Request.Builder()
                        .url(GEMINI_URL)
                        .post(RequestBody.create(json, MediaType.get("application/json")))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        runOnUiThread(() -> textView.setText("\u274C Request failed: " + response.code() + " " + response.message()));
                        return;
                    }

                    String body = response.body().string();
                    String output = parseResponse(body);
                    runOnUiThread(() -> textView.setText(formatForMarkdown(output)));
                }

            } catch (IOException e) {
                runOnUiThread(() -> textView.setText("\u274C Error: " + e.getMessage()));
            }
        });
    }

    private String buildCorrectJson(String prompt) {
        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);

        JsonArray partsArray = new JsonArray();
        partsArray.add(textPart);

        JsonObject content = new JsonObject();
        content.add("parts", partsArray);

        JsonArray contentsArray = new JsonArray();
        contentsArray.add(content);

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contentsArray);

        return requestBody.toString();
    }

    private String parseResponse(String responseBody) {
        try {
            JsonObject root = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray candidates = root.getAsJsonArray("candidates");
            if (candidates != null && candidates.size() > 0) {
                JsonObject content = candidates.get(0).getAsJsonObject().getAsJsonObject("content");
                JsonArray parts = content.getAsJsonArray("parts");
                if (parts != null && parts.size() > 0) {
                    return parts.get(0).getAsJsonObject().get("text").getAsString();
                }
            }
            return "\u26A0\uFE0F No response text found.";
        } catch (Exception e) {
            return "\u26A0\uFE0F Failed to parse response: " + e.getMessage();
        }
    }

    private String formatForMarkdown(String input) {
        return input
                .replace("**", "")
                .replaceAll("(?m)^#{1,6}\\s*", "")
                .replaceAll("```", "")
                .trim();
    }
}
