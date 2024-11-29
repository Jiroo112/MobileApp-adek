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

public class MakananBeratFragment extends Fragment {
    private static final String TAG = "MakananBeratFragment";
    private RequestQueue requestQueue;
    private List<ResepModel> menuList = new ArrayList<>();
    private ResepAdapter makananAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewMakanan;
    private EditText searchField;
    private Button btnMinumanSehat, btnDessert, btnFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_makanan_berat, container, false);

        recyclerViewMakanan = view.findViewById(R.id.recyclerView);
        btnMinumanSehat = view.findViewById(R.id.btn_minuman_sehat);
        btnDessert = view.findViewById(R.id.btn_desert);
        btnFilter = view.findViewById(R.id.btn_filter);
        progressBar = view.findViewById(R.id.progressBar);
        searchField = view.findViewById(R.id.searchField);

        // RecyclerView setup
        recyclerViewMakanan.setLayoutManager(new LinearLayoutManager(getContext()));
        makananAdapter = new ResepAdapter(menuList, new ResepAdapter.OnMakananClickListener() {
            @Override
            public void onMakananClick(ResepModel menu) {
                if (menu != null) {
                    navigateToDetail(menu);
                }
            }
        }, getParentFragmentManager()); // Passing FragmentManager
        recyclerViewMakanan.setAdapter(makananAdapter);

        requestQueue = Volley.newRequestQueue(requireContext());
        setupSearch();
        setupButtonListeners();
        getMenuMakanan();

        return view;
    }

    private void setupButtonListeners() {
        btnMinumanSehat.setOnClickListener(v -> navigateToFragment(new MinumanSehatFragment()));
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
        String url = "http://10.0.2.2/ads_mysql/search/get_only_makanan.php";
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
                            if (makananObj != null) {
                                // Convert base64 image to byte array
                                byte[] imageBytes = null;
                                if (makananObj.has("gambar") && !makananObj.isNull("gambar")) {
                                    String base64Image = makananObj.getString("gambar");
                                    imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                                }

                                ResepModel makanan = new ResepModel(
                                        makananObj.optInt("id", 0),
                                        makananObj.optString("nama_menu", ""),
                                        makananObj.optInt("kalori", 0),
                                        imageBytes
                                );
                                menuList.add(makanan);
                            }
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

    private void navigateToDetail(ResepModel menu) {
        DetailResepFragment detailFragment = new DetailResepFragment();

        // Pass data to the detail fragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("resep_key", menu); // Assuming ResepModel implements Parcelable
        detailFragment.setArguments(bundle);

        // Navigate to the detail fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detailFragment); // Replace with your container ID
        transaction.addToBackStack(null);
        transaction.commit();
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
