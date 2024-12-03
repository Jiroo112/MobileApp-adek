package com.alphatz.adek.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ResepModel implements Parcelable {
    private String idMenu;
    private String namaMenu;
    private int kalori;
    private String resep;
    private String gambar;
    private byte[] imageBytes;

    // constructor kosong, untuk inisialisasi default
    public ResepModel() {
        this.idMenu = "";
        this.namaMenu = "";
        this.kalori = 0;
        this.resep = getResep();
        this.gambar = "";
        this.imageBytes = null;
    }

    // constructor lengkap, buat semua data
    public ResepModel(String idMenu, String namaMenu, int kalori, String resep, String gambar, byte[] imageBytes) {
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.kalori = kalori;
        this.resep = resep;
        this.gambar = gambar;
        this.imageBytes = imageBytes;
    }

    // constructor buat load list aja
    public ResepModel(int id, String namaMenu, int kalori, byte[] imageBytes, String resep) {
        this.idMenu = String.valueOf(id);
        this.namaMenu = namaMenu;
        this.kalori = kalori;
        this.imageBytes = imageBytes;
        this.resep = resep;
        this.gambar = "";
    }
    // constructor buat parcelable, baca data dari Parcel
    protected ResepModel(Parcel in) {
        idMenu = in.readString();
        namaMenu = in.readString();
        kalori = in.readInt();
        resep = in.readString();
        gambar = in.readString();

        // baca byte array image, kalau ada
        int length = in.readInt();
        imageBytes = length > 0 ? new byte[length] : null;
        if (imageBytes != null) {
            in.readByteArray(imageBytes);
        }
    }

    // creator parcelable, standar Android
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

    // getter & setter
    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public int getKalori() {
        return kalori;
    }

    public void setKalori(int kalori) {
        this.kalori = kalori;
    }

    public String getResep() {
        return resep;
    }

    public void setResep(String resep) {
        this.resep = resep;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    // buat parcelable, serialize data ke Parcel
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
        dest.writeString(gambar);

        // tulis byte array kalau ada
        if (imageBytes != null) {
            dest.writeInt(imageBytes.length);
            dest.writeByteArray(imageBytes);
        } else {
            dest.writeInt(0);
        }
    }

    @Override
    public String toString() {
        return "ResepModel{" +
                "idMenu='" + idMenu + '\'' +
                ", namaMenu='" + namaMenu + '\'' +
                ", kalori=" + kalori +
                ", resep='" + (resep != null ? resep : "NULL") + '\'' +
                ", gambar='" + gambar + '\'' +
                ", imageBytes=" + (imageBytes != null ? imageBytes.length + " bytes" : "null") +
                '}';
    }
}
