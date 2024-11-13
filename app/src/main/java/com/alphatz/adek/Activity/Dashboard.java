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

        // Load default fragment (HomeFragment) hanya sekali saat pertama kali
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance(username));
        }

        // Set listener untuk BottomNavigationView
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            // Logika if-else untuk memilih fragment berdasarkan item yang diklik
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

        // Set listener untuk FloatingActionButton
        fab.setOnClickListener(view -> loadFragment(HomeFragment.newInstance(username)));
    }

    // Fungsi untuk memuat fragment ke dalam kontainer
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Fungsi untuk menyembunyikan BottomNavigationView dan FloatingActionButton
    public void hideBottomNavigation() {
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }
        if (fab != null) {
            fab.setVisibility(View.GONE);
        }
    }

    // Fungsi untuk menampilkan BottomNavigationView dan FloatingActionButton
    public void showBottomNavigation() {
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
        }
    }
}
