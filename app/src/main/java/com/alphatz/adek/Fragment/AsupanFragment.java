package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Adapter.AsupanAdapter;
import com.alphatz.adek.Model.AsupanModel;
import com.alphatz.adek.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AsupanFragment extends Fragment {
    private static final String TAG = "AsupanFragment";
    private RequestQueue requestQueue;
    private List<AsupanModel> menuList = new ArrayList<>();
    private AsupanAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private EditText searchField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asupan, container, false);

        initViews(view);
        setupRecyclerView();
        setupSearch();

        requestQueue = Volley.newRequestQueue(requireContext());
        fetchMenuData(); // Fetching menu data

        return view;
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchField = view.findViewById(R.id.search_field);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AsupanAdapter(menuList, menu -> {
            // Handle item click here
            Toast.makeText(getContext(), "Selected: " + menu.getNamaMenu(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMenu(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterMenu(String query) {
        List<AsupanModel> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();

        for (AsupanModel menu : menuList) {
            if (menu.getNamaMenu().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(menu);
            }
        }

        adapter.updateList(filteredList);
    }

    private void fetchMenuData() {
        String url = "http://10.0.2.2/ads_mysql/get_menu.php";
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
                            JSONObject menuObj = jsonArray.getJSONObject(i);
                            AsupanModel menu = new AsupanModel(
                                    menuObj.getInt("id_menu"),
                                    menuObj.getString("nama_menu"),
                                    menuObj.getDouble("kalori"),
                                    menuObj.getDouble("protein"),
                                    menuObj.getDouble("karbo"),
                                    menuObj.getDouble("gula")
                            );
                            menuList.add(menu);
                        }

                        adapter.updateList(menuList);
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

        requestQueue.add(request);
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
        Log.e(TAG, "Error: " + message);
    }
}
