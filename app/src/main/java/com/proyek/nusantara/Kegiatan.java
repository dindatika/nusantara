package com.proyek.nusantara;

public class Kegiatan {
    private String id;
    private String judul;
    private String ceritaSingkat;
    private String isiCerita;
    private String tanggal;
    private String thumbnailBase64;
    private String userId;

    public Kegiatan() {
        // Diperlukan oleh Firestore
    }

    public Kegiatan(String id, String judul, String ceritaSingkat, String isiCerita, String tanggal, String thumbnailBase64, String userId) {
        this.id = id;
        this.judul = judul;
        this.ceritaSingkat = ceritaSingkat;
        this.isiCerita = isiCerita;
        this.tanggal = tanggal;
        this.thumbnailBase64 = thumbnailBase64;
        this.userId = userId;
    }

    // Getter & Setter untuk ID
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // Getter & Setter untuk Judul
    public String getJudul() {
        return judul;
    }
    public void setJudul(String judul) {
        this.judul = judul;
    }

    // Getter & Setter untuk Cerita Singkat
    public String getCeritaSingkat() {
        return ceritaSingkat;
    }
    public void setCeritaSingkat(String ceritaSingkat) {
        this.ceritaSingkat = ceritaSingkat;
    }

    // Getter & Setter untuk Isi Cerita
    public String getIsiCerita() {
        return isiCerita;
    }
    public void setIsiCerita(String isiCerita) {
        this.isiCerita = isiCerita;
    }

    // Getter & Setter untuk Tanggal
    public String getTanggal() {
        return tanggal;
    }
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    // Getter & Setter untuk thumbnailBase64
    public String getThumbnailBase64() {
        return thumbnailBase64;
    }
    public void setThumbnailBase64(String thumbnailBase64) {
        this.thumbnailBase64 = thumbnailBase64;
    }

    // Getter & Setter untuk User ID
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}