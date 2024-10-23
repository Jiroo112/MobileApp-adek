package com.alphatz.adek.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alphatz.adek.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MakananAdapter extends RecyclerView.Adapter<MakananAdapter.MakananViewHolder> {

    private Context context;
    private JSONArray makananArray;

    public MakananAdapter(Context context, JSONArray makananArray) {
        this.context = context;
        this.makananArray = makananArray;
    }

    @NonNull
    @Override
    public MakananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_makanan, parent, false);
        return new MakananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MakananViewHolder holder, int position) {
        try {
            JSONObject makanan = makananArray.getJSONObject(position);
            String namaMenu = makanan.getString("nama_menu");
            String kalori = makanan.getString("kalori");

            holder.tvNamaMenu.setText(namaMenu);
            holder.tvKalori.setText("Kalori: " + kalori);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return makananArray.length();
    }

    public static class MakananViewHolder extends RecyclerView.ViewHolder {

        TextView tvNamaMenu, tvKalori;

        public MakananViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMenu = itemView.findViewById(R.id.tvNamaMenu);
            tvKalori = itemView.findViewById(R.id.tvKalori);
        }
    }
}
