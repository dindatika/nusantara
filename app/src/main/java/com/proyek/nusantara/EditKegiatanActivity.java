package com.proyek.nusantara;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditKegiatanActivity extends AppCompatActivity {

    private EditText etJudul, etCeritaSingkat, etIsiCerita, edtGambarUrl;
    private ImageView imgThumbnail;
    private Button btnPilihGambar, btnSimpan;
    private String postId, tanggal, thumbnailUrl;

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
        edtGambarUrl = findViewById(R.id.edtGambarUrl);
        btnSimpan = findViewById(R.id.btnSimpan);

        // Ambil data dari Intent
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        String judul = intent.getStringExtra("judul");
//        tanggal = intent.getStringExtra("tanggal");
        String ceritaSingkat = intent.getStringExtra("ceritaSingkat");
        String isiCerita = intent.getStringExtra("isiCerita");
        String thumbnailUrl  = intent.getStringExtra("thumbnailUrl");

        // Set data ke view
        etJudul.setText(judul);
        etCeritaSingkat.setText(ceritaSingkat);
        etIsiCerita.setText(isiCerita);
        edtGambarUrl.setText(thumbnailUrl);

        // Load dan preview gambar
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            imgThumbnail.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(thumbnailUrl)
                    .into(imgThumbnail);
        } else {
            imgThumbnail.setVisibility(View.GONE);
            imgThumbnail.setImageResource(R.drawable.rounded_bg);
        }

        // Button Simpan Perubahan
        btnSimpan.setOnClickListener(v -> {
            String judulBaru = etJudul.getText().toString().trim();
            String ceritaSingkatBaru = etCeritaSingkat.getText().toString().trim();
            String isiCeritaBaru = etIsiCerita.getText().toString().trim();
            String thumbnailUrlBaru = edtGambarUrl.getText().toString().trim();

            // Validasi input
            if (judulBaru.isEmpty() || ceritaSingkatBaru.isEmpty()
                    || isiCeritaBaru.isEmpty() || thumbnailUrlBaru.isEmpty()) {
                Toast.makeText(this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Preview gambar baru
            imgThumbnail.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(thumbnailUrlBaru)
                    .into(imgThumbnail);

            // Buat map untuk data yang akan diupdate
            Map<String, Object> dataUpdate = new HashMap<>();
            dataUpdate.put("judul",          judulBaru);
            dataUpdate.put("ceritaSingkat",  ceritaSingkatBaru);
            dataUpdate.put("isiCerita",      isiCeritaBaru);
            dataUpdate.put("thumbnailUrl",   thumbnailUrlBaru);

            // Update di Firestore
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
}