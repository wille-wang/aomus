package com.example.myapplication.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

  private final OnMapReadyCallback callback =
      new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
          // Set the coordinates for University of Melbourne, Parkville Campus
          LatLng melbourneUni = new LatLng(-37.7963, 144.9614);

          // Add a marker for University of Melbourne
          googleMap.addMarker(
              new MarkerOptions()
                  .position(melbourneUni)
                  .title("University of Melbourne, Parkville Campus"));

          // Move and zoom the camera to the University of Melbourne
          googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourneUni, 15));
        }
      };

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_map, container, false);
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
