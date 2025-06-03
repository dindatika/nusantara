package com.proyek.nusantara;

import static androidx.core.app.ActivityCompat.startActivityForResult;

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
import android.widget.ProgressBar;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class TambahKegiatanActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etJudul, etCerita, etIsiCerita, edtGambarUrl;
    private Button btnSimpan, btnPilihGambar;
    private ImageView imgThumbnail;
    private ProgressBar progressBar;
    private Uri imageUri;
    private String base64Image;

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

        // Inisialisasi view
        etJudul = findViewById(R.id.etJudul);
        etCerita = findViewById(R.id.etCerita);
        etIsiCerita = findViewById(R.id.etIsiCerita);
        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        btnSimpan = findViewById(R.id.btnSimpan);
        imgThumbnail = findViewById(R.id.imgThumbnail);
        progressBar = findViewById(R.id.progressBar);

        // Tombol kembali
        FloatingActionButton fabback = findViewById(R.id.fabback);
        fabback.setOnClickListener(v -> finish());

        // Pilih gambar dari galeri
        btnPilihGambar.setOnClickListener(v -> openImageChooser());

        // Event klik tombol simpan
        btnSimpan.setOnClickListener(v -> {
            String judul = etJudul.getText().toString().trim();
            String cerita = etCerita.getText().toString().trim();
            String isiCerita = etIsiCerita.getText().toString().trim();
            String tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            // Validasi
            if (judul.isEmpty() || cerita.isEmpty() || base64Image == null) {
                Toast.makeText(this, "Semua field wajib diisi dan gambar harus dipilih!", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Validasi base64 image
            if (base64Image == null) {
                Toast.makeText(this, "Gambar terlalu besar atau belum dipilih!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tampilkan thumbnail sebagai preview
            imgThumbnail.setVisibility(View.VISIBLE);
            Glide.with(this).load(imageUri).into(imgThumbnail);

            // Ambil userId dari session (misal via SessionManager)
            SessionManager session = new SessionManager(this);
            String userId = session.getUserId();
            if (userId == null) {
                Toast.makeText(this, "User tidak ditemukan, silakan login kembali.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tampilkan loading
            progressBar.setVisibility(View.VISIBLE);
            btnSimpan.setEnabled(false);

            // Siapkan data untuk disimpan
            Map<String, Object> kegiatan = new HashMap<>();
            kegiatan.put("judul", judul);
            kegiatan.put("ceritaSingkat", cerita);
            kegiatan.put("isiCerita", isiCerita);
            kegiatan.put("tanggal", tanggal);
            kegiatan.put("thumbnailBase64", base64Image);
            kegiatan.put("userId", userId);

            // Simpan ke Firestore
            FirebaseFirestore.getInstance()
                    .collection("kegiatan")
                    .add(kegiatan)
                    .addOnSuccessListener(docRef -> {
                        // Sembunyikan loading, tampilkan toast, lalu tutup activity
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Kegiatan berhasil disimpan!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // Sembunyikan loading, aktifkan kembali tombol, tampilkan error
                        progressBar.setVisibility(View.GONE);
                        btnSimpan.setEnabled(true);
                        Toast.makeText(this, "Gagal menyimpan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            imageUri = data.getData();
            imgThumbnail.setImageURI(imageUri);
            imgThumbnail.setVisibility(View.VISIBLE);
            convertImageToBase64(imageUri);
        }
    }

    private void convertImageToBase64(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);

            // Resize gambar jika lebih besar dari 800px
            int maxSize = 800;
            int width = originalBitmap.getWidth();
            int height = originalBitmap.getHeight();
            float scale = Math.min((float) maxSize / width, (float) maxSize / height);
            int scaledWidth = Math.round(scale * width);
            int scaledHeight = Math.round(scale * height);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, true);

            // Kompres gambar ke JPEG 70%
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] imageBytes = baos.toByteArray();

            // Cek ukuran hasil kompresi
            double sizeInKB = imageBytes.length / 1024.0;
            if (sizeInKB > 900) { // Aman di bawah 1 MB batas dokumen Firestore
                Toast.makeText(this, "Ukuran gambar terlalu besar: " + String.format("%.2f", sizeInKB) + " KB", Toast.LENGTH_LONG).show();
                base64Image = null;
                return;
            }

            // Konversi ke Base64
            base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            Toast.makeText(this, "Gambar berhasil dipilih. Ukuran: " + String.format("%.2f", sizeInKB) + " KB", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal mengonversi gambar", Toast.LENGTH_SHORT).show();
            base64Image = null;
        }
    }
}