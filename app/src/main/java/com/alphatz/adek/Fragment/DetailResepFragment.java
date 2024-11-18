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
 * Use the {@link DetailResepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailResepFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ImageView gambar_resep;
    private TextView judul_resep, deskripsi_resep;

    public DetailResepFragment() {
        // Required empty public constructor
    }

    public static DetailResepFragment newInstance(String param1, String param2) {
        DetailResepFragment fragment = new DetailResepFragment();
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

        gambar_resep = view.findViewById(R.id.gambar_resep);
        judul_resep = view.findViewById(R.id.judul_resep);
        deskripsi_resep = view.findViewById(R.id.deskripsi_resep);

        judul_resep.setText(mParam1);
        deskripsi_resep.setText(mParam2);

        return view;
    }
}