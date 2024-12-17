package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailProfileFragment extends Fragment {
    private static final String TAG = "DetailProfileFragment";
    private static final String URL_PROFILE = "http://adek-app.my.id/ads_mysql/account/get_profile.php";

    private TextView tvName, tvTipeDiet, tvGender, tvBirthDate, tvEmail, tvHeight, tvWeight, tvBmi;
    private ImageView profileImage;
    private String namaLengkap;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_profile, container, false);

        if (getArguments() != null) {
            namaLengkap = getArguments().getString("nama_lengkap");
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
        tvGender = view.findViewById(R.id.tvGender);
        tvBirthDate = view.findViewById(R.id.tvBirthDate);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvBmi = view.findViewById(R.id.tvBmi);
    }

    private void fetchProfileData() {
        Log.d(TAG, "Fetching data for nama_lengkap: " + namaLengkap);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e(TAG, "Network Error: " + error.networkResponse.statusCode);
                            Log.e(TAG, "Error Response Data: " + new String(error.networkResponse.data));
                        }
                        Log.e(TAG, "Volley error: " + Log.getStackTraceString(error));
                        Toast.makeText(getContext(), "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", namaLengkap);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(stringRequest);
    }

    private void updateUIWithProfileData(JSONObject data) throws JSONException {
        tvName.setText(data.optString("nama_lengkap", "Tidak diketahui"));
        tvTipeDiet.setText(data.optString("tipe_diet", "Tidak diketahui"));
        tvGender.setText(data.optString("gender", "Tidak diketahui"));
        tvBirthDate.setText(data.optString("tanggal_lahir", "Tidak diketahui"));
        tvEmail.setText(data.optString("email", "Tidak diketahui"));
        tvHeight.setText(data.optString("tinggi_badan", "0") + " cm");
        tvWeight.setText(data.optString("berat_badan", "0") + " kg");

        calculateAndDisplayBMI(data.optString("berat_badan", "0"), data.optString("tinggi_badan", "0"));

        if (data.has("gambar") && !data.isNull("gambar")) {
            String gambarPath = data.getString("gambar");
            if (!TextUtils.isEmpty(gambarPath)) {
                String gambarUrl = "https://adek-app.my.id/Images/" + gambarPath;
                Glide.with(getContext())
                        .load(gambarUrl)
                        .placeholder(R.drawable.ic_profil)
                        .error(R.drawable.ic_profil)
                        .into(profileImage);
            } else {
                profileImage.setImageResource(R.drawable.ic_profil);
            }
        } else {
            profileImage.setImageResource(R.drawable.ic_profil);
        }
    }

    private void calculateAndDisplayBMI(String beratStr, String tinggiStr) {
        if (!beratStr.equals("0") && !tinggiStr.equals("0")) {
            double berat = Double.parseDouble(beratStr);
            double tinggi = Double.parseDouble(tinggiStr) / 100;
            double bmi = berat / (tinggi * tinggi);
            tvBmi.setText(String.format("BMI: %.2f", bmi));
        } else {
            tvBmi.setText("BMI: Tidak tersedia");
        }
    }
}
