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
public class OlahragaFragment extends Fragment {

    private static final String TAG = "OlahragaFragment";
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
        View view = inflater.inflate(R.layout.fragment_olahraga, container, false);

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
                    navigateToDetail(olahraga);
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
        btnKelenturan.setOnClickListener(v -> navigateToFragment(new KardioFragment()));
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
        String url = "http://adek-app.my.id/ads_mysql/search/get_olahraga.php";
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
                            try {
                                JSONObject olahragaObj = jsonArray.getJSONObject(i);

                                // Extract values with null checks and default values
                                String namaOlahraga = olahragaObj.optString("nama_olahraga", "");
                                String deskripsi = olahragaObj.optString("deskripsi", "");
                                String gambarPath = olahragaObj.optString("gambar", "");
                                String caraOlahraga = olahragaObj.optString("cara_olahraga","");

                                // Prepend base URL to gambar path
                                String gambarUrl = "https://adek-app.my.id/Images/" + gambarPath;

                                // Create OlahragaModel with URL instead of byte array
                                OlahragaModel olahraga = new OlahragaModel(
                                        namaOlahraga,
                                        deskripsi,
                                        gambarUrl,
                                        caraOlahraga// Use URL instead of byte array
                                );

                                olahragaList.add(olahraga);
                            } catch (JSONException e) {
                                // Log the error and continue processing other items
                                Log.e("OlahragaParser", "Error parsing JSON object", e);
                            }
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


    private void navigateToDetail(OlahragaModel olahraga){
        if (olahraga == null) return;

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_detail_olahraga, null);

        builder.setView(dialogView);

        TextView titleText = dialogView.findViewById(R.id.title_olahraga);
        TextView detailOlahraga = dialogView.findViewById(R.id.detail_olahraga);
        ImageView imageResep = dialogView.findViewById(R.id.image_olahraga);
        Button closeButton = dialogView.findViewById(R.id.btn_close);

        titleText.setText(olahraga.getNamaOlahraga());

        closeButton.setOnClickListener(v -> builder.create().dismiss());
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
