package com.proyek.nusantara;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.proyek.nusantara.adapters.ProfileKegiatanAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvNama, tvEmail;
    private Button btnLogout;
    private FloatingActionButton fabback;
    private RecyclerView rvKegiatan;
    private ProfileKegiatanAdapter adapter;
    private FirebaseAuth mAuth;
    private SessionManager session;
    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ProfileActivity", "onCreate dijalankan");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        session = new SessionManager(this);
        mAuth = FirebaseAuth.getInstance();

        // Cek session
        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
            return;
        }

        tvNama = findViewById(R.id.tvNama);
        tvEmail = findViewById(R.id.tvEmail);

        // Ambil currentUid dari session
        currentUid = session.getUserId();

        // Ambil data pengguna dari Firestore
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nama = documentSnapshot.getString("nama");
                        String email = documentSnapshot.getString("email");

                        tvNama.setText("Nama: " + (nama != null ? nama : "Tidak tersedia"));
                        tvEmail.setText("Email: " + (email != null ? email : "Tidak tersedia"));
                    } else {
                        tvNama.setText("Nama: Tidak tersedia");
                        tvEmail.setText("Email: Tidak tersedia");
                    }
                })
                .addOnFailureListener(e -> {
                    tvNama.setText("Nama: Tidak tersedia");
                    tvEmail.setText("Email: Tidak tersedia");
                });

        rvKegiatan = findViewById(R.id.rvKegiatanProfil);

        // Set up RecyclerView
        rvKegiatan.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProfileKegiatanAdapter(this, new ProfileKegiatanAdapter.OnProfileActionListener() {
            @Override
            public void onViewDetail(Kegiatan item) {
                Intent i = new Intent(ProfileActivity.this, DetailActivity.class);
                i.putExtra("postId", item.getId());
                startActivity(i);
            }

            @Override
            public void onEdit(Kegiatan item) {
                Intent i = new Intent(ProfileActivity.this, EditKegiatanActivity.class);
                i.putExtra("postId", item.getId());
                startActivity(i);
            }

            @Override
            public void onDelete(Kegiatan item) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Hapus Postingan")
                        .setMessage("Yakin ingin menghapus “" + item.getJudul() + "”?")
                        .setPositiveButton("Hapus", (d, w) -> deletePost(item.getId()))
                        .setNegativeButton("Batal", null)
                        .show();
            }
        });
        rvKegiatan.setAdapter(adapter);

        // Load user posts
        loadUserPosts();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // Back FAB
        fabback = findViewById(R.id.fabback);
        fabback.setOnClickListener(v -> finish());

        // Logout button
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> confirmLogout());
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya", (d, w) -> {
                    session.logout(); // Clear session
                    mAuth.signOut(); // Sign out from Firebase
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Tidak", null)
                .setCancelable(false)
                .show();
    }

    private void loadUserPosts() {
        Log.d("ProfileActivity", "Loading posts for userId: " + currentUid);

        FirebaseFirestore.getInstance()
                .collection("kegiatan")
                .whereEqualTo("userId", currentUid)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Kegiatan> kegiatanList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Log.d("ProfileActivity", "Doc found: " + doc.getId());
                        Kegiatan kegiatan = doc.toObject(Kegiatan.class);
                        kegiatan.setId(doc.getId());
                        kegiatanList.add(kegiatan);
                    }

                    Log.d("ProfileActivity", "Total kegiatan ditemukan: " + kegiatanList.size());
                    adapter.submitList(kegiatanList);
                })
                .addOnFailureListener(e -> {
                    Log.e("ProfileActivity", "Gagal memuat kegiatan", e);
                    Toast.makeText(this, "Gagal memuat kegiatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Akan mengambil ulang data dari Firestore
    @Override
    protected void onResume() {
        super.onResume();
        loadUserPosts();
    }

    private void deletePost(String postId) {
        FirebaseFirestore.getInstance()
                .collection("kegiatan")
                .document(postId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Kegiatan dihapus", Toast.LENGTH_SHORT).show();
                    loadUserPosts(); // Refresh data
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal menghapus: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}