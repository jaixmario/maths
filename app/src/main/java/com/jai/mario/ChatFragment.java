package com.jai.mario.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jai.mario.R;

import java.util.concurrent.Executors;
import okhttp3.*;

public class ChatFragment extends Fragment {

    private EditText messageInput;
    private Button sendBtn;
    private TextView chatOutput;

    private final OkHttpClient client = new OkHttpClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        messageInput = view.findViewById(R.id.messageInput);
        sendBtn = view.findViewById(R.id.sendButton);
        chatOutput = view.findViewById(R.id.chatOutput);

        sendBtn.setOnClickListener(v -> {
            String prompt = messageInput.getText().toString().trim();
            if (TextUtils.isEmpty(prompt)) {
                Toast.makeText(getContext(), "Please enter a question", Toast.LENGTH_SHORT).show();
                return;
            }

            messageInput.setText("");
            chatOutput.append("You: " + prompt + "\n\n");

            SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
            String apiKey = prefs.getString("apiKey", null);

            if (apiKey == null || apiKey.trim().isEmpty()) {
                chatOutput.append("⚠️ Please enter Gemini API key in Settings.\n\n");
                return;
            }

            sendPromptToGemini(apiKey, prompt);
        });

        return view;
    }

    private void sendPromptToGemini(String apiKey, String prompt) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

                MediaType JSON = MediaType.get("application/json; charset=utf-8");
                String json = "{ \"contents\": [ { \"parts\": [ { \"text\": \"" + prompt + "\" } ] } ] }";

                Request request = new Request.Builder()
                        .url(GEMINI_URL)
                        .post(RequestBody.create(json, JSON))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        showResponse("❌ Error: " + response.message());
                        return;
                    }

                    String body = response.body().string();
                    String reply = extractReply(body);
                    showResponse("Gemini: " + reply);
                }
            } catch (Exception e) {
                showResponse("❌ Exception: " + e.getMessage());
            }
        });
    }

    private String extractReply(String responseBody) {
        try {
            return new org.json.JSONObject(responseBody)
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        } catch (Exception e) {
            return "⚠️ Could not parse reply.";
        }
    }

    private void showResponse(String text) {
        requireActivity().runOnUiThread(() -> chatOutput.append(text + "\n\n"));
    }
}