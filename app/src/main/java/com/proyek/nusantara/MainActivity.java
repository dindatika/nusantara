package com.proyek.nusantara;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarItemView;
import com.google.android.material.navigation.NavigationBarView;
import com.proyek.nusantara.databinding.ActivityMainBinding;
import com.proyek.nusantara.R;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private ActivityMainBinding binding;
    private BottomNavigationView bottomNavigationView;
    private BerandaFragment berandaFragment = new BerandaFragment();
    private SejarahFragment sejarahFragment = new SejarahFragment();
    private BudayaFragment budayaFragment = new BudayaFragment();
    private KegiatanFragment kegiatanFragment = new KegiatanFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomview);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.beranda);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.beranda) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, berandaFragment).commit();
            return true;
        } else if (id == R.id.sejarah) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, sejarahFragment).commit();
            return true;
        } else if (id == R.id.budaya) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, budayaFragment).commit();
            return true;
        } else if (id == R.id.kegiatan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, kegiatanFragment).commit();
            return true;
        }
        return false;
    }
}