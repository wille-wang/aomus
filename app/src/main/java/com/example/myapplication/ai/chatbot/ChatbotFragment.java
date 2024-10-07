package com.example.myapplication.ai.chatbot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.myapplication.databinding.FragmentChatbotBinding;

public class ChatbotFragment extends Fragment {

  private FragmentChatbotBinding binding;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentChatbotBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
    // Initialize the chatbot UI here
    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
