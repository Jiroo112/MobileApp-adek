package com.alphatz.adek.Model;

public class MenuModel {
    private int id_menu;
    private String nama_menu;
    private double protein;
    private double karbohidrat;
    private double lemak;
    private double kalori;

    public MenuModel(int id_menu, String nama_menu, double protein, double karbohidrat, double lemak, double kalori) {
        this.id_menu = id_menu;
        this.nama_menu = nama_menu;
        this.protein = protein;
        this.karbohidrat = karbohidrat;
        this.lemak = lemak;
        this.kalori = kalori;
    }

    public int getId_menu() { return id_menu; }
    public String getNama_menu() { return nama_menu; }
    public double getProtein() { return protein; }
    public double getKarbohidrat() { return karbohidrat; }
    public double getLemak() { return lemak; }
    public double getKalori() { return kalori; }
}