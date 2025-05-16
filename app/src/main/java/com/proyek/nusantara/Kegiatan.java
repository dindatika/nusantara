package com.proyek.nusantara;

public class Kegiatan {
    private String id;
    private String judul;
    private String ceritaSingkat;
    private String isiCerita;
    private String tanggal;
    private String thumbnailUrl;
    private String userId;

    public Kegiatan() {
        // Diperlukan oleh Firestore
    }

    public Kegiatan(String judul, String ceritaSingkat, String isiCerita, String tanggal, String thumbnailUrl, String userId) {
        this.judul = judul;
        this.ceritaSingkat = ceritaSingkat;
        this.isiCerita = isiCerita;
        this.tanggal = tanggal;
        this.thumbnailUrl = thumbnailUrl;
        this.userId = userId;
    }

    // Getter dan Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getCeritaSingkat() {
        return ceritaSingkat;
    }

    public void setCeritaSingkat(String ceritaSingkat) {
        this.ceritaSingkat = ceritaSingkat;
    }

    public String getIsiCerita() {
        return isiCerita;
    }

    public void setIsiCerita(String isiCerita) {
        this.isiCerita = isiCerita;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}