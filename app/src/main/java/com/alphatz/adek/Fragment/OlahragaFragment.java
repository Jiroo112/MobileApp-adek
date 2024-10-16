package com.alphatz.adek.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alphatz.adek.R;

public class OlahragaFragment extends Fragment {

    private Button btnKekuatan, btnKelenturan;

    public OlahragaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_olahraga, container, false);

        btnKekuatan = view.findViewById(R.id.btn_kekuatan);
        btnKelenturan = view.findViewById(R.id.btn_kelenturan);

        btnKekuatan.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new KekuatanFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        btnKelenturan.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new KelenturanFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}
