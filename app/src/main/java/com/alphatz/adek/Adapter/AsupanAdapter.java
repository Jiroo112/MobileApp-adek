package com.alphatz.adek.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Model.AsupanModel;
import com.alphatz.adek.R;

import java.util.List;

public class AsupanAdapter extends RecyclerView.Adapter<AsupanAdapter.ViewHolder> {
    private List<AsupanModel> menuList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AsupanModel menu);
    }

    public AsupanAdapter(List<AsupanModel> menuList, OnItemClickListener listener) {
        this.menuList = menuList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_asupan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AsupanModel menu = menuList.get(position);
        holder.tvNamaMenu.setText(menu.getNamaMenu());

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(menu);
            }
        });
    }
    @Override
    public int getItemCount() {
        return menuList != null ? menuList.size() : 0;
    }

    public void updateList(List<AsupanModel> newList) {
        this.menuList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMenu;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMenu = itemView.findViewById(R.id.tv_nama_menu);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}