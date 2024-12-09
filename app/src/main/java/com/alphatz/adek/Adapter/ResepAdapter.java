package com.alphatz.adek.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.alphatz.adek.Model.ResepModel;
import com.alphatz.adek.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ResepViewHolder> {
    private List<ResepModel> menuList;
    private final OnResepClickListener listener;
    private final FragmentManager fragmentManager;

    public interface OnResepClickListener {
        void onResepClick(ResepModel resep);
    }

    public ResepAdapter(List<ResepModel> menuList, OnResepClickListener listener, FragmentManager fragmentManager) {
        this.menuList = menuList != null ? menuList : List.of(); // Null safety
        this.listener = listener;
        this.fragmentManager = fragmentManager;
    }

    public void updateList(List<ResepModel> newList) {
        this.menuList = newList != null ? newList : List.of(); // Null safety
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resep, parent, false);
        return new ResepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResepViewHolder holder, int position) {
        ResepModel menu = menuList.get(position);
        holder.bind(menu);
    }

    @Override
    public int getItemCount() {
        return menuList != null ? menuList.size() : 0;
    }

    class ResepViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMenu, tvKalori;
        ImageView ivMenu;

        ResepViewHolder(View itemView) {
            super(itemView);
            tvNamaMenu = itemView.findViewById(R.id.tv_nama_menu);
            tvKalori = itemView.findViewById(R.id.tv_kalori);
            ivMenu = itemView.findViewById(R.id.iv_menu);

            // Set the click listener here
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    ResepModel resep = menuList.get(position);
                    listener.onResepClick(resep);
                    showDialog(itemView.getContext(), resep);
                }
            });
        }

        void bind(final ResepModel resep) {
            if (resep != null) {
                tvNamaMenu.setText(resep.getNamaMenu());
                tvKalori.setText(String.format("%d Kalori", resep.getKalori()));

                // Log the image source for debugging
                Log.d("ResepAdapter", "Loading image source");

                // Check if image is from URL or byte array
                String imageUrl = resep.getGambarUrl(); // Assuming "getGambar" returns URL or null
                if (imageUrl != null && !imageUrl.startsWith("http")) {
                    String baseUrl = "https://adek-app.my.id/";
                    imageUrl = baseUrl + imageUrl;
                }

                if (imageUrl != null) {
                    // Load image from URL
                    Glide.with(itemView.getContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.button_filter)
                            .error(R.drawable.button_filter)
                            .into(ivMenu);
                } else if (resep.getImageBytes() != null) {
                    // Load image from byte array
                    Glide.with(itemView.getContext())
                            .asBitmap()
                            .load(resep.getImageBytes())
                            .placeholder(R.drawable.button_filter)
                            .error(R.drawable.button_filter)
                            .into(ivMenu);
                } else {
                    // Default placeholder
                    ivMenu.setImageResource(R.drawable.button_filter);
                }
            }
        }
    }

    private void showDialog(Context context, ResepModel resep) {
        if (!(context instanceof FragmentActivity)) return;

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_detail_resep, null);
        builder.setView(dialogView);

        // Initialize views
        TextView titleText = dialogView.findViewById(R.id.title_resep);
        TextView kaloriText = dialogView.findViewById(R.id.kalori_resep);
        TextView detailResep = dialogView.findViewById(R.id.detail_resep);
        ImageView imageResep = dialogView.findViewById(R.id.image_resep);
        Button closeButton = dialogView.findViewById(R.id.btn_close);

        titleText.setText(resep.getNamaMenu());
        kaloriText.setText(String.format("%d Kalori", resep.getKalori()));

        String resepDetail = resep.getResep();
        if (resepDetail == null || resepDetail.isEmpty()) {
            resepDetail = "Resep tidak tersedia.";
        }
        detailResep.setText(resepDetail);

        // Set image if available
        String imageUrl = resep.getGambarUrl();
        if (imageUrl != null && !imageUrl.startsWith("http")) {
            String baseUrl = "https://adek-app.my.id/";
            imageUrl = baseUrl + imageUrl;
        }

        if (imageUrl != null) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.button_filter)
                    .error(R.drawable.default_image)
                    .into(imageResep);
        } else if (resep.getImageBytes() != null) {
            Glide.with(context)
                    .asBitmap()
                    .load(resep.getImageBytes())
                    .placeholder(R.drawable.button_filter)
                    .error(R.drawable.default_image)
                    .into(imageResep);
        } else {
            imageResep.setImageResource(R.drawable.default_image);
        }

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        closeButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
