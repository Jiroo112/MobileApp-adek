package com.alphatz.adek.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphatz.adek.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link detail_resep#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detail_resep extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ImageView gambar_resep;
    private TextView judul_resep, deskripsi_resep;

    public detail_resep() {
        // Required empty public constructor
    }

    public static detail_resep newInstance(String param1, String param2) {
        detail_resep fragment = new detail_resep();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_resep, container, false);

        // Initialize the views
        gambar_resep = view.findViewById(R.id.gambar_resep);
        judul_resep = view.findViewById(R.id.judul_resep);
        deskripsi_resep = view.findViewById(R.id.deskripsi_resep);

        // Set the content of the views
        // Assuming mParam1 is the title and mParam2 is the description
        judul_resep.setText(mParam1);
        deskripsi_resep.setText(mParam2);

        // If you have a drawable resource for the image, you can set it like this:
        // gambar_resep.setImageResource(R.drawable.your_image_resource);

        return view;
    }
}