package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alphatz.adek.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerakhirDimakan extends Fragment {
    private TextView tabCariMakanan, tabTerakhirDimakan, tabCatatanMinum;
    private FoodHistoryAdapter adapter;
    private List<FoodHistoryGroup> groupedHistory;

    public TerakhirDimakan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terakhir_dimakan, container, false);
        initializeViews(view);
        setupTabs();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFoodHistory();
    }

    private void initializeViews(View view) {
        // Initialize tabs
        tabCariMakanan = view.findViewById(R.id.tab_cari_makanan);
        tabTerakhirDimakan = view.findViewById(R.id.tab_terakhir_dimakan);
        tabCatatanMinum = view.findViewById(R.id.tab_catatan_minum);

        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_food_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new FoodHistoryAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void setupTabs() {
        tabCariMakanan.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AsupanFragment())
                    .addToBackStack(null)
                    .commit();
        });

        tabCatatanMinum.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new CatatanMinum())
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void loadFoodHistory() {
        // Create sample data - replace this with your actual data loading logic
        List<FoodHistoryItem> historyItems = new ArrayList<>();

        // Sample data for today
        String today = "25 October 2024";
        historyItems.add(new FoodHistoryItem("Nasi Goreng", 700, "100 g", today));
        historyItems.add(new FoodHistoryItem("Susu Murni", 150, "1 gelas kecil", today));
        historyItems.add(new FoodHistoryItem("Air(botol)", 0, "1 takaran", today));

        // Sample data for yesterday
        String yesterday = "24 October 2024";
        historyItems.add(new FoodHistoryItem("Ayam Goreng", 300, "1 potong", yesterday));
        historyItems.add(new FoodHistoryItem("Nasi Putih", 200, "1 piring", yesterday));

        groupedHistory = groupHistoryByDate(historyItems);
        adapter.setHistoryGroups(groupedHistory);
    }

    private List<FoodHistoryGroup> groupHistoryByDate(List<FoodHistoryItem> items) {
        Map<String, List<FoodHistoryItem>> groupedMap = new HashMap<>();

        for (FoodHistoryItem item : items) {
            String date = item.getDate();
            if (!groupedMap.containsKey(date)) {
                groupedMap.put(date, new ArrayList<>());
            }
            groupedMap.get(date).add(item);
        }

        List<FoodHistoryGroup> groups = new ArrayList<>();
        for (Map.Entry<String, List<FoodHistoryItem>> entry : groupedMap.entrySet()) {
            groups.add(new FoodHistoryGroup(entry.getKey(), entry.getValue()));
        }

        return groups;
    }
}

class FoodHistoryItem {
    private String foodName;
    private int calories;
    private String portion;
    private String date;

    public FoodHistoryItem(String foodName, int calories, String portion, String date) {
        this.foodName = foodName;
        this.calories = calories;
        this.portion = portion;
        this.date = date;
    }

    public String getFoodName() { return foodName; }
    public int getCalories() { return calories; }
    public String getPortion() { return portion; }
    public String getDate() { return date; }
}

class FoodHistoryGroup {
    private String date;
    private List<FoodHistoryItem> items;

    public FoodHistoryGroup(String date, List<FoodHistoryItem> items) {
        this.date = date;
        this.items = items;
    }

    public String getDate() { return date; }
    public List<FoodHistoryItem> getItems() { return items; }
}

class FoodHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_DATE = 0;
    private static final int TYPE_FOOD = 1;
    private List<Object> items = new ArrayList<>();

    public void setHistoryGroups(List<FoodHistoryGroup> groups) {
        items.clear();
        for (FoodHistoryGroup group : groups) {
            items.add(group.getDate());
            items.addAll(group.getItems());
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
        TextView dateText;

        DateViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.text_date);
        }

        void bind(String date) {
            dateText.setText(date);
        }
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMenu;
        TextView tvKalori;

        FoodViewHolder(View itemView) {
            super(itemView);
            tvNamaMenu = itemView.findViewById(R.id.tvNamaMenu);
            tvKalori = itemView.findViewById(R.id.tvKalori);
        }

        void bind(FoodHistoryItem item) {
            tvNamaMenu.setText(item.getFoodName());
            tvKalori.setText(String.format("%d kcal • %s",
                    item.getCalories(), item.getPortion()));
        }
    }
}