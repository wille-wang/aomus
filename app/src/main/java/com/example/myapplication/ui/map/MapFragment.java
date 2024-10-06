package com.example.myapplication.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment {

  private final Map<String, LatLng> locationMap =
      new HashMap<String, LatLng>() {
        {
          // mock data for locations
          put("University of Melbourne, Parkville Campus", new LatLng(-37.7963, 144.9614));
          put("Melbourne Central", new LatLng(-37.8100, 144.9628));
          put("Flinders Street Station", new LatLng(-37.8183, 144.9671));
          put("Federation Square", new LatLng(-37.8179, 144.9691));
        }
      };
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

          // Set the coordinates for University of Melbourne, Parkville Campus
          LatLng melbourneUni = locationMap.get("University of Melbourne, Parkville Campus");

          // Add a marker for University of Melbourne
          if (melbourneUni != null) {
            mMap.addMarker(
                new MarkerOptions()
                    .position(melbourneUni)
                    .title("University of Melbourne, Parkville Campus"));

            // Move and zoom the camera to the University of Melbourne
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourneUni, 15));
          }
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

    // Ensure the AutoCompleteTextView is not null
    if (autoCompleteTextView != null) {
      // Make sure AutoCompleteTextView is focusable and can receive input
      autoCompleteTextView.setFocusable(true);
      autoCompleteTextView.setFocusableInTouchMode(true);

      // Create an ArrayAdapter to hold suggestions
      suggestionAdapter =
          new ArrayAdapter<>(
              getContext(),
              android.R.layout.simple_dropdown_item_1line,
              new ArrayList<>(locationMap.keySet()));

      // Set the adapter to the AutoCompleteTextView
      autoCompleteTextView.setAdapter(suggestionAdapter);

      // Ensure suggestions are shown after typing 1 character
      autoCompleteTextView.setThreshold(1);

      // Handle item selection from suggestions
      autoCompleteTextView.setOnItemClickListener(
          (parent, view1, position, id) -> {
            String selectedLocation = (String) parent.getItemAtPosition(position);
            LatLng location = locationMap.get(selectedLocation);
            if (location != null) {
              mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
              mMap.addMarker(new MarkerOptions().position(location).title(selectedLocation));
            }
          });
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
}
