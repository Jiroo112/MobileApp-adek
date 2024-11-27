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

                            // Convert string values to double where needed
                            double totalGula = parseDoubleValue(data.getString("total_gula"));
                            double totalMinum = parseDoubleValue(data.getString("total_minum"));
                            double totalKarbohidrat = parseDoubleValue(data.getString("total_karbohidrat"));
                            double totalLemak = parseDoubleValue(data.getString("total_lemak"));
                            double totalProtein = parseDoubleValue(data.getString("total_protein"));

                            String tipeDiet = data.getString("tipe_diet").toLowerCase();
                            double beratBadan = parseDoubleValue(data.getString("berat_badan"));
                            double tinggiBadan = parseDoubleValue(data.getString("tinggi_badan")); // dalam cm

                            // Hitung BMI
                            double tinggiMeter = tinggiBadan / 100;
                            double bmi = beratBadan / (tinggiMeter * tinggiMeter);
                            Log.d(TAG, "BMI: " + bmi);

                            // Hitung BMR (Basal Metabolic Rate) menggunakan formula Harris-Benedict
                            boolean isOverweight = bmi >= 25;
                            double bmr;

                            // BMR berbeda untuk overweight dan normal weight
                            if (isOverweight) {
                                // Modifikasi formula untuk overweight
                                bmr = 10 * beratBadan + 6.25 * tinggiBadan - 5 * 25 + 5;
                            } else {
                                // Formula standar
                                bmr = 10 * beratBadan + 6.25 * tinggiBadan - 5 * 25 + 5;
                            }

                            // Hitung kebutuhan kalori harian berdasarkan tipe diet
                            double kaloriHarian;
                            switch (tipeDiet) {
                                case "mengurangi berat badan":
                                    kaloriHarian = bmr * 0.8; // Deficit 20%
                                    break;
                                case "menambah berat badan":
                                    kaloriHarian = bmr * 1.2; // Surplus 20%
                                    break;
                                case "menjaga berat badan":
                                    kaloriHarian = bmr * 1.0; // Maintenance
                                    break;
                                default:
                                    throw new IllegalStateException("Tipe diet tidak valid: " + tipeDiet);
                            }

                            // Hitung target nutrisi berdasarkan kalori harian
                            // Distribusi makronutrien berdasarkan tipe diet
                            double proteinTarget, karbohidratTarget, lemakTarget, gulaTarget, airTarget;

                            switch (tipeDiet) {
                                case "mengurangi berat badan":
                                    // High protein, moderate fat, lower carb
                                    proteinTarget = (kaloriHarian * 0.35) / 4; // 35% dari kalori (4 kal/g)
                                    lemakTarget = (kaloriHarian * 0.25) / 9;   // 25% dari kalori (9 kal/g)
                                    karbohidratTarget = (kaloriHarian * 0.40) / 4; // 40% dari kalori (4 kal/g)
                                    gulaTarget = karbohidratTarget * 0.1; // 10% dari karbohidrat
                                    airTarget = beratBadan * 35; // 35ml per kg berat badan
                                    break;

                                case "menambah berat badan":
                                    // High carb, high protein, moderate fat
                                    proteinTarget = (kaloriHarian * 0.30) / 4; // 30% dari kalori
                                    lemakTarget = (kaloriHarian * 0.25) / 9;   // 25% dari kalori
                                    karbohidratTarget = (kaloriHarian * 0.45) / 4; // 45% dari kalori
                                    gulaTarget = karbohidratTarget * 0.15; // 15% dari karbohidrat
                                    airTarget = beratBadan * 40; // 40ml per kg berat badan
                                    break;

                                case "menjaga berat badan":
                                    // Balanced distribution
                                    proteinTarget = (kaloriHarian * 0.30) / 4; // 30% dari kalori
                                    lemakTarget = (kaloriHarian * 0.30) / 9;   // 30% dari kalori
                                    karbohidratTarget = (kaloriHarian * 0.40) / 4; // 40% dari kalori
                                    gulaTarget = karbohidratTarget * 0.12; // 12% dari karbohidrat
                                    airTarget = beratBadan * 30; // 30ml per kg berat badan
                                    break;

                                default:
                                    throw new IllegalStateException("Tipe diet tidak valid");
                            }

                            // Faktor aktivitas tambahan berdasarkan BMI
                            if (bmi < 18.5) { // Underweight
                                proteinTarget *= 1.2;
                                karbohidratTarget *= 1.1;
                            } else if (bmi > 25) { // Overweight
                                lemakTarget *= 0.9;
                                gulaTarget *= 0.8;
                            }

                            // Konversi nilai ke persentase pencapaian
                            double proteinPercentage = (totalProtein / proteinTarget) * 100;
                            double lemakPercentage = (totalLemak / lemakTarget) * 100;
                            double karbohidratPercentage = (totalKarbohidrat / karbohidratTarget) * 100;
                            double gulaPercentage = (totalGula / gulaTarget) * 100;
                            double airPercentage = (totalMinum / airTarget) * 100;

                            // Log target values for debugging
                            Log.d(TAG, String.format("Targets - Protein: %.1f, Lemak: %.1f, Karbo: %.1f, Gula: %.1f, Air: %.1f",
                                    proteinTarget, lemakTarget, karbohidratTarget, gulaTarget, airTarget));

                            // Update UI on main thread
                            requireActivity().runOnUiThread(() -> {
                                persentaseProtein.setText(String.format("%.1f%%", proteinPercentage));
                                persentaseLemak.setText(String.format("%.1f%%", lemakPercentage));
                                persentaseKarbohidrat.setText(String.format("%.1f%%", karbohidratPercentage));
                                persentaseGula.setText(String.format("%.1f%%", gulaPercentage));
                                persentaseAir.setText(String.format("%.1f%%", airPercentage));

                                // Optional: Add target values in the UI
                                // Example: persentaseProtein.setHint(String.format("Target: %.1fg", proteinTarget));
                            });

                        } else {
                            showError("Data tidak ditemukan.");
                        }
                    } catch (JSONException | IllegalStateException e) {
                        Log.e(TAG, "Error: " + e.getMessage());
                        showError("Kesalahan memproses data: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMessage = error.networkResponse != null
                            ? "Status Code: " + error.networkResponse.statusCode
                            : "Kesalahan jaringan.";
                    Log.e(TAG, "Request error: " + error.getMessage() + " " + errorMessage);
                    showError("Gagal mengambil data. Periksa" +
                            " koneksi Anda.");
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }

    // Helper method to safely parse string values to double
    private double parseDoubleValue(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing value: " + value);
            return 0.0;
        }
    }
    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
