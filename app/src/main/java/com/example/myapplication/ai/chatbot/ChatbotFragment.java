package com.example.myapplication.ai.chatbot;

import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

public class ChatbotFragment extends Fragment {

  private FragmentChatbotBinding binding;
  private MessageAdapter messageAdapter;
  private List<String> messageList;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentChatbotBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    RecyclerView recyclerView = binding.recyclerViewMessages;
    EditText editTextMessage = binding.editTextMessage;
    Button buttonSend = binding.buttonSend;

    messageList = new ArrayList<>();
    messageAdapter = new MessageAdapter(messageList);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    recyclerView.setAdapter(messageAdapter);

    buttonSend.setOnClickListener(
        v -> {
          String message = editTextMessage.getText().toString();
          if (!message.isEmpty()) {
            messageList.add(message);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
            editTextMessage.setText("");
            // Here you can add code to handle the chatbot response
          }
        });

    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
