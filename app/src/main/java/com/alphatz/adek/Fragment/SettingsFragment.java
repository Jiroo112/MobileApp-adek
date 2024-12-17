package com.alphatz.adek.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alphatz.adek.Activity.LoginActivity;
import com.alphatz.adek.R;

public class SettingsFragment extends Fragment {

    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_NAMA_LENGKAP = "namaLengkap";

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView tvGantiPassword = view.findViewById(R.id.tv_changepw);
        TextView tvLogout = view.findViewById(R.id.logout_tv);
        LinearLayout layoutProfile = view.findViewById(R.id.layout_profile);
        TextView tvProfile = view.findViewById(R.id.text_profile);

        // Retrieve nama_lengkap from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, requireActivity().MODE_PRIVATE);
        String namaLengkap = sharedPreferences.getString(KEY_NAMA_LENGKAP, "Pengguna");

        // Klik untuk Ganti Password
        tvGantiPassword.setOnClickListener(v -> {
            Fragment changepwFragment = new ChangepwFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, changepwFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Update both click listeners to pass nama_lengkap
        tvProfile.setOnClickListener(v -> openDetailProfileFragment(namaLengkap));

        layoutProfile.setOnClickListener(v -> openDetailProfileFragment(namaLengkap));

        // Implementasi Logout yang terintegrasi dengan LoginActivity
        tvLogout.setOnClickListener(v -> performLogout());

        return view;
    }

    private void openDetailProfileFragment(String namaLengkap) {
        // Use the newInstance method to pass the name
        Fragment detailProfileFragment = DetailProfileFragment.newInstance(namaLengkap);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailProfileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void performLogout() {
        if (getActivity() != null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Konfirmasi Logout")
                    .setMessage("Apakah Anda yakin ingin keluar?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, getActivity().MODE_PRIVATE);
                        LoginActivity.logout(sharedPreferences);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        }
    }
}