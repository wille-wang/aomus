package com.example.myapplication.ui.route;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.util.Route;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class RouteSelectDialogFragment extends DialogFragment {

    private final List<Route> routeList = new ArrayList<>();
    private RouteAdapter routeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_select, container, false);

        // RecyclerView initialization
        RecyclerView routeRecyclerView = view.findViewById(R.id.routeRecyclerView);
        routeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        routeAdapter = new RouteAdapter(routeList, this::onRouteSelected);
        routeRecyclerView.setAdapter(routeAdapter);

        // get route data from Firebase
        fetchRoutesFromFirebase();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // width 95%, height 80%
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.95),
                    (int) (getResources().getDisplayMetrics().heightPixels * 0.8)
            );
        }
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
            }
        });
    }

    private void onRouteSelected(Route route) {
        if (getParentFragment() instanceof RouteSelectionListener) {
            ((RouteSelectionListener) getParentFragment()).onRouteSelected(route);
        }
        dismiss();
    }

    public interface RouteSelectionListener {
        void onRouteSelected(Route route);
    }
}
