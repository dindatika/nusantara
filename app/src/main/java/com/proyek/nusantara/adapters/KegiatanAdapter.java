package com.proyek.nusantara.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.proyek.nusantara.Kegiatan;
import com.proyek.nusantara.R;

import java.util.ArrayList;
import java.util.List;

public class KegiatanAdapter extends RecyclerView.Adapter<KegiatanAdapter.ViewHolder> {
    private List<Kegiatan> kegiatanList;
    private Context context;

    public KegiatanAdapter(Context context) {
        this.context = context;
        this.kegiatanList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kegiatan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kegiatan kegiatan = kegiatanList.get(position);
        holder.tvJudul.setText(kegiatan.getJudul());
        holder.tvTanggal.setText(kegiatan.getTanggal());
        holder.tvCerita.setText(kegiatan.getCerita());
        Glide.with(context).load(kegiatan.getThumbnailUrl()).into(holder.imgThumbnail);
        holder.tvLihatDetail.setOnClickListener(v -> {
            Toast.makeText(context, "Klik: " + kegiatan.getJudul(), Toast.LENGTH_SHORT).show();
            // atau kirim intent ke DetailActivity
        });
    }

    @Override
    public int getItemCount() {
        return kegiatanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvCerita, tvTanggal;
        ImageView imgThumbnail;
        TextView tvLihatDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvCerita = itemView.findViewById(R.id.tvCerita);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            tvLihatDetail = itemView.findViewById(R.id.tvLihatDetail);
        }
    }

    public void submitList(List<Kegiatan> kegiatanList) {
        this.kegiatanList.clear();
        this.kegiatanList.addAll(kegiatanList);
        notifyDataSetChanged();
    }
}