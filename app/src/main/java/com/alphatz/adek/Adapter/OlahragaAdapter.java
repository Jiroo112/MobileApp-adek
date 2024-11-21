package com.alphatz.adek.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager; // Make sure to import this
import androidx.fragment.app.FragmentTransaction; // Make sure to import this
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.Model.OlahragaModel;
import com.alphatz.adek.R;
import com.alphatz.adek.Fragment.DetailOlahragaFragment; // Import your DetailOlahragaFragment class
import java.util.List;

public class OlahragaAdapter extends RecyclerView.Adapter<OlahragaAdapter.OlahragaViewHolder> {
    private List<OlahragaModel> olahragaList;
    private final OnOlahragaClickListener listener;
    private final FragmentManager fragmentManager; // Add FragmentManager reference

    public interface OnOlahragaClickListener {
        void onOlahragaClick(OlahragaModel olahraga);
    }

    public OlahragaAdapter(List<OlahragaModel> olahragaList, OnOlahragaClickListener listener, FragmentManager fragmentManager) {
        this.olahragaList = olahragaList;
        this.listener = listener;
        this.fragmentManager = fragmentManager; // Initialize the FragmentManager
    }

    public void updateList(List<OlahragaModel> newList) {
        this.olahragaList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OlahragaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resep, parent, false);
        return new OlahragaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OlahragaViewHolder holder, int position) {
        OlahragaModel olahraga = olahragaList.get(position);
        holder.bind(olahraga);
    }

    @Override
    public int getItemCount() {
        return olahragaList != null ? olahragaList.size() : 0;
    }

    class OlahragaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaOlahraga;
        TextView tvKalori;
        Button detailButton;

        OlahragaViewHolder(View itemView) {
            super(itemView);
            tvNamaOlahraga = itemView.findViewById(R.id.tv_nama_menu);
            tvKalori = itemView.findViewById(R.id.tv_kalori);
            detailButton = itemView.findViewById(R.id.detail_button);
        }

        void bind(final OlahragaModel olahraga) {
            if (olahraga != null) {
                tvNamaOlahraga.setText(olahraga.getNamaOlahraga() != null ?
                        "Olahraga: " + olahraga.getNamaOlahraga() : "Olahraga: -");
                tvKalori.setText("Kalori: " + olahraga.getKalori());

                detailButton.setOnClickListener(v -> {
                    // Check if listener is not null (if you still need it for other purposes)
                    if (listener != null) {
                        listener.onOlahragaClick(olahraga);
                    }

                    // Start a transaction to switch to DetailOlahragaFragment
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Create an instance of DetailOlahragaFragment
                    DetailOlahragaFragment detailFragment = new DetailOlahragaFragment();

                    // Pass data to the new fragment using a Bundle
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("key", olahraga); // Use putParcelable for Parcelable
                    detailFragment.setArguments(bundle);

                    // Replace the current fragment with DetailOlahragaFragment
                    fragmentTransaction.replace(R.id.fragment_container, detailFragment); // Replace 'fragment_container' with your actual container ID
                    fragmentTransaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                    fragmentTransaction.commit(); // Commit the transaction
                });
            }
        }
    }
}