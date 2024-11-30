package com.alphatz.adek.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Model.ArtikelModel;
import com.alphatz.adek.R;
import com.alphatz.adek.Fragment.DetailArtikelFragment;

import java.util.List;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder> {
    private List<ArtikelModel> artikelList;
    private final OnArtikelClickListener listener;
    private final FragmentManager fragmentManager;

    public interface OnArtikelClickListener {
        void onArtikelClick(ArtikelModel artikel);
    }

    public ArtikelAdapter(List<ArtikelModel> artikelList, OnArtikelClickListener listener, FragmentManager fragmentManager) {
        this.artikelList = artikelList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
    }

    public void updateList(List<ArtikelModel> newList) {
        this.artikelList = newList;
        notifyDataSetChanged();
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
        }

        void bind(final ArtikelModel artikel) {
            if (artikel != null) {
                tvJudulArtikel.setText(artikel.getJudulArtikell());
                tvDeskripsi.setText(artikel.getKategori());

                if (artikel.getGambar() != null && artikel.getGambar().length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(artikel.getGambar(), 0, artikel.getGambar().length);
                    ivArtikel.setImageBitmap(bitmap);
                } else {
                    ivArtikel.setImageResource(R.drawable.default_image); // Ganti dengan drawable default Anda
                }

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onArtikelClick(artikel);
                    }

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    DetailArtikelFragment detailFragment = new DetailArtikelFragment();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("artikel_key", artikel);
                    detailFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.fragment_container, detailFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });
            }
        }
    }
}
