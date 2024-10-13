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

        // inisialisasi CurvedGauge
        curvedGauge = view.findViewById(R.id.curvedGauge);

        etHeight = view.findViewById(R.id.etHeight);
        etWeight = view.findViewById(R.id.etWeight);
        btnCalculate = view.findViewById(R.id.btnCalculate);
        hasilBmi = view.findViewById(R.id.hasil_bmi);
        btnCalculate.setOnClickListener(v -> calculateAndDisplayBmi());

        return view;
    }

    private void calculateAndDisplayBmi() {
        // ngambil value dari edit text
        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(getActivity(), "Masukkan tinggi badan dan berat badan", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the height and weight input
        float heightInMeters;
        float weightInKg;

        try {
            heightInMeters = Float.parseFloat(heightStr) / 100;
            weightInKg = Float.parseFloat(weightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "inputan invalid. Masukkan dalam bentuk numerik.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ngitung bmi
        float bmiValue = weightInKg / (heightInMeters * heightInMeters);

        // update curvedgauge dan bmi yg udh di itung
        setCurvedGaugeProgress(bmiValue);

        // display hasil perhitungan bmi ke hasil_bmi
        hasilBmi.setText(String.format("BMI: %.1f", bmiValue));
    }

    private void setCurvedGaugeProgress(float bmiValue) {
        if (bmiValue >= 10 && bmiValue <= 40) {
            curvedGauge.setProgressWithAnimation(bmiValue); //animasi
        } else {
            curvedGauge.setProgressWithAnimation(0);
            Toast.makeText(getActivity(), "BMI out of range (10-40)", Toast.LENGTH_SHORT).show();
        }
    }
}
