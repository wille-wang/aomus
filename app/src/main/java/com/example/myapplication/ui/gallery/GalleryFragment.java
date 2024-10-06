package com.example.myapplication.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.databinding.FragmentGalleryBinding;
import com.example.myapplication.util.Building;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

  private final List<String> photoUrlList = new ArrayList<>();
  private final List<String> nameList = new ArrayList<>();
  private final List<Integer> yearList = new ArrayList<>();
  private FragmentGalleryBinding binding;
  private DatabaseReference buildingsRef;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

    binding = FragmentGalleryBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    // Initialize Firebase reference
    buildingsRef = FirebaseDatabase.getInstance().getReference("buildings");

    // Set up the RecyclerView
    RecyclerView recyclerView = binding.recyclerView;
    recyclerView.setLayoutManager(
        new LinearLayoutManager(
            getContext(), RecyclerView.VERTICAL, false)); // 1 photo per row (vertical)

    // Fetch data from Firebase
    fetchBuildingData(recyclerView);

    return root;
  }

  private void fetchBuildingData(RecyclerView recyclerView) {
    buildingsRef.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            photoUrlList.clear();
            nameList.clear();
            yearList.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              Building building = snapshot.getValue(Building.class);
              if (building != null) {
                photoUrlList.add(building.getImgUrl());
                nameList.add(building.getName());
                yearList.add(building.getYear());
              }
            }
            // Pass the photo list, name list, and year list to the adapter
            PhotoWallAdapter adapter = new PhotoWallAdapter(photoUrlList, nameList, yearList);
            recyclerView.setAdapter(adapter);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
          }
        });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
