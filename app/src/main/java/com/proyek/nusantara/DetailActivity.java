package com.proyek.nusantara;

import android.os.Bundle;
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
        String thumbnailUrl = getIntent().getStringExtra("thumbnailUrl");
        String isiCerita = getIntent().getStringExtra("isiCerita");

        // Tampilkan data di UI
        tvJudul.setText(judul);
        tvTanggal.setText(tanggal);
        tvCeritaSingkat.setText(ceritaSingkat);
        tvIsiCerita.setText(isiCerita);

        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            Glide.with(this).load(thumbnailUrl).into(ivThumbnail);
        } else {
            ivThumbnail.setImageResource(R.drawable.background);
        }

        // Tombol kembali
        findViewById(R.id.fabBackDetail).setOnClickListener(v -> finish());
    }
}