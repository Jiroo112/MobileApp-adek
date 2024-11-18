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

import com.alphatz.adek.Activity.Konfirmasi;
import com.alphatz.adek.Adapter.KonsultasiAdapter;
import com.alphatz.adek.Model.KonsultasiModel;
import com.alphatz.adek.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KonsultasiFragment extends Fragment {
    private static final String TAG = "KonsultasiFragment";
    private RequestQueue requestQueue;
    private List<KonsultasiModel> konsultanList = new ArrayList<>();
    private KonsultasiAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private EditText searchField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_konsultasi, container, false);

        // Initialize views
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchField = view.findViewById(R.id.search_field);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new KonsultasiAdapter(konsultanList, konsultan -> showKonfirmasiDialog(konsultan));
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(requireContext());
        setupSearch();
        getDokterData();

        return view;
    }

    private void setupSearch() {
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDoctors(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterDoctors(String query) {
        List<KonsultasiModel> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();

        for (KonsultasiModel konsultan : konsultanList) {
            if (konsultan.getNamaLengkap().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(konsultan);
            }
        }

        adapter.updateList(filteredList);
    }

    private void getDokterData() {
        String url = "http://10.0.2.2/ads_mysql/get_dokter.php";
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        //buat debug
                        Log.d(TAG, "Raw response: " + response.toString());

                        if (!response.has("data")) {
                            showError("Format response tidak valid: Tidak ada field 'data'");
                            return;
                        }

                        JSONArray jsonArray = response.getJSONArray("data");
                        konsultanList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject konsultanObj = jsonArray.getJSONObject(i);

                            Log.d(TAG, "Processing konsultan: " + konsultanObj.toString());

                            String id = konsultanObj.optString("id_konsultan", "");
                            String nama = konsultanObj.optString("nama_lengkap", "");
                            String noHp = konsultanObj.optString("no_hp", "");
                            String foto = konsultanObj.optString("foto_dokter", null);

                            //validasi data konsultannya
                            if (id.isEmpty() || nama.isEmpty()) {
                                Log.e(TAG, "Invalid data for konsultan: " + konsultanObj.toString());
                                continue;
                            }

                            KonsultasiModel konsultan = new KonsultasiModel(id, nama, noHp, foto);
                            konsultanList.add(konsultan);
                        }

                        // Update adapter
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                adapter.updateList(konsultanList);
                                if (konsultanList.isEmpty()) {
                                    showError("Tidak ada data konsultan");
                                }
                            });
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                        showError("Gagal memproses data: " + e.getMessage());
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Volley Error: " + error.toString());
                    showError("Gagal mengambil data: " + error.getMessage());
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }

    private void showKonfirmasiDialog(KonsultasiModel konsultan) {
        if (getActivity() == null) return;

        Konfirmasi konfirmasiDialog = new Konfirmasi();
        Bundle args = new Bundle();
        args.putString("id_konsultan", konsultan.getIdKonsultan());
        args.putString("nama_konsultan", konsultan.getNamaLengkap());
        konfirmasiDialog.setArguments(args);
        konfirmasiDialog.show(getChildFragmentManager(), "KonfirmasiDialog");
    }

    private void showError(String message) {
        if (getContext() != null) {
            getActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
            );
        }
        Log.e(TAG, "Error: " + message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}