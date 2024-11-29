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

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    DetailOlahragaFragment detailFragment = new DetailOlahragaFragment();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("olahraga_key", olahraga);
                    detailFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.fragment_container, detailFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });
            }
        }
    }
}