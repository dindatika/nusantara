package com.proyek.nusantara;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class TambahKegiatanActivity extends AppCompatActivity {

    private EditText etJudul, etCerita, edtGambarUrl;
    private Button btnSimpan;
    private ImageView imgThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tambah_kegiatan);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tambahkan logika form di bawah sini:
        etJudul = findViewById(R.id.etJudul);
        etCerita = findViewById(R.id.etCerita);
        edtGambarUrl = findViewById(R.id.edtGambarUrl);
        btnSimpan = findViewById(R.id.btnSimpan);
        imgThumbnail = findViewById(R.id.imgThumbnail);

        FloatingActionButton fabback = findViewById(R.id.fabback);
        fabback.setOnClickListener(v -> {
            // Menutup aktivitas dan kembali ke sebelumnya
            finish();
        });

        // Event klik tombol simpan
        btnSimpan.setOnClickListener(v -> {
            String judul = etJudul.getText().toString().trim();
            String cerita = etCerita.getText().toString().trim();
            String gambarUrl = edtGambarUrl.getText().toString().trim();
            String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            if (judul.isEmpty() || cerita.isEmpty() || gambarUrl.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tampilkan gambar ke ImageView sebagai preview (tidak diubah)
            imgThumbnail.setVisibility(View.VISIBLE);
            Glide.with(this).load(gambarUrl).into(imgThumbnail);

            // Ambil userId dari session
            SessionManager session = new SessionManager(this);
            String userId = session.getUserId();

            if (userId == null) {
                Toast.makeText(this, "User tidak ditemukan, silakan login kembali.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Siapkan data kegiatan
            Map<String, Object> kegiatan = new HashMap<>();
            kegiatan.put("judul", judul);
            kegiatan.put("cerita", cerita);
            kegiatan.put("tanggal", tanggal);
            kegiatan.put("thumbnailUrl", gambarUrl);
            kegiatan.put("userId", userId);

            // Simpan ke Firestore
            FirebaseFirestore.getInstance().collection("kegiatan")
                    .add(kegiatan)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Kegiatan berhasil disimpan!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gagal menyimpan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}