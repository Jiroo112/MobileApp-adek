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

    private static final String ARG_USERNAME = "username";

    private String username;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String username) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME, "Pengguna");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


       /* ini buat ngambil uname dari db (ngambilnya dari url di login activity (atas sendiri),
        setelah uname didapet, uname kemudian dibawa ke dashboard -> baru ke sini */
        welcome = view.findViewById(R.id.welcome);
        welcome.setText("Halo, " + username + "! Selamat datang di aplikasi.");

        return view;
    }
}