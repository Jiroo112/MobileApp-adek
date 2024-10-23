package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.alphatz.adek.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsupanFragment extends Fragment {

    private LinearLayout menuContainer;

    public AsupanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asupan, container, false);

        // Inisialisasi menu container dan tombol tambah asupan
        menuContainer = view.findViewById(R.id.menu_container);
        Button btnTambahAsupan = view.findViewById(R.id.btn_tambahasupan);

        // Memuat data menu dari database
        fetchMenuData();

        // Listener untuk tombol tambah asupan
        btnTambahAsupan.setOnClickListener(v -> showAsupanBottomSheet());

        return view;
    }

    private void fetchMenuData() {
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/ads_mysql/get_menu.php"); // Ganti dengan URL server PHP-mu
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray data = jsonResponse.getJSONArray("data");

                // Menampilkan data di dalam CardView
                getActivity().runOnUiThread(() -> {
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            JSONObject menu = data.getJSONObject(i);
                            String namaMenu = menu.getString("nama_menu");
                            addMenuCard(namaMenu);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                Log.e("AsupanFragment", "Error fetching menu data", e);
            }
        });
        thread.start();
    }

    private void addMenuCard(String namaMenu) {
        // Inflate CardView layout
        View cardView = getLayoutInflater().inflate(R.layout.item_daftarmenu, null);
        TextView namaDaftarMenu = cardView.findViewById(R.id.nama_daftarmenu);
        namaDaftarMenu.setText(namaMenu);

        // Set click listener for the card
        cardView.setOnClickListener(v -> showAsupanBottomSheetWithData(namaMenu));

        // Tambahkan card ke dalam container
        menuContainer.addView(cardView);
    }

    private void showAsupanBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_asupan, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        EditText takaranEditText = bottomSheetView.findViewById(R.id.edit_text_takaran);
        EditText porsiEditText = bottomSheetView.findViewById(R.id.edit_text_porsi);
        Button tambahkanButton = bottomSheetView.findViewById(R.id.button_tambahkan);

        tambahkanButton.setOnClickListener(v -> {
            // Handle the "Tambahkan" button click
            // Tambahkan logic untuk menyimpan data asupan di sini
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void showAsupanBottomSheetWithData(String namaMenu) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_asupan, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        TextView titleTextView = bottomSheetView.findViewById(R.id.text_title);
        EditText takaranEditText = bottomSheetView.findViewById(R.id.edit_text_takaran);
        EditText porsiEditText = bottomSheetView.findViewById(R.id.edit_text_porsi);
        TextView kaloriTextView = bottomSheetView.findViewById(R.id.text_kalori);
        TextView proteinTextView = bottomSheetView.findViewById(R.id.text_protein);
        TextView karboTextView = bottomSheetView.findViewById(R.id.text_karbo);
        TextView gulaTextView = bottomSheetView.findViewById(R.id.text_gula);
        Button tambahkanButton = bottomSheetView.findViewById(R.id.button_tambahkan);

        titleTextView.setText(namaMenu);
        kaloriTextView.setText("Kalori : 1.4");
        proteinTextView.setText("Protein: 0.025");
        karboTextView.setText("Karbo : 0.285");
        gulaTextView.setText("Gula : 0.000");

        tambahkanButton.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}
