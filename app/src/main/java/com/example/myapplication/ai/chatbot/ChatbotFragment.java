// File: ChatbotFragment.java

package com.example.myapplication.ai.chatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.databinding.FragmentChatbotBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatbotFragment extends Fragment {

  private FragmentChatbotBinding binding;
  private MessageAdapter messageAdapter;
  private List<String> messageList;
  private List<GitHubModelsRequest.Message> chatHistory;
  private GitHubModelsApiService gitHubModelsApiService;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentChatbotBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    RecyclerView recyclerView = binding.recyclerViewMessages;
    EditText editTextMessage = binding.editTextMessage;
    Button buttonSend = binding.buttonSend;

    messageList = new ArrayList<>();
    chatHistory = new ArrayList<>();
    messageAdapter = new MessageAdapter(messageList);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(messageAdapter);

    // Initialize Retrofit
    // TODO: Replace the GitHub PAT with the environment variable
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("https://models.inference.ai.azure.com/") // Base URL without the path
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                new OkHttpClient.Builder()
                    .addInterceptor(
                        chain -> {
                          Request original = chain.request();
                          Request request =
                              original
                                  .newBuilder()
                                  .header(
                                      "Authorization",
                                      "Bearer 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")
                                  .method(original.method(), original.body())
                                  .build();
                          return chain.proceed(request);
                        })
                    .build())
            .build();
    gitHubModelsApiService = retrofit.create(GitHubModelsApiService.class);

    buttonSend.setOnClickListener(
        v -> {
          String message = editTextMessage.getText().toString();
          if (!message.isEmpty()) {
            messageList.add(message);
            chatHistory.add(new GitHubModelsRequest.Message("user", message));
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
            editTextMessage.setText("");
            getGitHubModelsResponse(chatHistory);
          }
        });

    // Add initial system message
    chatHistory.add(new GitHubModelsRequest.Message("system", "You are a helpful assistant."));

    return root;
  }

  private void getGitHubModelsResponse(List<GitHubModelsRequest.Message> messages) {
    // Log messages for debugging
    for (GitHubModelsRequest.Message message : messages) {
      Log.d("ChatbotFragment", "Role: " + message.getRole() + ", Content: " + message.getContent());
    }

    GitHubModelsRequest request = new GitHubModelsRequest(messages, "gpt-4o-mini", 1, 4096, 1);
    gitHubModelsApiService
        .getGitHubModelsResponse(request)
        .enqueue(
            new Callback<GitHubModelsResponse>() {
              @Override
              public void onResponse(
                  Call<GitHubModelsResponse> call, Response<GitHubModelsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                  Log.d("ChatbotFragment", "Full Response: " + response.body());
                  List<GitHubModelsResponse.Choice> choices = response.body().getChoices();
                  if (choices != null && !choices.isEmpty()) {
                    String reply = choices.get(0).getMessage().getContent();
                    if (reply != null) {
                      messageList.add(reply);
                      chatHistory.add(new GitHubModelsRequest.Message("assistant", reply));
                      messageAdapter.notifyItemInserted(messageList.size() - 1);
                      binding.recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                    } else {
                      Log.e("ChatbotFragment", "Received null reply from assistant");
                    }
                  } else {
                    Log.e("ChatbotFragment", "Choices list is empty or null");
                  }
                } else {
                  try {
                    String errorBody = response.errorBody().string();
                    Log.e("ChatbotFragment", "Response not successful: " + errorBody);
                  } catch (IOException e) {
                    Log.e("ChatbotFragment", "Error reading error body", e);
                  }
                }
              }

              @Override
              public void onFailure(Call<GitHubModelsResponse> call, Throwable t) {
                Log.e("ChatbotFragment", "API call failed", t);
              }
            });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
