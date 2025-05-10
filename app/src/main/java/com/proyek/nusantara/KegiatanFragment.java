package com.proyek.nusantara;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
    FloatingActionButton fabTambahKegiatan;
    EditText etPencarian;

    RecyclerView recyclerView;
    KegiatanAdapter adapter;
    List<Kegiatan> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        ImageView imgProfile = view.findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(v -> {
            Log.d("KegiatanFragment", "Profile diklik");
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        etPencarian = view.findViewById(R.id.searchEditText);
        etPencarian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Kegiatan> filteredList = new ArrayList<>();
                for (Kegiatan kegiatan : list) {
                    if (kegiatan.getJudul().toLowerCase().contains(s.toString().toLowerCase()) || kegiatan.getCerita().toLowerCase().contains(s.toString().toLowerCase())) {
                        filteredList.add(kegiatan);
                    }
                }
                adapter.submitList(filteredList);
            }
        });

        // Inisialisasi FAB
        fabTambahKegiatan = view.findViewById(R.id.fabTambahKegiatan);
        fabTambahKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahKegiatanActivity.class);
                startActivity(intent);
            }
        });


        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewKegiatan);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new KegiatanAdapter(getContext());
        recyclerView.setAdapter(adapter);

        // Ambil data dari Firestore
        ambilDataKegiatan();

        return view;
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
                    adapter.submitList(list);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        ambilDataKegiatan(); // refresh data setiap fragment terlihat
    }
}