package com.proyek.nusantara;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BerandaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BerandaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BerandaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BerandaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BerandaFragment newInstance(String param1, String param2) {
        BerandaFragment fragment = new BerandaFragment();
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
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        ImageView imgProfile = view.findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(v -> {
            Log.d("BerandaFragment", "Profile diklik");
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });

        Button btnTontonVideo = view.findViewById(R.id.btnTontonVideo);
        btnTontonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoUrl = "https://youtu.be/PLESqcjC1yM?si=GUxYrpP-RM2077i6";
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                appIntent.setPackage("com.google.android.youtube");

                if (appIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivity(appIntent);
                } else {
                    // Buka lewat browser kalau aplikasi YouTube tidak tersedia
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(videoUrl));
                    startActivity(webIntent);
                }
            }
        });

        return view;
    }
}