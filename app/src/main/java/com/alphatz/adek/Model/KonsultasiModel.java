package com.alphatz.adek.Model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class KonsultasiModel implements Parcelable {
    private String id_konsultan;
    private String nama_lengkap;
    private String no_hp;
    private String gambarUrl;

    // Primary constructor
    public KonsultasiModel(@NonNull String id_konsultan, @NonNull String nama_lengkap, @NonNull String no_hp, @NonNull String gambarUrl) {
        this.id_konsultan = id_konsultan;
        this.nama_lengkap = nama_lengkap;
        this.no_hp = no_hp;
        this.gambarUrl = gambarUrl;
    }

    // Getters with null handling
    @NonNull
    public String getIdKonsultan() {
        return id_konsultan != null ? id_konsultan : "";
    }

    @NonNull
    public String getNamaLengkap() {
        return nama_lengkap != null ? nama_lengkap : "";
    }

    @NonNull
    public String getNoHp() {
        return no_hp != null ? no_hp : "";
    }

    @NonNull
    public String getGambarUrl() {
        return gambarUrl != null ? gambarUrl : "";
    }

    // Setters with null protection
    public void setIdKonsultan(@NonNull String id_konsultan) {
        this.id_konsultan = id_konsultan;
    }

    public void setNamaLengkap(@NonNull String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public void setNoHp(@NonNull String no_hp) {
        this.no_hp = no_hp;
    }

    public void setGambarUrl(@NonNull String gambarUrl) {
        this.gambarUrl = gambarUrl;
    }

    // Parcelable implementation
    protected KonsultasiModel(Parcel in) {
        id_konsultan = in.readString();
        nama_lengkap = in.readString();
        no_hp = in.readString();
        gambarUrl = in.readString();
    }

    public static final Creator<KonsultasiModel> CREATOR = new Creator<KonsultasiModel>() {
        @Override
        public KonsultasiModel createFromParcel(Parcel in) {
            return new KonsultasiModel(in);
        }

        @Override
        public KonsultasiModel[] newArray(int size) {
            return new KonsultasiModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_konsultan);
        dest.writeString(nama_lengkap);
        dest.writeString(no_hp);
        dest.writeString(gambarUrl);
    }
}