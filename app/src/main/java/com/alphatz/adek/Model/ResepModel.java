package com.alphatz.adek.Model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class ResepModel implements Parcelable {
    private String idMenu;
    private String namaMenu;
    private int kalori;
    private String resep;
    private String gambarUrl; // Untuk gambar dari URL
    private byte[] imageBytes; // Untuk gambar dari byte array

    // Constructor utama
    public ResepModel(@NonNull String idMenu, @NonNull String namaMenu, int kalori, @NonNull String resep, String gambarUrl, byte[] imageBytes) {
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.kalori = kalori;
        this.resep = resep;
        this.gambarUrl = gambarUrl;
        this.imageBytes = imageBytes;
    }

    // Constructor minimalis (untuk kompatibilitas)
    public ResepModel(@NonNull String idMenu, @NonNull String namaMenu, int kalori, @NonNull String resep, @NonNull String gambarUrl) {
        this(idMenu, namaMenu, kalori, resep, gambarUrl, null);
    }

    // Getters dengan null safety
    @NonNull
    public String getIdMenu() {
        return idMenu != null ? idMenu : "";
    }

    @NonNull
    public String getNamaMenu() {
        return namaMenu != null ? namaMenu : "";
    }

    public int getKalori() {
        return kalori;
    }

    @NonNull
    public String getResep() {
        return resep != null ? resep : "";
    }

    @NonNull
    public String getGambarUrl() {
        return gambarUrl != null ? gambarUrl : "";
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    // Setters dengan null safety
    public void setIdMenu(@NonNull String idMenu) {
        this.idMenu = idMenu;
    }

    public void setNamaMenu(@NonNull String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public void setKalori(int kalori) {
        this.kalori = kalori;
    }

    public void setResep(@NonNull String resep) {
        this.resep = resep;
    }

    public void setGambarUrl(String gambarUrl) {
        this.gambarUrl = gambarUrl;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    // Parcelable implementation
    protected ResepModel(Parcel in) {
        idMenu = in.readString();
        namaMenu = in.readString();
        kalori = in.readInt();
        resep = in.readString();
        gambarUrl = in.readString();
        int imageBytesLength = in.readInt();
        if (imageBytesLength > 0) {
            imageBytes = new byte[imageBytesLength];
            in.readByteArray(imageBytes);
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idMenu);
        dest.writeString(namaMenu);
        dest.writeInt(kalori);
        dest.writeString(resep);
        dest.writeString(gambarUrl);
        if (imageBytes != null) {
            dest.writeInt(imageBytes.length);
            dest.writeByteArray(imageBytes);
        } else {
            dest.writeInt(0);
        }
    }
}
