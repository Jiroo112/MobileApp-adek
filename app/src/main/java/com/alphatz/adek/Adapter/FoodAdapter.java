package com.alphatz.adek.Adapter;

import static android.graphics.Color.parseColor;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alphatz.adek.R;
import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private List<Object> foodItems = new ArrayList<>(); // Use Object to hold both headers and items

    public void setFoodItems(List<Object> items) {
        this.foodItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_date_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_makanan, parent, false);
            return new FoodViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            String date = (String) foodItems.get(position); // Assuming headers are strings
            ((HeaderViewHolder) holder).bind(date);
        } else if (holder instanceof FoodViewHolder) {
            FoodItem item = (FoodItem) foodItems.get(position);
            ((FoodViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (foodItems.get(position) instanceof String) {
            return VIEW_TYPE_HEADER; // Header view type
        } else {
            return VIEW_TYPE_ITEM; // Food item view type
        }
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNamaMenu;
        private final TextView tvKalori;

        FoodViewHolder(View itemView) {
            super(itemView);
            tvNamaMenu = itemView.findViewById(R.id.tvNamaMenu);
            tvKalori = itemView.findViewById(R.id.tvKalori);
        }

        void bind(FoodItem item) {
            tvNamaMenu.setText(item.getNama());
            tvKalori.setText(String.format("%d kcal • %s", item.getKalori(), item.getTakaran()));
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDateHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            tvDateHeader = itemView.findViewById(R.id.text_date);
        }

        void bind(String date) {
            tvDateHeader.setText(date);
            tvDateHeader.setTextColor(parseColor("#32CD32"));
        }
    }

    public static class FoodItem {
        private String nama;
        private int kalori;
        private String takaran;

        public FoodItem(String nama, int kalori, String takaran) {
            this.nama = nama;
            this.kalori = kalori;
            this.takaran = takaran;
        }

        public String getNama() { return nama; }
        public int getKalori() { return kalori; }
        public String getTakaran() { return takaran; }
    }
}