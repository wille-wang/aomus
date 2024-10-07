package com.example.myapplication.ai.chatbot;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GitHubModelsApiService {

  // TODO: Replace the GitHub PAT with the environment variable
  @Headers({
    "Content-Type: application/json",
    "Authorization: Bearer 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"
  })
  @POST("v1/models/completions")
  Call<GitHubModelsResponse> getGitHubModelsResponse(@Body GitHubModelsRequest request);
}
