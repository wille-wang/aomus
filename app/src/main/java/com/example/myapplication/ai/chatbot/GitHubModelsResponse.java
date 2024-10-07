package com.example.myapplication.ai.chatbot;

import java.util.List;

public class GitHubModelsResponse {
  private List<Choice> choices;

  public List<Choice> getChoices() {
    return choices;
  }

  public void setChoices(List<Choice> choices) {
    this.choices = choices;
  }

  @Override
  public String toString() {
    return "GitHubModelsResponse{" + "choices=" + choices + '}';
  }

  public static class Choice {
    private String text;

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    @Override
    public String toString() {
      return "Choice{" + "text='" + text + '\'' + '}';
    }
  }
}
