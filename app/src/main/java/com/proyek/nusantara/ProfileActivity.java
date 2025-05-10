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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // jika tidak ada user, langsung ke login
            finish();
            return;
        }
        currentUid = user.getUid();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        // back FAB
        fabback = findViewById(R.id.fabback);
        fabback.setOnClickListener(v -> finish());

        // logout button
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> confirmLogout());

        // header info
        tvNama  = findViewById(R.id.tvNama);
        tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText("Email: " + user.getEmail());
        // ambil nama di DB
        FirebaseDatabase.getInstance()
                .getReference("Users").child(currentUid).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        String name = snap.getValue(String.class);
                        tvNama.setText("Nama: " + (name!=null?name:"-"));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError err) {
                        tvNama.setText("Nama: -");
                    }
                });

        // RecyclerView & Adapter
        rvKegiatan = findViewById(R.id.rvKegiatanProfil);
        rvKegiatan.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProfileKegiatanAdapter(this, new ProfileKegiatanAdapter.OnProfileActionListener() {
            @Override
            public void onViewDetail(Kegiatan item) {
                // TODO: buka DetailActivity, kirim item ID
                Intent i = new Intent(ProfileActivity.this, DetailActivity.class);
                i.putExtra("postId", item.getId());
                startActivity(i);
            }
            @Override
            public void onEdit(Kegiatan item) {
                // TODO: buka EditActivity atau dialog
                Intent i = new Intent(ProfileActivity.this, EditKegiatanActivity.class);
                i.putExtra("postId", item.getId());
                startActivity(i);
            }

            @Override
            public void onDelete(Kegiatan item) {
                // konfirmasi penghapusan
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Hapus Kegiatan")
                        .setMessage("Yakin ingin menghapus “" + item.getJudul() + "”?")
                        .setPositiveButton("Hapus", (d,w) -> deletePost(item.getId()))
                        .setNegativeButton("Batal", null)
                        .show();
            }
        });
        rvKegiatan.setAdapter(adapter);

        // load data user posts
        loadUserPosts();
    }
    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya", (d,w) -> {
                    session.logout();
                    mAuth.signOut();
                    startActivity(new Intent(this, LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                })
                .setNegativeButton("Tidak", null)
                .setCancelable(false)
                .show();
    }

    private void loadUserPosts() {
        DatabaseReference postRef = FirebaseDatabase.getInstance()
                .getReference("Posts");
        postRef.orderByChild("userId").equalTo(currentUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        List<Kegiatan> list = new ArrayList<>();
                        for (DataSnapshot child : snap.getChildren()) {
                            Kegiatan k = child.getValue(Kegiatan.class);
                            if (k != null) {
                                k.setId(child.getKey());
                                list.add(k);
                            }
                        }
                        adapter.submitList(list);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError err) {
                        Toast.makeText(ProfileActivity.this,
                                "Gagal memuat kegiatan", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deletePost(String postId) {
        FirebaseDatabase.getInstance()
                .getReference("Posts")
                .child(postId)
                .removeValue((err, ref) -> {
                    if (err == null) {
                        Toast.makeText(this, "Kegiatan dihapus", Toast.LENGTH_SHORT).show();
                        loadUserPosts(); // refresh
                    } else {
                        Toast.makeText(this,
                                "Gagal menghapus: " + err.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}