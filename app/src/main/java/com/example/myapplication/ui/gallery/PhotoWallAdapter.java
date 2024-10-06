package com.example.myapplication.ui.gallery;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import java.util.List;

public class PhotoWallAdapter extends RecyclerView.Adapter<PhotoWallAdapter.PhotoViewHolder> {

  private final List<String> photoUrlList;
  private final List<String> nameList;
  private final List<Integer> yearList;

  public PhotoWallAdapter(
      List<String> photoUrlList, List<String> nameList, List<Integer> yearList) {
    this.photoUrlList = photoUrlList;
    this.nameList = nameList;
    this.yearList = yearList;
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
    Glide.with(holder.itemView.getContext())
        .load(photoUrlList.get(position))
        .into(holder.imageView);
    holder.photoDescription.setText(nameList.get(position) + " (" + yearList.get(position) + ")");

    holder.itemView.setOnClickListener(
        v -> {
          new AlertDialog.Builder(v.getContext())
              .setTitle("Building Introduction")
              .setMessage(nameList.get(position) + " (" + yearList.get(position) + ")")
              .setPositiveButton(android.R.string.ok, null)
              .show();
        });
  }

  @Override
  public int getItemCount() {
    return photoUrlList.size();
  }

  static class PhotoViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView photoDescription;

    PhotoViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.imageView);
      photoDescription = itemView.findViewById(R.id.photoDescription);
    }
  }
}
