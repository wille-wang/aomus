package com.example.myapplication.ui.gallery;

import android.app.Activity;
import android.content.SharedPreferences;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {

  private final List<String> photoUrlList = new ArrayList<>();
  private final List<String> nameList = new ArrayList<>();
  private final List<Integer> yearList = new ArrayList<>();
  private final List<String> descList = new ArrayList<>();
  private final List<String> codeList = new ArrayList<>(); // Add a list for building codes
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
            descList.clear();
            codeList.clear(); // Clear codeList
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              Building building = snapshot.getValue(Building.class);
              if (building != null) {
                photoUrlList.add(building.getImgUrl());
                nameList.add(building.getName());
                yearList.add(building.getYear());
                descList.add(building.getDesc());
                codeList.add(building.getCode()); // Add building code to codeList
              }
            }
            // Fetch visit counts after fetching building data
            fetchVisitCounts(recyclerView);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
          }
        });
  }

  private void fetchVisitCounts(RecyclerView recyclerView) {
    SharedPreferences sharedPreferences =
        getActivity().getSharedPreferences("LoginPrefs", Activity.MODE_PRIVATE);
    String username = sharedPreferences.getString("username", "unknown");

    DatabaseReference visitCountsRef =
        FirebaseDatabase.getInstance().getReference("users").child(username).child("checkins");

    visitCountsRef.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Map<String, Integer> visitCounts = new HashMap<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              String buildingCode = snapshot.getKey();
              Integer count = snapshot.child("counts").getValue(Integer.class);
              if (buildingCode != null && count != null) {
                visitCounts.put(buildingCode, count);
              }
            }
            // Update the adapter with the new data
            PhotoWallAdapter adapter =
                new PhotoWallAdapter(
                    photoUrlList, nameList, yearList, descList, codeList, visitCounts);
            recyclerView.setAdapter(adapter);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getContext(), "Failed to load visit counts", Toast.LENGTH_SHORT).show();
          }
        });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
