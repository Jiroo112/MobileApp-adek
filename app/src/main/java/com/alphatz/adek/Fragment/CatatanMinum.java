package com.alphatz.adek.Fragment;

import static android.content.ContentValues.TAG;

import static com.alphatz.adek.Activity.LoginActivity.KEY_ID_USER;
import static com.alphatz.adek.Activity.LoginActivity.PREF_NAME;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alphatz.adek.Model.WaterIntakeView;
import com.alphatz.adek.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CatatanMinum extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_ID_USER = "id_user"; // Declare ARG_ID_USER only once

    private String mParam1;
    private String mParam2;
    private String idUser;

    private Button btn100ml, btn250ml, btn500ml, btnLainnya, btnAddWater;
    private ImageView btnDecrease;
    private TextView tabCariMakanan, tabTerakhirDimakan, tabCatatanMinum;
    private WaterIntakeView waterIntakeView;
    private static final int DECREASE_AMOUNT = 2500;
    private String baseUrl = "http://adek-app.my.id/ads_mysql/account/profile_adek.php";
    private Context context;
    private RequestQueue requestQueue;
    public CatatanMinum() {
        // Required empty public constructor
    }

    public static CatatanMinum newInstance(String param1, String param2, String idUser ) {
        CatatanMinum fragment = new CatatanMinum();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_ID_USER, idUser ); // Pass idUser  to the arguments
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireContext();
        requestQueue = Volley.newRequestQueue(requireContext());// Initialize context
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            idUser = getArguments().getString(ARG_ID_USER, ""); // Retrieve id_user
            Log.d(TAG, "idUser  di onCreateView: " + idUser);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catatan_minum, container, false);

        initializeViews(view);
        loadWaterIntakeData();
        setupClickListeners();
        fetchuserId();

        return view;
    }

    private void initializeViews(View view) {
        btn100ml = view.findViewById(R.id.btn100ml);
        btn250ml = view.findViewById(R.id.btn250ml);
        btn500ml = view.findViewById(R.id.btn500ml);
        btnLainnya = view.findViewById(R.id.btnLainnya);
        btnAddWater = view.findViewById(R.id.btnAddWater);
        btnDecrease = view.findViewById(R.id.btnDecrease);
        waterIntakeView = view.findViewById(R.id.waterIntakeView);
        tabCariMakanan = view.findViewById(R.id.tab_cari_makanan);
        tabTerakhirDimakan = view.findViewById(R.id.tab_terakhir_dimakan);
        tabCatatanMinum = view.findViewById(R.id.tab_catatan_minum);

        tabTerakhirDimakan.setOnClickListener(v -> {
            TerakhirDimakan terakhirDimakanFragment = new TerakhirDimakan();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, terakhirDimakanFragment)
                    .addToBackStack(null)
                    .commit();
        });

    }

    private void setupClickListeners() {
        btn100ml.setOnClickListener(v -> addWaterIntake(100));
        btn250ml.setOnClickListener(v -> addWaterIntake(250));
        btn500ml.setOnClickListener(v -> addWaterIntake(500));
        btnLainnya.setOnClickListener(v -> showCustomAmountDialog());
        btnAddWater.setOnClickListener(v -> SaveWaterAmount());
        tabCariMakanan.setOnClickListener(v -> {
            openAsupanFragment();
        });

        btnDecrease.setOnClickListener(v -> decreaseWaterIntake());
    }

    private void openAsupanFragment() {
        AsupanFragment asupanFragment = new AsupanFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, asupanFragment)
                .addToBackStack(null)
                .commit();
    }


    private void decreaseWaterIntake() {
        int currentIntake = waterIntakeView.getCurrent();
        if (currentIntake >= DECREASE_AMOUNT) {
            waterIntakeView.setCurrent(currentIntake - DECREASE_AMOUNT);
            saveWaterIntakeData();
            Toast.makeText(getContext(), "Dikurangi " + DECREASE_AMOUNT + " ml", Toast.LENGTH_SHORT).show();
        } else if (currentIntake > 0) {
            waterIntakeView.setCurrent(0);
            saveWaterIntakeData();
            Toast.makeText(getContext(), "Asupan air direset ke 0 ml", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Asupan air sudah 0 ml", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchuserId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString(KEY_ID_USER, "");

        if (idUser.isEmpty()) {
            Log.e(TAG, "User ID not found in SharedPreferences");
            Toast.makeText(getContext(), "User ID not found", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "User ID retrieved from SharedPreferences: " + idUser);
        }
    }

    private void SaveWaterAmount() {
        if (idUser == null || idUser.isEmpty()) {
            Toast.makeText(context, "User  ID is not set", Toast.LENGTH_SHORT).show();
            return; // Exit the method early to avoid sending the request
        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "http://adek-app.my.id/ads_mysql/asupan/simpan_minum.php"; // Change to your server URL

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error processing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, "Failed to save data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_user", idUser); // Ensure id_user is initialized
                params.put("tanggal", currentDate);
                params.put("total_minum", String.valueOf(waterIntakeView.getCurrent())); // Retrieve the correct value
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void addWaterIntake(int amount) {
        waterIntakeView.addWater(amount);
        saveWaterIntakeData();
        Toast.makeText(getContext(), "Ditambahkan " + amount + " ml", Toast.LENGTH_SHORT).show();
    }

    private void showCustomAmountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Masukkan Jumlah Air (ml)");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            try {
                int amount = Integer.parseInt(input.getText().toString());
                if (amount > 0) {
                    addWaterIntake(amount);
                } else {
                    Toast.makeText(getContext(), "Masukkan jumlah yang valid", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Masukkan jumlah yang valid", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void saveWaterIntakeData() {
        SharedPreferences prefs = requireContext().getSharedPreferences("WaterIntake", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("currentIntake", waterIntakeView.getCurrent());
        editor.apply();
    }

    private void loadWaterIntakeData() {
        SharedPreferences prefs = requireContext().getSharedPreferences("WaterIntake", Context.MODE_PRIVATE);
        int savedIntake = prefs.getInt("currentIntake", 0);
        waterIntakeView.setCurrent(savedIntake);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveWaterIntakeData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWaterIntakeData();
        resetDailyIntake();
    }

    public void resetDailyIntake() {
        // Get the current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Retrieve the last reset date from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("WaterIntake", Context.MODE_PRIVATE);
        String lastResetDate = prefs.getString("lastResetDate", "");

        // Check if the current date is different from the last reset date
        if (!currentDate.equals(lastResetDate)) {
            // Reset the water intake
            waterIntakeView.reset();
            saveWaterIntakeData();

            // Update the last reset date in SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("lastResetDate", currentDate);
            editor.apply();

            Toast.makeText(getContext(), "Asupan air direset ke 0 ml untuk hari baru", Toast.LENGTH_SHORT).show();
        }
    }
}