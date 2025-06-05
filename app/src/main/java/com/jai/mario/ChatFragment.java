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

import org.json.JSONObject;

import java.util.concurrent.Executors;
import okhttp3.*;

import com.frhnfrq.mathview.MathView;

public class ChatFragment extends Fragment {

    private EditText messageInput;
    private Button sendBtn;
    private LinearLayout chatContainer;
    private ScrollView chatScroll;

    private final OkHttpClient client = new OkHttpClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        messageInput = view.findViewById(R.id.messageInput);
        sendBtn = view.findViewById(R.id.sendButton);
        chatContainer = view.findViewById(R.id.chatContainer);
        chatScroll = view.findViewById(R.id.chatScroll);

        sendBtn.setOnClickListener(v -> {
            String prompt = messageInput.getText().toString().trim();
            if (TextUtils.isEmpty(prompt)) {
                Toast.makeText(getContext(), "Please enter a question", Toast.LENGTH_SHORT).show();
                return;
            }

            messageInput.setText("");
            addMessage("<b>You:</b><br>" + prompt, true);

            SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", requireContext().MODE_PRIVATE);
            String apiKey = prefs.getString("apiKey", null);

            if (apiKey == null || apiKey.trim().isEmpty()) {
                addMessage("<b>⚠️ Please enter Gemini API key in Settings.</b>", false);
                return;
            }

            sendPromptToGemini(apiKey, prompt);
        });

        return view;
    }

    private void sendPromptToGemini(String apiKey, String prompt) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
                MediaType JSON = MediaType.get("application/json; charset=utf-8");
                String json = "{ \"contents\": [ { \"parts\": [ { \"text\": \"" + prompt + "\" } ] } ] }";

                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(json, JSON))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        showResponse("<b>❌ Error:</b> " + response.message());
                        return;
                    }

                    String body = response.body().string();
                    String reply = extractReply(body);
                    showResponse(reply);
                }
            } catch (Exception e) {
                showResponse("<b>❌ Exception:</b> " + e.getMessage());
            }
        });
    }

    private void showResponse(String text) {
        requireActivity().runOnUiThread(() -> addMessage(text, false));
    }

    private void addMessage(String message, boolean isUser) {
        if (getContext() == null || getView() == null) return;

        MathView mathView = new MathView(getContext());
        mathView.setTextColor(isUser ? "#121212" : "#FFFFFF");
        mathView.setTextSize(16);
        mathView.setText("<p style='color:" + (isUser ? "#121212" : "#FFFFFF") + ";'>" + message + "</p>");
        mathView.getSettings().setJavaScriptEnabled(true);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 12, 0, 0);
        params.gravity = isUser ? Gravity.END : Gravity.START;
        mathView.setLayoutParams(params);

        chatContainer.addView(mathView);
        chatScroll.post(() -> chatScroll.fullScroll(View.FOCUS_DOWN));
    }

    private String extractReply(String responseBody) {
        try {
            return new JSONObject(responseBody)
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
}  
