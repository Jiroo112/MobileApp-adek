package com.alphatz.adek.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alphatz.adek.R;

public class KelenturanFragment extends Fragment {

    private Button btnKekuatan, btnFilter;

    public KelenturanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelenturan, container, false);

        btnKekuatan = view.findViewById(R.id.btn_kekuatan);
        btnFilter = view.findViewById(R.id.btn_filter);

        btnKekuatan.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new KekuatanFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        btnFilter.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new OlahragaFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
