package com.alphatz.adek.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alphatz.adek.Activity.LoginActivity;
import com.alphatz.adek.R;

public class SettingsFragment extends Fragment {

    private static final String PREF_NAME = "LoginPrefs"; // Sama dengan di LoginActivity

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView tvGantiPassword = view.findViewById(R.id.tv_changepw);
        TextView tvLogout = view.findViewById(R.id.logout_tv);

        // Klik untuk Ganti Password
        tvGantiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment changepwFragment = new ChangepwFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, changepwFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Implementasi Logout yang terintegrasi dengan LoginActivity
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });

        return view;
    }

    private void performLogout() {
        if (getActivity() != null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Konfirmasi Logout")
                    .setMessage("Apakah Anda yakin ingin keluar?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, getActivity().MODE_PRIVATE);
                            LoginActivity.logout(sharedPreferences);
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("Tidak", null) // Jika "Tidak" ditekan, dialog akan ditutup
                    .show();
        }
    }
}
