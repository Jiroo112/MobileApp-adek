package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alphatz.adek.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AsupanFragment extends Fragment {

    public AsupanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asupan, container, false);

        Button btnTambahAsupan = view.findViewById(R.id.btn_tambahasupan);
        btnTambahAsupan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAsupanBottomSheet();
            }
        });

        return view;
    }

    private void showAsupanBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_asupan, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView titleTextView = bottomSheetView.findViewById(R.id.text_title);
        EditText takaranEditText = bottomSheetView.findViewById(R.id.edit_text_takaran);
        EditText porsiEditText = bottomSheetView.findViewById(R.id.edit_text_porsi);
        TextView kaloriTextView = bottomSheetView.findViewById(R.id.text_kalori);
        TextView proteinTextView = bottomSheetView.findViewById(R.id.text_protein);
        TextView karboTextView = bottomSheetView.findViewById(R.id.text_karbo);
        TextView gulaTextView = bottomSheetView.findViewById(R.id.text_gula);
        Button tambahkanButton = bottomSheetView.findViewById(R.id.button_tambahkan);

        titleTextView.setText("Nasi Putih");
        kaloriTextView.setText("Kalori : 1.4");
        proteinTextView.setText("Protein: 0.025");
        karboTextView.setText("Karbo : 0.285");
        gulaTextView.setText("Gula : 0.000");

        tambahkanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Tambahkan" button click
                // You can add logic here to save the data
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }
}