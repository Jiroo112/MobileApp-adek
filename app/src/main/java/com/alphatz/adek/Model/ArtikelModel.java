package com.alphatz.adek.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ArtikelModel implements Parcelable {
    private String judulArtikel;
    private String kategori;
    private byte[] gambar;

    public ArtikelModel(@NonNull String judulartikel, @NonNull String kategori_artikel, byte[] gambar) {
        this.judulArtikel = judulartikel;
        this.kategori = kategori_artikel;
        this.gambar = gambar;
    }

    // Getters with improved null handling
    @NonNull
    public String getJudulArtikell() {
        return judulArtikel != null ? judulArtikel : "";
    }

    @NonNull
    public String getKategori() {
        return kategori != null ? kategori : "";
    }

    public byte[] getGambar() {
        return gambar;
    }

    // Setters with null protection
    public void setJudulArtikell(@NonNull String judulArtikell) {
        this.judulArtikel = judulArtikell;
    }

    public void setKategori(@NonNull String kategori) {
        this.kategori = kategori;
    }

    public void setGambar(byte[] gambar) {
        this.gambar = gambar;
    }

    // Parcelable implementation (remains the same as in original code)
    protected ArtikelModel(Parcel in) {
        judulArtikel = in.readString();
        kategori = in.readString();

        int gambarLength = in.readInt();
        if (gambarLength > 0) {
            gambar = new byte[gambarLength];
            in.readByteArray(gambar);
        }
    }

    public static final Creator<ArtikelModel> CREATOR = new Creator<ArtikelModel>() {
        @Override
        public ArtikelModel createFromParcel(Parcel in) {
            return new ArtikelModel(in);
        }

        @Override
        public ArtikelModel[] newArray(int size) {
            return new ArtikelModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(judulArtikel);
        dest.writeString(kategori);

        if (gambar != null) {
            dest.writeInt(gambar.length);
            dest.writeByteArray(gambar);
        } else {
            dest.writeInt(0);
        }
    }
}
