package com.alphatz.adek.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.alphatz.adek.R;

public class Konfirmasi extends DialogFragment {
    private ImageView imageView;
    private Button btnIya, btnTidak;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // No title bar
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent); // Transparent background
        dialog.setCanceledOnTouchOutside(false); // Dialog cannot be canceled by touching outside
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_konsultasi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupListeners();
        setDialogImage();
    }

    private void initializeViews(View view) {
        imageView = view.findViewById(R.id.konfirmasi_image);
        btnIya = view.findViewById(R.id.btn_iya);
        btnTidak = view.findViewById(R.id.btn_tidak);
    }

    private void setupListeners() {
        btnIya.setOnClickListener(v -> onIyaClicked());
        btnTidak.setOnClickListener(v -> onTidakClicked());
    }

    private void setDialogImage() {
        imageView.setImageResource(R.drawable.konf_kons_kosong);
    }

    private void onIyaClicked() {
        String phoneNumber = "+6282132640141";
        String message = "Permisi dok, saya ingin berkonsultasi..";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message)));

        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "WhatsApp tidak terinstall", Toast.LENGTH_SHORT).show();
        }

        dismiss();
    }

    private void onTidakClicked() {
        dismiss();
    }
}
