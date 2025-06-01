package com.proyek.nusantara;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.proyek.nusantara.adapters.KegiatanAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KegiatanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KegiatanFragment extends Fragment {

    // Menambahkan kegiatan
    private FloatingActionButton fabTambahKegiatan;
    private EditText etPencarian;

    private RecyclerView recyclerView;
    private KegiatanAdapter adapter;
    private List<Kegiatan> list = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public KegiatanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KegiatanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KegiatanFragment newInstance(String param1, String param2) {
        KegiatanFragment fragment = new KegiatanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kegiatan, container, false);

        // Profile click
        ImageView imgProfile = view.findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(v -> {
            Log.d("KegiatanFragment", "Profile diklik");
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        // Pencarian
        etPencarian = view.findViewById(R.id.searchEditText);
        etPencarian.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int aft) {}
            @Override public void onTextChanged(CharSequence s, int st, int bef, int cnt) {}
            @Override public void afterTextChanged(Editable s) {
                performSearch(s.toString());
            }
        });

        etPencarian.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(v.getText().toString());
                return true;
            }
            return false;
        });

        // Inisialisasi FAB
        fabTambahKegiatan = view.findViewById(R.id.fabTambahKegiatan);
        fabTambahKegiatan.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TambahKegiatanActivity.class);
            startActivity(intent);
        });


        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewKegiatan);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new KegiatanAdapter(getContext());
        recyclerView.setAdapter(adapter);

        // Ambil data dari Firestore
        ambilDataKegiatan();

        return view;
    }

    private void performSearch(String query) {
        String lower = query.toLowerCase();
        List<Kegiatan> filteredList = new ArrayList<>();
        for (Kegiatan k : list) {
            if (k.getJudul().toLowerCase().contains(lower)
                    || k.getCeritaSingkat().toLowerCase().contains(lower)) {
                filteredList.add(k);
            }
        }
        adapter.submitList(filteredList);
    }

    private void ambilDataKegiatan() {
        db.collection("kegiatan")
                .orderBy("tanggal", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    list.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Kegiatan kegiatan = doc.toObject(Kegiatan.class);
                        list.add(kegiatan);
                    }
                    // Kirim list penuh ke adapter
                    adapter.submitList(list);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        // refresh data setiap fragment terlihat
        ambilDataKegiatan();
    }
}