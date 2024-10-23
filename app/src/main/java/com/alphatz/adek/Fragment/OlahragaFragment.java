package com.alphatz.adek.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alphatz.adek.Activity.Dashboard;
import com.alphatz.adek.R;

public class OlahragaFragment extends Fragment {

    private Button btnKekuatan, btnKelenturan;

    public OlahragaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_olahraga, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi tombol2
        btnKekuatan = view.findViewById(R.id.btn_kekuatan);
        btnKelenturan = view.findViewById(R.id.btn_kelenturan);

        // Setup OnClickListener buat btnKekuatan
        btnKekuatan.setOnClickListener(v -> {
            KekuatanFragment kekuatanFragment = new KekuatanFragment();
            replaceFragment(kekuatanFragment);
        });

        // Setup OnClickListener buat btnKelenturan
        btnKelenturan.setOnClickListener(v -> {
            KelenturanFragment kelenturanFragment = new KelenturanFragment();
            replaceFragment(kelenturanFragment);
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // back stack buat backnya
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        //di on resume ini pastiin dulu kalo bottom nav nya udh di hide
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).hideBottomNavigation();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // nampilin lagi bottom navigation waktu fragment nya di destroy
        if (getActivity() instanceof Dashboard) {
            ((Dashboard) getActivity()).showBottomNavigation();
        }
    }
}
