package com.example.myapplication.ui.gallery;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import java.util.List;

public class PhotoWallAdapter extends RecyclerView.Adapter<PhotoWallAdapter.PhotoViewHolder> {

  private final List<Integer> photoList; // list of photo resources
  private final List<String> descriptionList; // list of photo descriptions

  public PhotoWallAdapter(List<Integer> photoList, List<String> descriptionList) {
    this.photoList = photoList;
    this.descriptionList = descriptionList;
  }

  @NonNull
  @Override
  public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
    return new PhotoViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
    // Set photos
    holder.imageView.setImageResource(photoList.get(position));
    // Set descriptions
    holder.photoDescription.setText(descriptionList.get(position));

    // Set click listener to show dialog with description
    holder.itemView.setOnClickListener(
        v -> {
          new AlertDialog.Builder(v.getContext())
              .setTitle("Building Introduction")
              .setMessage(descriptionList.get(position))
              .setPositiveButton(android.R.string.ok, null)
              .show();
        });
  }

  @Override
  public int getItemCount() {
    return photoList.size();
  }

  static class PhotoViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView photoDescription; // reference for the TextView to display description

    PhotoViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.imageView);
      // Bind TextView for description
      photoDescription = itemView.findViewById(R.id.photoDescription);
    }
  }
}
