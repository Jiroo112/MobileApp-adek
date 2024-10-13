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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        // Initialize the CurvedGauge
        curvedGauge = view.findViewById(R.id.curvedGauge);

        // Initialize EditText fields and Button
        etHeight = view.findViewById(R.id.etHeight);
        etWeight = view.findViewById(R.id.etWeight);
        btnCalculate = view.findViewById(R.id.btnCalculate);

        // Initialize hasil_bmi TextView
        hasilBmi = view.findViewById(R.id.hasil_bmi);

        // Set button click listener to calculate BMI
        btnCalculate.setOnClickListener(v -> calculateAndDisplayBmi());

        return view;
    }

    private void calculateAndDisplayBmi() {
        // Retrieve values from EditText fields
        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter both height and weight", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the height and weight input
        float heightInMeters;
        float weightInKg;

        try {
            heightInMeters = Float.parseFloat(heightStr) / 100; // Convert cm to meters
            weightInKg = Float.parseFloat(weightStr); // kg is already in correct unit
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Invalid input. Please enter numeric values.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate BMI
        float bmiValue = weightInKg / (heightInMeters * heightInMeters);

        // Update the CurvedGauge with the calculated BMI with animation
        setCurvedGaugeProgress(bmiValue);

        // Display the calculated BMI in the hasil_bmi TextView
        hasilBmi.setText(String.format("BMI: %.1f", bmiValue)); // Set BMI value to TextView with one decimal place
    }

    private void setCurvedGaugeProgress(float bmiValue) {
        // Assuming BMI value is between 10 and 40 for your gauge
        if (bmiValue >= 10 && bmiValue <= 40) {
            curvedGauge.setProgressWithAnimation(bmiValue); // Use the animated method
        } else {
            // Handle out-of-range values gracefully
            curvedGauge.setProgressWithAnimation(0); // Reset or set to a default value
            Toast.makeText(getActivity(), "BMI out of range (10-40)", Toast.LENGTH_SHORT).show();
        }
    }
}
