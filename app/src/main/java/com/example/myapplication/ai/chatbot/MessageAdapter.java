// File: MessageAdapter.java

package com.example.myapplication.ai.chatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

  private final List<GitHubModelsRequest.Message> messageList;

  public MessageAdapter(List<GitHubModelsRequest.Message> messageList) {
    this.messageList = messageList;
  }

  @NonNull
  @Override
  public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
    return new MessageViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
    GitHubModelsRequest.Message message = messageList.get(position);
    holder.textViewMessage.setText(message.getContent());

    // Set background drawable and gravity based on message role
    LinearLayout.LayoutParams params =
        (LinearLayout.LayoutParams) holder.textViewMessage.getLayoutParams();
    if ("user".equals(message.getRole())) {
      holder.textViewMessage.setBackgroundResource(R.drawable.bubble_user);
    } else if ("assistant".equals(message.getRole())) {
      holder.textViewMessage.setBackgroundResource(R.drawable.bubble_assistant);
    }
    holder.textViewMessage.setLayoutParams(params);
  }

  @Override
  public int getItemCount() {
    return messageList.size();
  }

  static class MessageViewHolder extends RecyclerView.ViewHolder {
    TextView textViewMessage;

    public MessageViewHolder(@NonNull View itemView) {
      super(itemView);
      textViewMessage = itemView.findViewById(R.id.text_view_message);
    }
  }
}
