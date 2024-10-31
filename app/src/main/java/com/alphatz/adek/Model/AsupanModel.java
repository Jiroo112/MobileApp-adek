package com.alphatz.adek.Model;

public class AsupanModel {
    private int idMenu;
    private String namaMenu;
    private double kalori;
    private double protein;
    private double karbo;
    private double gula;

    public AsupanModel(int idMenu, String namaMenu, double kalori, double protein, double karbo, double gula) {
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.kalori = kalori;
        this.protein = protein;
        this.karbo = karbo;
        this.gula = gula;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public double getKalori() {
        return kalori;
    }

    public double getProtein() {
        return protein;
    }

    public double getKarbo() {
        return karbo;
    }

    public double getGula() {
        return gula;
    }
}
