package com.alphatz.adek.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ArtikelModel implements Parcelable {
    private String judulArtikel;
    private String kategori;
    private String gambarUrl; // Changed from byte[] to String for URL storage

    public ArtikelModel(@NonNull String judulartikel, @NonNull String kategori_artikel, @NonNull String gambarUrl) {
        this.judulArtikel = judulartikel;
        this.kategori = kategori_artikel;
        this.gambarUrl = gambarUrl;
    }

    // Getters with null handling
    @NonNull
    public String getJudulArtikel() {
        return judulArtikel != null ? judulArtikel : "";
    }

    @NonNull
    public String getKategori() {
        return kategori != null ? kategori : "";
    }

    @NonNull
    public String getGambarUrl() {
        return gambarUrl != null ? gambarUrl : "";
    }

    // Setters with null protection
    public void setJudulArtikel(@NonNull String judulArtikel) {
        this.judulArtikel = judulArtikel;
    }

    public void setKategori(@NonNull String kategori) {
        this.kategori = kategori;
    }

    public void setGambarUrl(@NonNull String gambarUrl) {
        this.gambarUrl = gambarUrl;
    }

    // Parcelable implementation
    protected ArtikelModel(Parcel in) {
        judulArtikel = in.readString();
        kategori = in.readString();
        gambarUrl = in.readString(); // Read the URL as String
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
        dest.writeString(gambarUrl); // Write the URL as String
    }
}
