package com.alphatz.adek.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alphatz.adek.R;
import com.alphatz.adek.Model.KonsultasiModel;
import java.util.List;

public class KonsultasiAdapter extends RecyclerView.Adapter<KonsultasiAdapter.DoctorViewHolder> {
    private List<KonsultasiModel> konsultanList;
    private final OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onDoctorClick(KonsultasiModel konsultan);
    }

    public KonsultasiAdapter(List<KonsultasiModel> konsultanList, OnDoctorClickListener listener) {
        this.konsultanList = konsultanList;
        this.listener = listener;
    }

    public void updateList(List<KonsultasiModel> newList) {
        this.konsultanList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dokter, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        KonsultasiModel konsultan = konsultanList.get(position);
        holder.bind(konsultan);
    }

    @Override
    public int getItemCount() {
        return konsultanList.size();
    }

    class DoctorViewHolder extends RecyclerView.ViewHolder {
        ImageView doctorImage;
        TextView doctorName;
        TextView doctorSpecialty;
        Button contactButton;

        DoctorViewHolder(View itemView) {
            super(itemView);
            doctorImage = itemView.findViewById(R.id.doctor_image);
            doctorName = itemView.findViewById(R.id.doctor_name);
            doctorSpecialty = itemView.findViewById(R.id.doctor_specialty);
            contactButton = itemView.findViewById(R.id.contact_button);
        }

        void bind(final KonsultasiModel konsultan) {
            doctorName.setText(konsultan.getNamaLengkap());
            doctorSpecialty.setText(konsultan.getJenis());

            contactButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDoctorClick(konsultan);
                }
            });
        }
    }
}