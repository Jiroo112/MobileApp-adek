package com.alphatz.adek.Adapter;

import android.util.Log;
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
import com.bumptech.glide.Glide;
import java.util.List;

public class KonsultasiAdapter extends RecyclerView.Adapter<KonsultasiAdapter.DoctorViewHolder> {
    private List<KonsultasiModel> konsultanList;
    private final OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onDoctorClick(KonsultasiModel konsultan);
    }

    public KonsultasiAdapter(List<KonsultasiModel> konsultanList, OnDoctorClickListener listener) {
        this.konsultanList = konsultanList != null ? konsultanList : List.of(); // Null safety
        this.listener = listener;
    }

    public void updateList(List<KonsultasiModel> newList) {
        if (newList != null) {
            this.konsultanList = newList;
            notifyDataSetChanged();
        }
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
        return konsultanList != null ? konsultanList.size() : 0;
    }

    class DoctorViewHolder extends RecyclerView.ViewHolder {
        ImageView doctorImage;
        TextView doctorName;
        Button contactButton;

        DoctorViewHolder(View itemView) {
            super(itemView);
            doctorImage = itemView.findViewById(R.id.doctor_image);
            doctorName = itemView.findViewById(R.id.doctor_name);
            contactButton = itemView.findViewById(R.id.contact_button);

            contactButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    KonsultasiModel konsultan = konsultanList.get(position);
                    listener.onDoctorClick(konsultan);
                }
            });
        }

        void bind(final KonsultasiModel konsultan) {
            if (konsultan != null) {
                doctorName.setText(konsultan.getNamaLengkap());

                String imageUrl = konsultan.getGambarUrl();
                if (imageUrl != null && !imageUrl.startsWith("http")) {
                    // Tambahkan base URL jika gambar tidak dimulai dengan "http"
                    String baseUrl = "https://adek-app.my.id/";
                    imageUrl = baseUrl + imageUrl;
                }

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    // Muat gambar menggunakan Glide
                    Glide.with(itemView.getContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.gambar_kosong)
                            .error(R.drawable.gambar_kosong)
                            .into(doctorImage);
                } else {
                    // Gunakan gambar placeholder default jika URL tidak valid
                    doctorImage.setImageResource(R.drawable.gambar_kosong);
                }
            }
        }
    }
}