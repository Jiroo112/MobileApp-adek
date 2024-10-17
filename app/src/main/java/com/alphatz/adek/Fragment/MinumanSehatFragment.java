package com.alphatz.adek.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alphatz.adek.R;

public class MinumanSehatFragment extends Fragment {

    private Button btnMakananSehat, btnDesert, btnFilter;

    public MinumanSehatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minuman_sehat, container, false);

        btnMakananSehat = view.findViewById(R.id.btn_makanan_sehat);
        btnDesert = view.findViewById(R.id.btn_desert);
        btnFilter = view.findViewById(R.id.btn_filter);

        btnMakananSehat.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new MakananBeratFragment());
            transaction.addToBackStack(null); // Menambahkan fragment ke backstack agar bisa kembali
            transaction.commit();
        });
        btnFilter.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new ResepFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
        btnDesert.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new DessertFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Tambahkan logika lainnya untuk tombol jika diperlukan
        return view;
    }
}
