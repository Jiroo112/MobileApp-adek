package com.alphatz.adek.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ResepModel implements Parcelable {
    private String namaMenu;
    private int kalori;

    // Constructor
    public ResepModel(String namaMenu, int kalori) {
        this.namaMenu = namaMenu != null ? namaMenu : "";
        this.kalori = kalori;
    }

    // Getters and Setters
    public String getNamaMenu() {
        return namaMenu != null ? namaMenu : "";
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu != null ? namaMenu : "";
    }

    public int getKalori() {
        return kalori;
    }

    public void setKalori(int kalori) {
        this.kalori = kalori;
    }

    // Parcelable implementation
    protected ResepModel(Parcel in) {
        namaMenu = in.readString();
        kalori = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(namaMenu);
        dest.writeInt(kalori);
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
