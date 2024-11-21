package com.alphatz.adek.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class OlahragaModel implements Parcelable {
    private String namaOlahraga;
    private int kalori;

    // Constructor
    public OlahragaModel(String namaOlahraga, int kalori) {
        this.namaOlahraga = namaOlahraga != null ? namaOlahraga : "";
        this.kalori = kalori;
    }

    // Getters
    public String getNamaOlahraga() {
        return namaOlahraga != null ? namaOlahraga : "";
    }

    public int getKalori() {
        return kalori;
    }

    // Setters
    public void setNamaOlahraga(String namaOlahraga) {
        this.namaOlahraga = namaOlahraga != null ? namaOlahraga : "";
    }

    public void setKalori(int kalori) {
        this.kalori = kalori;
    }

    // Parcelable implementation
    protected OlahragaModel(Parcel in) {
        namaOlahraga = in.readString();
        kalori = in.readInt();
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
        dest.writeInt(kalori);
    }
}