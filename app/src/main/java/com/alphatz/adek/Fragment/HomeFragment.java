package com.alphatz.adek.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alphatz.adek.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    TextView welcome;
    private TextView persentaseGula;
    private TextView persentaseProtein;
    private TextView persentaseLemak;
    private TextView persentaseAir;
    private TextView persentaseKarbohidrat;

    private static final String ARG_NAMA_LENGKAP = "nama_lengkap";
    private String namaLengkap;

    private RequestQueue requestQueue;

    public HomeFragment() {
        // Default constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchTotalsData();
    }

    public static HomeFragment newInstance(String namaLengkap) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAMA_LENGKAP, namaLengkap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            namaLengkap = getArguments().getString(ARG_NAMA_LENGKAP, "Pengguna");
        }
        requestQueue = Volley.newRequestQueue(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        persentaseGula = view.findViewById(R.id.persentase_gula);
        persentaseAir = view.findViewById(R.id.persentase_air);
        persentaseKarbohidrat = view.findViewById(R.id.persentase_karbohidrat);
        persentaseLemak = view.findViewById(R.id.persentase_lemak);
        persentaseProtein = view.findViewById(R.id.persentase_protein);

        welcome = view.findViewById(R.id.welcome);
        welcome.setText("Halo, " + namaLengkap + "!");

        return view;
    }

    private void fetchTotalsData() {

        if (!isAdded()) return;

        SharedPreferences prefs = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String idUser = prefs.getString("idUser", "");

        if (idUser.isEmpty()) {
            showError("User ID not found");
            return;
        }
        String url = "http://10.0.2.2/ads_mysql/get_totals.php?id_user=" + idUser;
        Log.d(TAG, "Fetching totals data from URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        Log.d(TAG, "Response received: " + response.toString());

                        if (response.getBoolean("success")) {
                            JSONObject data = response.getJSONObject("data");

                            // Ambil data dari respons
                            double totalGula = data.getDouble("total_gula");
                            double totalMinum = data.getDouble("total_minum");
                            double totalKarbohidrat = data.getDouble("total_karbohidrat");
                            double totalLemak = data.getDouble("total_lemak");
                            double totalProtein = data.getDouble("total_protein");

                            // Konversi ke persentase (contoh: asumsikan nilai maksimum 100 per komponen)
                            double gulaPercentage = (totalGula / 100) * 100;
                            double airPercentage = (totalMinum / 100) * 100;
                            double karbohidratPercentage = (totalKarbohidrat / 100) * 100;
                            double lemakPercentage = (totalLemak / 100) * 100;
                            double proteinPercentage = (totalProtein / 100) * 100;

                            // Tampilkan di TextView dalam format persentase
                            persentaseGula.setText(String.format("%.1f%%", gulaPercentage));
                            persentaseAir.setText(String.format("%.1f%%", airPercentage));
                            persentaseKarbohidrat.setText(String.format("%.1f%%", karbohidratPercentage));
                            persentaseLemak.setText(String.format("%.1f%%", lemakPercentage));
                            persentaseProtein.setText(String.format("%.1f%%", proteinPercentage));

                        } else {
                            showError("Data tidak ditemukan.");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        showError("Kesalahan memproses data.");
                    }
                },
                error -> {
                    String errorMessage = error.networkResponse != null
                            ? "Status Code: " + error.networkResponse.statusCode
                            : "Kesalahan jaringan.";
                    Log.e(TAG, "Request error: " + error.getMessage() + " " + errorMessage);
                    showError("Gagal mengambil data. Periksa koneksi Anda.");
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }


    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
