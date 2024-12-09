package com.alphatz.adek.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alphatz.adek.Activity.LinearGauge;
import com.alphatz.adek.Activity.UpdateTinggi;
import com.alphatz.adek.Activity.UpdateBerat;
import com.alphatz.adek.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private ImageView tipe_diet;
    private ImageView settings;
    private ImageView fotoProfil;
    private TextView beratTextView;
    private TextView tinggiTextView;
    private TextView bmiTextView;
    private TextView txt_nama_lengkap;
    private TextView textUsia;
    private LinearGauge linearGauge;
    private String currentNamaLengkap;
    private RequestQueue requestQueue;
    private Bitmap foto_profile;

    private String baseUrl = "http://10.0.2.2/ads_mysql/account/profile_adek.php";

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String nama_lengkap) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("nama_lengkap", nama_lengkap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());

        if (getArguments() != null) {
            currentNamaLengkap = getArguments().getString("nama_lengkap", "Pengguna");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            // Dapatkan URI file yang dipilih
            Uri imageUri = data.getData();

            Toast.makeText(getContext(), "Gambar dipilih: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        settings = view.findViewById(R.id.settings);

        textUsia = view.findViewById(R.id.text_usia);
        beratTextView = view.findViewById(R.id.text_berat);
        tinggiTextView = view.findViewById(R.id.text_tinggi);
        bmiTextView = view.findViewById(R.id.text_bmi);
        txt_nama_lengkap = view.findViewById(R.id.txt_username);
        linearGauge = view.findViewById(R.id.linearGauge);
        fotoProfil = view.findViewById(R.id.foto_profil);
        beratTextView.setText("Loading...");
        tinggiTextView.setText("Loading...");
        bmiTextView.setText("Loading...");

        txt_nama_lengkap.setText(currentNamaLengkap);
        getUserDataFromApi();

        settings.setOnClickListener(v -> openSettingsFragment());

        beratTextView.setOnClickListener(v -> {
            UpdateBerat konfirmasiDialog = new UpdateBerat();
            konfirmasiDialog.show(getChildFragmentManager(), "KonfirmasiDialog");
        });

        tinggiTextView.setOnClickListener(v -> {
            showUpdateTinggi();
        });

        return view;
    }

    private void getUserDataFromApi() {
        Log.d(TAG, "Fetching data for nama_lengkap: " + currentNamaLengkap);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response); // Debug log
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("success")) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                String berat = data.getString("berat_badan");
                                String tinggi = data.getString("tinggi_badan");
                                String tanggalLahir = data.getString("tanggal_lahir");

                                // Set data ke TextView
                                beratTextView.setText(berat + " kg");
                                tinggiTextView.setText(tinggi + " cm");

                                // Hitung dan tampilkan BMI dan usia
                                calculateAndDisplayBMI(berat, tinggi);
                                calculateAndDisplayAge(tanggalLahir);

                                if (data.has("gambar") && !data.isNull("gambar")) {
                                    String gambarBase64 = data.getString("gambar"); // Ambil data gambar dalam base64
                                    if (!gambarBase64.isEmpty()) {
                                        try {
                                            // Decode Base64 menjadi bitmap
                                            byte[] decodedString = Base64.decode(gambarBase64, Base64.DEFAULT);
                                            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                            // Pasang bitmap ke ImageView
                                            foto_profile = decodedBitmap;
                                            fotoProfil.setImageBitmap(decodedBitmap);

                                            Log.d(TAG, "Gambar berhasil didecode dan ditampilkan.");
                                        } catch (IllegalArgumentException e) {
                                            Log.e(TAG, "Error decoding Base64 image: " + e.getMessage());
                                            fotoProfil.setImageResource(R.drawable.ic_profil); // Gunakan gambar default
                                        }
                                    } else {
                                        Log.d(TAG, "Data gambar kosong. Menggunakan gambar default.");
                                        fotoProfil.setImageResource(R.drawable.ic_profil);
                                    }
                                } else {
                                    Log.d(TAG, "Field gambar tidak ada atau null. Menggunakan gambar default.");
                                    fotoProfil.setImageResource(R.drawable.ic_profil);
                                }
                            } else {
                                String message = jsonObject.getString("message");
                                Log.e(TAG, "API Error: " + message);
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                            Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + (error.getMessage() != null ? error.getMessage() : "Unknown error"));
                        Toast.makeText(getContext(), "Network error occurred", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", currentNamaLengkap);  // Ubah menjadi nama_lengkap
                Log.d(TAG, "Sending params: " + params.toString()); // Debug log
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

    private void calculateAndDisplayAge(String tanggalLahir) {
        try {
            // Format tanggal lahir
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(tanggalLahir, formatter);
            LocalDate today = LocalDate.now();

            // Hitung usia
            int age = Period.between(birthDate, today).getYears();
            textUsia.setText(age + " tahun");
        } catch (Exception e) {
            Log.e(TAG, "Error parsing tanggal_lahir: " + e.getMessage());
            textUsia.setText("Error menghitung tahun");
        }
    }

    private void calculateAndDisplayBMI(String beratStr, String tinggiStr) {
        try {
            double berat = Double.parseDouble(beratStr);
            double tinggi = Double.parseDouble(tinggiStr) / 100;

            double bmi = berat / (tinggi * tinggi);

            String bmiText = String.format("%.2f", bmi);
            bmiTextView.setText("BMI: " + bmiText);

            // Simpan BMI ke SharedPreferences
            requireContext().getSharedPreferences("UserPrefs", Activity.MODE_PRIVATE)
                    .edit()
                    .putString("BMI", bmiText)
                    .apply();

            float bmiFinal = (float) bmi;
            if (bmiFinal < 0) bmiFinal = 0;
            if (bmiFinal > 100) bmiFinal = 100;

            if (linearGauge != null) {
                linearGauge.setProgressWithAnimation(bmiFinal);
            } else {
                Log.e(TAG, "LinearGauge is null");
            }

        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing berat or tinggi: " + e.getMessage());
            Toast.makeText(getContext(), "Error calculating BMI", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpdateTinggi() {
        UpdateTinggi updateTinggiFragment = new UpdateTinggi();
        updateTinggiFragment.show(getChildFragmentManager(), "UpdateTinggiDialog");
    }

    private void openSettingsFragment() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SettingsFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}