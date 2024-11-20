package com.alphatz.adek.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alphatz.adek.R;

public class ResepModel implements Parcelable {
    private String namaMenu;
    private String deskripsi; // Tambahkan deskripsi
    private int kalori;
    private int imageResId; // Tambahkan ID sumber gambar

    // Constructor dengan semua parameter
    public ResepModel(String namaMenu, String deskripsi, int kalori, int imageResId) {
        this.namaMenu = namaMenu != null ? namaMenu : "";
        this.deskripsi = deskripsi != null ? deskripsi : ""; // Inisialisasi deskripsi
        this.kalori = kalori;
        this.imageResId = imageResId; // Inisialisasi ID gambar
    }

    // Constructor dengan dua parameter
    public ResepModel(String namaMenu, int kalori) {
        this(namaMenu, "", kalori, R.drawable.default_image); // Ganti R.drawable.default_image dengan ID gambar default
    }

    // Getters and Setters
    public String getNamaMenu() {
        return namaMenu != null ? namaMenu : "";
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu != null ? namaMenu : "";
    }

    public String getDeskripsi() {
        return deskripsi != null ? deskripsi : ""; // Tambahkan getter untuk deskripsi
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi != null ? deskripsi : ""; // Tambahkan setter untuk deskripsi
    }

    public int getKalori() {
        return kalori;
    }

    public void setKalori(int kalori) {
        this.kalori = kalori;
    }

    public int getImageResId() {
        return imageResId; // Tambahkan getter untuk ID gambar
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId; // Tambahkan setter untuk ID gambar
    }

    // Parcelable implementation
    protected ResepModel(Parcel in) {
        namaMenu = in.readString();
        deskripsi = in.readString(); // Baca deskripsi dari Parcel
        kalori = in.readInt();
        imageResId = in.readInt(); // Baca ID gambar dari Parcel
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(namaMenu);
        dest.writeString(deskripsi); // Tulis deskripsi ke Parcel
        dest.writeInt(kalori);
        dest.writeInt(imageResId); // Tulis ID gambar ke Parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResepModel> CREATOR = new Creator<ResepModel>() {
        @Override
        public ResepModel createFromParcel(Parcel in) {
            return new ResepModel(in);
        }

        @Override
        public ResepModel[] newArray(int size) {
            return new ResepModel[size];
        }
    };
}