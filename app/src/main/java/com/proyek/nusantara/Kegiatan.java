package com.proyek.nusantara;

public class Kegiatan {
    private String id;
    private String judul;
    private String cerita;
    private String tanggal;
    private String thumbnailUrl;
    private String userId;

    public Kegiatan() {} // Diperlukan untuk Firestore tes ubah benar

    public Kegiatan(String judul, String cerita, String tanggal, String thumbnailUrl, String userId) {
        this.judul = judul;
        this.cerita = cerita;
        this.tanggal = tanggal;
        this.thumbnailUrl = thumbnailUrl;
        this.userId = userId;
    }

    // Getter dan Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getCerita() { return cerita; }
    public void setCerita(String cerita) { this.cerita = cerita; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}