package com.alphatz.adek;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        setupAnimations();
    }

    private void setupAnimations() {
        setClickListener(makananBerat);
        setClickListener(minumanSehat);
        setClickListener(desert);
        setClickListener(kardio);
        setClickListener(kekuatan);
        setClickListener(interval);
        setClickListener(tampilkanLebih);
    }

    private void setClickListener(View view) {
        view.setOnClickListener(v -> animateView(v));
    }

    private void animateView(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(200);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();

        // TODO: Add your click handling logic here
        // For example: if (view.getId() == R.id.makanan_berat) { // Handle makanan berat click }
    }
}