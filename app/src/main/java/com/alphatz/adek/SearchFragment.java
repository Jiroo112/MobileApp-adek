package com.alphatz.adek;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SearchFragment extends Fragment {

    private ImageView makananBerat, minumanSehat, desert;
    private ImageView kardio, kekuatan, interval;
    private ImageView tampilkanLebih;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        makananBerat = view.findViewById(R.id.makanan_berat);
        minumanSehat = view.findViewById(R.id.minuman_sehat);
        desert = view.findViewById(R.id.desert);
        kardio = view.findViewById(R.id.kardio);
        kekuatan = view.findViewById(R.id.kekuatan);
        interval = view.findViewById(R.id.interval);
        tampilkanLebih = view.findViewById(R.id.tampilkan_lebih);

        // Set click listeners
        setupClickListeners();
    }

    private void setupClickListeners() {
        makananBerat.setOnClickListener(v -> {
            showRoast();
            openResepFragment();
        });

        // Add click listeners for other ImageViews if needed
        // Example: minumanSehat.setOnClickListener(v -> openResepFragment());
    }

    private void openResepFragment() {
        Log.d("SearchFragment", "openResepFragment called");
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ResepFragment());
        transaction.addToBackStack(null); // Allows the user to go back to SearchFragment
        transaction.commit();
        Log.d("SearchFragment", "Fragment transaction committed");
    }

    private void showRoast() {
        String roastMessage = "Makanan berat? Siapa bilang diet itu mudah!";
        Toast.makeText(getActivity(), roastMessage, Toast.LENGTH_SHORT).show();
    }
}
