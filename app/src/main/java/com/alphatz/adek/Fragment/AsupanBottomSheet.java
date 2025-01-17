package com.alphatz.adek.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.alphatz.adek.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AsupanBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "AsupanBottomSheet";
    private static final String ARG_NAMA_MENU = "nama_menu";
    private static final String ARG_ID_USER = "idUser";

    // Interface untuk callback setelah menu disimpan
    public interface OnMenuSavedListener {
        void onMenuSaved();
    }

    private String idUser;
    private String idMenu;
    private double baseKalori;
    private double baseProtein;
    private double baseKarbohidrat;
    private double baseLemak;
    private double baseGula;
    private TextView textTitle, textKalori, textProtein, textKarbohidrat, textLemak, textGula;
    private EditText porsiEditText;
    private Button tambahButton;
    private RequestQueue requestQueue;

    // Listener untuk callback
    private OnMenuSavedListener mListener;

    public static AsupanBottomSheet newInstance(String namaMenu, String idUser) {
        AsupanBottomSheet fragment = new AsupanBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_NAMA_MENU, namaMenu);
        args.putString(ARG_ID_USER, idUser);
        fragment.setArguments(args);
        return fragment;
    }

    // Metode untuk set listener
    public void setOnMenuSavedListener(OnMenuSavedListener listener) {
        this.mListener = listener;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_asupan, container, false);

        // Initialize views
        textTitle = view.findViewById(R.id.text_title);
        textKalori = view.findViewById(R.id.text_kalori);
        textProtein = view.findViewById(R.id.text_protein);
        textKarbohidrat = view.findViewById(R.id.text_karbo);
        textLemak = view.findViewById(R.id.text_lemak);
        textGula = view.findViewById(R.id.text_gula);
        porsiEditText = view.findViewById(R.id.edit_porsi);
        tambahButton = view.findViewById(R.id.button_tambahkan);

        requestQueue = Volley.newRequestQueue(requireContext());

        porsiEditText.setText("1");
        porsiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateNutritionValues();
            }
        });

        if (getArguments() != null) {
            String namaMenu = getArguments().getString(ARG_NAMA_MENU);
            idUser = getArguments().getString(ARG_ID_USER, "");
            Log.d(TAG, "idUser di onCreateView: " + idUser);

            if (namaMenu != null && !namaMenu.isEmpty()) {
                fetchMenuDetail(namaMenu);
            }
        }

        tambahButton.setOnClickListener(v -> simpanDetailKalori());

        return view;
    }

    private void updateNutritionValues() {
        try {
            int porsi = Integer.parseInt(porsiEditText.getText().toString());
            if (porsi < 1) porsi = 1;

            double totalKalori = baseKalori * porsi;
            double totalProtein = baseProtein * porsi;
            double totalKarbohidrat = baseKarbohidrat * porsi;
            double totalLemak = baseLemak * porsi;
            double totalGula = baseGula * porsi;

            textKalori.setText(String.format("Kalori: %.1f", totalKalori));
            textProtein.setText(String.format("Protein: %.1f", totalProtein));
            textKarbohidrat.setText(String.format("Karbohidrat: %.1f", totalKarbohidrat));
            textLemak.setText(String.format("Lemak: %.1f", totalLemak));
            textGula.setText(String.format("Gula: %.1f", totalGula));

            tambahButton.setEnabled(true);
        } catch (NumberFormatException e) {
            tambahButton.setEnabled(false);
        }
    }

    private void fetchMenuDetail(String namaMenu) {
        try {
            String encodedNamaMenu = URLEncoder.encode(namaMenu, "UTF-8");
            String url = "http://adek-app.my.id/ads_mysql/asupan/bot_sheet_asupan.php?nama_menu=" + encodedNamaMenu;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            if (response.has("data")) {
                                JSONObject data = response.getJSONObject("data");
                                idMenu = data.getString("id_menu");
                                baseKalori = data.getDouble("kalori");
                                baseProtein = data.getDouble("protein");
                                baseKarbohidrat = data.getDouble("karbohidrat");
                                baseLemak = data.getDouble("lemak");
                                baseGula = data.getDouble("gula");

                                textTitle.setText(data.getString("nama_menu"));
                                updateNutritionValues();

                                tambahButton.setEnabled(true);
                            } else {
                                Toast.makeText(getContext(), "Data menu tidak ditemukan", Toast.LENGTH_SHORT).show();
                                tambahButton.setEnabled(false);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            Toast.makeText(getContext(), "Error memproses data", Toast.LENGTH_SHORT).show();
                            tambahButton.setEnabled(false);
                        }
                    },
                    error -> {
                        Log.e(TAG, "Error fetching menu: " + error.getMessage());
                        Toast.makeText(getContext(), "Gagal mengambil data menu", Toast.LENGTH_SHORT).show();
                        tambahButton.setEnabled(false);
                    });

            requestQueue.add(request);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error encoding menu name: " + e.getMessage());
            Toast.makeText(getContext(), "Error encoding menu name", Toast.LENGTH_SHORT).show();
        }
    }

    private void simpanDetailKalori() {
        Log.d(TAG, "idMenu: " + idMenu);
        Log.d(TAG, "porsi: " + porsiEditText.getText().toString());
        Log.d(TAG, "idUser: " + idUser);

        if (idMenu == null || porsiEditText.getText().toString().isEmpty() || idUser == null || idUser.isEmpty()) {
            Toast.makeText(getContext(), "Data tidak lengkap", Toast.LENGTH_SHORT).show();
            return;
        }

        int porsi = Integer.parseInt(porsiEditText.getText().toString());
        double totalKalori = baseKalori * porsi;
        double totalProtein = baseProtein * porsi;
        double totalKarbohidrat = baseKarbohidrat * porsi;
        double totalLemak = baseLemak * porsi;
        double totalGula = baseGula * porsi;

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String url = "http://adek-app.my.id/ads_mysql/asupan/simpan_detail_kalori.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        if (success) {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                            // Panggil callback jika listener tidak null
                            if (mListener != null) {
                                mListener.onMenuSaved();
                            }

                            getDialog().dismiss();
                        } else {
                            Toast.makeText(getContext(), "Gagal: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing response: " + e.getMessage());
                        Toast.makeText(getContext(), "Error memproses response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error saving data: " + error.getMessage());
                    Toast.makeText(getContext(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", idUser);
                params.put("id_menu", idMenu);
                params.put("tanggal", currentDate);
                params.put("jumlah", String.valueOf(porsi));
                params.put("total_kalori", String.valueOf(totalKalori));
                params.put("total_protein", String.valueOf(totalProtein));
                params.put("total_karbohidrat", String.valueOf(totalKarbohidrat));
                params.put("total_lemak", String.valueOf(totalLemak));
                params.put("total_gula", String.valueOf(totalGula));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}