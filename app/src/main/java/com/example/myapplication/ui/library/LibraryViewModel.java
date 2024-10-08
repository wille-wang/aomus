package com.example.myapplication.ui.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LibraryViewModel extends ViewModel {

  private final MutableLiveData<String> url;

  public LibraryViewModel() {
    url = new MutableLiveData<>();
    // TODO: Set the URL of study space here
    url.setValue("https://library.unimelb.edu.au/services/book-a-room-or-computer");
  }

  public LiveData<String> getUrl() {
    return url;
  }

  public void setUrl(String newUrl) {
    url.setValue(newUrl);
  }
}
