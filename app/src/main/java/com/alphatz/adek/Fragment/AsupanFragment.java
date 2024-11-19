package com.alphatz.adek.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Activity.Dashboard;
import com.alphatz.adek.Adapter.AsupanAdapter;
import com.alphatz.adek.Model.AsupanModel;
import com.alphatz.adek.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AsupanFragment extends Fragment {
    private static final String TAG = "AsupanFragment";

    private RequestQueue requestQueue;
    private List<AsupanModel> menuList;

    private AsupanAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout showDataTotal;
    private RecyclerView recyclerView;
    private RecyclerView setupRecyclerViewDetail;
    private EditText searchField;
    private TextView tabCariMakanan;
    private TextView tabTerakhirDimakan;
    private TextView tabCatatanMinum;
    private TextView buttonTambahMenu;
    private TextView tvJumlahMenu, kaloriMenuDetail;

    public AsupanFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asupan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            TextView buttonAddAsupan = view.findViewById(R.id.button_tambah_menu);
            buttonAddAsupan.setOnClickListener(v -> openAddAsupanFragment());

            initViews(view);
            setupRecyclerView();
            setupSearch();
            setupTabs();
            fetchMenuData();
            fetchTotalMenuAndCalories();

        } catch (Exception e) {
            Log.e(TAG, "Error in onViewCreated: " + e.getMessage());
            showError("Terjadi kesalahan saat memuat aplikasi");
        }
    }

    private void openAddAsupanFragment() {
        Log.d(TAG, "openAddAsupanFragment called");

        try {
            if (!isAdded()) {
                Log.e(TAG, "Fragment not attached to activity");
                return;
            }
            if (getActivity() instanceof Dashboard) {
                ((Dashboard) getActivity()).hideBottomNavigation();
            }

            // Menggunakan fragment transaction untuk mengganti fragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new AddAsupanFragment());
            transaction.addToBackStack("AsupanFragment");
            transaction.commit();

            Log.d(TAG, "Fragment transaction committed to AddAsupanFragment");
        } catch (Exception e) {
            Log.e(TAG, "Error in openAddAsupanFragment: " + e.getMessage());
            showError("Terjadi kesalahan saat membuka form tambah asupan");
        }
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        showDataTotal = view.findViewById(R.id.showDataTotal);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvJumlahMenu = view.findViewById(R.id.tv_jumlah_menu);
        kaloriMenuDetail = view.findViewById(R.id.tv_total_kalori);
        searchField = view.findViewById(R.id.search_field);
        buttonTambahMenu = view.findViewById(R.id.button_tambah_menu);
        tabCariMakanan = view.findViewById(R.id.tab_cari_makanan);
        tabTerakhirDimakan = view.findViewById(R.id.tab_terakhir_dimakan);
        tabCatatanMinum = view.findViewById(R.id.tab_catatan_minum);
    }

    private void setupTabs() {
        tabCariMakanan.setOnClickListener(v -> {
            tabCariMakanan.setTextColor(requireContext().getColor(R.color.active));
            tabTerakhirDimakan.setTextColor(requireContext().getColor(android.R.color.darker_gray));
            tabCatatanMinum.setTextColor(requireContext().getColor(android.R.color.darker_gray));
            fetchMenuData();
        });

        tabTerakhirDimakan.setOnClickListener(v -> {
            tabCariMakanan.setTextColor(requireContext().getColor(android.R.color.darker_gray));
            tabTerakhirDimakan.setTextColor(requireContext().getColor(R.color.active));
            tabCatatanMinum.setTextColor(requireContext().getColor(android.R.color.darker_gray));

            replaceFragment(new TerakhirDimakan());
        });

        tabCatatanMinum.setOnClickListener(v -> {
            tabCariMakanan.setTextColor(requireContext().getColor(android.R.color.darker_gray));
            tabTerakhirDimakan.setTextColor(requireContext().getColor(android.R.color.darker_gray));
            tabCatatanMinum.setTextColor(requireContext().getColor(R.color.active));

            replaceFragment(new CatatanMinum());
        });


        showDataTotal.setOnClickListener(v -> {
            if (!isAdded()) return;

            SharedPreferences prefs = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
            String idUser = prefs.getString("idUser", "");

            if (idUser.isEmpty()) {
                showError("User ID not found");
                return;
            }

            String url = "http://10.0.2.2/ads_mysql/get_detail_kalori.php?id_user=" + idUser;
            showLoading(true);

            Log.d(TAG, "Fetching detailed data from URL: " + url);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> {
                        try {
                            Log.d(TAG, "Response received: " + response.toString());

                            if (response.getBoolean("success")) {
                                JSONArray dataArray = response.getJSONArray("data");
                                StringBuilder dataMessage = new StringBuilder();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject data = dataArray.getJSONObject(i);
                                    String namaMenu = data.getString("nama_menu");
                                    int jumlah = data.getInt("jumlah");
                                    int totalKalori = data.getInt("total_kalori");

                                    dataMessage.append(namaMenu)
                                            .append("\nJumlah: ").append(jumlah)
                                            .append("\nKalori: ").append(totalKalori)
                                            .append("\n\n");
                                }

                                // Create and show custom dialog
                                Dialog dialog = new Dialog(requireContext());
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_detail_info);

                                // Set dialog width to match parent with margins
                                Window window = dialog.getWindow();
                                if (window != null) {
                                    window.setLayout(
                                            WindowManager.LayoutParams.MATCH_PARENT,
                                            WindowManager.LayoutParams.WRAP_CONTENT
                                    );
                                    // Add margin to dialog
                                    WindowManager.LayoutParams params = window.getAttributes();
                                    params.width = getResources().getDisplayMetrics().widthPixels - 64; // 32dp margin each side
                                    window.setAttributes(params);
                                }

                                TextView contentView = dialog.findViewById(R.id.dialogContent);
                                contentView.setText(dataMessage.toString());

                                Button btnOk = dialog.findViewById(R.id.btnOk);
                                btnOk.setOnClickListener(view -> dialog.dismiss());

                                dialog.show();
                            } else {
                                showError("Gagal mendapatkan detail data");
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            showError("Terjadi kesalahan saat memproses data");
                        } finally {
                            showLoading(false);
                        }
                    },
                    error -> {
                        showLoading(false);
                        String errorMessage = "";
                        if (error.networkResponse != null) {
                            errorMessage = "Status Code: " + error.networkResponse.statusCode;
                        }
                        Log.e(TAG, "Request error: " + error.getMessage() + " " + errorMessage);
                        showError("Gagal mengambil data. Periksa koneksi internet Anda.");
                    }
            );

            request.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            requestQueue.add(request);
        });
    }

        private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AsupanAdapter(menuList, menu -> {
            if (menu != null) {
                // Ambil id_user dari SharedPreferences dengan key yang sama seperti di LoginActivity
                SharedPreferences prefs = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
                String idUser = prefs.getString("idUser", "");

                // Debug log memeriksa nilai idUser
                Log.d(TAG, "ID User from SharedPreferences: " + idUser);

                if (idUser.isEmpty()) {
                    Toast.makeText(requireContext(), "Session tidak valid, silakan login ulang", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Teruskan id_user ke bottom sheet
                AsupanBottomSheet bottomSheet = AsupanBottomSheet.newInstance(menu.getNamaMenu(), idUser);
                bottomSheet.show(getParentFragmentManager(), "AsupanBottomSheet");
            }
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
        if (menuList == null) return;

        List<AsupanModel> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (AsupanModel menu : menuList) {
            if (menu.getNamaMenu().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(menu);
            }
        }

        if (adapter != null) {
            adapter.updateList(filteredList);
        }
    }

    private void fetchMenuData() {
        if (!isAdded()) return;

        String url = "http://10.0.2.2/ads_mysql/get_menu_asupan.php";  // Ensure URL is correct
        showLoading(true);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> handleResponse(response),
                error -> handleError(error)
        );

        requestQueue.add(request);
    }

    private void fetchTotalMenuAndCalories() {
        if (!isAdded()) return;

        SharedPreferences prefs = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String idUser = prefs.getString("idUser", "");

        if (idUser.isEmpty()) {
            showError("User ID not found");
            return;
        }

        String url = "http://10.0.2.2/ads_mysql/get_detail_kalorinew.php?id_user=" + idUser;
        showLoading(true);

        Log.d(TAG, "Fetching data from URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        // Log response untuk debugging
                        Log.d(TAG, "Response received: " + response.toString());

                        if (response.getBoolean("success")) {
                            JSONObject data = response.getJSONObject("data");
                            // Ubah nama field sesuai dengan response JSON
                            int totalJumlah = data.getInt("jumlah_menu");
                            int totalKalori = data.getInt("total_kalori");

                            // Update UI with the totals
                            if (tvJumlahMenu != null && kaloriMenuDetail != null) {
                                tvJumlahMenu.setText(String.valueOf(totalJumlah));
                                kaloriMenuDetail.setText(String.valueOf(totalKalori));
                            }
                        } else {
                            showError("Gagal mendapatkan total menu dan kalori");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        showError("Terjadi kesalahan saat memuat data");
                    } finally {
                        showLoading(false);
                    }
                },
                error -> {
                    showLoading(false);
                    // Tambahkan detail error untuk debugging
                    String errorMessage = "";
                    if (error.networkResponse != null) {
                        errorMessage = "Status Code: " + error.networkResponse.statusCode;
                    }
                    Log.e(TAG, "Request error: " + error.getMessage() + " " + errorMessage);
                    showError("Gagal mengambil data. Periksa koneksi internet Anda.");
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000, // 30 detik timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }

    private void handleResponse(JSONObject response) {
        try {
            String status = response.optString("status");
            if (!"success".equals(status)) {
                showError(response.optString("message", "Data tidak tersedia"));
                return;
            }

            JSONArray data = response.getJSONArray("data");
            menuList.clear();

            for (int i = 0; i < data.length(); i++) {
                JSONObject menuObj = data.getJSONObject(i);

                // Pastikan AsupanModel memiliki semua field yang diperlukan
                AsupanModel menu = new AsupanModel(
                        menuObj.getString("nama_menu"),
                        menuObj.optInt("kalori", 0),  // Default 0 jika tidak ada
                        menuObj.optInt("protein", 0),
                        menuObj.optInt("karbohidrat", 0),
                        menuObj.optInt("lemak", 0)
                );
                menuList.add(menu);
            }

            if (adapter != null) {
                adapter.updateList(menuList);
            }

            if (menuList.isEmpty()) {
                showError("Tidak ada data menu");
            }

        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error: " + e.getMessage());
            showError("Gagal memproses data");
        } finally {
            showLoading(false);
        }
    }

    private void showLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void handleError(VolleyError error) {
        showLoading(false);
        Log.e(TAG, "Request error: " + error.getMessage());
        showError("Gagal mengambil data. Periksa koneksi internet Anda.");
    }
}
