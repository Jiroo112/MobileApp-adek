package com.alphatz.adek.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private ImageView tipe_diet;
    private ImageView settings;
    private TextView beratTextView;
    private TextView tinggiTextView;
    private TextView bmiTextView;
    private TextView txt_nama_lengkap;
    private LinearGauge linearGauge;
    private String currentNamaLengkap;
    private RequestQueue requestQueue;
    private String baseUrl = "http://10.0.2.2/ads_mysql/profile_adek.php";

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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        settings = view.findViewById(R.id.settings);
        beratTextView = view.findViewById(R.id.text_berat);
        tinggiTextView = view.findViewById(R.id.text_tinggi);
        bmiTextView = view.findViewById(R.id.text_bmi);
        txt_nama_lengkap = view.findViewById(R.id.txt_username);
        linearGauge = view.findViewById(R.id.linearGauge);

        beratTextView.setText("Loading...");
        tinggiTextView.setText("Loading...");
        bmiTextView.setText("Loading...");

        // Set nama_lengkap dan langsung ambil data
        txt_nama_lengkap.setText(currentNamaLengkap);  // Ubah menjadi currentNamaLengkap
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

                                beratTextView.setText(berat + " kg");
                                tinggiTextView.setText(tinggi + " cm");

                                calculateAndDisplayBMI(berat, tinggi);
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

    private void calculateAndDisplayBMI(String beratStr, String tinggiStr) {
        try {
            double berat = Double.parseDouble(beratStr);
            double tinggi = Double.parseDouble(tinggiStr) / 100;

            double bmi = berat / (tinggi * tinggi);

            String bmiText = String.format("%.2f", bmi);
            bmiTextView.setText("BMI: " + bmiText);

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
