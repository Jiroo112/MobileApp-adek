package com.alphatz.adek.Model;

public class AsupanModel {
    private String namaMenu;
    private String kategori;

    public AsupanModel(String namaMenu, int kalori, int protein, int karbohidrat, int lemak) {
        this.namaMenu = namaMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}