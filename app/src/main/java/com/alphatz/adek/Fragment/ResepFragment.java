package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResepFragment extends Fragment {
    private static final String TAG = "ResepFragment";
    private RequestQueue requestQueue;
    private List<ResepModel> menuList = new ArrayList<>();
    private ResepAdapter makananAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewMakanan;
    private EditText searchField;
    private Button btnMinumanSehat, btnDessert, btnMakananBerat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_resep, container, false);
            initializeViews(view);
            setupRecyclerView();
            setupVolley();
            setupSearch();
            setupButtonListeners();
            fetchMenuMakanan();
            return view;
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage());
            return null;
        }
    }

    private void initializeViews(View view) {
        try {
            recyclerViewMakanan = view.findViewById(R.id.recyclerView);
            btnMinumanSehat = view.findViewById(R.id.btn_minuman_sehat);
            btnDessert = view.findViewById(R.id.btn_desert);
            btnMakananBerat = view.findViewById(R.id.btn_makanan_sehat);
            progressBar = view.findViewById(R.id.progressBar);
            searchField = view.findViewById(R.id.searchField);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage());
            throw e;
        }
    }

    // In ResepFragment.java
    private void setupRecyclerView() {
        try {
            recyclerViewMakanan.setLayoutManager(new LinearLayoutManager(getContext()));
            makananAdapter = new ResepAdapter(new ArrayList<>(), new ResepAdapter.OnMakananClickListener() {
                @Override
                public void onMakananClick(ResepModel menu) {
                    if (menu != null) {
                        showDetailMakanan(menu);
                    }
                }

                @Override
                public void onDetailButtonClick(ResepModel menu) {
                    if (menu != null) {
                        // Navigate to detail fragment
                        DetailResepFragment fragmentDetailResep = new DetailResepFragment();
                        // You might want to pass menu data to the fragment using Bundle
                        Bundle args = new Bundle();
                        args.putParcelable("menu", menu);  // Assuming ResepModel is Parcelable
                        fragmentDetailResep.setArguments(args);

                        safeNavigateToFragment(fragmentDetailResep);
                    }
                }
            });
            recyclerViewMakanan.setAdapter(makananAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView: " + e.getMessage());
        }
    }

    private void setupVolley() {
        try {
            requestQueue = Volley.newRequestQueue(requireContext());
        } catch (Exception e) {
            Log.e(TAG, "Error setting up Volley: " + e.getMessage());
        }
    }

    private void setupButtonListeners() {
        try {
            btnMinumanSehat.setOnClickListener(v -> safeNavigateToFragment(new MinumanSehatFragment()));
            btnMakananBerat.setOnClickListener(v -> safeNavigateToFragment(new MakananBeratFragment()));
            btnDessert.setOnClickListener(v -> safeNavigateToFragment(new DessertFragment()));
        } catch (Exception e) {
            Log.e(TAG, "Error setting up button listeners: " + e.getMessage());
        }
    }

    private void setupSearch() {
        try {
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
        } catch (Exception e) {
            Log.e(TAG, "Error setting up search: " + e.getMessage());
        }
    }

    private void filterMakanan(String query) {
        try {
            if (menuList == null) return;

            List<ResepModel> filteredList = new ArrayList<>();
            String lowerCaseQuery = query.toLowerCase().trim();

            for (ResepModel makanan : menuList) {
                if (makanan != null && makanan.getNamaMenu() != null &&
                        makanan.getNamaMenu().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(makanan);
                }
            }

            makananAdapter.updateList(filteredList);
        } catch (Exception e) {
            Log.e(TAG, "Error filtering makanan: " + e.getMessage());
        }
    }

    private void fetchMenuMakanan() {
        if (getContext() == null) return;

        String url = "http://10.0.2.2/ads_mysql/search/get_menu.php";
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response == null || !response.has("data")) {
                            showError("Format response tidak valid");
                            return;
                        }

                        JSONArray jsonArray = response.getJSONArray("data");
                        menuList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject makananObj = jsonArray.getJSONObject(i);
                            if (makananObj != null) {
                                ResepModel makanan = new ResepModel(
                                        makananObj.optString("nama_menu", ""),
                                        makananObj.optInt("kalori", 0)
                                );
                                menuList.add(makanan);
                            }
                        }

                        if (makananAdapter != null) {
                            makananAdapter.updateList(menuList);
                        }

                        if (menuList.isEmpty()) {
                            showError("Tidak ada data makanan");
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                        showError("Gagal memproses data");
                    } finally {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                error -> {
                    Log.e(TAG, "Volley Error: " + (error.getMessage() != null ? error.getMessage() : "Unknown error"));
                    showError("Gagal mengambil data makanan");
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    private void showDetailMakanan(ResepModel menu) {
        try {
            if (getContext() == null || menu == null) return;

            String message = String.format("Menu: %s\nKalori: %d",
                    menu.getNamaMenu(),
                    menu.getKalori());
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error showing detail: " + e.getMessage());
        }
    }

    private void safeNavigateToFragment(Fragment fragment) {
        try {
            if (getActivity() == null) return;

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to fragment: " + e.getMessage());
        }
    }

    private void showError(String message) {
        try {
            if (getContext() != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG, "Error: " + message);
        } catch (Exception e) {
            Log.e(TAG, "Error showing error message: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            if (getActivity() instanceof Dashboard) {
                ((Dashboard) getActivity()).showBottomNavigation();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onDestroy: " + e.getMessage());
        }
    }
}