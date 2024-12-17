package com.alphatz.adek.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Model.ArtikelModel;
import com.alphatz.adek.R;
import com.alphatz.adek.Fragment.DetailArtikelFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder> {
    private List<ArtikelModel> artikelList;
    private final OnArtikelClickListener listener;
    private final FragmentManager fragmentManager;

    public interface OnArtikelClickListener {
        void onArtikelClick(ArtikelModel artikel);
    }

    public ArtikelAdapter(List<ArtikelModel> artikelList, OnArtikelClickListener listener, FragmentManager fragmentManager) {
        this.artikelList = artikelList != null ? artikelList : List.of(); // Null safety
        this.listener = listener;
        this.fragmentManager = fragmentManager;
    }

    public void updateList(List<ArtikelModel> newList) {
        if (newList != null) {
            this.artikelList = newList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ArtikelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artikel, parent, false);
        return new ArtikelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtikelViewHolder holder, int position) {
        ArtikelModel artikel = artikelList.get(position);
        holder.bind(artikel);
    }

    @Override
    public int getItemCount() {
        return artikelList != null ? artikelList.size() : 0;
    }

    class ArtikelViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudulArtikel;
        TextView tvDeskripsi;
        ImageView ivArtikel;

        ArtikelViewHolder(View itemView) {
            super(itemView);
            tvJudulArtikel = itemView.findViewById(R.id.tv_judul_artikel);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi_artikel);
            ivArtikel = itemView.findViewById(R.id.iv_artikel);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    ArtikelModel artikel = artikelList.get(position);
                    listener.onArtikelClick(artikel);
                    showDialog(itemView.getContext(), artikel);
                }
            });
        }

        private void showDialog(Context context, ArtikelModel artikel) {
            if  (!(context instanceof FragmentActivity)) return;

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_artikel, null);
            builder.setView(dialogView);

            TextView titleText = dialogView.findViewById(R.id.title_artikel);
            TextView detailArtikel = dialogView.findViewById(R.id.isi_artikel);
            ImageView imageResep = dialogView.findViewById(R.id.image_artikel);
            Button closeButton = dialogView.findViewById(R.id.btn_close);

            titleText.setText(artikel.getJudulArtikel());

            String artikelDetail = artikel.getIsiArtikel();
            if (artikelDetail == null || artikelDetail.isEmpty()) {
                artikelDetail = "sEBENTAARRR, Artikel akan di update.";
            }
            detailArtikel.setText(artikelDetail);

            Glide.with(context)
                    .load(artikel.getGambarUrl())
                    .placeholder(R.drawable.button_filter) // Placeholder image
                    .error(R.drawable.button_filter) // Error image
                    .into(imageResep);

            androidx.appcompat.app.AlertDialog dialog = builder.create();
            closeButton.setOnClickListener(view -> dialog.dismiss());
            dialog.show();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

        }

        void bind(final ArtikelModel artikel) {
            if (artikel != null) {

                tvJudulArtikel.setText(artikel.getJudulArtikel());
                tvDeskripsi.setText(artikel.getKategori());

                Log.d("ArtikelAdapter", "loading image URL : " + artikel.getGambarUrl());

                String imageUrl = artikel.getGambarUrl();
                if (imageUrl != null && !imageUrl.startsWith("http")) {
                    String baseUrl = "https://adek-app.my.id/";
                    imageUrl = baseUrl + imageUrl;
                }

                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("Glide", "Image load failed", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("Glide", "Image loaded successfully");
                                return false;
                            }
                        })
                        .placeholder(R.drawable.button_filter)
                        .error(R.drawable.button_filter)
                        .into(ivArtikel);
            }
        }
        }
    }
