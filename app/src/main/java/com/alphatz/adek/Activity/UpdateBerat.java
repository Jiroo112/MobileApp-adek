package com.alphatz.adek.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alphatz.adek.R;

public class UpdateBerat extends DialogFragment {
    private ImageView imageView;
    private Button btnIya, btnTidak;
    private int imageResourceId = R.drawable.update_usia; // Set default gambar "makanan_kering"

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupListeners();
    }

    private void initializeViews(View view) {
        imageView = view.findViewById(R.id.konfirmasi_image);
        btnIya = view.findViewById(R.id.btn_iya);
        btnTidak = view.findViewById(R.id.btn_tidak);

        imageView.setImageResource(imageResourceId);
    }

    private void setupListeners() {
        btnIya.setOnClickListener(v -> onIyaClicked());
        btnTidak.setOnClickListener(v -> onTidakClicked());
    }

    private void onIyaClicked() {
        // Logic untuk tombol Iya
        dismiss();
    }

    private void onTidakClicked() {
        // Logic untuk tombol Tidak
        dismiss();
    }
}
