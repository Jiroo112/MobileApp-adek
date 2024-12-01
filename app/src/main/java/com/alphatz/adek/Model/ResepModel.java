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

    // Default constructor (for frameworks or ORM)
    public ResepModel() {
        // Default values
        this.idMenu = "";
        this.namaMenu = "";
        this.kalori = 0;
        this.resep = "";
        this.gambar = "";
        this.imageBytes = null;
    }

    // Full constructor
    public ResepModel(String idMenu, String namaMenu, int kalori, String resep, String gambar, byte[] imageBytes) {
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.kalori = kalori;
        this.resep = resep;
        this.gambar = gambar;
        this.imageBytes = imageBytes;
    }

    // Constructor for list loading
    public ResepModel(int id, String namaMenu, int kalori, byte[] imageBytes) {
        this.idMenu = String.valueOf(id);
        this.namaMenu = namaMenu;
        this.kalori = kalori;
        this.imageBytes = imageBytes;
        this.resep = "";
        this.gambar = "";
    }

    // Parcelable constructor
    protected ResepModel(Parcel in) {
        idMenu = in.readString();
        namaMenu = in.readString();
        kalori = in.readInt();
        resep = in.readString();
        gambar = in.readString();

        // Read byte array for image
        int length = in.readInt();
        if (length > 0) {
            imageBytes = new byte[length];
            in.readByteArray(imageBytes);
        } else {
            imageBytes = null;
        }
    }

    // Parcelable Creator
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

    // Getters and Setters
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

    // Parcelable implementation
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

        // Write imageBytes
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
                ", resep='" + resep + '\'' +
                ", gambar='" + gambar + '\'' +
                ", imageBytes=" + (imageBytes != null ? imageBytes.length + " bytes" : "null") +
                '}';
    }
}
