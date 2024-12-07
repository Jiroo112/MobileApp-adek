package com.alphatz.adek.Model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class OlahragaModel implements Parcelable {
    private String namaOlahraga;
    private String deskripsi;
    private String gambarUrl;
    private String caraOlahraga;// Store URL as String instead of byte[]

    // Primary constructor
    public OlahragaModel(@NonNull String namaOlahraga, @NonNull String deskripsi, @NonNull String gambarUrl, @NonNull String caraOlahraga) {
        this.namaOlahraga = namaOlahraga;
        this.deskripsi = deskripsi;
        this.gambarUrl = gambarUrl;
        this.caraOlahraga = caraOlahraga;
    }

    // Getters with null handling
    @NonNull
    public String getNamaOlahraga() {
        return namaOlahraga != null ? namaOlahraga : "";
    }

    @NonNull
    public String getDeskripsi() {
        return deskripsi != null ? deskripsi : "";
    }

    @NonNull
    public String getCaraOlahraga(){
        return caraOlahraga != null ? caraOlahraga : "";
    }

    @NonNull
    public String getGambarUrl() {
        return gambarUrl != null ? gambarUrl : "";
    }

    // Setters with null protection
    public void setNamaOlahraga(@NonNull String namaOlahraga) {
        this.namaOlahraga = namaOlahraga;
    }

    public void setDeskripsi(@NonNull String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setCaraOlahraga(@NonNull String caraOlahraga){
        this.caraOlahraga = caraOlahraga;
    }

    public void setGambarUrl(@NonNull String gambarUrl) {
        this.gambarUrl = gambarUrl;
    }

    // Parcelable implementation
    protected OlahragaModel(Parcel in) {
        namaOlahraga = in.readString();
        deskripsi = in.readString();
        gambarUrl = in.readString(); // Read the URL as String
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
        dest.writeString(gambarUrl); // Write the URL as String
    }
}
