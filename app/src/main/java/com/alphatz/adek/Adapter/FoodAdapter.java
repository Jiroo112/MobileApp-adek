package com.alphatz.adek.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alphatz.adek.R;
import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodItem> foodItems = new ArrayList<>();

    public void setFoodItems(List<FoodItem> items) {
        this.foodItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_makanan, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem item = foodItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
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