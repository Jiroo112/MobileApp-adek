package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alphatz.adek.R;

public class AddAsupanFragment extends Fragment {

    public AddAsupanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_asupan, container, false);

        Button btnSimpan = view.findViewById(R.id.btnSimpan);
        if (btnSimpan != null) {
            btnSimpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Here you can add the logic to save the data
                    Toast.makeText(getContext(), "Data saved", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }
}

