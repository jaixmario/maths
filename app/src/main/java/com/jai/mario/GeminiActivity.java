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
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

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
                OkHttpClient client = new OkHttpClient();

                String json = buildCorrectJson(prompt); // ✅ correct format

                Request request = new Request.Builder()
                        .url(GEMINI_URL)
                        .post(RequestBody.create(json, MediaType.get("application/json")))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        runOnUiThread(() -> textView.setText("Request failed: " + response.code() + " " + response.message()));
                        return;
                    }

                    String body = response.body().string();
                    String output = parseResponse(body);
                    runOnUiThread(() -> textView.setText(output));
                }

            } catch (IOException e) {
                runOnUiThread(() -> textView.setText("Error: " + e.getMessage()));
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
            return "No response text found.";
        } catch (Exception e) {
            return "Failed to parse response: " + e.getMessage();
        }
    }
}