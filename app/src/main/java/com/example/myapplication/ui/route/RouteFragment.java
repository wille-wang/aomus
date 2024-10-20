package com.example.myapplication.ui.route;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.util.Building;
import com.example.myapplication.util.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;


public class RouteFragment extends Fragment {

    private final List<Route> routeList = new ArrayList<>();
    private final Map<String, Building> buildingCache = new HashMap<>();
    private final LatLng universityMelbourne = new LatLng(-37.7963, 144.9614); // Hardcode the University of Melbourne
    private GoogleMap mMap;
    private RecyclerView routeRecyclerView;
    private RouteAdapter routeAdapter;
    private TextView routeNameTextView, routeDetailsTextView;
    private boolean isMapReady = false; // 用于检查地图是否已准备好
    private Route selectedRoute;

    private final OnMapReadyCallback callback = googleMap -> {
        mMap = googleMap;
        isMapReady = true; // 地图已准备好

        // Enable UI controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        // Move and zoom the camera to the University of Melbourne
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(universityMelbourne, 15));
    };

    private Button navigateButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);

        // 初始化RecyclerView
        routeRecyclerView = view.findViewById(R.id.routeRecyclerView);
        routeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        routeAdapter = new RouteAdapter(routeList, this::onRouteSelected);
        routeRecyclerView.setAdapter(routeAdapter);

        // 初始化TextView
        routeNameTextView = view.findViewById(R.id.routeName);
        routeDetailsTextView = view.findViewById(R.id.routeDetails);

        // 初始化地图
        SupportMapFragment routeFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.route);
        if (routeFragment != null) {
            routeFragment.getMapAsync(callback);
        }

        navigateButton = view.findViewById(R.id.navigateButton);
        // 为按钮设置点击事件
        navigateButton.setOnClickListener(v -> {
            if (selectedRoute != null) {
                startNavigation(selectedRoute);
            } else {
                Toast.makeText(getContext(), "Please select a route first.", Toast.LENGTH_SHORT).show();
            }
        });

        // 从Firebase获取路线和建筑数据
        fetchBuildingDataFromFirebase();

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
                // 在建筑数据加载完成后获取路线数据
                fetchRoutesFromFirebase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RouteFragment", "Failed to read building data", databaseError.toException());
            }
        });
    }

    private void fetchRoutesFromFirebase() {
        DatabaseReference routeRef = FirebaseDatabase.getInstance().getReference("route");
        routeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                routeList.clear();
                for (DataSnapshot routeSnapshot : dataSnapshot.getChildren()) {
                    Route route = routeSnapshot.getValue(Route.class);
                    if (route != null) {
                        routeList.add(route);
                    }
                }
                routeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("RouteFragment", "Failed to read routes data", databaseError.toException());
            }
        });
    }

    private void onRouteSelected(Route route) {
        selectedRoute = route;
        // 检查地图是否已准备好
        if (isMapReady && mMap != null) {
            mMap.clear(); // 清除地图上的标记
            // 添加新的标记
            for (String buildingId : route.getBuildings()) {
                Building building = buildingCache.get(buildingId);
                if (building != null) {
                    LatLng location = new LatLng(building.getLatitude(), building.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(location).title(building.getName()));
                }
            }

            // 显示路线详细信息
            routeNameTextView.setText(route.getName());
            StringBuilder details = new StringBuilder("Length: " + route.getLength() + "\nTime: " + route.getTime() + "\nBuildings: ");
            for (String buildingId : route.getBuildings()) {
                Building building = buildingCache.get(buildingId);
                if (building != null) {
                    details.append(building.getName()).append(", ");
                }
            }
            // 去掉最后的逗号
            if (details.length() > 0) {
                details.setLength(details.length() - 2);
            }
            routeDetailsTextView.setText(details.toString());
        }
    }

    private void startNavigation(Route route) {
        if (selectedRoute == null || selectedRoute.getBuildings() == null || selectedRoute.getBuildings().isEmpty()) {
            Toast.makeText(getContext(), "No buildings found for navigation.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取起点和终点
        Building originBuilding = buildingCache.get(selectedRoute.getBuildings().get(0));
        Building destinationBuilding = buildingCache.get(selectedRoute.getBuildings().get(selectedRoute.getBuildings().size() - 1));

        if (originBuilding == null || destinationBuilding == null) {
            Toast.makeText(getContext(), "Failed to find origin or destination building.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取起点和终点的坐标
        String origin = originBuilding.getLatitude() + "," + originBuilding.getLongitude();
        String destination = destinationBuilding.getLatitude() + "," + destinationBuilding.getLongitude();

        // 构建途经点
        List<String> waypointCoordinates = new ArrayList<>();
        for (int i = 1; i < selectedRoute.getBuildings().size() - 1; i++) {
            Building waypointBuilding = buildingCache.get(selectedRoute.getBuildings().get(i));
            if (waypointBuilding != null) {
                waypointCoordinates.add(waypointBuilding.getLatitude() + "," + waypointBuilding.getLongitude());
            }
        }

        StringBuilder waypoints = new StringBuilder();
        for (String waypoint : waypointCoordinates) {
            waypoints.append(waypoint).append("|");
        }

        String uri = "https://www.google.com/maps/dir/?api=1" +
                "&origin=" + origin +
                "&destination=" + destination +
                "&waypoints=" + waypoints.toString() +
                "&travelmode=walking";

        // 启动 Intent 跳转到 Google 地图
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Google Maps is not installed", Toast.LENGTH_SHORT).show();
        }
    }
}

