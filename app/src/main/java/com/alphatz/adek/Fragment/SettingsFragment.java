package com.alphatz.adek.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphatz.adek.R;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView tvGantiPassword = view.findViewById(R.id.tv_changepw);

        tvGantiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment changepwFragment = new ChangepwFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, changepwFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
