package com.proyek.nusantara;

public class Kegiatan {
    private String judul;
    private String cerita;
    private String tanggal;
    private String thumbnailUrl;

    public Kegiatan() {} // Diperlukan untuk Firestore tes ubah benar

    public Kegiatan(String judul, String cerita, String tanggal, String thumbnailUrl) {
        this.judul = judul;
        this.cerita = cerita;
        this.tanggal = tanggal;
        this.thumbnailUrl = thumbnailUrl;
    }

    // Getter dan Setter
    public String getJudul() { return judul; }
    public String getCerita() { return cerita; }
    public String getTanggal() { return tanggal; }
    public String getThumbnailUrl() { return thumbnailUrl; }
}