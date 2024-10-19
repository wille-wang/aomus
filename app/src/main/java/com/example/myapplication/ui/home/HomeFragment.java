package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.R;
import com.example.myapplication.ai.chatbot.ChatbotFragment;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.gallery.GalleryFragment;
import com.example.myapplication.ui.library.LibraryFragment;
import com.example.myapplication.ui.map.MapFragment;
import com.example.myapplication.ui.scanner.ScannerFragment;

public class HomeFragment extends Fragment {

  private FragmentHomeBinding binding;

  @Override
  public View onCreateView(
          @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

    binding = FragmentHomeBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    // Set the text of the home fragment
    final TextView textView = binding.textHome;
    homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

    // Set onClickListeners for each card
    binding.mapCard.setOnClickListener(v -> openFragment(new MapFragment()));
    binding.photoCard.setOnClickListener(v -> openFragment(new GalleryFragment()));
    binding.libraryCard.setOnClickListener(v -> openFragment(new LibraryFragment()));
    binding.botCard.setOnClickListener(v -> openFragment(new ChatbotFragment()));
    binding.cameraCard.setOnClickListener(v -> openFragment(new ScannerFragment()));

    return root;
  }

  private void openFragment(Fragment fragment) {
    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.nav_host_fragment_content_main, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
