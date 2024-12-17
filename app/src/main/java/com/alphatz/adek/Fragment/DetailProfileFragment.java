package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alphatz.adek.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DetailProfileFragment extends Fragment {
    private static final String TAG = "DetailProfileFragment";
    private static final String URL_PROFILE = "http://adek-app.my.id/ads_mysql/account/profile_adek.php";

    private TextView tvName,
            tvTipeDiet,
            tvGender,
            tvBirthDate,
            tvEmail,
            tvHeight,
            tvWeight,
            tvBmi,
            tvUsia,  // Make sure this matches your layout
            tvNoHP;

    private String currentNamaLengkap;
    private RequestQueue requestQueue;

    public DetailProfileFragment() {
    }

    public static DetailProfileFragment newInstance(String nama_lengkap) {
        DetailProfileFragment fragment = new DetailProfileFragment();
        Bundle args = new Bundle();
        args.putString("nama_lengkap", nama_lengkap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve nama_lengkap from arguments
        if (getArguments() != null) {
            currentNamaLengkap = getArguments().getString("nama_lengkap");
        }

        // If nama_lengkap is still null, set to a default value
        if (TextUtils.isEmpty(currentNamaLengkap)) {
            currentNamaLengkap = "Pengguna";
        }

        requestQueue = Volley.newRequestQueue(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_profile, container, false);

        initializeViews(view);
        fetchProfileData();

        return view;
    }

    private void initializeViews(View view) {
        tvName = view.findViewById(R.id.tvName);
        tvTipeDiet = view.findViewById(R.id.tv_tipediet);
        tvGender = view.findViewById(R.id.tvGender);
        tvBirthDate = view.findViewById(R.id.tvBirthDate);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvBmi = view.findViewById(R.id.tvBmi);

        // Optional: Set default text to prevent null issues
        if (tvUsia != null) {
            tvUsia.setText("Usia: Tidak tersedia");
        }
    }

    private void fetchProfileData() {
        Log.d(TAG, "Fetching data for nama_lengkap: " + currentNamaLengkap);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROFILE,
                response -> {
                    Log.d(TAG, "Response: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            updateUIWithProfileData(data);
                        } else {
                            String message = jsonObject.getString("message");
                            Log.e(TAG, "API Error: " + message);
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        Log.e(TAG, "Network Error: " + error.networkResponse.statusCode);
                        Log.e(TAG, "Error Response Data: " + new String(error.networkResponse.data));
                    }
                    Log.e(TAG, "Volley error: " + Log.getStackTraceString(error));
                    Toast.makeText(getContext(), "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", currentNamaLengkap);
                Log.d(TAG, "Sending params: " + params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void updateUIWithProfileData(JSONObject data) throws JSONException {
        // Null checks for all TextViews
        if (tvName != null)
            tvName.setText(data.optString("nama_lengkap", "Tidak diketahui"));

        if (tvTipeDiet != null)
            tvTipeDiet.setText(data.optString("tipe_diet", "Tidak diketahui"));

        if (tvGender != null)
            tvGender.setText(data.optString("gender", "Tidak diketahui"));

        if (tvEmail != null)
            tvEmail.setText(data.optString("email", "Tidak diketahui"));

        if (tvNoHP != null)
            tvNoHP.setText(data.optString("no_hp", "Tidak diketahui"));

        String berat = data.optString("berat_badan", "0");
        String tinggi = data.optString("tinggi_badan", "0");
        String tanggalLahir = data.optString("tanggal_lahir", "");

        if (tvWeight != null)
            tvWeight.setText(berat + " kg");

        if (tvHeight != null)
            tvHeight.setText(tinggi + " cm");

        // Calculate and display BMI
        calculateAndDisplayBMI(berat, tinggi);

        // Set birth date and calculate age
        if (tvBirthDate != null)
            tvBirthDate.setText(tanggalLahir);

        calculateAndDisplayAge(tanggalLahir);
    }

    private void calculateAndDisplayAge(String tanggalLahir) {
        // Null check for tvUsia
        if (tvUsia == null) {
            Log.e(TAG, "tvUsia is null, cannot display age");
            return;
        }

        try {
            // Format tanggal lahir
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(tanggalLahir, formatter);
            LocalDate today = LocalDate.now();

            // Hitung usia
            int age = Period.between(birthDate, today).getYears();
            tvUsia.setText(age + " tahun");
        } catch (Exception e) {
            Log.e(TAG, "Error parsing tanggal_lahir: " + e.getMessage());
            tvUsia.setText("Error menghitung tahun");
        }
    }

    private void calculateAndDisplayBMI(String beratStr, String tinggiStr) {
        // Null check for tvBmi
        if (tvBmi == null) {
            Log.e(TAG, "tvBmi is null, cannot display BMI");
            return;
        }

        try {
            double berat = Double.parseDouble(beratStr);
            double tinggi = Double.parseDouble(tinggiStr) / 100;

            double bmi = berat / (tinggi * tinggi);
            tvBmi.setText(String.format("BMI: %.2f", bmi));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing berat or tinggi: " + e.getMessage());
            tvBmi.setText("BMI: Tidak tersedia");
        }
    }
}