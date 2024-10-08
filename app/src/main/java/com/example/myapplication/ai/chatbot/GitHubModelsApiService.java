// File: GitHubModelsRequest.java

package com.example.myapplication.ai.chatbot;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GitHubModelsApiService {

  @Headers("Content-Type: application/json")
  @POST("chat/completions")
  Call<GitHubModelsResponse> getGitHubModelsResponse(@Body GitHubModelsRequest request);
}
