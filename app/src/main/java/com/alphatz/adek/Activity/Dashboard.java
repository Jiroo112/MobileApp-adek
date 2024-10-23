package com.alphatz.adek.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.alphatz.adek.Fragment.AsupanFragment;
import com.alphatz.adek.Fragment.BmiFragment;
import com.alphatz.adek.Fragment.HomeFragment;
import com.alphatz.adek.Fragment.KonsultasiFragment;
import com.alphatz.adek.Fragment.OlahragaFragment;
import com.alphatz.adek.Fragment.ProfileFragment;
import com.alphatz.adek.Fragment.SearchFragment;
import com.alphatz.adek.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Dashboard extends AppCompatActivity {

    private String username;
    private BottomNavigationView bottomNav;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inisialisasi views
        bottomNav = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.fab);

        // Menerima username dari Intent
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        // Pastikan data username diterima
        if (username != null) {
            Toast.makeText(this, "Welcome, " + username, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No username received", Toast.LENGTH_SHORT).show();
            username = "Pengguna"; // Default value jika username null
        }

        // Load default fragment (HomeFragment)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment.newInstance(username))
                    .commit();
        }

        loadFragment(HomeFragment.newInstance(username));

        // Setup Bottom Navigation
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.asupan) {
                selectedFragment = new AsupanFragment();
            } else if (itemId == R.id.search) {
                selectedFragment = new SearchFragment();
            } else if (itemId == R.id.konsultasi) {
                selectedFragment = new KonsultasiFragment();
            } else if (itemId == R.id.profile) {
                selectedFragment = ProfileFragment.newInstance(username);
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        // Setup Floating Action Button
        fab.setOnClickListener(view -> {
            // Action for FAB click - show HomeFragment
            loadFragment(HomeFragment.newInstance(username));
        });
    }

    // Method untuk memuat fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Method untuk menyembunyikan bottom navigation dan fab
    public void hideBottomNavigation() {
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }
        if (fab != null) {
            fab.setVisibility(View.GONE);
        }
    }

    // Method untuk menampilkan bottom navigation dan fab
    public void showBottomNavigation() {
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        // Cek apakah fragment saat ini adalah OlahragaFragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof OlahragaFragment) {
            // Tampilkan kembali bottom navigation saat kembali
            showBottomNavigation();
        }
        super.onBackPressed();
    }
}