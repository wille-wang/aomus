package com.example.myapplication.ui.scanner;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import java.util.List;

public class ScannerFragment extends Fragment {

  private final BarcodeCallback callback =
      new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
          if (result.getText() != null) {
            // Handle scanned QR code result
            String scannedData = result.getText();
            // Process the scannedData (e.g., update the UI or do something with it)
          }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
          // You can highlight result points on the camera preview if needed
        }
      };
  private CompoundBarcodeView barcodeView;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_scanner, container, false);
    barcodeView = view.findViewById(R.id.barcode_scanner);
    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // Start continuous scanning immediately
    barcodeView.decodeContinuous(callback);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    barcodeView.resume();
  }

  @Override
  public void onPause() {
    super.onPause();
    barcodeView.pause();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
  }
}
