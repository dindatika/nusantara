package com.proyek.nusantara;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.proyek.nusantara.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseFirestore db;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        session = new SessionManager(this);

        setupAction();
    }

    private void setupAction() {
        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "Email atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else {
                checkAkun(email, password);
            }
        });
    }

    private void checkAkun(String email, String password) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        boolean isPasswordCorrect = false;
                        String userId = null, userName = null, userToken = null;

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String storedPassword = doc.getString("password");
                            if (storedPassword != null && storedPassword.equals(password)) {
                                isPasswordCorrect = true;
                                userId    = doc.getId();
                                userName  = doc.getString("name");   // asumsikan ada field "name"
                                userToken = doc.getString("token");  // atau token jika ada
                                break;
                            }
                        }

                        if (isPasswordCorrect) {
                            // Simpan session
                            session.createSession(userId, userName, userToken);

                            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish(); // agar user tidak bisa kembali ke Login
                        } else {
                            Toast.makeText(this, "Password Salah", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Akun Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Gagal Login: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}