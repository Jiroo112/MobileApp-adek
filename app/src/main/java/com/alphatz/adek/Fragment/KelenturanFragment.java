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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Activity.Dashboard;
import com.alphatz.adek.Adapter.OlahragaAdapter;
import com.alphatz.adek.Model.OlahragaModel;
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


public class KelenturanFragment extends Fragment {

    private static final String TAG = "KelenturanFragment";
    private RequestQueue requestQueue;
    private List<OlahragaModel> olahragaList = new ArrayList<>();
    private OlahragaAdapter olahragaAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewOlahraga;
    private EditText searchField;
    private Button btnKelenturan, btnKekuatan, btnFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kekuatan, container, false);

        // Initialize views
        recyclerViewOlahraga = view.findViewById(R.id.recyclerView);
        btnKekuatan = view.findViewById(R.id.btn_kekuatan);
        btnKelenturan = view.findViewById(R.id.btn_kelenturan);
        btnFilter = view.findViewById(R.id.btn_filter);
        progressBar = view.findViewById(R.id.progressBar);
        searchField = view.findViewById(R.id.searchField);

        // Setup RecyclerView
        recyclerViewOlahraga.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the FragmentManager
        FragmentManager fragmentManager = getParentFragmentManager();

        // Initialize the adapter with the FragmentManager
        olahragaAdapter = new OlahragaAdapter(olahragaList, new OlahragaAdapter.OnOlahragaClickListener() {
            @Override
            public void onOlahragaClick(OlahragaModel olahraga) {
                if (olahraga != null) {
                    showDetailOlahraga(olahraga);
                }
            }
        }, fragmentManager); // Pass the FragmentManager

        recyclerViewOlahraga.setAdapter(olahragaAdapter);

        requestQueue = Volley.newRequestQueue(requireContext());
        setupSearch();
        setupButtonListeners();
        getOlahragaData();

        return view;
    }

    private void setupButtonListeners() {
        btnKekuatan.setOnClickListener(v -> navigateToFragment(new KekuatanFragment()));
        btnKelenturan.setOnClickListener(v -> navigateToFragment(new KelenturanFragment()));
        btnFilter.setOnClickListener(v -> navigateToFragment(new OlahragaFragment()));
    }

    private void setupSearch() {
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOlahraga(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterOlahraga(String query) {
        List<OlahragaModel> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();

        for (OlahragaModel olahraga : olahragaList) {
            if (olahraga.getNamaOlahraga().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(olahraga);
            }
        }

        olahragaAdapter.updateList(filteredList);
    }

    private void getOlahragaData() {
        String url = "http://10.0.2.2/ads_mysql/get_only_interval.php";
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (!response.has("data")) {
                            showError("Format response tidak valid");
                            return;
                        }

                        JSONArray jsonArray = response.getJSONArray("data");
                        olahragaList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject olahragaObj = jsonArray.getJSONObject(i);
                            OlahragaModel olahraga = new OlahragaModel(
                                    olahragaObj.getString("nama_olahraga"),
                                    olahragaObj.getInt("kalori")
                            );
                            olahragaList.add(olahraga);
                        }

                        olahragaAdapter.updateList(olahragaList);

                        if (olahragaList.isEmpty()) {
                            showError("Tidak ada data olahraga");
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
                    showError("Gagal mengambil data olahraga");
                    progressBar.setVisibility(View.GONE);
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

    private void showDetailOlahraga(OlahragaModel olahraga) {
        try {
            String message = String.format("Olahraga : %s\nKalori yang terbakar: %d",
                    olahraga.getNamaOlahraga(),
                    olahraga.getKalori());
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
        // nampilin lagi nav bottom misal fragment ini di destroy
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).showBottomNavigation();
        }
    }
}
