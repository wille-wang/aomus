package com.example.myapplication.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentGalleryBinding;

import java.util.Arrays;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Sample photo list
        List<Integer> photoList = Arrays.asList(
                R.drawable.photo_par_112,
                R.drawable.photo_par_113,
                R.drawable.photo_par_122,
                R.drawable.photo_par_128,
                R.drawable.photo_par_134,
                R.drawable.photo_par_150
        );

        // Sample description list
        List<String> descriptionList = Arrays.asList(
                "Photo 1 description",
                "Photo 2 description",
                "Photo 3 description",
                "Photo 4 description",
                "Photo 5 description",
                "Photo 6 description"
        );

        // Set up the RecyclerView
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false)); // 1 photo per row (vertical)

        // Pass both the photo list and description list to the adapter
        PhotoWallAdapter adapter = new PhotoWallAdapter(photoList, descriptionList);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
