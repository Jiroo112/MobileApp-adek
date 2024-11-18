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

    private String namaLengkap;
    private String idUser;
    private BottomNavigationView bottomNav;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inisialisasi views
        bottomNav = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.fab);

        // Menerima nama_lengkap dan idUser dari Intent
        Intent intent = getIntent();
        namaLengkap = intent.getStringExtra("namaLengkap");
        idUser = intent.getStringExtra("idUser");

        // Pastikan data nama_lengkap diterima
        if (namaLengkap != null) {
            Toast.makeText(this, "Welcome, " + namaLengkap, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No name received", Toast.LENGTH_SHORT).show();
            namaLengkap = "Pengguna"; // Default value jika nama_lengkap null
        }

        // Load default fragment (HomeFragment) hanya sekali saat pertama kali
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance(namaLengkap));
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
                selectedFragment = ProfileFragment.newInstance(namaLengkap);
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        // Set listener untuk FloatingActionButton
        fab.setOnClickListener(view -> loadFragment(HomeFragment.newInstance(namaLengkap)));
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
