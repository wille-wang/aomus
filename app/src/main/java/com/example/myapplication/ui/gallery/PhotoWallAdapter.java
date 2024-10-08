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
import java.util.Map;

public class PhotoWallAdapter extends RecyclerView.Adapter<PhotoWallAdapter.PhotoViewHolder> {

  private final List<String> photoUrlList;
  private final List<String> nameList;
  private final List<Integer> yearList;
  private final List<String> descList;
  private final List<String> codeList; // Add a list for building codes
  private final Map<String, Integer> visitCounts;

  public PhotoWallAdapter(
      List<String> photoUrlList,
      List<String> nameList,
      List<Integer> yearList,
      List<String> descList,
      List<String> codeList, // Add codeList to the constructor
      Map<String, Integer> visitCounts) {
    this.photoUrlList = photoUrlList;
    this.nameList = nameList;
    this.yearList = yearList;
    this.descList = descList;
    this.codeList = codeList; // Initialize codeList
    this.visitCounts = visitCounts;
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
    String buildingName = nameList.get(position);
    holder.photoDescription.setText(buildingName + " (" + yearList.get(position) + ")");
    String buildingCode = codeList.get(position); // Get the building code
    Integer visitCount = visitCounts.get(buildingCode); // Use building code to fetch visit count
    if (visitCount != null) {
      holder.visitCount.setText("Visits: " + visitCount);
    } else {
      holder.visitCount.setText("Visits: 0");
    }

    holder.itemView.setOnClickListener(
        v -> {
          new AlertDialog.Builder(v.getContext())
              .setTitle(buildingName + " (" + yearList.get(position) + ")")
              .setMessage(descList.get(position))
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
    TextView visitCount;

    PhotoViewHolder(@NonNull View itemView) {
      super(itemView);
      imageView = itemView.findViewById(R.id.imageView);
      photoDescription = itemView.findViewById(R.id.photoDescription);
      visitCount = itemView.findViewById(R.id.visitCount);
    }
  }
}
