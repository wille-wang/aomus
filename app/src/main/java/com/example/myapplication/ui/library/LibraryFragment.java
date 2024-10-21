package com.example.myapplication.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;

public class LibraryFragment extends Fragment {

  private LibraryViewModel libraryViewModel;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_library, container, false);

    // Initialize the ViewModel
    libraryViewModel = new ViewModelProvider(this).get(LibraryViewModel.class);

    // Get the WebView and load a web page
    WebView webView = view.findViewById(R.id.libraryWebView);
    // Ensure links open within the WebView
    webView.setWebViewClient(new WebViewClient());

    // Observe the URL LiveData from the ViewModel
    libraryViewModel.getUrl().observe(getViewLifecycleOwner(), webView::loadUrl);

    return view;
  }
}
