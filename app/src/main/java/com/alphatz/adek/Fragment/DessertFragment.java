package com.alphatz.adek.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.activity.OnBackPressedCallback;

import com.alphatz.adek.R;

public class DessertFragment extends Fragment {

    private Button btnMinumanSehat, btnMakananSehat, btnFilter;

    public DessertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle back press
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToResepFragment();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dessert, container, false);

        btnMinumanSehat = view.findViewById(R.id.btn_minuman_sehat);
        btnMakananSehat = view.findViewById(R.id.btn_makanan_sehat);
        btnFilter = view.findViewById(R.id.btn_filter);

        btnMakananSehat.setOnClickListener(v -> navigateToFragment(new MakananBeratFragment()));
        btnFilter.setOnClickListener(v -> navigateToFragment(new ResepFragment()));
        btnMinumanSehat.setOnClickListener(v -> navigateToFragment(new MinumanSehatFragment()));

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToResepFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ResepFragment());
        transaction.commit();
    }

    public boolean isCurrentFragment() {
        return isVisible() && getUserVisibleHint();
    }
}