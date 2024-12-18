package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alphatz.adek.R;
import com.android.volley.AuthFailureError;
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
    private static final String URL_EDIT_PROFILE = "http://adek-app.my.id/ads_mysql/account/edit_profile_adek.php";

    // TextView fields
    private TextView tvName, tvTipeDiet, tvGender, tvBirthDate,
            tvEmail, tvHeight, tvWeight, tvBmi,
            tvUsia, tvNoHP;

    // EditText fields
    private EditText etTipeDiet, etGender, etBirthDate,
            etEmail, etHeight, etWeight, etNoHP;

    // Edit Button
    private Button ButtonEdit;

    private String currentNamaLengkap;
    private RequestQueue requestQueue;
    private boolean isEditMode = false;

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
        // Initialize TextViews
        tvName = view.findViewById(R.id.tvName);
        tvTipeDiet = view.findViewById(R.id.tv_tipediet);
        tvGender = view.findViewById(R.id.tvGender);
        tvBirthDate = view.findViewById(R.id.tvBirthDate);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvBmi = view.findViewById(R.id.tvBmi);
        tvNoHP = view.findViewById(R.id.tvNoHP);

        // Initialize EditTexts
        etTipeDiet = view.findViewById(R.id.etTipeDiet);
        etGender = view.findViewById(R.id.etGender);
        etBirthDate = view.findViewById(R.id.etBirthDate);
        etEmail = view.findViewById(R.id.etEmail);
        etHeight = view.findViewById(R.id.etHeight);
        etWeight = view.findViewById(R.id.etWeight);
        etNoHP = view.findViewById(R.id.etNoHP);

        // Initialize Edit Button
        ButtonEdit = view.findViewById(R.id.btnEditProfile);
        ButtonEdit.setOnClickListener(v -> toggleEditMode());
    }

    private void toggleEditMode() {
        isEditMode = !isEditMode;

        // Toggle visibility of TextViews and EditTexts
        int textViewVisibility = isEditMode ? View.GONE : View.VISIBLE;
        int editTextVisibility = isEditMode ? View.VISIBLE : View.GONE;

        // TextViews
        tvTipeDiet.setVisibility(textViewVisibility);
        tvGender.setVisibility(textViewVisibility);
        tvBirthDate.setVisibility(textViewVisibility);
        tvEmail.setVisibility(textViewVisibility);
        tvHeight.setVisibility(textViewVisibility);
        tvWeight.setVisibility(textViewVisibility);
        tvNoHP.setVisibility(textViewVisibility);

        // EditTexts
        etTipeDiet.setVisibility(editTextVisibility);
        etGender.setVisibility(editTextVisibility);
        etBirthDate.setVisibility(editTextVisibility);
        etEmail.setVisibility(editTextVisibility);
        etHeight.setVisibility(editTextVisibility);
        etWeight.setVisibility(editTextVisibility);
        etNoHP.setVisibility(editTextVisibility);

        if (isEditMode) {
            // Populate EditTexts with current values
            etTipeDiet.setText(tvTipeDiet.getText());
            etGender.setText(tvGender.getText());
            etBirthDate.setText(tvBirthDate.getText());
            etEmail.setText(tvEmail.getText());
            String heightValue = tvHeight.getText().toString();
            etHeight.setText(heightValue.replace(" cm", ""));
            String weightValue = tvWeight.getText().toString();
            etWeight.setText(weightValue.replace(" kg", ""));
            etNoHP.setText(tvNoHP.getText());

            ButtonEdit.setText("Simpan");
        } else {
            // Save changes
            saveProfileChanges();
            ButtonEdit.setText("Edit Profile");
        }
    }

    private void fetchProfileData() {
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
                    Log.e(TAG, "Volley error: " + Log.getStackTraceString(error));
                    Toast.makeText(getContext(), "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", currentNamaLengkap);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void saveProfileChanges() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_PROFILE,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {
                            Toast.makeText(getContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();

                            // Update TextViews with new values
                            tvTipeDiet.setText(etTipeDiet.getText());
                            tvGender.setText(etGender.getText());
                            tvBirthDate.setText(etBirthDate.getText());
                            tvEmail.setText(etEmail.getText());
                            tvHeight.setText(etHeight.getText() + " cm");
                            tvWeight.setText(etWeight.getText() + " kg");
                            tvNoHP.setText(etNoHP.getText());

                            // Recalculate BMI and Age
                            calculateAndDisplayBMI(etWeight.getText().toString(), etHeight.getText().toString());
                            calculateAndDisplayAge(etBirthDate.getText().toString());
                        } else {
                            String message = jsonObject.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley error: " + Log.getStackTraceString(error));
                    Toast.makeText(getContext(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", currentNamaLengkap);
                params.put("tipe_diet", etTipeDiet.getText().toString());
                params.put("gender", etGender.getText().toString());
                params.put("email", etEmail.getText().toString());
                params.put("no_hp", etNoHP.getText().toString());
                params.put("berat_badan", etWeight.getText().toString());
                params.put("tinggi_badan", etHeight.getText().toString());
                params.put("tanggal_lahir", etBirthDate.getText().toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void updateUIWithProfileData(JSONObject data) throws JSONException {
        // Update TextViews with profile data
        tvName.setText(data.optString("nama_lengkap", "Tidak diketahui"));
        tvTipeDiet.setText(data.optString("tipe_diet", "Tidak diketahui"));
        tvGender.setText(data.optString("gender", "Tidak diketahui"));
        tvEmail.setText(data.optString("email", "Tidak diketahui"));
        tvNoHP.setText(data.optString("no_hp", "Tidak diketahui"));

        String berat = data.optString("berat_badan", "0");
        String tinggi = data.optString("tinggi_badan", "0");
        String tanggalLahir = data.optString("tanggal_lahir", "");

        tvWeight.setText(berat + " kg");
        tvHeight.setText(tinggi + " cm");
        tvBirthDate.setText(tanggalLahir);

        // Calculate and display BMI
        calculateAndDisplayBMI(berat, tinggi);

        // Calculate and display Age
        calculateAndDisplayAge(tanggalLahir);
    }

    private void calculateAndDisplayBMI(String beratStr, String tinggiStr) {
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

    private void calculateAndDisplayAge(String tanggalLahir) {
        if (tvUsia == null) {
            Log.e(TAG, "tvUsia is null, cannot display age");
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(tanggalLahir, formatter);
            LocalDate today = LocalDate.now();

            int age = Period.between(birthDate, today).getYears();
            tvUsia.setText(age + " tahun");
        } catch (Exception e) {
            Log.e(TAG, "Error parsing tanggal_lahir: " + e.getMessage());
            tvUsia.setText("Error menghitung tahun");
        }
    }
}