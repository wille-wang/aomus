package com.example.myapplication.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.util.Building;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment {

  private final Map<String, LatLng> locationMap = new HashMap<>();
  private final LatLng universityMelbourne =
      new LatLng(-37.7963, 144.9614); // Hardcode the University of Melbourne
  private final List<String> suggestionsList = new ArrayList<>();
  private GoogleMap mMap;
  private final OnMapReadyCallback callback =
      new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
          mMap = googleMap;

          // Enable UI controls
          mMap.getUiSettings().setZoomControlsEnabled(true);
          mMap.getUiSettings().setCompassEnabled(true);
          mMap.getUiSettings().setMapToolbarEnabled(true);

          // Move and zoom the camera to the University of Melbourne
          mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(universityMelbourne, 15));
        }
      };
  private ArrayAdapter<String> suggestionAdapter;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_map, container, false);

    // Initialize the AutoCompleteTextView
    AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);

    if (autoCompleteTextView != null) {
      autoCompleteTextView.setFocusable(true);
      autoCompleteTextView.setFocusableInTouchMode(true);

      suggestionAdapter =
          new ArrayAdapter<>(
              getContext(), android.R.layout.simple_dropdown_item_1line, suggestionsList);
      autoCompleteTextView.setAdapter(suggestionAdapter);
      autoCompleteTextView.setThreshold(1); // Show suggestions after typing 1 character

      // Handle item selection
      autoCompleteTextView.setOnItemClickListener(
          (parent, view1, position, id) -> {
            String selectedSuggestion = (String) parent.getItemAtPosition(position);
            LatLng location = locationMap.get(selectedSuggestion);
            if (location != null) {
              mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
              mMap.addMarker(new MarkerOptions().position(location).title(selectedSuggestion));

              // Hide the keyboard
              InputMethodManager imm =
                  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
              if (imm != null) {
                imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
              }

              // Clear focus from AutoCompleteTextView
              autoCompleteTextView.clearFocus();
            } else {
              Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT).show();
            }
          });

      // Fetch data from Firebase and update suggestions
      fetchBuildingDataFromFirebase();
    }

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    SupportMapFragment mapFragment =
        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
    if (mapFragment != null) {
      mapFragment.getMapAsync(callback);
    }
  }

  private void fetchBuildingDataFromFirebase() {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference buildingsRef = database.getReference("buildings");

    buildingsRef.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            suggestionsList.clear();
            locationMap.clear();

            for (DataSnapshot buildingSnapshot : dataSnapshot.getChildren()) {
              Building building = buildingSnapshot.getValue(Building.class);
              if (building != null) {
                String nameSuggestion = building.getName();
                String codeSuggestion = building.getCode();
                // Do not use parentheses in the combined suggestion
                String combinedSuggestion = nameSuggestion + "  —  " + codeSuggestion.toUpperCase();

                // Add the combined suggestion to the list
                // suggestionsList.add(nameSuggestion);
                // suggestionsList.add(codeSuggestion);
                suggestionsList.add(combinedSuggestion);

                // Store the building location in the map
                LatLng location =
                    new LatLng(
                        buildingSnapshot.child("latitude").getValue(Double.class),
                        buildingSnapshot.child("longitude").getValue(Double.class));
                locationMap.put(nameSuggestion, location);
                locationMap.put(codeSuggestion, location);
                locationMap.put(combinedSuggestion, location);
              }
            }

            // Notify the adapter to update the suggestions list
            suggestionAdapter.notifyDataSetChanged();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("MapFragment", "Failed to read buildings data", databaseError.toException());
          }
        });
  }
}
