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

  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);

    binding = FragmentGalleryBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    // Visit https://www.unimelb.edu.au/filming-on-campus/gallery for sample photos

    // sample photo list
    List<Integer> photoList =
        Arrays.asList(
            R.drawable.photo_par_112,
            R.drawable.photo_par_113,
            R.drawable.photo_par_122,
            R.drawable.photo_par_128,
            R.drawable.photo_par_134,
            R.drawable.photo_par_150);

    // sample description list
    List<String> descriptionList =
        Arrays.asList(
            "Building 112: University House (1884)",
            "Building 113: Baldwin Spencer Building (1887)",
            "Building 122: System Garden Tower (1866)",
            "Building 128: Old Physics (1860)",
            "Building 134: Elisabeth Murdoch Building (1884)",
            "Building 150: Old Quadrangle (1854)");

    // Set up the RecyclerView
    RecyclerView recyclerView = binding.recyclerView;
    recyclerView.setLayoutManager(
        new LinearLayoutManager(
            getContext(), RecyclerView.VERTICAL, false)); // 1 photo per row (vertical)

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
