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

public class AsupanFragment extends Fragment {
    private static final String TAG = "AsupanFragment";

    private RequestQueue requestQueue;
    private List<AsupanModel> menuList;
    private AsupanAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private EditText searchField;
    private TextView tabCariMakanan;
    private TextView tabTerakhirDimakan;
    private TextView tabCatatanMinum;
    private TextView buttonTambahMenu;


    public AsupanFragment() {
        // Required empty public constructor
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
            // Perbaikan implementasi onClick
            TextView buttonAddAsupan = view.findViewById(R.id.button_tambah_menu);
            buttonAddAsupan.setOnClickListener(v -> openAddAsupanFragment());

            initViews(view);
            setupRecyclerView();
            setupSearch();
            setupTabs();
            fetchMenuData();
        } catch (Exception e) {
            Log.e(TAG, "Error in onViewCreated: " + e.getMessage());
            showError("Terjadi kesalahan saat memuat aplikasi");
        }
    }

    private void openAddAsupanFragment() {
        Log.d(TAG, "openAddAsupanFragment called");

        try {
            // Pastikan fragment masih attached ke activity
            if (!isAdded()) {
                Log.e(TAG, "Fragment not attached to activity");
                return;
            }

            // Hide bottom navigation if we're in Dashboard activity
            if (getActivity() instanceof Dashboard) {
                ((Dashboard) getActivity()).hideBottomNavigation();
            }

            // Gunakan childFragmentManager jika fragment di dalam fragment
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
        recyclerView = view.findViewById(R.id.recyclerView);
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

            // Pindah ke fragment CatatanMinum
            replaceFragment(new CatatanMinum());
        });
    }

    // Fungsi untuk mengganti fragment
    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // Pastikan ID sesuai dengan container di layout
                .addToBackStack(null) // Menyimpan ke back stack jika ingin dapat kembali ke fragment sebelumnya
                .commit();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AsupanAdapter(menuList, menu -> {
            // Handle item click
            if (menu != null) {
                Toast.makeText(getContext(), "Selected: " + menu.getNamaMenu(), Toast.LENGTH_SHORT).show();
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

        String url = "http://10.0.2.2/ads_mysql/get_menu_asupan.php";
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
                AsupanModel menu = new AsupanModel(menuObj.getString("nama_menu"));
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
    private void handleError(VolleyError error) {
        Log.e(TAG, "Volley error: " + error.getMessage());
        showError("Gagal mengambil data. Periksa koneksi internet Anda.");
        showLoading(false);
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void showError(String message) {
        if (isAdded() && getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}