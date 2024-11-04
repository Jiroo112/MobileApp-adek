package com.alphatz.adek.Model;

public class OlahragaModel {
    private String namaOlahraga;
    private int kalori;

    public OlahragaModel(String namaOlahraga, int kalori) {
        this.namaOlahraga = namaOlahraga != null ? namaOlahraga : "";
        this.kalori = kalori;
    }

    public String getNamaOlahraga() {
        return namaOlahraga != null ? namaOlahraga : "";
    }

    public void setNamaOlahraga(String namaOlahraga) {
        this.namaOlahraga = namaOlahraga != null ? namaOlahraga : "";
    }

    public int getKalori() {
        return kalori;
    }

    public void setKalori(int kalori) {
        this.kalori = kalori;
    }
}