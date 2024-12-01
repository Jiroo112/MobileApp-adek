package com.alphatz.adek.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alphatz.adek.Model.ResepModel;
import com.alphatz.adek.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailResepFragment extends Fragment {

    private static final String ARG_RESEP_KEY = "resep_key";
    private static final String ARG_ID_MENU = "id_menu";

    private ImageView gambar_resep;
    private TextView judul_resep, deskripsi_resep;

    private RequestQueue requestQueue;

    public DetailResepFragment() {
        // Required empty public constructor
    }

    public static DetailResepFragment newInstance(String idMenu) {
        DetailResepFragment fragment = new DetailResepFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID_MENU, idMenu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext()); // Initialize RequestQueue
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_resep, container, false);

        // Initialize views
        gambar_resep = view.findViewById(R.id.gambar_resep);
        judul_resep = view.findViewById(R.id.judul_resep);
        deskripsi_resep = view.findViewById(R.id.deskripsi_resep);

        // Get arguments
        Bundle args = getArguments();
        if (args != null) {
            String idMenu = args.getString(ARG_ID_MENU, "");
            if (!idMenu.isEmpty()) {
                fetchSelectedRecipe(idMenu); // Fetch recipe details using the ID
            } else {
                ResepModel resepModel = args.getParcelable(ARG_RESEP_KEY);
                if (resepModel != null) {
                    displayRecipeDetails(resepModel);
                } else {
                    Log.e(TAG, "No valid data passed to DetailResepFragment");
                }
            }
        }

        return view;
    }

    private void fetchSelectedRecipe(String id_Menu) {
        if (requestQueue == null) {
            Log.e(TAG, "RequestQueue is not initialized");
            return;
        }

        try {
            String url = "http://10.0.2.2/ads_mysql/search/get_selected_recipe.php?id_menu=" + id_Menu;

            Log.d(TAG, "Fetching recipe with URL: " + url);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        // Log the full server response to see the data being passed
                        Log.d(TAG, "Response received: " + response.toString());

                        try {
                            // Check for error in response
                            if (response.has("error")) {
                                String errorMessage = response.getString("error");
                                Log.e(TAG, "Server returned error: " + errorMessage);
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            if (response.has("data")) {
                                JSONObject data = response.getJSONObject("data");

                                // Log the data being returned from the server
                                Log.d(TAG, "Data received: " + data.toString());

                                String idMenu = data.getString("id_menu");
                                String namaMenuResponse = data.getString("nama_menu");
                                String resep = data.getString("resep");
                                String gambar = data.getString("gambar");

                                // Log individual fields to check their values
                                Log.d(TAG, "Parsed recipe details - ID: " + idMenu
                                        + ", Name: " + namaMenuResponse
                                        + ", Recipe length: " + (resep != null ? resep.length() : "0")
                                        + ", Image URL: " + gambar);

                                // Log before updating UI elements
                                Log.d(TAG, "Updating UI - Judul: " + namaMenuResponse + ", Deskripsi: " + resep + ", Gambar: " + gambar);

                                // Update UI elements
                                judul_resep.setText(namaMenuResponse);
                                deskripsi_resep.setText(resep); // Update description
                                loadImage(gambar); // Update image

                            } else {
                                Log.e(TAG, "No data found in response");
                                Toast.makeText(getContext(), "Data menu tidak ditemukan", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage(), e);
                            Toast.makeText(getContext(), "Error memproses data", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Log.e(TAG, "Volley Error: " + (error.getMessage() != null ? error.getMessage() : "Unknown error"), error);
                        Toast.makeText(getContext(), "Gagal mengambil data menu", Toast.LENGTH_SHORT).show();
                    });

            requestQueue.add(request);
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error in fetchSelectedRecipe: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        }
    }



    private void displayRecipeDetails(ResepModel menu) {
        judul_resep.setText(menu.getNamaMenu() != null ? menu.getNamaMenu() : "Nama Menu Tidak Tersedia");
        deskripsi_resep.setText(menu.getResep() != null ? menu.getResep() : "Resep Tidak Tersedia");

        if (menu.getGambar() != null && !menu.getGambar().isEmpty()) {
            loadImage(menu.getGambar());
        } else {
            gambar_resep.setImageResource(R.drawable.default_image);
        }
    }

    private void loadImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            gambar_resep.setImageResource(R.drawable.default_image);
            return;
        }

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_image)
                .into(gambar_resep);
    }
}
