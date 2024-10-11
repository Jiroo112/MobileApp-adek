package com.alphatz.adek.Fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphatz.adek.Activity.LoginActivity;
import com.alphatz.adek.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 *
 */
public class ResepFragment extends Fragment {

    public ResepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ngeganti nama resep soalnya pake item
        View view = inflater.inflate(R.layout.fragment_resep, container, false);

        //susu_stroberi itu di resep_fragment, dst
        setupResepInfo(view, R.id.susu_stroberi, "Susu Stroberi", R.drawable.ic_profil0);

        setupResepInfo(view, R.id.es_jeruk, "Es Jeruk", R.drawable.ic_profil0);

        setupResepInfo(view, R.id.es_teh, "Es Teh", R.drawable.ic_profil0);

        return view;
    }

    private void setupResepInfo(View parentView, int resepLayoutId, String resepName, int imageResourceId) {
        View resepView = parentView.findViewById(resepLayoutId);

        TextView namaResep = resepView.findViewById(R.id.nama_resep);
        ImageView fotoResep = resepView.findViewById(R.id.foto_resep);
        TextView kategoriResep = resepView.findViewById(R.id.kategori_resep);
        TextView likeCount = resepView.findViewById(R.id.likeCount);
        TextView viewCount = resepView.findViewById(R.id.viewCount);

        // set nama dan foto resep
        namaResep.setText(resepName);
        fotoResep.setImageResource(imageResourceId);
        kategoriResep.setText("Minuman Sehat");
        likeCount.setText("60");
        viewCount.setText("120");
    }
}
