package com.proyek.nusantara;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditKegiatanActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etJudul, etCeritaSingkat, etIsiCerita;
    private ImageView imgThumbnail;
    private Button btnPilihGambar, btnSimpan;
    private String postId, tanggal;
    private String existingBase64;      // Base64 lama dari Intent
    private String newBase64Image;      // Base64 jika user memilih gambar baru

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_kegiatan);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // tombol kembali
        FloatingActionButton fabback = findViewById(R.id.fabback);
        fabback.setOnClickListener(v -> {
            // Menutup aktivitas dan kembali ke sebelumnya
            finish();
        });

        // Inisialisasi view
        etJudul = findViewById(R.id.etJudul);
        etCeritaSingkat = findViewById(R.id.etCeritaSingkat);
        etIsiCerita = findViewById(R.id.etIsiCerita);
        imgThumbnail = findViewById(R.id.imgThumbnail);
        btnPilihGambar  = findViewById(R.id.btnPilihGambar);
        btnSimpan = findViewById(R.id.btnSimpan);

        // Ambil data dari Intent
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        String judulLama    = intent.getStringExtra("judul");
//        tanggal = intent.getStringExtra("tanggal");
        String ceritaLama   = intent.getStringExtra("ceritaSingkat");
        String isiLama      = intent.getStringExtra("isiCerita");
        existingBase64      = intent.getStringExtra("thumbnailBase64");

        // Set data ke view
        etJudul.setText(judulLama);
        etCeritaSingkat.setText(ceritaLama);
        etIsiCerita.setText(isiLama);

        // Tampilkan thumbnail lama (jika ada)
        if (existingBase64 != null && !existingBase64.isEmpty()) {
            imgThumbnail.setVisibility(View.VISIBLE);
            byte[] decodedBytes = Base64.decode(existingBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imgThumbnail.setImageBitmap(bitmap);
        } else {
            imgThumbnail.setVisibility(View.GONE);
        }

        // Tombol Pilih Gambar: buka galeri
        btnPilihGambar.setOnClickListener(v -> openImageChooser());

        // Button Simpan Perubahan
        btnSimpan.setOnClickListener(v -> {
            String judulBaru        = etJudul.getText().toString().trim();
            String ceritaBaru       = etCeritaSingkat.getText().toString().trim();
            String isiBaru          = etIsiCerita.getText().toString().trim();

            // Pilih Base64 mana yang dipakai: baru atau lama
            String thumbnailToSave;
            if (newBase64Image != null) {
                thumbnailToSave = newBase64Image;
            } else {
                thumbnailToSave = existingBase64 != null ? existingBase64 : "";
            }

            // Validasi semua field wajib diisi, termasuk thumbnail
            if (judulBaru.isEmpty() || ceritaBaru.isEmpty() || isiBaru.isEmpty() || thumbnailToSave.isEmpty()) {
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Buat map data untuk update Firestore
            Map<String, Object> dataUpdate = new HashMap<>();
            dataUpdate.put("judul",         judulBaru);
            dataUpdate.put("ceritaSingkat", ceritaBaru);
            dataUpdate.put("isiCerita",     isiBaru);
            dataUpdate.put("thumbnailBase64", thumbnailToSave);

            // Update dokumen di Firestore
            FirebaseFirestore.getInstance()
                    .collection("kegiatan")
                    .document(postId)
                    .update(dataUpdate)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Kegiatan berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gagal memperbarui: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    // Buka galeri untuk memilih gambar.
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar Thumbnail"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Tampilkan preview ke ImageView
            imgThumbnail.setVisibility(View.VISIBLE);
            imgThumbnail.setImageURI(imageUri);

            // Konversi gambar yang dipilih menjadi Base64
            convertImageToBase64(imageUri);
        }
    }

    // Konversi gambar (URI) ke string Base64, simpan di newBase64Image.
    private void convertImageToBase64(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();
            newBase64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal mengonversi gambar", Toast.LENGTH_SHORT).show();
        }
    }
}