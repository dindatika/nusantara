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

public class ProfileKegiatanAdapter extends RecyclerView.Adapter<ProfileKegiatanAdapter.ViewHolder> {
    public interface OnProfileActionListener {
        void onViewDetail(Kegiatan item);
        void onEdit(Kegiatan item);
        void onDelete(Kegiatan item);
    }
    private final List<Kegiatan> kegiatanList;
    private final Context context;
    private final OnProfileActionListener actionListener;

    public ProfileKegiatanAdapter(Context context, OnProfileActionListener actionListener) {
        this.context = context;
        this.actionListener = actionListener;
        this.kegiatanList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kegiatan_profil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kegiatan kegiatan = kegiatanList.get(position);

        holder.tvJudul.setText(kegiatan.getJudul());
        holder.tvTanggal.setText(kegiatan.getTanggal());
        holder.tvCerita.setText(kegiatan.getCerita());

        // Load thumbnail if available
        if (kegiatan.getThumbnailUrl() != null && !kegiatan.getThumbnailUrl().isEmpty()) {
            Glide.with(context).load(kegiatan.getThumbnailUrl()).into(holder.imgThumbnail);
        } else {
            holder.imgThumbnail.setImageResource(R.drawable.background); // Placeholder image
        }

        // Set click listeners for actions
        holder.tvLihatDetail.setOnClickListener(v -> actionListener.onViewDetail(kegiatan));
        holder.tvEdit.setOnClickListener(v -> actionListener.onEdit(kegiatan));
        holder.tvHapus.setOnClickListener(v -> actionListener.onDelete(kegiatan));
    }

    @Override
    public int getItemCount() {
        return kegiatanList.size();
    }

    public void submitList(List<Kegiatan> newKegiatanList) {
        kegiatanList.clear();
        kegiatanList.addAll(newKegiatanList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvTanggal, tvCerita;
        TextView tvLihatDetail, tvEdit, tvHapus;
        ImageView imgThumbnail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvCerita = itemView.findViewById(R.id.tvCerita);
            tvLihatDetail = itemView.findViewById(R.id.tvLihatDetail);
            tvEdit = itemView.findViewById(R.id.tvEdit);
            tvHapus = itemView.findViewById(R.id.tvHapus);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
        }
    }
}