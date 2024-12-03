package com.alphatz.adek.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.appcompat.app.AlertDialog;
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
    private static final String ARG_NAMA_MENU = "nama_menu";
    private ProgressBar progressBar;
    private RecyclerView recyclerViewMakanan;
    private EditText searchField;
    private Button btnMinumanSehat, btnDessert, btnMakananBerat;

    public static ResepFragment newInstance (String namaMenu){
        ResepFragment fragment = new ResepFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAMA_MENU, namaMenu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_resep, container, false);

            // Initialize views
            recyclerViewMakanan = view.findViewById(R.id.recyclerView);
            btnMinumanSehat = view.findViewById(R.id.btn_minuman_sehat);
            btnDessert = view.findViewById(R.id.btn_desert);
            btnMakananBerat = view.findViewById(R.id.btn_makanan_sehat);
            progressBar = view.findViewById(R.id.progressBar);
            searchField = view.findViewById(R.id.searchField);

            // Setup RecyclerView
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

            // Setup functionality
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

    private void setupVolley() {
        try {
            requestQueue = Volley.newRequestQueue(requireContext());
        } catch (Exception e) {
            Log.e(TAG, "Error setting up Volley: " + e.getMessage());
        }
    }

    private void setupButtonListeners() {
        try {
            btnMinumanSehat.setOnClickListener(v -> navigateToFragment(new MinumanSehatFragment()));
            btnMakananBerat.setOnClickListener(v -> navigateToFragment(new MakananBeratFragment()));
            btnDessert.setOnClickListener(v -> navigateToFragment(new DessertFragment()));
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
        String url = "http://10.0.2.2/ads_mysql/search/get_menu.php";
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d(TAG, "Full JSON Response: " + response.toString());

                        if (response == null || !response.has("data")) {
                            showError("Format response tidak valid");
                            return;
                        }

                        JSONArray jsonArray = response.getJSONArray("data");
                        menuList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject makananObj = jsonArray.getJSONObject(i);

                            String resep = makananObj.optString("resep", "");
                            Log.d(TAG, "Resep Raw: " + resep);

                            byte[] imageBytes = null;
                            if (makananObj.has("gambar") && !makananObj.isNull("gambar")) {
                                String base64Image = makananObj.getString("gambar");
                                imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                            }

                            ResepModel makanan = new ResepModel(
                                    makananObj.optInt("id_menu", 0),
                                    makananObj.optString("nama_menu", ""),
                                    makananObj.optInt("kalori", 0),
                                    imageBytes,
                                    "" //resep kosong
                            );

                            menuList.add(makanan);

                            // Simpan resep ke list lokal
                            if (!resep.isEmpty()) {
                                Log.d(TAG, "Resep saved: " + resep);
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
                    Log.e(TAG, "Volley Error: " + (error.getMessage() != null ? error.getMessage() : "Unknown error"));
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
        if (menu == null) return;

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_detail_resep, null);

        builder.setView(dialogView);

        TextView titleText = dialogView.findViewById(R.id.title_resep);
        TextView kaloriText = dialogView.findViewById(R.id.kalori_resep);
        TextView detailResep = dialogView.findViewById(R.id.detail_resep);
        ImageView imageResep = dialogView.findViewById(R.id.image_resep);
        Button closeButton = dialogView.findViewById(R.id.btn_close);

        titleText.setText(menu.getNamaMenu());
        kaloriText.setText(String.format("%d Kalori", menu.getKalori()));

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