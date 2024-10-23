package com.alphatz.adek.Fragment;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alphatz.adek.Adapter.MakananAdapter;
import com.alphatz.adek.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MakananBeratFragment extends Fragment {

    private Button btnMinumanSehat, btnDessert, btnFilter;
    private RecyclerView recyclerViewMakanan;
    private MakananAdapter makananAdapter;
    private RequestQueue requestQueue;

    public MakananBeratFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Handle back press
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToResepFragment();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_makanan_berat, container, false);

        btnMinumanSehat = view.findViewById(R.id.btn_minuman_sehat);
        btnDessert = view.findViewById(R.id.btn_desert);
        btnFilter = view.findViewById(R.id.btn_filter);
        recyclerViewMakanan = view.findViewById(R.id.recyclerViewMakanan);

        requestQueue = Volley.newRequestQueue(requireContext());

        btnMinumanSehat.setOnClickListener(v -> navigateToFragment(new MinumanSehatFragment()));
        btnFilter.setOnClickListener(v -> navigateToFragment(new ResepFragment()));
        btnDessert.setOnClickListener(v -> navigateToFragment(new DessertFragment()));

        recyclerViewMakanan.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Panggil method untuk mengambil data menu makanan
        getMenuMakanan();

        return view;
    }

    private void getMenuMakanan() {
        String url = "http://10.0.2.2/ads_mysql/get_menu.php?kategori=makanan";  // Ganti dengan URL API Anda

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                JSONArray data = response.getJSONArray("data");

                                // Set Adapter
                                makananAdapter = new MakananAdapter(requireContext(), data);
                                recyclerViewMakanan.setAdapter(makananAdapter);

                            } else {
                                Toast.makeText(requireContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(requireContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToResepFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ResepFragment());
        transaction.commit();
    }
}
