package com.proyek.nusantara;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudayaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudayaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BudayaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BudayaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BudayaFragment newInstance(String param1, String param2) {
        BudayaFragment fragment = new BudayaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Data budaya (nama dan id LinearLayout)
    private static class BudayaItem {
        String nama;
        int layoutId;
        BudayaItem(String nama, int layoutId) {
            this.nama = nama;
            this.layoutId = layoutId;
        }
    }

    private List<BudayaItem> budayaList = new ArrayList<>();


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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_budaya, container, false);

        // Pindah menu profil
        ImageView imgProfile = view.findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(v -> {
            Log.d("BudayaFragment", "Profile diklik");
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });


        // Pindah Menu Provinsi Aceh
        LinearLayout layoutAceh = view.findViewById(R.id.layoutAceh);
        layoutAceh.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProvinsiAceh.class);
            startActivity(intent);
        });

        // Pindah Menu Provinsi Sumatera Utara
        LinearLayout layoutSumatraUtara = view.findViewById(R.id.layoutSumateraUtara);
        layoutSumatraUtara.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProvinsiSumatraUtara.class);
            startActivity(intent);
        });

        // Inisialisasi data budaya (isi sesuai id LinearLayout di XML)
        budayaList.add(new BudayaItem("ACEH", R.id.layoutAceh));
        budayaList.add(new BudayaItem("SUMATERA UTARA", R.id.layoutSumateraUtara));
        budayaList.add(new BudayaItem("SUMATERA SELATAN", R.id.layoutSumateraSelatan));
        budayaList.add(new BudayaItem("SUMATERA BARAT", R.id.layoutSumateraBarat));
        budayaList.add(new BudayaItem("BENGKULU", R.id.layoutBengkulu));
        budayaList.add(new BudayaItem("RIAU", R.id.layoutRiau));
        budayaList.add(new BudayaItem("KEPULAUAN RIAU", R.id.layoutKepulauanRiau));
        budayaList.add(new BudayaItem("JAMBI", R.id.layoutJambi));
        budayaList.add(new BudayaItem("LAMPUNG", R.id.layoutLampung));
        budayaList.add(new BudayaItem("BANGKA BELITUNG", R.id.layoutBangkaBelitung));
        budayaList.add(new BudayaItem("KALIMANTAN BARAT", R.id.layoutKalimantanBarat));
        budayaList.add(new BudayaItem("KALIMANTAN TIMUR", R.id.layoutKalimantanTmur));
        budayaList.add(new BudayaItem("KALIMANTAN SELATAN", R.id.layoutKalimantanSelatan));
        budayaList.add(new BudayaItem("KALIMANTAN TENGAH", R.id.layoutKalimantanTengah));
        budayaList.add(new BudayaItem("KALIMANTAN UTARA", R.id.layoutKalimantanUtara));
        budayaList.add(new BudayaItem("BANTEN", R.id.layoutBanten));
        budayaList.add(new BudayaItem("DKI JAKARTA", R.id.layoutDkiJakarta));
        budayaList.add(new BudayaItem("JAWA BARAT", R.id.layoutJawaBarat));
        budayaList.add(new BudayaItem("JAWA TENGAH", R.id.layoutJawaTengah));
        budayaList.add(new BudayaItem("DAERAH ISTIMEWA YOGYAKARTA", R.id.layoutYogyakarta));
        budayaList.add(new BudayaItem("JAWA TIMUR", R.id.layoutJawaTimur));
        budayaList.add(new BudayaItem("BALI", R.id.layoutBali));
        budayaList.add(new BudayaItem("NUSA TENGGARA TIMUR", R.id.layoutNusaTenggaraTimur));
        budayaList.add(new BudayaItem("NUSA TENGGARA BARAT", R.id.layoutNusaTenggaraBarat));
        budayaList.add(new BudayaItem("GORONTALO", R.id.layoutGorontalo));
        budayaList.add(new BudayaItem("SULAWESI BARAT", R.id.layoutSulawesiBarat));
        budayaList.add(new BudayaItem("SULAWESI TENGAH", R.id.layoutSulawesiTengah));
        budayaList.add(new BudayaItem("SULAWESI UTARA", R.id.layoutSulawesiUtara));
        budayaList.add(new BudayaItem("SULAWESI TENGGARA", R.id.layoutSulawesiTenggara));
        budayaList.add(new BudayaItem("SULAWESI SELATAN", R.id.layoutSulawesiSelatan));
        budayaList.add(new BudayaItem("MALUKU UTARA", R.id.layoutMalukuUtara));
        budayaList.add(new BudayaItem("MALUKU", R.id.layoutMaluku));
        budayaList.add(new BudayaItem("PAPUA BARAT", R.id.layoutPapuaBarat));
        budayaList.add(new BudayaItem("PAPUA", R.id.layoutPapua));
        budayaList.add(new BudayaItem("PAPUA TENGAH", R.id.layoutPapuaTengah));
        budayaList.add(new BudayaItem("PAPUA PEGUNUNGAN", R.id.layoutPapuaPegunungan));
        budayaList.add(new BudayaItem("PAPUA SELATAN", R.id.layoutPapuaSelatan));
        budayaList.add(new BudayaItem("PAPUA BARAT DAYA", R.id.layoutPapuaBaratDaya));

        EditText searchEditText = view.findViewById(R.id.searchEditText);
        GridLayout gridLayout = view.findViewById(R.id.gridLayoutBudaya);

        // Listener untuk tombol Enter
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAndSortBudaya(view, searchEditText.getText().toString().trim());
                return true;
            }
            return false;
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchAndSortBudaya(view, s.toString().trim());
            }
        });

        return view;
    }

    private void searchAndSortBudaya(View view, String query) {
        String lowerQuery = query.toLowerCase();
        List<BudayaItem> exactMatch = new ArrayList<>();
        List<BudayaItem> partialMatch = new ArrayList<>();

        for (BudayaItem item : budayaList) {
            String name = item.nama.toLowerCase();
            if (name.equals(lowerQuery)) {
                exactMatch.add(item);
            } else if (name.contains(lowerQuery) && !lowerQuery.isEmpty()) {
                partialMatch.add(item);
            }
        }

        // Gabungkan hasil
        List<BudayaItem> result = new ArrayList<>();
        result.addAll(exactMatch);
        result.addAll(partialMatch);

        // Sembunyikan semua
        for (BudayaItem item : budayaList) {
            LinearLayout layout = view.findViewById(item.layoutId);
            layout.setVisibility(View.GONE);
        }

        // Tampilkan dan pindahkan ke atas sesuai urutan result
        GridLayout grid = view.findViewById(R.id.gridLayoutBudaya);
        int childIndex = 0;
        for (BudayaItem item : result) {
            LinearLayout layout = view.findViewById(item.layoutId);
            layout.setVisibility(View.VISIBLE);
            grid.removeView(layout);
            grid.addView(layout, childIndex++);
        }

        // Jika query kosong, tampilkan semua sesuai urutan awal
        if (lowerQuery.isEmpty()) {
            for (BudayaItem item : budayaList) {
                LinearLayout layout = view.findViewById(item.layoutId);
                layout.setVisibility(View.VISIBLE);
                grid.removeView(layout);
                grid.addView(layout);
            }
        }
    }
}