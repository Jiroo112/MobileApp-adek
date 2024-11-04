package com.alphatz.adek.Model;

public class ResepModel {
    private String namaMenu;
    private int kalori;

    public ResepModel(String namaMenu, int kalori) {
        this.namaMenu = namaMenu != null ? namaMenu : "";
        this.kalori = kalori;
    }

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
}
