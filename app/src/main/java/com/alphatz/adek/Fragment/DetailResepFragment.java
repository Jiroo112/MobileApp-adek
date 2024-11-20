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
 */
public class DetailResepFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGE_RES_ID = "imageResId";

    private String title;
    private String description;
    private int imageResId;

    private ImageView gambar_resep;
    private TextView judul_resep, deskripsi_resep;

    public DetailResepFragment() {
        // Required empty public constructor
    }

    public static DetailResepFragment newInstance(String title, String description, int imageResId) {
        DetailResepFragment fragment = new DetailResepFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE, "Default Title"); // Default Title if null
            description = getArguments().getString(ARG_DESCRIPTION, "Default Description"); // Default Description if null
            imageResId = getArguments().getInt(ARG_IMAGE_RES_ID, R.drawable.default_image); // Default image resource if null
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

        // Set data ke UI
        judul_resep.setText(title);
        deskripsi_resep.setText(description);
        gambar_resep.setImageResource(imageResId); // Set gambar berdasarkan resource ID

        return view;
    }
}