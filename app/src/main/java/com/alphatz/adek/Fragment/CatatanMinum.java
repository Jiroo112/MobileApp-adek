package com.alphatz.adek.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
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

public class CatatanMinum extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button btn100ml, btn250ml, btn500ml, btnLainnya, btnAddWater;
    private ImageView btnDecrease;
    private TextView tabCariMakanan, tabTerakhirDimakan, tabCatatanMinum;
    private WaterIntakeView waterIntakeView;
    private static final int DECREASE_AMOUNT = 2500; // Amount to decrease in ml

    public CatatanMinum() {
        // Required empty public constructor
    }

    public static CatatanMinum newInstance(String param1, String param2) {
        CatatanMinum fragment = new CatatanMinum();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catatan_minum, container, false);

        // Initialize views
        initializeViews(view);
        // Load saved data
        loadWaterIntakeData();
        // Set up click listeners
        setupClickListeners();

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
        btnAddWater.setOnClickListener(v -> showAddWaterDialog());
        tabCariMakanan.setOnClickListener(v -> {
            openAsupanFragment();
        });
        // Add click listener for decrease button
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
            // If current intake is less than DECREASE_AMOUNT but greater than 0
            waterIntakeView.setCurrent(0);
            saveWaterIntakeData();
            Toast.makeText(getContext(), "Asupan air direset ke 0 ml", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Asupan air sudah 0 ml", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddWaterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Pilih jumlah air yang akan ditambahkan");

        final String[] amounts = {"100 ml", "250 ml", "500 ml", "Jumlah lain"};
        builder.setItems(amounts, (dialog, which) -> {
            switch (which) {
                case 0:
                    addWaterIntake(100);
                    break;
                case 1:
                    addWaterIntake(250);
                    break;
                case 2:
                    addWaterIntake(500);
                    break;
                case 3:
                    showCustomAmountDialog();
                    break;
            }
        });

        builder.show();
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
    }

    public void resetDailyIntake() {
        waterIntakeView.reset();
        saveWaterIntakeData();
    }
}