package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Activity.Dashboard;
import com.alphatz.adek.Adapter.ResepAdapter;
import com.alphatz.adek.Model.ResepModel;
import com.alphatz.adek.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MinumanSehatFragment extends Fragment {

    private static final String TAG = "MinumanSehatFragment";
    private RequestQueue requestQueue;
    private List<ResepModel> menuList = new ArrayList<>();
    private ResepAdapter resepAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewResep;
    private EditText searchField;
    private Button btnMakananSehat, btnDessert, btnFilter;

    private static final String URL = "http://adek-app.my.id/ads_mysql/get_only_minuman.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minuman_sehat, container, false);

        recyclerViewResep = view.findViewById(R.id.recyclerView);
        btnMakananSehat = view.findViewById(R.id.btn_makanan_sehat);
        btnDessert = view.findViewById(R.id.btn_desert);
        btnFilter = view.findViewById(R.id.btn_filter);
        progressBar = view.findViewById(R.id.progressBar);
        searchField = view.findViewById(R.id.searchField);

        // Setup RecyclerView
        recyclerViewResep.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the FragmentManager
        FragmentManager fragmentManager = getParentFragmentManager();

        // Initialize the adapter with the FragmentManager
        resepAdapter = new ResepAdapter(menuList, new ResepAdapter.OnResepClickListener() {
            @Override
            public void onResepClick(ResepModel resep) {
                if (resep != null) {
                    navigateToDetail(resep);
                }
            }
        }, fragmentManager);

        recyclerViewResep.setAdapter(resepAdapter);
        requestQueue = Volley.newRequestQueue(requireContext());
        setupSearch();
        setupButtonListeners();
        getResepData();

        return view;
    }

    private void setupButtonListeners() {
        btnMakananSehat.setOnClickListener(v -> navigateToFragment(new MinumanSehatFragment()));
        btnFilter.setOnClickListener(v -> navigateToFragment(new ResepFragment()));
        btnDessert.setOnClickListener(v -> navigateToFragment(new DessertFragment()));
    }

    private void setupSearch() {
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMakanan(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterMakanan(String query) {
        List<ResepModel> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();

        for (ResepModel makanan : menuList) {
            if (makanan.getNamaMenu().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(makanan);
            }
        }

        resepAdapter.updateList(filteredList);
    }

    private void getResepData() {
        String url = "http://10.0.2.2/ads_mysql/search/get_only_minuman.php";
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (!response.has("data")) {
                            showError("Format response tidak valid");
                            return;
                        }

                        JSONArray jsonArray = response.getJSONArray("data");
                        menuList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject resepObj = jsonArray.getJSONObject(i);

                                // Extract values with null checks and default values
                                String namaMenu = resepObj.optString("nama_menu", "");
                                int kalori = resepObj.optInt("kalori", 0);
                                String gambarPath = resepObj.optString("gambar", "");
                                String resep = resepObj.optString("resep", "");

                                // Prepend base URL to gambar path
                                String gambarUrl = "https://adek-app.my.id/Images/" + gambarPath;

                                // Create ResepModel with URL instead of byte array
                                ResepModel menu = new ResepModel(
                                        resepObj.optString("id_menu", ""),
                                        namaMenu,
                                        kalori,
                                        resep,
                                        gambarUrl,
                                        null
                                );

                                menuList.add(menu);
                            } catch (JSONException e) {
                                // Log the error and continue processing other items
                                Log.e("ResepParser", "Error parsing JSON object", e);
                            }
                        }

                        resepAdapter.updateList(menuList);

                        if (menuList.isEmpty()) {
                            showError("Tidak ada data menu");
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                        showError("Gagal memproses data");
                    } finally {
                        progressBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    Log.e(TAG, "Volley Error: " + error.getMessage());
                    showError("Gagal mengambil data menu");
                    progressBar.setVisibility(View.GONE);
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    private void navigateToDetail(ResepModel resep) {
        if (resep == null) return;

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_detail_resep, null);

        builder.setView(dialogView);

        TextView titleText = dialogView.findViewById(R.id.title_resep);
        TextView detailResep = dialogView.findViewById(R.id.detail_resep);
        ImageView imageResep = dialogView.findViewById(R.id.image_resep);
        Button closeButton = dialogView.findViewById(R.id.btn_close);

        titleText.setText(resep.getNamaMenu());
        detailResep.setText(resep.getResep());

        closeButton.setOnClickListener(v -> builder.create().dismiss());

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
        Log.e(TAG, "Error: " + message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).showBottomNavigation();
        }
    }
}