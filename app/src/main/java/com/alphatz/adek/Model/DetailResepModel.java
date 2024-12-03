package com.alphatz.adek.Model;

public class DetailResepModel {
    private String namaMenu;
    private int kalori;
    private String resep;
    private String gambarBase64;

    // Constructor
    public DetailResepModel(String namaMenu, int kalori, String resep, String gambarBase64) {
        this.namaMenu = namaMenu;
        this.kalori = kalori;
        this.resep = resep;
        this.gambarBase64 = gambarBase64;
    }

    // Getter dan Setter
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

    public String getGambarBase64() {
        return gambarBase64;
    }

    public void setGambarBase64(String gambarBase64) {
        this.gambarBase64 = gambarBase64;
    }
}
