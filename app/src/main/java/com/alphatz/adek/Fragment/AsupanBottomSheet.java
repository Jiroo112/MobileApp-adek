package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alphatz.adek.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class AsupanBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "AsupanBottomSheet";
    private static final String ARG_NAMA_MENU = "nama_menu";

    private TextView textTitle, textKalori, textProtein, textKarbohidrat;
    private RequestQueue requestQueue;

    public static AsupanBottomSheet newInstance(String namaMenu) {
        AsupanBottomSheet fragment = new AsupanBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_NAMA_MENU, namaMenu);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_asupan, container, false);

        textTitle = view.findViewById(R.id.text_title);
        textKalori = view.findViewById(R.id.text_kalori);
        textProtein = view.findViewById(R.id.text_protein);
        textKarbohidrat = view.findViewById(R.id.text_karbo);

        requestQueue = Volley.newRequestQueue(requireContext());

        // ngambil data nama menu dari argumen
        if (getArguments() != null) {
            String namaMenu = getArguments().getString(ARG_NAMA_MENU);
            fetchMenuDetail(namaMenu);
        }

        return view;
    }

    private void fetchMenuDetail(String namaMenu) {
        String url = "http://10.0.2.2/ads_mysql/bot_sheet_asupan.php?nama_menu=" + namaMenu;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            textTitle.setText(data.getString("nama_menu"));
                            textKalori.setText("Kalori: " + data.getString("kalori"));
                            textProtein.setText("Protein: " + data.getString("protein"));
                            textKarbohidrat.setText("Karbohidrat: " + data.getString("karbohidrat"));
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            Toast.makeText(getContext(), "Gagal memproses data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + error.getMessage());
                        Toast.makeText(getContext(), "Gagal mengambil data. Periksa koneksi internet Anda.", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
