// File: ChatbotFragment.java

package com.example.myapplication.chatbot;

import java.util.List;

public class GitHubModelsRequest {
  private final List<Message> messages;
  private final String model;
  private final int temperature;
  private final int max_tokens;
  private final int top_p;

  public GitHubModelsRequest(
      List<Message> messages, String model, int temperature, int max_tokens, int top_p) {
    this.messages = messages;
    this.model = model;
    this.temperature = temperature;
    this.max_tokens = max_tokens;
    this.top_p = top_p;
  }

  public static class Message {
    private String role;
    private String content;

    public Message(String role, String content) {
      this.role = role;
      this.content = content;
    }

    public String getRole() {
      return role;
    }

    public void setRole(String role) {
      this.role = role;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }
  }
}
