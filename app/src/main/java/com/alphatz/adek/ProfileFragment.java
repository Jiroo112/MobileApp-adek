package com.alphatz.adek;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ProfileFragment extends Fragment {

    private ImageView dokter_fav;
    private ImageView tipe_diet;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button logoutButton = view.findViewById(R.id.button_logout);

        // Set click listener for the logout button
        logoutButton.setOnClickListener(v -> logout());

        tipe_diet = view.findViewById(R.id.tipe_diet);
        dokter_fav = view.findViewById(R.id.dokter_fav);

        setupAnimations();

        // Set click listener for tipe_diet to open ResepFragment
        tipe_diet.setOnClickListener(v -> openResepFragment());

        return view;
    }

    private void setupAnimations() {
        dokter_fav.setOnClickListener(v -> animateView(dokter_fav));
    }

    private void animateView(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f, 1f);

        scaleX.setDuration(300);
        scaleY.setDuration(300);

        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());

        scaleX.start();
        scaleY.start();
    }

    private void logout() {
        // Clear SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Navigate back to LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void openResepFragment() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ResepFragment());
        transaction.addToBackStack(null); // Allow user to go back to ProfileFragment
        transaction.commit();
    }
}
