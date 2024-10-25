// File: GitHubModelsResponse.java

package com.example.myapplication.ui.chatbot;

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
    private Message message;

    public Message getMessage() {
      return message;
    }

    public void setMessage(Message message) {
      this.message = message;
    }

    @Override
    public String toString() {
      return "Choice{" + "message=" + message + '}';
    }

    public static class Message {
      private String content;

      public String getContent() {
        return content;
      }

      public void setContent(String content) {
        this.content = content;
      }

      @Override
      public String toString() {
        return "Message{" + "content='" + content + '\'' + '}';
      }
    }
  }
}
