package com.alphatz.adek.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Model.FoodHistoryItem;
import com.alphatz.adek.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FoodHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_DATE = 0;
    private static final int TYPE_FOOD = 1;
    private List<Object> items;

    public FoodHistoryAdapter() {
        this.items = new ArrayList<>();
    }

    public void updateList(List<FoodHistoryItem> newList) {
        if (newList == null || newList.isEmpty()) {
            items.clear();
            notifyDataSetChanged();
            return;
        }

        // Group items by date using LinkedHashMap to preserve order
        Map<String, List<FoodHistoryItem>> groupedItems = new LinkedHashMap<>();
        for (FoodHistoryItem item : newList) {
            String date = item.getDate();
            if (!groupedItems.containsKey(date)) {
                groupedItems.put(date, new ArrayList<>());
            }
            groupedItems.get(date).add(item);
        }

        // Populate the items list for the adapter
        items.clear();
        for (Map.Entry<String, List<FoodHistoryItem>> entry : groupedItems.entrySet()) {
            items.add(entry.getKey()); // Add date header
            items.addAll(entry.getValue()); // Add food items
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_DATE) {
            View view = inflater.inflate(R.layout.item_date_header, parent, false);
            return new DateViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_makanan, parent, false);
            return new FoodViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_DATE) {
            ((DateViewHolder) holder).bind((String) items.get(position));
        } else {
            ((FoodViewHolder) holder).bind((FoodHistoryItem) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? TYPE_DATE : TYPE_FOOD;
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateText;

        DateViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.text_date);
        }

        void bind(String date) {
            dateText.setText(date);
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

        void bind(FoodHistoryItem item) {
            tvNamaMenu.setText(item.getFoodName());
            tvKalori.setText(String.format("%d kcal • %s", item.getCalories(), item.getPortion()));
        }
    }
}
