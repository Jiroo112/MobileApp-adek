package com.alphatz.adek.Adapter;

import android.content.Context;
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

import com.alphatz.adek.Activity.Dashboard;
import com.alphatz.adek.Model.ResepModel;
import com.alphatz.adek.R;
import com.alphatz.adek.Fragment.DetailResepFragment; // Import the fragment class

import java.util.List;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ResepViewHolder> {
    private List<ResepModel> menuList;
    private final OnMakananClickListener listener;
    private final FragmentManager fragmentManager;

    public interface OnMakananClickListener {
        void onMakananClick(ResepModel menu);
    }

    public ResepAdapter(List<ResepModel> menuList, OnMakananClickListener listener, FragmentManager fragmentManager) {
        this.menuList = menuList;
        this.listener = listener;
        this.fragmentManager = fragmentManager;
    }

    public void updateList(List<ResepModel> newList) {
        this.menuList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resep, parent, false); // Make sure layout matches your needs
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

        // Add constructor to initialize the views
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
                ivMenu.setImageResource(R.drawable.button_filter); // Default image
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMakananClick(menu);
                }

                // Ensure the RecyclerView context is correct
                Context context = itemView.getContext();
                if (context instanceof Dashboard) {
                    Dashboard activity = (Dashboard) context;
                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();

                    DetailResepFragment detailFragment = new DetailResepFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("resep_key", menu);
                    detailFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.fragment_container, detailFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
