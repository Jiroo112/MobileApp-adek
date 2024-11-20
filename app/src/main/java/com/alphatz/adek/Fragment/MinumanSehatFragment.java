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

public class MinumanSehatFragment extends Fragment {

    private static final String TAG = "MinumanSehatFragment";
    private RequestQueue requestQueue;
    private List<ResepModel> menuList = new ArrayList<>();
    private ResepAdapter makananAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewMakanan;
    private EditText searchField;
    private Button btnMakananSehat, btnDessert, btnFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minuman_sehat, container, false);

        // Initialize views
        recyclerViewMakanan = view.findViewById(R.id.recyclerView);
        btnMakananSehat = view.findViewById(R.id.btn_makanan_sehat);
        btnDessert = view.findViewById(R.id.btn_desert);
        btnFilter = view.findViewById(R.id.btn_filter);
        progressBar = view.findViewById(R.id.progressBar);
        searchField = view.findViewById(R.id.searchField);

        // Setup RecyclerView
        //rcv
        recyclerViewMakanan.setLayoutManager(new LinearLayoutManager(getContext()));
        makananAdapter = new ResepAdapter(menuList, new ResepAdapter.OnMakananClickListener() {
            @Override
            public void onMakananClick(ResepModel menu) {
                if (menu != null) {
                    showDetailMakanan(menu);
                }
            }

            @Override
            public void onDetailButtonClick(ResepModel menu) {
                if (menu != null) {
                    DetailResepFragment fragmentDetailResep = new DetailResepFragment();

                    // Optional: Pass data to detail fragment
                    Bundle args = new Bundle();
                    args.putString("nama_menu", menu.getNamaMenu());
                    args.putInt("kalori", menu.getKalori());
                    fragmentDetailResep.setArguments(args);

                    // Navigate to the detail fragment
                    if (getParentFragmentManager() != null) {
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragmentDetailResep)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });
        recyclerViewMakanan.setAdapter(makananAdapter);

        requestQueue = Volley.newRequestQueue(requireContext());
        setupSearch();
        setupButtonListeners();
        getMenuMakanan();

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

        makananAdapter.updateList(filteredList);
    }

    private void getMenuMakanan() {
        String url = "http://10.0.2.2/ads_mysql/get_only_minuman.php";
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
                            JSONObject makananObj = jsonArray.getJSONObject(i);
                            ResepModel makanan = new ResepModel(
                                    makananObj.getString("nama_menu"),
                                    makananObj.getInt("kalori")
                            );
                            menuList.add(makanan);
                        }

                        makananAdapter.updateList(menuList);

                        if (menuList.isEmpty()) {
                            showError("Tidak ada data makanan");
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
                    showError("Gagal mengambil data makanan");
                    progressBar.setVisibility(View.GONE);
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    private void showDetailMakanan(ResepModel menu) {
        try {
            String message = String.format("Menu: %s\nKalori: %d",
                    menu.getNamaMenu(),
                    menu.getKalori());
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error showing detail: " + e.getMessage());
        }
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
        // nampilin lagi bottom navigation waktu fragment nya di destroy
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).showBottomNavigation();
        }
    }
}
