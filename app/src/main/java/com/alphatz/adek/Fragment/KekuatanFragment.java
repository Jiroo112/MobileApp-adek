package com.alphatz.adek.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alphatz.adek.R;

public class KekuatanFragment extends Fragment {

    private Button btnKelenturan, btnFilter;

    public KekuatanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kekuatan, container, false);

        btnKelenturan = view.findViewById(R.id.btn_kelenturan);
        btnFilter = view.findViewById(R.id.btn_filter);

        btnKelenturan.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new KelenturanFragment());
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
