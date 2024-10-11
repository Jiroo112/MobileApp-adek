package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.alphatz.adek.R;

public class KonsultasiFragment extends Fragment {

    public KonsultasiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_konsultasi, container, false);

        //buat ganti nama dokter, pake item_dokter//

        setupDoctorInfo(view, R.id.doctor_sonya, "dr. Sonya", R.drawable.ic_profil0);

        setupDoctorInfo(view, R.id.doctor_boyke, "dr. Boyke", R.drawable.ic_profil0);

        setupDoctorInfo(view, R.id.doctor_jiro, "dr. Jiro", R.drawable.ic_profil0);

        return view;
    }

    private void setupDoctorInfo(View parentView, int doctorLayoutId, String doctorName, int imageResourceId) {
        View doctorView = parentView.findViewById(doctorLayoutId);

        TextView nameTextView = doctorView.findViewById(R.id.doctor_name);
        ImageView imageView = doctorView.findViewById(R.id.doctor_image);
        TextView chatTextView = doctorView.findViewById(R.id.doctor_chat);
        TextView callTextView = doctorView.findViewById(R.id.doctor_call);
        Button contactButton = doctorView.findViewById(R.id.contact_button);

        nameTextView.setText(doctorName);
        imageView.setImageResource(imageResourceId);
        chatTextView.setText("Chat");
        callTextView.setText("Call/Video Call");
        contactButton.setText("CONTACT");

        //button sek kosong
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}