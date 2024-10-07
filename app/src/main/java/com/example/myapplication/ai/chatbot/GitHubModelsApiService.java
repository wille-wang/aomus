package com.example.myapplication.ai.chatbot;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GitHubModelsApiService {

  @Headers({"Content-Type: application/json", "Authorization: Bearer YOUR_API_KEY"})
  @POST("v1/models/completions")
  Call<GitHubModelsResponse> getGitHubModelsResponse(@Body GitHubModelsRequest request);
}
