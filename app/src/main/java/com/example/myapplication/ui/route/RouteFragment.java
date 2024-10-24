package com.example.myapplication.ui.route;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.util.Building;
import com.example.myapplication.util.Route;
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

public class RouteFragment extends Fragment implements RouteSelectDialogFragment.RouteSelectionListener {

    private final Map<String, Building> buildingCache = new HashMap<>();
    private final LatLng universityMelbourne = new LatLng(-37.7963, 144.9614); // Hardcode the University of Melbourne
    private GoogleMap mMap;
    private TextView routeNameTextView, routeDetailsTextView;
    private boolean isMapReady = false;
    private Route selectedRoute;
    private Button navigateButton;

    private final OnMapReadyCallback callback = googleMap -> {
        mMap = googleMap;
        isMapReady = true;

        // Enable UI controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        // Move and zoom the camera to the University of Melbourne
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(universityMelbourne, 15));

        if (selectedRoute != null) {
            displayRoute(selectedRoute);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);

        // TextView initialization
        routeNameTextView = view.findViewById(R.id.routeName);
        routeDetailsTextView = view.findViewById(R.id.routeDetails);

        // Set default text when no route is selected
        routeNameTextView.setText("Route not selected");
        routeDetailsTextView.setText("");

        // map initialization
        SupportMapFragment routeFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.route);
        if (routeFragment != null) {
            routeFragment.getMapAsync(callback);
        }

        navigateButton = view.findViewById(R.id.navigateButton);
        // Hide navigation button initially
        navigateButton.setVisibility(View.GONE);

        navigateButton.setOnClickListener(v -> {
            if (selectedRoute != null) {
                startNavigation(selectedRoute);
            } else {
                Toast.makeText(getContext(), "Please select a route first.", Toast.LENGTH_SHORT).show();
            }
        });

        fetchBuildingDataFromFirebase();

        // Show route selection dialog if no route is selected initially
        if (selectedRoute == null) {
            showRouteSelectDialog();
        }

        // back button click event
        Button backButton = view.findViewById(R.id.backButton);
        backButton.setText("Select route");
        backButton.setOnClickListener(v -> showRouteSelectDialog());

        return view;
    }

    private void fetchBuildingDataFromFirebase() {
        DatabaseReference buildingRef = FirebaseDatabase.getInstance().getReference("buildings");

        buildingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                buildingCache.clear();
                for (DataSnapshot buildingSnapshot : dataSnapshot.getChildren()) {
                    Building building = buildingSnapshot.getValue(Building.class);
                    if (building != null) {
                        buildingCache.put(buildingSnapshot.getKey(), building);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RouteFragment", "Failed to read building data", databaseError.toException());
            }
        });
    }

    private void showRouteSelectDialog() {
        RouteSelectDialogFragment dialogFragment = new RouteSelectDialogFragment();
        dialogFragment.show(getChildFragmentManager(), "RouteSelectDialog");
    }

    @Override
    public void onRouteSelected(Route route) {
        selectedRoute = route;
        if (isMapReady) {
            displayRoute(route);
        }

        // Update UI after route selection
        if (selectedRoute != null) {
            // Show navigation button after a route is selected
            navigateButton.setVisibility(View.VISIBLE);

            // Change backButton text
            Button backButton = getView().findViewById(R.id.backButton);
            if (backButton != null) {
                backButton.setText("Select another route");
            }
        }
    }

    private void displayRoute(Route route) {
        if (isMapReady && mMap != null) {
            mMap.clear();
            // display marks on map
            for (String buildingId : route.getBuildings()) {
                Building building = buildingCache.get(buildingId);
                if (building != null) {
                    LatLng location = new LatLng(building.getLatitude(), building.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(location).title(building.getName()));
                }
            }

            // display route details
            routeNameTextView.setText(route.getName());
            StringBuilder details = new StringBuilder("Length: " + route.getLength() + "\nTime: " + route.getTime() + "\nBuildings: ");
            for (String buildingId : route.getBuildings()) {
                Building building = buildingCache.get(buildingId);
                if (building != null) {
                    details.append(building.getName()).append(", ");
                }
            }

            if (details.length() > 0) {
                details.setLength(details.length() - 2);
            }
            routeDetailsTextView.setText(details.toString());

            // load building images
            loadBuildingImages(route.getBuildings());
        }
    }

    private void loadBuildingImages(List<String> buildingIds) {
        LinearLayout imageContainer = getView().findViewById(R.id.buildingImagesContainer);
        imageContainer.removeAllViews();

        for (String buildingId : buildingIds) {
            Building building = buildingCache.get(buildingId);
            if (building != null && building.getImgUrl() != null) {
                ImageView imageView = new ImageView(getContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        500, // image width
                        500 // image height
                );
                layoutParams.setMargins(8, 0, 8, 0); // 图片间距
                imageView.setLayoutParams(layoutParams);

                // Glide loading image
                Glide.with(getContext())
                        .load(building.getImgUrl())
                        .into(imageView);

                imageContainer.addView(imageView);
            }
        }
    }

    private void startNavigation(Route route) {
        if (selectedRoute == null || selectedRoute.getBuildings() == null || selectedRoute.getBuildings().isEmpty()) {
            Toast.makeText(getContext(), "No buildings found for navigation.", Toast.LENGTH_SHORT).show();
            return;
        }

        // start
        String origin = "current+location";

        // destination
        Building destinationBuilding = buildingCache.get(selectedRoute.getBuildings().get(selectedRoute.getBuildings().size() - 1));

        if (destinationBuilding == null) {
            Toast.makeText(getContext(), "Failed to find destination building.", Toast.LENGTH_SHORT).show();
            return;
        }
        String destination = destinationBuilding.getLatitude() + "," + destinationBuilding.getLongitude();

        // waypoints
        List<String> waypointCoordinates = new ArrayList<>();
        for (int i = 0; i < selectedRoute.getBuildings().size() - 1; i++) {
            Building waypointBuilding = buildingCache.get(selectedRoute.getBuildings().get(i));
            if (waypointBuilding != null) {
                waypointCoordinates.add(waypointBuilding.getLatitude() + "," + waypointBuilding.getLongitude());
            }
        }

        StringBuilder waypoints = new StringBuilder();
        for (String waypoint : waypointCoordinates) {
            waypoints.append(waypoint).append("|");
        }

        // build navigation URL
        String uri = "https://www.google.com/maps/dir/?api=1" +
                "&origin=" + origin +
                "&destination=" + destination +
                "&waypoints=" + waypoints.toString() +
                "&travelmode=walking";

        // go to google map
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Google Maps is not installed", Toast.LENGTH_SHORT).show();
        }
    }

}
