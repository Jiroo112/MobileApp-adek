package com.alphatz.adek.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import com.alphatz.adek.R;
import com.alphatz.adek.Activity.CurvedGauge;

public class BmiFragment extends Fragment {

    private CurvedGauge curvedGauge;
    private EditText etHeight;
    private EditText etWeight;
    private Button btnCalculate;
    private TextView hasilBmi;
    private TextView hasilBmiText;

    public BmiFragment() {
        // Required empty public constructor
    }

    public static BmiFragment newInstance(String param1, String param2) {
        BmiFragment fragment = new BmiFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        // Inisialisasi CurvedGauge
        curvedGauge = view.findViewById(R.id.curvedGauge);

        etHeight = view.findViewById(R.id.etHeight);
        etWeight = view.findViewById(R.id.etWeight);
        btnCalculate = view.findViewById(R.id.btnCalculate);
        hasilBmi = view.findViewById(R.id.hasil_bmi);
        hasilBmiText = view.findViewById(R.id.hasil_bmi_text);

        btnCalculate.setOnClickListener(v -> calculateAndDisplayBmi());

        return view;
    }

    private void calculateAndDisplayBmi() {
        // Ambil nilai dari EditText
        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(getActivity(), "Masukkan tinggi badan dan berat badan", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse input tinggi dan berat badan
        float heightInMeters;
        float weightInKg;

        try {
            heightInMeters = Float.parseFloat(heightStr) / 100;
            weightInKg = Float.parseFloat(weightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Input invalid. Masukkan dalam bentuk numerik.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hitung BMI
        float bmiValue = weightInKg / (heightInMeters * heightInMeters);

        // Update CurvedGauge dan hasil BMI yang sudah dihitung
        setCurvedGaugeProgress(bmiValue);

        // Tampilkan hasil perhitungan BMI di hasil_bmi
        hasilBmi.setText(String.format("%.1f", bmiValue));

        // Tentukan kategori BMI dan tampilkan di hasil_bmi_text
        String bmiCategory = getCategoryFromBmi(bmiValue);
        hasilBmiText.setText(bmiCategory);
    }

    private void setCurvedGaugeProgress(float bmiValue) {
        if (bmiValue >= 10 && bmiValue <= 40) {
            curvedGauge.setProgressWithAnimation(bmiValue); // Animasi
        } else {
            curvedGauge.setProgressWithAnimation(0);
            Toast.makeText(getActivity(), "BMI out of range (10-40)", Toast.LENGTH_SHORT).show();
        }
    }

    // Metode untuk mengkategorikan nilai BMI
    private String getCategoryFromBmi(float bmiValue) {
        if (bmiValue < 16.0) {
            return "Severe underweight";
        } else if (bmiValue >= 16.0 && bmiValue < 17.0) {
            return "Underweight";
        } else if (bmiValue >= 17.0 && bmiValue < 18.5) {
            return "Slightly underweight";
        } else if (bmiValue >= 18.5 && bmiValue < 25.0) {
            return "Normal";
        } else if (bmiValue >= 25.0 && bmiValue < 30.0) {
            return "Overweight";
        } else if (bmiValue >= 30.0 && bmiValue < 35.0) {
            return "Obese Class I";
        } else if (bmiValue >= 35.0 && bmiValue < 40.0) {
            return "Obese Class II";
        } else {
            return "Obese Class III";
        }
    }
}
