package com.example.myapplication.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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

public class MapFragment extends Fragment {

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
          LatLng melbourneUni = new LatLng(-37.7963, 144.9614);

          // Add a marker for University of Melbourne
          mMap.addMarker(
              new MarkerOptions()
                  .position(melbourneUni)
                  .title("University of Melbourne, Parkville Campus"));

          // Move and zoom the camera to the University of Melbourne
          mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourneUni, 15));
        }
      };

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_map, container, false);

    // Initialize the search bar
    SearchView searchView = view.findViewById(R.id.searchView);

    // Expand the search view by default
    searchView.setIconified(false);
    searchView.clearFocus();

    searchView.setOnQueryTextListener(
        new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
            // Handle search query submission
            // For example, you can use Geocoder to find the location and move the map
            return false;
          }

          @Override
          public boolean onQueryTextChange(String newText) {
            return false;
          }
        });

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
