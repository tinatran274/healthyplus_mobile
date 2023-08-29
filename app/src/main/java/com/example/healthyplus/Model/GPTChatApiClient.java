package com.example.healthyplus.Model;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class GPTChatApiClient {
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/engines/gpt-3.5-turbo/completions";
    public static void sendMessage(String userInput, Callback callback) throws JSONException {
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", "You are a helpful assistant."));
        messages.put(new JSONObject().put("role", "user").put("content", userInput));

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", messages);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody.toString());
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
