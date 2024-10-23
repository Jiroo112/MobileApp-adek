package com.alphatz.adek.Fragment;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alphatz.adek.R;

public class MinumanSehatFragment extends Fragment {

    private Button btnMakananSehat, btnDessert, btnFilter;

    public MinumanSehatFragment() {
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
        View view = inflater.inflate(R.layout.fragment_minuman_sehat, container, false);

        btnMakananSehat = view.findViewById(R.id.btn_makanan_sehat);
        btnDessert = view.findViewById(R.id.btn_desert);
        btnFilter = view.findViewById(R.id.btn_filter);

        btnMakananSehat.setOnClickListener(v -> navigateToFragment(new MakananBeratFragment()));
        btnFilter.setOnClickListener(v -> navigateToFragment(new ResepFragment()));
        btnDessert.setOnClickListener(v -> navigateToFragment(new DessertFragment()));

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