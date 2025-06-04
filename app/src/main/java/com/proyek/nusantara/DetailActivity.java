package com.proyek.nusantara;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ambil elemen UI
        ImageView ivThumbnail = findViewById(R.id.ivThumbnail);
        TextView tvJudul = findViewById(R.id.tvJudul);
        TextView tvTanggal = findViewById(R.id.tvTanggal);
        TextView tvCeritaSingkat = findViewById(R.id.tvCeritaSingkat);
        TextView tvIsiCerita = findViewById(R.id.tvIsiCerita);

        // Ambil data dari Intent
        String judul = getIntent().getStringExtra("judul");
        String tanggal = getIntent().getStringExtra("tanggal");
        String ceritaSingkat = getIntent().getStringExtra("ceritaSingkat");
        String thumbnailBase64   = getIntent().getStringExtra("thumbnailBase64");
        String isiCerita = getIntent().getStringExtra("isiCerita");

        // Tampilkan data di UI
        tvJudul.setText(judul);
        tvTanggal.setText(tanggal);
        tvCeritaSingkat.setText(ceritaSingkat);
        tvIsiCerita.setText(isiCerita);

        // Decode Base64 menjadi Bitmap, lalu set ke ImageView
        if (thumbnailBase64 != null && !thumbnailBase64.isEmpty()) {
            byte[] decodedBytes = Base64.decode(thumbnailBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            ivThumbnail.setImageBitmap(bitmap);
        } else {
            // Jika tidak ada Base64, tampilkan placeholder
            ivThumbnail.setImageResource(R.drawable.background);
        }

        // Tombol kembali
        findViewById(R.id.fabBackDetail).setOnClickListener(v -> finish());
    }
}