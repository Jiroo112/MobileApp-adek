package com.alphatz.adek.Fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alphatz.adek.Activity.LoginActivity;
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
    private ImageView dokter_fav;
    private ImageView tipe_diet;
    private ImageView settings;
    private TextView beratTextView;
    private TextView tinggiTextView;
    private String idUser = "670b5";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String username) {
        return null;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Log.d(TAG, "onCreateView started");

        Button logoutButton = view.findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(v -> logout());

        tipe_diet = view.findViewById(R.id.tipe_diet);
        dokter_fav = view.findViewById(R.id.dokter_fav);
        settings = view.findViewById(R.id.settings);
        beratTextView = view.findViewById(R.id.text_berat);
        tinggiTextView = view.findViewById(R.id.text_tinggi);

        // Set nilai awal TextView
        beratTextView.setText("Loading...");
        tinggiTextView.setText("Loading...");

        Log.d(TAG, "Calling getUserDataFromApi");
        getUserDataFromApi(idUser);

        setupAnimations();

        tipe_diet.setOnClickListener(v -> openResepFragment());
        settings.setOnClickListener(v -> openSettingsFragment());

        Log.d(TAG, "onCreateView completed");
        return view;
    }

    private void getUserDataFromApi(String idUser) {
        String url = "http://10.0.2.2/ads_mysql/profil2.php?id_user=" + idUser;
        Log.d(TAG, "Requesting URL: " + url);

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d(TAG, "Response received: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String berat = data.getString("berat_badan");
                            String tinggi = data.getString("tinggi_badan");

                            Log.d(TAG, "Parsed data - Berat: " + berat + ", Tinggi: " + tinggi);

                            requireActivity().runOnUiThread(() -> {
                                beratTextView.setText(berat + " kg");
                                tinggiTextView.setText(tinggi + " cm");
                                Log.d(TAG, "TextView updated on UI thread");
                            });
                        } else {
                            Log.e(TAG, "API returned non-success status");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error", e);
                    }
                },
                error -> {
                    Log.e(TAG, "Volley error: " + error.toString());
                });

        // Set timeout policy to handle slow network
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,  // Timeout in ms
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    private void setupAnimations() {
        dokter_fav.setOnClickListener(v -> animateView(dokter_fav));
    }

    private void animateView(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f, 1f);

        scaleX.setDuration(300);
        scaleY.setDuration(300);

        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());

        scaleX.start();
        scaleY.start();
    }

    private void logout() {
        Log.d(TAG, "Logout initiated");
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
        Log.d(TAG, "Logout completed, starting LoginActivity");
    }

    private void openResepFragment() {
        Log.d(TAG, "Opening ResepFragment");
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ResepFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openSettingsFragment() {
        Log.d(TAG, "Opening SettingsFragment");
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SettingsFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
