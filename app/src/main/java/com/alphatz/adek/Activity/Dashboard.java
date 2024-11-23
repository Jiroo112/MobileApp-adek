package com.alphatz.adek.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAMA_LENGKAP = "namaLengkap";
    private static final String KEY_ID_USER = "idUser";

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

        // Mengambil data dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        namaLengkap = sharedPreferences.getString(KEY_NAMA_LENGKAP, "");
        idUser = sharedPreferences.getString(KEY_ID_USER, "");

        // Debugging untuk memastikan data terambil
        Log.d("DashboardActivity", "Loaded from SharedPreferences: email=" + email + ", namaLengkap=" + namaLengkap + ", idUser=" + idUser);

        // Validasi data
        if (namaLengkap == null || namaLengkap.isEmpty()) {
            Toast.makeText(this, "Data tidak ditemukan, silakan login ulang.", Toast.LENGTH_SHORT).show();
            finish(); // Tutup Dashboard jika data tidak valid
            return;
        }

        // Tampilkan pesan selamat datang
        Toast.makeText(this, "Welcome, " + namaLengkap, Toast.LENGTH_LONG).show();

        // Load default fragment (HomeFragment) hanya sekali saat pertama kali
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance(namaLengkap));
        }

        // Set listener untuk BottomNavigationView
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
