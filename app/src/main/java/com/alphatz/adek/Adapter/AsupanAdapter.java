package com.alphatz.adek.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Model.AsupanModel;
import com.alphatz.adek.R;

import java.util.List;

public class AsupanAdapter extends RecyclerView.Adapter<AsupanAdapter.AsupanViewHolder> {
    private final List<AsupanModel> menuList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AsupanModel menu);
    }

    public AsupanAdapter(List<AsupanModel> menuList, OnItemClickListener listener) {
        this.menuList = menuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AsupanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_makanan, parent, false);
        return new AsupanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsupanViewHolder holder, int position) {
        AsupanModel menu = menuList.get(position);
        holder.bind(menu, listener);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public void updateList(List<AsupanModel> newList) {
        menuList.clear();
        menuList.addAll(newList);
        notifyDataSetChanged();
    }

    static class AsupanViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewNama;

        public AsupanViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNama = itemView.findViewById(R.id.text_view_nama);
        }

        public void bind(AsupanModel menu, OnItemClickListener listener) {
            textViewNama.setText(menu.getNamaMenu());
            itemView.setOnClickListener(v -> listener.onItemClick(menu));
        }
    }
}
