package com.alphatz.adek.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import androidx.appcompat.widget.TooltipCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alphatz.adek.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private TextView welcome;
    private TextView persentaseGula, persentaseProtein, persentaseLemak, persentaseAir, persentaseKarbohidrat;

    private static final String ARG_NAMA_LENGKAP = "nama_lengkap";
    private String namaLengkap;

    private RequestQueue requestQueue;

    public HomeFragment() {
        // Default constructor
    }

    public static HomeFragment newInstance(String namaLengkap) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAMA_LENGKAP, namaLengkap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            namaLengkap = getArguments().getString(ARG_NAMA_LENGKAP, "Pengguna");
        }
        requestQueue = Volley.newRequestQueue(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Bind views
        persentaseGula = view.findViewById(R.id.persentase_gula);
        persentaseAir = view.findViewById(R.id.persentase_air);
        persentaseKarbohidrat = view.findViewById(R.id.persentase_karbohidrat);
        persentaseLemak = view.findViewById(R.id.persentase_lemak);
        persentaseProtein = view.findViewById(R.id.persentase_protein);

        welcome = view.findViewById(R.id.welcome);
        welcome.setText("Halo, " + namaLengkap + "!");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchTotalsData();

        CardView cardTipeDiet = view.findViewById(R.id.card_tipeDiet);
        cardTipeDiet.setOnClickListener(v -> showDietTypeDialog());

        // Initialize CardViews
        CardView cardGula = view.findViewById(R.id.card_gula);
        CardView cardKarbohidrat = view.findViewById(R.id.card_karbohidrat);
        CardView cardProtein = view.findViewById(R.id.card_protein);
        CardView cardLemak = view.findViewById(R.id.card_lemak);
        CardView cardAir = view.findViewById(R.id.card_air);

        // Set custom tooltip listeners
        cardGula.setOnLongClickListener(v -> {
            showCustomTooltip(v, "Kelas King Gula");
            return true;
        });

        cardKarbohidrat.setOnLongClickListener(v -> {
            showCustomTooltip(v, "Kelas King Karbohidrat");
            return true;
        });

        cardProtein.setOnLongClickListener(v -> {
            showCustomTooltip(v, "Kelas King Protein");
            return true;
        });

        cardLemak.setOnLongClickListener(v -> {
            showCustomTooltip(v, "Kelas King Lemak");
            return true;
        });

        cardAir.setOnLongClickListener(v -> {
            showCustomTooltip(v, "Kelas King Air");
            return true;
        });
    }

    private void showCustomTooltip(View anchorView, String message) {
        // Inflate the custom tooltip layout
        View tooltipView = LayoutInflater.from(requireContext())
                .inflate(R.layout.custom_tooltip, null);

        // Find the TextView in the custom layout
        TextView tooltipText = tooltipView.findViewById(R.id.tooltip_text);
        tooltipText.setText(message);

        // Create the PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                tooltipView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Set background and enable outside touch dismissal
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.tooltip_background));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // Calculate position
        int[] anchorLocation = new int[2];
        anchorView.getLocationOnScreen(anchorLocation);

        // Show tooltip above the view
        popupWindow.showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                anchorLocation[0] + (anchorView.getWidth() / 2),
                anchorLocation[1] - tooltipView.getHeight()
        );
    }

    private void showDietTypeDialog() {
        // Create the dialog using AlertDialog.Builder with a custom theme
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(requireContext(), R.style.RoundedDialogTheme)
        );

        // Inflate the custom layout for the dialog
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_upberat_badan, null);

        // Set the custom view for the dialog
        builder.setView(dialogView);

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Get the button and set an OnClickListener to close the dialog
        Button btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }


    private void fetchTotalsData() {
        if (!isAdded()) return;

        SharedPreferences prefs = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String idUser = prefs.getString("idUser", "");

        if (idUser.isEmpty()) {
            showError("User ID tidak ditemukan");
            return;
        }

        String url = "http://adek-app.my.id/ads_mysql/get_totals.php?id_user=" + idUser;
        Log.d(TAG, "Fetching totals data from URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject data = response.getJSONObject("data");

                            // Ambil data total nutrisi
                            double totalGula = parseDoubleValue(data.getString("total_gula"));
                            double totalMinum = parseDoubleValue(data.getString("total_minum"));
                            double totalKarbohidrat = parseDoubleValue(data.getString("total_karbohidrat"));
                            double totalLemak = parseDoubleValue(data.getString("total_lemak"));
                            double totalProtein = parseDoubleValue(data.getString("total_protein"));

                            String tipeDiet = data.getString("tipe_diet").toLowerCase();
                            double beratBadan = parseDoubleValue(data.getString("berat_badan"));
                            double tinggiBadan = parseDoubleValue(data.getString("tinggi_badan"));

                            // Hitung BMI dan kebutuhan kalori harian
                            double tinggiMeter = tinggiBadan / 100;
                            double bmi = beratBadan / (tinggiMeter * tinggiMeter);
                            double kaloriHarian = hitungKaloriHarian(tipeDiet, beratBadan, tinggiBadan, bmi);

                            // Hitung target nutrisi
                            double[] targets = hitungTargetNutrisi(tipeDiet, kaloriHarian, beratBadan);
                            double proteinTarget = targets[0];
                            double lemakTarget = targets[1];
                            double karbohidratTarget = targets[2];
                            double gulaTarget = targets[3];
                            double airTarget = targets[4];

                            // Konversi ke persentase
                            double proteinPercentage = (totalProtein / proteinTarget) * 100;
                            double lemakPercentage = (totalLemak / lemakTarget) * 100;
                            double karbohidratPercentage = (totalKarbohidrat / karbohidratTarget) * 100;
                            double gulaPercentage = (totalGula / gulaTarget) * 100;
                            double airPercentage = (totalMinum / airTarget) * 100;

                            // Update UI
                            requireActivity().runOnUiThread(() -> {
                                updateNutrientTextView(persentaseProtein, proteinPercentage);
                                updateNutrientTextView(persentaseLemak, lemakPercentage);
                                updateNutrientTextView(persentaseKarbohidrat, karbohidratPercentage);
                                updateNutrientTextView(persentaseGula, gulaPercentage);
                                updateNutrientTextView(persentaseAir, airPercentage);
                            });

                        } else {
                            showError("Data tidak ditemukan");
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                        showError("Kesalahan memproses data");
                    }
                },
                error -> {
                    Log.e(TAG, "Network error: " + error.getMessage());
                    showError("Gagal mengambil data. Periksa koneksi Anda.");
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }

    private void updateNutrientTextView(TextView textView, double percentage) {
        textView.setText(String.format("%.1f%%", percentage));

        if (percentage > 100) {
            // Exceeded target
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.warning_red));

            // Create a drawable with a warning icon (circle with exclamation mark)
            Drawable warningIcon = ContextCompat.getDrawable(requireContext(), R.drawable.warning_icon);
            if (warningIcon != null) {
                // Apply tint if needed (optional, since the icon is already red)
                warningIcon.setTint(ContextCompat.getColor(requireContext(), R.color.warning_red));

                // Adjust the size of the icon to match the TextView's line height
                int iconSize = textView.getLineHeight();
                warningIcon.setBounds(0, 0, iconSize, iconSize);

                // Set the warning icon to the end of the TextView
                textView.setCompoundDrawables(null, null, warningIcon, null);
                textView.setCompoundDrawablePadding(8); // Padding between text and icon
            }
        } else {
            // Within target
            textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));
            textView.setCompoundDrawables(null, null, null, null);
        }
    }

    private double hitungKaloriHarian(String tipeDiet, double beratBadan, double tinggiBadan, double bmi) {
        double bmr = 10 * beratBadan + 6.25 * tinggiBadan - 5 * 25 + 5;
        switch (tipeDiet) {
            case "mengurangi berat badan":
                return bmr * 0.8;
            case "menambah berat badan":
                return bmr * 1.2;
            default:
                return bmr;
        }
    }

    private double[] hitungTargetNutrisi(String tipeDiet, double kaloriHarian, double beratBadan) {
        double protein, lemak, karbohidrat, gula, air;
        if (tipeDiet.equals("mengurangi berat badan")) {
            protein = kaloriHarian * 0.35 / 4;
            lemak = kaloriHarian * 0.25 / 9;
            karbohidrat = kaloriHarian * 0.40 / 4;
            gula = karbohidrat * 0.1;
            air = beratBadan * 35;
        } else if (tipeDiet.equals("menambah berat badan")) {
            protein = kaloriHarian * 0.30 / 4;
            lemak = kaloriHarian * 0.25 / 9;
            karbohidrat = kaloriHarian * 0.45 / 4;
            gula = karbohidrat * 0.15;
            air = beratBadan * 40;
        } else {
            protein = kaloriHarian * 0.30 / 4;
            lemak = kaloriHarian * 0.30 / 9;
            karbohidrat = kaloriHarian * 0.40 / 4;
            gula = karbohidrat * 0.12;
            air = beratBadan * 30;
        }
        return new double[]{protein, lemak, karbohidrat, gula, air};
    }

    private double parseDoubleValue(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
