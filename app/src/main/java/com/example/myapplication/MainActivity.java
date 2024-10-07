package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.myapplication.ai.chatbot.ChatbotFragment;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.gallery.GalleryFragment;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.library.LibraryFragment;
import com.example.myapplication.ui.login.LoginActivity;
import com.example.myapplication.ui.map.MapFragment;
import com.example.myapplication.ui.scanner.ScannerFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private static final String TAG = "MainActivity";
  private AppBarConfiguration mAppBarConfiguration;
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    setSupportActionBar(binding.appBarMain.toolbar);
//    binding.appBarMain.fab.setOnClickListener(
//        new View.OnClickListener() {
//          @Override
//          public void onClick(View view) {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab)
//                .show();
//          }
//        });
    DrawerLayout drawer = binding.drawerLayout;
    NavigationView navigationView = binding.navView;
    mAppBarConfiguration =
        new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_library,
                R.id.nav_scanner,
                R.id.nav_map,
                R.id.nav_chatbot)
            .setOpenableLayout(drawer)
            .build();
    NavController navController =
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);

    navigationView.setNavigationItemSelectedListener(this);

    // Initial update of nav_header_main with username if logged in
    updateNavHeaderUsername();
  }

  @Override
  protected void onResume() {
    super.onResume();
    updateNavHeaderUsername();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController =
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
        || super.onSupportNavigateUp();
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      // Hide the keyboard
      View view = this.getCurrentFocus();
      if (view != null) {
        InputMethodManager imm =
            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
          imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
      }
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.nav_home) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.nav_host_fragment_content_main, new HomeFragment())
          .commit();
    } else if (id == R.id.nav_gallery) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.nav_host_fragment_content_main, new GalleryFragment())
          .commit();
    } else if (id == R.id.nav_scanner) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.nav_host_fragment_content_main, new ScannerFragment())
          .commit();
    } else if (id == R.id.nav_map) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.nav_host_fragment_content_main, new MapFragment())
          .commit();
    } else if (id == R.id.nav_library) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.nav_host_fragment_content_main, new LibraryFragment())
          .commit();
    } else if (id == R.id.nav_chatbot) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.nav_host_fragment_content_main, new ChatbotFragment())
          .commit();
    }

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  // click the icon to be navigated to the login page
  public void toLoginPage(View view) {
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
  }

  // update the username in the nav header
  private void updateNavHeaderUsername() {
    Log.d(TAG, "Updating nav header username");
    SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
    String username = sharedPreferences.getString("username", "Log In or Register");

    NavigationView navigationView = binding.navView;
    if (navigationView == null) {
      Log.e(TAG, "NavigationView is null");
      return;
    }

    View headerView = navigationView.getHeaderView(0);
    if (headerView == null) {
      Log.e(TAG, "HeaderView is null");
      return;
    }

    TextView usernameTextView = headerView.findViewById(R.id.usernameTextView);
    if (usernameTextView != null) {
      Log.d(TAG, "Username TextView found");
      usernameTextView.setText(isLoggedIn ? username : "Log In or Register");
    } else {
      Log.e(TAG, "Username TextView not found");
    }
  }

  // handle the profile click
  public void handleProfileClick(View view) {
    SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
    boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

    Intent intent;
    if (isLoggedIn) {
      // Redirect to SettingsActivity
      intent = new Intent(this, SettingsActivity.class);
    } else {
      // Redirect to LoginActivity
      intent = new Intent(this, LoginActivity.class);
    }
    startActivity(intent);
  }
}
