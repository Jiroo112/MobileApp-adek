package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alphatz.adek.Activity.Dashboard;
import com.alphatz.adek.R;
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

public class AddAsupanFragment extends Fragment {

    private EditText editTextNamaMenu, editTextTakaran, editTextKalori, editTextKarbohidrat, editTextLemak, editTextProtein;
    private Spinner spinnerKategori;
    private String URL_POST_MENU = "http://10.0.2.2/ads_mysql/post_menu.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_asupan, container, false);

        editTextNamaMenu = view.findViewById(R.id.nama_menu);
        editTextTakaran = view.findViewById(R.id.edit_text_takaran);
        editTextKalori = view.findViewById(R.id.jumlah_kalori);
        editTextKarbohidrat = view.findViewById(R.id.jumlah_karbohidrat);
        editTextLemak = view.findViewById(R.id.jumlah_lemak);
        editTextProtein = view.findViewById(R.id.jumlah_protein);

        spinnerKategori = view.findViewById(R.id.spinner_kategori);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.kategori_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(adapter);

        Button btnSimpan = view.findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });
        return view;
    }

    private void simpanData() {
        String namaMenu = editTextNamaMenu.getText().toString().trim();
        String takaran = editTextTakaran.getText().toString().trim();
        String kalori = editTextKalori.getText().toString().trim();
        String karbohidrat = editTextKarbohidrat.getText().toString().trim();
        String lemak = editTextLemak.getText().toString().trim();
        String protein = editTextProtein.getText().toString().trim();
        String kategori = spinnerKategori.getSelectedItem().toString();

        if (namaMenu.isEmpty() || takaran.isEmpty() || kalori.isEmpty() ||
                karbohidrat.isEmpty() || lemak.isEmpty() || protein.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST_MENU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            if (success) {
                                clearFields();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),
                                    "Failed to parse response: " + response,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),
                                "Network error: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_menu", namaMenu);
                params.put("protein", protein);
                params.put("karbohidrat", karbohidrat);
                params.put("lemak", lemak);
                params.put("kalori", kalori);
                params.put("satuan", takaran);
                params.put("kategori_menu", kategori);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,  // 30 detik timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void clearFields() {
        editTextNamaMenu.setText("");
        editTextTakaran.setText("");
        editTextKalori.setText("");
        editTextKarbohidrat.setText("");
        editTextLemak.setText("");
        editTextProtein.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).showBottomNavigation();
        }
    }
}
