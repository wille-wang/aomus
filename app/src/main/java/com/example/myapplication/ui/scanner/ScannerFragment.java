package com.example.myapplication.ui.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import org.json.JSONObject;

public class ScannerFragment extends Fragment {

  private static final int PICK_IMAGE_REQUEST = 1;
  private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
  private CompoundBarcodeView barcodeView;
  private DatabaseReference databaseReference;
  private final BarcodeCallback callback =
      new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
          if (result.getText() != null) {
            String scannedData = result.getText();
            handleScannedData(scannedData);
          } else {
            Log.e("ScannerFragment", "No barcode detected");
          }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
          // You can highlight result points on the camera preview if needed
        }
      };

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_scanner, container, false);
    barcodeView = view.findViewById(R.id.barcode_scanner);
    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // Initialize Firebase
    databaseReference = FirebaseDatabase.getInstance().getReference();

    // Request camera permission if not granted
    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
          getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    } else {
      // Start continuous scanning immediately
      barcodeView.decodeContinuous(callback);
    }

    view.findViewById(R.id.choosePhotoButton).setOnClickListener(v -> openGallery());

    return view;
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        barcodeView.decodeContinuous(callback);
      } else {
        Toast.makeText(
                getContext(), "Camera permission is required to scan barcodes", Toast.LENGTH_LONG)
            .show();
      }
    }
  }

  private void openGallery() {
    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(intent, PICK_IMAGE_REQUEST);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PICK_IMAGE_REQUEST) {
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
        String scannedData = result.getText();
        handleScannedData(scannedData);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Check if the user is logged in
  private boolean isLoggedIn() {
    SharedPreferences sharedPreferences =
        getActivity().getSharedPreferences("LoginPrefs", Activity.MODE_PRIVATE);
    return sharedPreferences.getBoolean("isLoggedIn", false);
  }

  // Handle the scanned data
  //   schema: { "app": "aomus", "action": "check-in", "code": "par-112" }
  private void handleScannedData(String scannedData) {
    if (!isLoggedIn()) {
      Toast.makeText(getContext(), "Please log in to check in", Toast.LENGTH_LONG).show();
      return;
    }

    try {
      JSONObject jsonObject = new JSONObject(scannedData);
      String app = jsonObject.getString("app");
      String action = jsonObject.getString("action");
      String code = jsonObject.getString("code");

      if ("aomus".equals(app) && "check-in".equals(action)) {
        // Store check-in records in Firebase
        storeCheckInRecord(code);

        Toast.makeText(getContext(), "Check in successfully", Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(getContext(), "Invalid QR code data", Toast.LENGTH_LONG).show();
      }
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(getContext(), "Failed to parse QR code data", Toast.LENGTH_LONG).show();
    }
  }

  // Store check-in records in Firebase
  private void storeCheckInRecord(String buildingCode) {
    SharedPreferences sharedPreferences =
        getActivity().getSharedPreferences("LoginPrefs", Activity.MODE_PRIVATE);
    String username = sharedPreferences.getString("username", "unknown");

    DatabaseReference userCheckinsRef =
        databaseReference.child("users").child(username).child("checkins").child(buildingCode);

    userCheckinsRef
        .child("counts")
        .get()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                long counts = 0;
                if (task.getResult().exists()) {
                  counts = task.getResult().getValue(Long.class);
                }
                userCheckinsRef.child("buildingCode").setValue(buildingCode);
                userCheckinsRef.child("counts").setValue(counts + 1);
                userCheckinsRef.child("lastCheckIn").setValue(System.currentTimeMillis());
              } else {
                userCheckinsRef.child("buildingCode").setValue(buildingCode);
                userCheckinsRef.child("counts").setValue(1);
                userCheckinsRef.child("lastCheckIn").setValue(System.currentTimeMillis());
              }
            });
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
