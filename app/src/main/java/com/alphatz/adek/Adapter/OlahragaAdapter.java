package com.alphatz.adek.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Model.OlahragaModel;
import com.alphatz.adek.R;
import java.util.List;

public class OlahragaAdapter extends RecyclerView.Adapter<OlahragaAdapter.OlahragaViewHolder> {
    private List<OlahragaModel> olahragaList;
    private final OnOlahragaClickListener listener;

    public interface OnOlahragaClickListener {
        void onOlahragaClick(OlahragaModel olahraga);
    }

    public OlahragaAdapter(List<OlahragaModel> olahragaList, OnOlahragaClickListener listener) {
        this.olahragaList = olahragaList;
        this.listener = listener;
    }

    public void updateList(List<OlahragaModel> newList) {
        this.olahragaList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OlahragaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resep, parent, false);
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
        TextView tvKalori;
        Button detailButton;

        OlahragaViewHolder(View itemView) {
            super(itemView);
            tvNamaOlahraga = itemView.findViewById(R.id.tv_nama_menu);
            tvKalori = itemView.findViewById(R.id.tv_kalori);
            detailButton = itemView.findViewById(R.id.detail_button);
        }

        void bind(final OlahragaModel olahraga) {
            if (olahraga != null) {
                tvNamaOlahraga.setText(olahraga.getNamaOlahraga() != null ?
                        "Olahraga: " + olahraga.getNamaOlahraga() : "Olahraga: -");
                tvKalori.setText("Kalori: " + olahraga.getKalori());

                detailButton.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onOlahragaClick(olahraga);
                    }
                });
            }
        }
    }
}