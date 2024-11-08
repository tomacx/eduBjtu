package com.example.edubjtu.service;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class AiService {
    public static final String API_KEY = "4V4xcdYY37OlMWYFTLdJNHKN";
    public static final String SECRET_KEY = "53CFATbPWvVOplQWUAy9PJooSE6PXyEv";

    // Increase timeout for connection, write, and read
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)  // Set connection timeout (60 seconds)
            .writeTimeout(60, TimeUnit.SECONDS)    // Set write timeout (60 seconds)
            .readTimeout(60, TimeUnit.SECONDS)     // Set read timeout (60 seconds)
            .build();
    public static String getAnswerOfAi(String question) throws IOException {
        // Create JSON body with the question included
        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("temperature", 0.95);
        requestBodyJson.put("top_p", 0.8);
        requestBodyJson.put("penalty_score", 1);
        requestBodyJson.put("enable_system_memory", false);
        requestBodyJson.put("disable_search", false);
        requestBodyJson.put("enable_citation", false);

        // Add the question to the request body
        JSONObject messageJson = new JSONObject();
        messageJson.put("role", "user");
        messageJson.put("content", question);

        requestBodyJson.put("messages", new JSONObject[] {messageJson}); // Wrap in array if needed by API

        // Create the request body as a string
        String jsonBody = requestBodyJson.toString();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonBody);

        // Create and execute the request
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute the request and retrieve the response
        Response response = HTTP_CLIENT.newCall(request).execute();
        if (response.isSuccessful()) {
            // Get the response body as a string
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            // Extract the "result" part from the response
            return jsonResponse.getString("result");  // Return only the result field as a String
        } else {
            throw new IOException("Failed to get response: " + response.message());
        }
    }

    /**
     * 获取Access Token
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        if (response.isSuccessful()) {
            return new JSONObject(response.body().string()).getString("access_token");
        } else {
            throw new IOException("Failed to retrieve access token: " + response.message());
        }
    }
}
