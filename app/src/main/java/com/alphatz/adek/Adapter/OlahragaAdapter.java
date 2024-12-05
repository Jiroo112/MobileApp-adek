package com.alphatz.adek.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Model.OlahragaModel;
import com.alphatz.adek.R;
import com.alphatz.adek.Fragment.DetailOlahragaFragment;

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
        // Consider changing this to an Olahraga-specific layout if it exists
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
            // Ensure these IDs match your actual layout
            tvNamaOlahraga = itemView.findViewById(R.id.tv_nama_olahraga);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi_olahraga);
            ivOlahraga = itemView.findViewById(R.id.iv_olahraga);
        }

        void bind(final OlahragaModel olahraga) {
            if (olahraga != null) {
                tvNamaOlahraga.setText(olahraga.getNamaOlahraga());
                tvDeskripsi.setText(olahraga.getDeskripsi());

                // Set image with null check
                if (olahraga.getGambar() != null && olahraga.getGambar().length > 0) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(olahraga.getGambar(), 0, olahraga.getGambar().length);
                    ivOlahraga.setImageBitmap(bitmap);
                } else {
                    ivOlahraga.setImageResource(R.drawable.button_filter);
                }

                // OnClickListener to navigate to detail
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onOlahragaClick(olahraga);
                    }
                    showDialog(itemView.getContext(), olahraga); // Pass context and olahraga
                });
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
        String deskripsi = olahraga.getDeskripsi();
        if (deskripsi == null || deskripsi.isEmpty()) {
            deskripsi = "Deskripsi tidak tersedia.";
        }
        deskripsiText.setText(deskripsi);

        // Set image if available
        byte[] gambarBytes = olahraga.getGambar();
        if (gambarBytes != null && gambarBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(gambarBytes, 0, gambarBytes.length);
            imageOlahraga.setImageBitmap(bitmap);
        } else {
            imageOlahraga.setImageResource(R.drawable.button_filter);
        }

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        closeButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}