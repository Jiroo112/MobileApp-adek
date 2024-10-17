package com.alphatz.adek.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphatz.adek.R;

public class ResepFragment extends Fragment {

    private Button btnMakananBerat, btnDesert, btnMinumanSehat;

    public ResepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resep, container, false);

        // Setup resep items
        setupResepInfo(view, R.id.susu_stroberi, "Susu Stroberi", R.drawable.ic_profil0);
        setupResepInfo(view, R.id.es_jeruk, "Es Jeruk", R.drawable.ic_profil0);
        setupResepInfo(view, R.id.es_teh, "Es Teh", R.drawable.ic_profil0);

        // Setup buttons
        btnMakananBerat = view.findViewById(R.id.btn_makanan_berat);
        btnDesert = view.findViewById(R.id.btn_desert);
        btnMinumanSehat = view.findViewById(R.id.btn_minuman_sehat);

        btnMakananBerat.setOnClickListener(v -> {
            // Handle filter logic or replace with another fragment
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new MakananBeratFragment()); // Replace with the correct fragment for filter
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Set action for Dessert button
        btnDesert.setOnClickListener(v -> {
            // Replace with DessertFragment or another fragment for desserts
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new DessertFragment()); // Replace with DessertFragment
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Set action for Minuman Sehat button
        btnMinumanSehat.setOnClickListener(v -> {
            // Replace with MinumanSehatFragment
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new MinumanSehatFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private void setupResepInfo(View parentView, int resepLayoutId, String resepName, int imageResourceId) {
        View resepView = parentView.findViewById(resepLayoutId);

        TextView namaResep = resepView.findViewById(R.id.nama_resep);
        ImageView fotoResep = resepView.findViewById(R.id.foto_resep);
        TextView kategoriResep = resepView.findViewById(R.id.kategori_resep);
        TextView likeCount = resepView.findViewById(R.id.likeCount);
        TextView viewCount = resepView.findViewById(R.id.viewCount);

        // Set nama dan foto resep
        namaResep.setText(resepName);
        fotoResep.setImageResource(imageResourceId);
        kategoriResep.setText("Minuman Sehat");
        likeCount.setText("60");
        viewCount.setText("120");
    }
}
