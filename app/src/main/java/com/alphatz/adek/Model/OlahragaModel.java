package com.alphatz.adek.Model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class OlahragaModel implements Parcelable {
    private String namaOlahraga;
    private String deskripsi;
    private byte[] gambar;

    // Primary constructor
    public OlahragaModel(@NonNull String namaOlahraga, @NonNull String deskripsi, byte[] gambar) {
        this.namaOlahraga = namaOlahraga;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
    }

    // Getters with improved null handling
    @NonNull
    public String getNamaOlahraga() {
        return namaOlahraga != null ? namaOlahraga : "";
    }

    @NonNull
    public String getDeskripsi() {
        return deskripsi != null ? deskripsi : "";
    }

    public byte[] getGambar() {
        return gambar;
    }

    // Setters with null protection
    public void setNamaOlahraga(@NonNull String namaOlahraga) {
        this.namaOlahraga = namaOlahraga;
    }

    public void setDeskripsi(@NonNull String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setGambar(byte[] gambar) {
        this.gambar = gambar;
    }

    // Parcelable implementation (remains the same as in original code)
    protected OlahragaModel(Parcel in) {
        namaOlahraga = in.readString();
        deskripsi = in.readString();

        int gambarLength = in.readInt();
        if (gambarLength > 0) {
            gambar = new byte[gambarLength];
            in.readByteArray(gambar);
        }
    }

    public static final Creator<OlahragaModel> CREATOR = new Creator<OlahragaModel>() {
        @Override
        public OlahragaModel createFromParcel(Parcel in) {
            return new OlahragaModel(in);
        }

        @Override
        public OlahragaModel[] newArray(int size) {
            return new OlahragaModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(namaOlahraga);
        dest.writeString(deskripsi);

        if (gambar != null) {
            dest.writeInt(gambar.length);
            dest.writeByteArray(gambar);
        } else {
            dest.writeInt(0);
        }
    }
}