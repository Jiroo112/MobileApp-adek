package com.alphatz.adek.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Activity.Dashboard;
import com.alphatz.adek.Model.ResepModel;
import com.alphatz.adek.R;
//import com.alphatz.adek.Fragment.DetailResepFragment; // Import the fragment class

import java.util.List;
public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ResepViewHolder> {
    private List<ResepModel> menuList;
    private final OnMakananClickListener listener;
    private final FragmentManager fragmentManager;

    public interface OnMakananClickListener {
        void onMakananClick(ResepModel menu);
    }

    public ResepAdapter(List<ResepModel> menuList, OnMakananClickListener listener, FragmentManager fragmentManager) {
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
                .inflate(R.layout.item_resep, parent, false); // Ensure layout exists
        return new ResepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResepViewHolder holder, int position) {
        ResepModel menu = menuList.get(position);
        holder.bind(menu);

        // Tetap set click listener, tapi dialog hanya untuk nama menu dan kalori
        holder.itemView.setOnClickListener(v -> showDialog(holder.itemView.getContext(), menu));
    }

    @Override
    public int getItemCount() {
        return menuList != null ? menuList.size() : 0;
    }

        class ResepViewHolder extends RecyclerView.ViewHolder {
            TextView tvNamaMenu, tvKalori;
            ImageView ivMenu;

            ResepViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNamaMenu = itemView.findViewById(R.id.tv_nama_menu);
                tvKalori = itemView.findViewById(R.id.tv_kalori);
                ivMenu = itemView.findViewById(R.id.iv_menu);
            }

            void bind(final ResepModel menu) {
                tvNamaMenu.setText(menu.getNamaMenu());
                tvKalori.setText(String.format("%d Kalori", menu.getKalori()));

                // Set image if available
                if (menu.getImageBytes() != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(menu.getImageBytes(), 0, menu.getImageBytes().length);
                    ivMenu.setImageBitmap(bitmap);
                } else {
                    ivMenu.setImageResource(R.drawable.button_filter);
                }
            }
    }

    private void showDialog(Context context, ResepModel menu) {
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

        titleText.setText(menu.getNamaMenu());
        kaloriText.setText(String.format("%d Kalori", menu.getKalori()));

        String resep = menu.getResep();
        if (resep == null || resep.isEmpty()) {
            resep = "Resep tidak tersedia.";
        }
        detailResep.setText(resep);

        // Set image if available
        if (menu.getImageBytes() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(menu.getImageBytes(), 0, menu.getImageBytes().length);
            imageResep.setImageBitmap(bitmap);
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