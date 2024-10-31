package com.alphatz.adek.Model;

public class KonsultasiModel {
    private int id_konsultan;
    private String email;
    private String nama_lengkap;
    private String jenis;
    private String no_hp;

    public KonsultasiModel(int id_konsultan, String email, String nama_lengkap, String jenis, String no_hp) {
        this.id_konsultan = id_konsultan;
        this.email = email;
        this.nama_lengkap = nama_lengkap;
        this.jenis = jenis;
        this.no_hp = no_hp;
    }

    public int getIdKonsultan() { return id_konsultan; }
    public String getEmail() { return email; }
    public String getNamaLengkap() { return nama_lengkap; }
    public String getJenis() { return jenis; }
    public String getNoHp() { return no_hp; }

    @Override
    public String toString() {
        return "Konsultan{" +
                "id_konsultan=" + id_konsultan +
                ", nama_lengkap='" + nama_lengkap + '\'' +
                ", jenis='" + jenis + '\'' +
                '}';
    }
}