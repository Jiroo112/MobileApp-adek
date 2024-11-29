package com.alphatz.adek.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailProfileFragment extends Fragment {
    private static final String TAG = "DetailProfileFragment";
    private static final String URL_PROFILE = "http://10.0.2.2/ads_mysql/account/get_profile.php";

    // TextViews untuk profil
    private TextView tvName, tvTipeDiet, tvGender, tvBirthDate,
            tvEmail, tvHeight, tvWeight, tvNoTelepon;
    private ImageView profileImage;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_ID_USER = "idUser";
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Pastikan menggunakan layout yang benar
        View view = inflater.inflate(R.layout.fragment_detail_profile, container, false);

        // Inisialisasi SharedPreferences dengan pengecekan null
        Context context = getContext();
        if (context != null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }

        // Inisialisasi RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());

        // Inisialisasi Views
        initializeViews(view);

        // Ambil dan tampilkan profil
        fetchProfileData();

        return view;
    }

    private void initializeViews(View view) {
        // Pastikan ID view sesuai dengan layout
        profileImage = view.findViewById(R.id.profileImage);
        tvName = view.findViewById(R.id.tvName);
        tvTipeDiet = view.findViewById(R.id.tv_tipediet);
        tvGender = view.findViewById(R.id.tvGender);
        tvBirthDate = view.findViewById(R.id.tvBirthDate);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvNoTelepon = view.findViewById(R.id.tvNoTelepon);
    }

    private void fetchProfileData() {
        // Tambahkan pengecekan null untuk SharedPreferences
        if (sharedPreferences == null) {
            Toast.makeText(requireContext(), "Gagal mengakses preferensi", Toast.LENGTH_SHORT).show();
            return;
        }

        String idUser = sharedPreferences.getString(KEY_ID_USER, "");

        if (idUser.isEmpty()) {
            Toast.makeText(requireContext(), "ID Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROFILE,
                response -> {
                    Log.d(TAG, "Response: " + response);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");

                        if (status.equals("success")) {
                            // Tambahkan pengecekan null untuk objek data
                            if (!jsonResponse.isNull("data")) {
                                JSONObject userData = jsonResponse.getJSONObject("data");

                                // Update UI dengan data profil
                                updateUIWithProfileData(userData);
                            } else {
                                Toast.makeText(requireContext(), "Data kosong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing error: " + e.getMessage(), e);
                        Toast.makeText(requireContext(), "Kesalahan parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley error: " + (error.getMessage() != null ? error.getMessage() : "Unknown error"), error);
                    Toast.makeText(requireContext(), "Gagal mengambil data profil", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", idUser);
                Log.d(TAG, "Sending params: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(stringRequest);
    }

    private void updateUIWithProfileData(JSONObject userData) throws JSONException {
        // Metode terpisah untuk update UI dengan penanganan null
        tvName.setText(userData.optString("nama_lengkap", "Tidak diketahui"));
        tvTipeDiet.setText(userData.optString("tipe_diet", "Tidak diketahui"));
        tvGender.setText(userData.optString("gender", "Tidak diketahui"));
        tvBirthDate.setText(userData.optString("tanggal_lahir", "Tidak diketahui"));
        tvEmail.setText(userData.optString("email", "Tidak diketahui"));
        tvHeight.setText(userData.optString("tinggi_badan", "0") + " cm");
        tvWeight.setText(userData.optString("berat_badan", "0") + " kg");
        tvNoTelepon.setText(userData.optString("no_hp", "Tidak diketahui"));
    }
}