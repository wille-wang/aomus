package com.example.myapplication.ui.route;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.util.Route;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<Route> routeList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Route route);
    }

    public RouteAdapter(List<Route> routeList, OnItemClickListener listener) {
        this.routeList = routeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routeList.get(position);
        holder.bind(route, listener);
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView routeName;
        TextView routeDescription;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.routeName);
            routeDescription = itemView.findViewById(R.id.routeDescription);
        }

        public void bind(final Route route, final OnItemClickListener listener) {
            routeName.setText(route.getName());
            routeDescription.setText("Length: " + route.getLength() + ", Time: " + route.getTime());
            itemView.setOnClickListener(v -> listener.onItemClick(route));
        }
    }
}
