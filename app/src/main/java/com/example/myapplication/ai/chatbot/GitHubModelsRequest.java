package com.example.myapplication.ai.chatbot;

public class GitHubModelsRequest {
  private String prompt;
  private int max_tokens;

  public GitHubModelsRequest(String prompt, int max_tokens) {
    this.prompt = prompt;
    this.max_tokens = max_tokens;
  }

  // Getters and setters
  public String getPrompt() {
    return prompt;
  }

  public void setPrompt(String prompt) {
    this.prompt = prompt;
  }

  public int getMaxTokens() {
    return max_tokens;
  }

  public void setMaxTokens(int max_tokens) {
    this.max_tokens = max_tokens;
  }
}
