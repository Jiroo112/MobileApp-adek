package com.alphatz.adek.Model;

public class KonsultasiModel {
    private String id_konsultan;
    private String nama_lengkap;
    private String no_hp;
    private String fotoDokter;

    public KonsultasiModel(String id_konsultan, String nama_lengkap, String no_hp, String fotoDokter) {
        this.id_konsultan = id_konsultan;
        this.nama_lengkap = nama_lengkap;
        this.no_hp = no_hp;
        this.fotoDokter = fotoDokter;
    }

    public String getFotoDokter() {
        return fotoDokter;
    }

    public String getIdKonsultan() { return id_konsultan; }
    public String getNamaLengkap() { return nama_lengkap; }
    public String getNoHp() { return no_hp; }

    @Override
    public String toString() {
        return "Konsultan{" +
                "id_konsultan=" + id_konsultan +
                ", nama_lengkap='" + nama_lengkap + '\'' +
                '}';
    }
}
