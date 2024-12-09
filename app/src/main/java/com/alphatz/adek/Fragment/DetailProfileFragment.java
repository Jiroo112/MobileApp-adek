package com.alphatz.adek.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private static final String URL_UPDATE_PROFILE = "http://10.0.2.2/ads_mysql/account/update_profile.php";

    // TextViews untuk profil
    private TextView tvName, tvTipeDiet, tvGender, tvBirthDate,
            tvEmail, tvHeight, tvWeight, tvNoTelepon, tvBmi;
    private ImageView profileImage;

    // EditTexts untuk edit profil
    private EditText etName, etTipeDiet, etGender, etBirthDate, etEmail, etHeight, etWeight, etNoTelepon;
    private Button btnEditProfile;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_ID_USER = "idUser";
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_profile, container, false);

        Context context = getContext();
        if (context != null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }

        requestQueue = Volley.newRequestQueue(requireContext());
        initializeViews(view);
        fetchProfileData();

        return view;
    }

    private void initializeViews(View view) {
        profileImage = view.findViewById(R.id.profileImage);
        tvName = view.findViewById(R.id.tvName);
        tvTipeDiet = view.findViewById(R.id.tv_tipediet);
        tvBmi = view.findViewById(R.id.tvBmi);

        // Initialize EditTexts and Button
        etName = view.findViewById(R.id.etName);
        etTipeDiet = view.findViewById(R.id.etTipeDiet);
        etBirthDate = view.findViewById(R.id.etBirthDate);
        etEmail = view.findViewById(R.id.etEmail);
        etHeight = view.findViewById(R.id.etHeight);
        etWeight = view.findViewById(R.id.etWeight);

        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        // Set onClickListener for button
        btnEditProfile.setOnClickListener(v -> updateProfileData());
    }

    private void fetchProfileData() {
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
                            if (!jsonResponse.isNull("data")) {
                                JSONObject userData = jsonResponse.getJSONObject("data");
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

    private void updateUIWithProfileData(JSONObject userData) throws JSONException {
        tvName.setText(userData.optString("nama_lengkap", "Tidak diketahui"));
        etName.setText(userData.optString("nama_lengkap", ""));

        tvTipeDiet.setText(userData.optString("tipe_diet", "Tidak diketahui"));
        etTipeDiet.setText(userData.optString("tipe_diet", ""));

        tvGender.setText(userData.optString("gender", "Tidak diketahui"));
        etGender.setText(userData.optString("gender", ""));

        tvBirthDate.setText(userData.optString("tanggal_lahir", "Tidak diketahui"));
        etBirthDate.setText(userData.optString("tanggal_lahir", ""));

        tvEmail.setText(userData.optString("email", "Tidak diketahui"));
        etEmail.setText(userData.optString("email", ""));

        tvHeight.setText(userData.optString("tinggi_badan", "0") + " cm");
        etHeight.setText(userData.optString("tinggi_badan", ""));

        tvWeight.setText(userData.optString("berat_badan", "0") + " kg");
        etWeight.setText(userData.optString("berat_badan", ""));

        tvNoTelepon.setText(userData.optString("no_hp", "-"));
        etNoTelepon.setText(userData.optString("no_hp", ""));

        // Perhitungan BMI
        calculateAndDisplayBMI(userData);
    }

    private void calculateAndDisplayBMI(JSONObject userData) throws JSONException {
        String heightStr = userData.optString("tinggi_badan", "0");
        String weightStr = userData.optString("berat_badan", "0");

        if (!heightStr.equals("0") && !weightStr.equals("0")) {
            double height = Double.parseDouble(heightStr) / 100; // Konversi ke meter
            double weight = Double.parseDouble(weightStr);
            double bmi = weight / (height * height); // Rumus BMI

            // Set BMI ke TextView
            tvBmi.setText(String.format("BMI: %.2f", bmi));
        } else {
            tvBmi.setText("BMI: Tidak tersedia");
        }
    }

    private void updateProfileData() {
        String idUser = sharedPreferences.getString(KEY_ID_USER, "");

        if (idUser.isEmpty()) {
            Toast.makeText(requireContext(), "ID Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_PROFILE,
                response -> {
                    Log.d(TAG, "Update Response: " + response);
                    Toast.makeText(requireContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.e(TAG, "Volley error: " + (error.getMessage() != null ? error.getMessage() : "Unknown error"), error);
                    Toast.makeText(requireContext(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", idUser);
                params.put("nama_lengkap", etName.getText().toString());
                params.put("tipe_diet", etTipeDiet.getText().toString());
                params.put("gender", etGender.getText().toString());
                params.put("tanggal_lahir", etBirthDate.getText().toString());
                params.put("email", etEmail.getText().toString());
                params.put("tinggi_badan", etHeight.getText().toString());
                params.put("berat_badan", etWeight.getText().toString());
                params.put("no_hp", etNoTelepon.getText().toString());
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
}