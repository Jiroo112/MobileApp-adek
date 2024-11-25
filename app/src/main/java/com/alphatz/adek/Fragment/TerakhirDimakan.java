package com.alphatz.adek.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Adapter.FoodHistoryAdapter;
import com.alphatz.adek.Model.FoodHistoryItem;
import com.alphatz.adek.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TerakhirDimakan extends Fragment {
    private static final String TAG = "TerakhirDimakan";

    private TextView tabCariMakanan, tabTerakhirDimakan, tabCatatanMinum;
    private FoodHistoryAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;

    private List<FoodHistoryItem> terakhirDimakanList;

    public TerakhirDimakan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terakhir_dimakan, container, false);

        initializeViews(view);

        setupRecyclerView();

        setupTabs();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch data from the server
        fetchTerakhirDimakan();
    }

    private void initializeViews(View view) {
        // Initialize UI elements
        tabCariMakanan = view.findViewById(R.id.tab_cari_makanan);
        tabTerakhirDimakan = view.findViewById(R.id.tab_terakhir_dimakan);
        tabCatatanMinum = view.findViewById(R.id.tab_catatan_minum);

        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recycler_view_food_history);

        // Initialize adapter and request queue
        adapter = new FoodHistoryAdapter();
        requestQueue = Volley.newRequestQueue(requireContext());

        // Initialize data list
        terakhirDimakanList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
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

    private void fetchTerakhirDimakan() {
        if (!isAdded()) return;

        // Get user ID from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String idUser = prefs.getString("idUser", "");

        if (idUser.isEmpty()) {
            showError("User ID not found");
            return;
        }

        // API URL
        String url = "http://10.0.2.2/ads_mysql/asupan/get_terakhir_makanan.php?id_user=" + idUser;

        // Show loading indicator
        showLoading(true);

        Log.d(TAG, "Fetching data from URL: " + url);

        // Create JSON request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    // Hide loading indicator
                    showLoading(false);

                    Log.d(TAG, "Response received: " + response.toString());

                    try {
                        // Check response success
                        if (!response.getBoolean("success")) {
                            showError(response.optString("message", "Terjadi kesalahan"));
                            return;
                        }

                        // Parse data
                        JSONArray jsonArray = response.getJSONArray("data");
                        terakhirDimakanList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);

                            FoodHistoryItem makanan = new FoodHistoryItem(
                                    item.getString("tanggal"),
                                    item.getString("menu_name"),
                                    item.getInt("total_kalori"),
                                    "Jumlah: " + item.getInt("jumlah")
                            );
                            terakhirDimakanList.add(makanan);
                        }

                        if (terakhirDimakanList.isEmpty()) {
                            showError("Tidak ada data terakhir dimakan");
                        }

                        // Update adapter data
                        adapter.updateList(terakhirDimakanList);

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                        showError("Gagal memproses data");
                    }
                },
                error -> {
                    // Hide loading indicator
                    showLoading(false);

                    Log.e(TAG, "Volley Error: " + error.toString());
                    showError("Gagal terhubung ke server");
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    private void showLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
