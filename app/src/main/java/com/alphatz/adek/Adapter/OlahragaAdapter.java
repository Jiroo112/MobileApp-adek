package com.alphatz.adek.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Model.OlahragaModel;
import com.alphatz.adek.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class OlahragaAdapter extends RecyclerView.Adapter<OlahragaAdapter.OlahragaViewHolder> {
    private List<OlahragaModel> olahragaList;
    private final OnOlahragaClickListener listener;
    private final FragmentManager fragmentManager;

    public interface OnOlahragaClickListener {
        void onOlahragaClick(OlahragaModel olahraga);
    }

    public OlahragaAdapter(List<OlahragaModel> olahragaList, OnOlahragaClickListener listener, FragmentManager fragmentManager) {
        this.olahragaList = olahragaList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
    }

    public void updateList(List<OlahragaModel> newList) {
        this.olahragaList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OlahragaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_olahraga, parent, false);
        return new OlahragaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OlahragaViewHolder holder, int position) {
        OlahragaModel olahraga = olahragaList.get(position);
        holder.bind(olahraga);
    }

    @Override
    public int getItemCount() {
        return olahragaList != null ? olahragaList.size() : 0;
    }

    class OlahragaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaOlahraga;
        TextView tvDeskripsi;
        ImageView ivOlahraga;

        OlahragaViewHolder(View itemView) {
            super(itemView);
            tvNamaOlahraga = itemView.findViewById(R.id.tv_nama_olahraga);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi_olahraga);
            ivOlahraga = itemView.findViewById(R.id.iv_olahraga);

            // Set the click listener here once
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    OlahragaModel olahraga = olahragaList.get(position);
                    listener.onOlahragaClick(olahraga);
                    showDialog(itemView.getContext(), olahraga);
                }
            });
        }

        void bind(final OlahragaModel olahraga) {
            if (olahraga != null) {
                tvNamaOlahraga.setText(olahraga.getNamaOlahraga());
                tvDeskripsi.setText(olahraga.getDeskripsi());

                // Log the URL for debugging
                Log.d("OlahragaAdapter", "Loading image URL: " + olahraga.getGambarUrl());

                // Normalize and load image
                String imageUrl = olahraga.getGambarUrl();
                if (imageUrl != null && !imageUrl.startsWith("http")) {
                    String baseUrl = "https://adek-app.my.id/";
                    imageUrl = baseUrl + imageUrl;
                }

                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.button_filter)
                        .error(R.drawable.button_filter)
                        .into(ivOlahraga);
            }
        }
    }


    private void showDialog(Context context, OlahragaModel olahraga) {
        if (!(context instanceof FragmentActivity)) return;

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_detail_olahraga, null);
        builder.setView(dialogView);

        // Initialize views
        TextView titleText = dialogView.findViewById(R.id.title_olahraga);
        TextView deskripsiText = dialogView.findViewById(R.id.detail_olahraga);
        ImageView imageOlahraga = dialogView.findViewById(R.id.image_olahraga);
        Button closeButton = dialogView.findViewById(R.id.btn_close);

        // Set title (exercise name)
        titleText.setText(olahraga.getNamaOlahraga());

        // Set description
        String deskripsi = olahraga.getCaraOlahraga();
        if (deskripsi == null || deskripsi.isEmpty()) {
            deskripsi = "Tunggu ya, Bentarlagi cara olahraganya akan diupdate";
        }
        deskripsiText.setText(deskripsi);

        // Load image using Glide
        Glide.with(context)
                .load(olahraga.getGambarUrl())
                .placeholder(R.drawable.button_filter) // Placeholder image
                .error(R.drawable.button_filter) // Error image
                .into(imageOlahraga);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        closeButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
