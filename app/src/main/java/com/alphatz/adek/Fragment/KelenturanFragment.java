package com.alphatz.adek.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.activity.OnBackPressedCallback;

import com.alphatz.adek.Activity.Dashboard;
import com.alphatz.adek.R;

public class KelenturanFragment extends Fragment {

    private Button btnKekuatan, btnFilter;

    public KelenturanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle back press
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // nampilin bottom navigation sebelum balik ke SearchFragment
                if (getActivity() instanceof Dashboard) {
                    ((Dashboard) getActivity()).showBottomNavigation();
                }

                // balik lagi ke SearchFragment
                navigateToSearchFragment();

                // hapus callback setelah digunakan
                remove();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Sembunyikan bottom navigation saat fragment ini ditampilkan
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).hideBottomNavigation();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelenturan, container, false);

        btnKekuatan = view.findViewById(R.id.btn_kekuatan);
        btnFilter = view.findViewById(R.id.btn_filter);

        btnKekuatan.setOnClickListener(v -> navigateToFragment(new KekuatanFragment()));
        btnFilter.setOnClickListener(v -> navigateToFragment(new OlahragaFragment()));

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToSearchFragment() {
        // Clear back stack nya dlu
        requireActivity().getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // ganti sama  SearchFragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SearchFragment());
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // nampilin lagi bot nav pas fragment di destroy
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).showBottomNavigation();
        }
    }
}