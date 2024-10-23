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

public class KekuatanFragment extends Fragment {

    private Button btnKelenturan, btnFilter;

    public KekuatanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle back press
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Show bottom navigation before navigating back
                if (getActivity() instanceof Dashboard) {
                    ((Dashboard) getActivity()).showBottomNavigation();
                }

                // balik lagi ke fragment KelenturanFragment
                navigateToKelenturanFragment();

                // Remove callback after use
                remove();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // ini sama kek yang ada di OlahragaFragment
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).hideBottomNavigation();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kekuatan, container, false);

        btnKelenturan = view.findViewById(R.id.btn_kelenturan);
        btnFilter = view.findViewById(R.id.btn_filter);

        btnKelenturan.setOnClickListener(v -> navigateToFragment(new KelenturanFragment()));
        btnFilter.setOnClickListener(v -> navigateToFragment(new OlahragaFragment()));

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToKelenturanFragment() {
        requireActivity().getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new KelenturanFragment());
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // sama kaya di OlahragaFragment
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).showBottomNavigation();
        }
    }
}
