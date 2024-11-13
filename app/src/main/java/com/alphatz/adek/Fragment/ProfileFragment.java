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
import com.alphatz.adek.Activity.UpdateTinggi; // Import UpdateTinggi
import com.alphatz.adek.Activity.UpdateBerat;
import com.alphatz.adek.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private ImageView tipe_diet;
    private ImageView settings;
    private TextView beratTextView;
    private TextView tinggiTextView;
    private TextView bmiTextView;
    private TextView txt_username;
    private LinearGauge linearGauge;

    private RequestQueue requestQueue;
    private String baseUrl = "http://10.0.2.2/ads_mysql/profil2.php";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String username) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());
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
        txt_username = view.findViewById(R.id.txt_username);
        linearGauge = view.findViewById(R.id.linearGauge);

        beratTextView.setText("Loading...");
        tinggiTextView.setText("Loading...");
        bmiTextView.setText("Loading...");

        if (getArguments() != null) {
            String username = getArguments().getString("username", "Pengguna");
            txt_username.setText(username);
            getUserDataFromApi(username);
        } else {
            txt_username.setText("Pengguna");
        }

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

    private void showUpdateTinggi() {
        UpdateTinggi updateTinggiFragment = new UpdateTinggi();
        updateTinggiFragment.show(getChildFragmentManager(), "UpdateTinggiDialog");
    }

    private void getUserDataFromApi(String username) {
        String url = baseUrl + "?username=" + username;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
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
                            Toast.makeText(getContext(), "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley error: " + error.getMessage());
                    Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(stringRequest);
    }

    private void calculateAndDisplayBMI(String beratStr, String tinggiStr) {
        try {
            double berat = Double.parseDouble(beratStr);
            double tinggi = Double.parseDouble(tinggiStr) / 100; // Mengubah tinggi ke meter

            double bmi = berat / (tinggi * tinggi); // Rumus BMI

            String bmiText = String.format("%.2f", bmi);
            bmiTextView.setText("BMI: " + bmiText);
            linearGauge.setProgressWithAnimation((float) bmi); // Menampilkan BMI pada LinearGauge

        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing berat or tinggi: " + e.getMessage());
            Toast.makeText(getContext(), "Error calculating BMI", Toast.LENGTH_SHORT).show();
        }
    }

    private void openSettingsFragment() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SettingsFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
