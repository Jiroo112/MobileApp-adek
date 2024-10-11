package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alphatz.adek.R;

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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        makananBerat = view.findViewById(R.id.makanan_berat);
        minumanSehat = view.findViewById(R.id.minuman_sehat);
        desert = view.findViewById(R.id.desert);
        kardio = view.findViewById(R.id.kardio);
        kekuatan = view.findViewById(R.id.kekuatan);
        interval = view.findViewById(R.id.interval);
        tampilkanLebih = view.findViewById(R.id.tampilkan_lebih);

        setupClickListeners();

        // set up artikel 1
        setupArtikelInfo(view, R.id.artikel1, "Meningkatkan Laju Metabolisme", R.drawable.gambar_kosong, "Kesehatan", "60", "120");
        //artikel 2
        setupArtikelInfo(view, R.id.artikel2, "Panduan Makanan Sehat", R.drawable.gambar_kosong, "Gaya Hidup", "45", "100");
    }

    private void setupClickListeners() {
        makananBerat.setOnClickListener(v -> {
            showRoast();
            openResepFragment();
        });

        // tambain aja klik listener misal emg perlu
        // contohe ntar : minumanSehat.setOnClickListener(v -> openResepFragment());
    }

    private void openResepFragment() {
        Log.d("SearchFragment", "openResepFragment called");
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ResepFragment());
        transaction.addToBackStack(null); // ini pas back ga balik ke awal ( baliknya ke search fragment)
        transaction.commit();
        Log.d("SearchFragment", "Fragment transaction committed");
    }

    private void showRoast() {
        String roastMessage = "Makanan berat? Siapa bilang diet itu mudah!";
        Toast.makeText(getActivity(), roastMessage, Toast.LENGTH_SHORT).show();
    }

    private void setupArtikelInfo(View parentView, int artikelLayoutId, String judul, int imageResourceId, String kategori, String likeCount, String viewCount) {
        View artikelView = parentView.findViewById(artikelLayoutId);

        TextView judulArtikel = artikelView.findViewById(R.id.judulArtikel);
        ImageView gambarArtikel = artikelView.findViewById(R.id.gambarArtikel);
        TextView kategoriArtikel = artikelView.findViewById(R.id.kategoriArtikel);
        TextView likeCountArtikel = artikelView.findViewById(R.id.likeCountArtikel);
        TextView viewCountArtikel = artikelView.findViewById(R.id.viewCountArtikel);

        // set judul artikel dan gambar
        judulArtikel.setText(judul);
        gambarArtikel.setImageResource(imageResourceId);
        kategoriArtikel.setText(kategori);
        likeCountArtikel.setText(likeCount);
        viewCountArtikel.setText(viewCount);
    }
}
