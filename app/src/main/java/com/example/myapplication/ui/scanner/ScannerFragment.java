package com.example.myapplication.ui.scanner;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import java.io.InputStream;
import java.util.List;

public class ScannerFragment extends Fragment {

  private static final int PICK_IMAGE_REQUEST = 1;
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

    view.findViewById(R.id.choosePhotoButton).setOnClickListener(v -> openGallery());

    return view;
  }

  private void openGallery() {
    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(intent, PICK_IMAGE_REQUEST);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST) {
      getActivity();
      if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
        Uri imageUri = data.getData();
        decodeImage(imageUri);
      }
    }
  }

  private void decodeImage(Uri imageUri) {
    try {
      InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
      Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
      int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
      bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

      com.google.zxing.RGBLuminanceSource source =
          new com.google.zxing.RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
      BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
      Result result = new MultiFormatReader().decode(binaryBitmap);

      if (result != null) {
        // Handle the decoded QR code result
        String scannedData = result.getText();
        // Process the scannedData (e.g., update the UI or do something with it)
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
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
