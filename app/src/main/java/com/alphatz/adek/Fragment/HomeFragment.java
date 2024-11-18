package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alphatz.adek.R;

public class HomeFragment extends Fragment {

    TextView welcome;

    private static final String ARG_NAMA_LENGKAP = "nama_lengkap";

    private String namaLengkap;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String namaLengkap) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAMA_LENGKAP, namaLengkap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            namaLengkap = getArguments().getString(ARG_NAMA_LENGKAP, "Pengguna");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Menampilkan nama lengkap pengguna di welcome message
        welcome = view.findViewById(R.id.welcome);
        welcome.setText("Halo, " + namaLengkap + "!");

        return view;
    }
}
