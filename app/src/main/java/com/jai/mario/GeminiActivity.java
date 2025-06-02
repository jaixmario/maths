package com.jai.mario;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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

    private static final String API_KEY = "AIzaSyAvnw_XPRrCXPG2hUrhn9DgU6a9G_K4KuQ";
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scrollView = new ScrollView(this);
        TextView textView = new TextView(this);
        textView.setPadding(30, 30, 30, 30);
        scrollView.addView(textView);
        setContentView(scrollView);

        String prompt = getIntent().getStringExtra("prompt");
        if (prompt == null || prompt.isEmpty()) {
            textView.setText("No prompt received.");
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String jsonRequest = buildJsonPrompt(prompt);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(GEMINI_URL)
                        .post(RequestBody.create(jsonRequest, MediaType.get("application/json")))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        runOnUiThread(() -> textView.setText("Request failed: " + response.message()));
                        return;
                    }

                    String responseBody = response.body().string();
                    String parsedText = parseGeminiResponse(responseBody);
                    runOnUiThread(() -> textView.setText(parsedText));
                }

            } catch (IOException e) {
                runOnUiThread(() -> textView.setText("Error: " + e.getMessage()));
            }
        });
    }

    private String buildJsonPrompt(String prompt) {
        JsonObject textObj = new JsonObject();
        textObj.addProperty("text", prompt);

        JsonArray contents = new JsonArray();
        contents.add(textObj);

        JsonObject finalObj = new JsonObject();
        finalObj.add("contents", contents);

        return finalObj.toString();
    }

    private String parseGeminiResponse(String responseBody) {
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
            return "No response text found.";
        } catch (Exception e) {
            return "Failed to parse response: " + e.getMessage();
        }
    }
}