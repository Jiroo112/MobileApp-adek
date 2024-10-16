package com.alphatz.adek.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alphatz.adek.R;

public class Konfirmasi extends DialogFragment {

    private ImageView imageView;
    private Button btnIya, btnTidak;

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false); // Membuat dialog tidak bisa ditutup dengan sentuhan di luar

        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_konfirmasi, container, false);

        imageView = view.findViewById(R.id.konfirmasi_image);
        btnIya = view.findViewById(R.id.btn_iya);
        btnTidak = view.findViewById(R.id.btn_tidak);

        // Set gambar
        imageView.setImageResource(R.drawable.konf_kons_kosong);

        // Set OnClickListener untuk tombol Iya
        btnIya.setOnClickListener(v -> {
            // Logika untuk tombol Iya
            dismiss(); // Menutup dialog
        });

        // Set OnClickListener untuk tombol Tidak
        btnTidak.setOnClickListener(v -> {
            // Logika untuk tombol Tidak
            dismiss(); // Menutup dialog
        });

        return view;
    }
}
