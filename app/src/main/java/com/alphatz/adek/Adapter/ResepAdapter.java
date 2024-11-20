package com.alphatz.adek.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alphatz.adek.Model.ResepModel;
import com.alphatz.adek.R;
import java.util.List;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.MakananViewHolder> {
    private List<ResepModel> menuList;
    private final OnMakananClickListener listener;

    // Interface untuk menangani klik
    public interface OnMakananClickListener {
        void onMakananClick(ResepModel menu);
        void onDetailButtonClick(ResepModel menu);  // Callback untuk tombol detail
    }

    // Constructor
    public ResepAdapter(List<ResepModel> menuList, OnMakananClickListener listener) {
        this.menuList = menuList;
        this.listener = listener;
    }

    // Update daftar resep
    public void updateList(List<ResepModel> newList) {
        this.menuList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MakananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resep, parent, false);
        return new MakananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MakananViewHolder holder, int position) {
        ResepModel menu = menuList.get(position);
        holder.bind(menu, listener);
    }

    @Override
    public int getItemCount() {
        return menuList != null ? menuList.size() : 0;
    }

    static class MakananViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMenu;
        TextView tvKalori;
        Button detailButton;

        MakananViewHolder(View itemView) {
            super(itemView);
            tvNamaMenu = itemView.findViewById(R.id.tv_nama_menu);
            tvKalori = itemView.findViewById(R.id.tv_kalori);
            detailButton = itemView.findViewById(R.id.detail_button);
        }

        void bind(final ResepModel menu, final OnMakananClickListener listener) {
            if (menu != null) {
                tvNamaMenu.setText(menu.getNamaMenu() != null ? "Menu: " + menu.getNamaMenu() : "Menu: -");
                tvKalori.setText("Kalori: " + menu.getKalori());

                // Set listener untuk item klik
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onMakananClick(menu);
                    }
                });

                // Set listener untuk tombol detail
                detailButton.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onDetailButtonClick(menu);
                    }
                });
            }
        }
    }
}