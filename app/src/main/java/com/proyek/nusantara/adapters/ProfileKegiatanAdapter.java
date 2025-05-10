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
    private List<Kegiatan> list;
    private Context ctx;
    private OnProfileActionListener callback;

    public ProfileKegiatanAdapter(Context ctx, OnProfileActionListener callback) {
        this.ctx = ctx;
        this.callback = callback;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.item_kegiatan_profil, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kegiatan k = list.get(position);
        holder.tvJudul.setText(k.getJudul());
        holder.tvTanggal.setText(k.getTanggal());
        holder.tvCerita.setText(k.getCerita());
        Glide.with(ctx).load(k.getThumbnailUrl()).into(holder.imgThumbnail);

        holder.tvLihatDetail.setOnClickListener(v -> callback.onViewDetail(k));
        holder.tvEdit.setOnClickListener(v -> callback.onEdit(k));
        holder.tvHapus.setOnClickListener(v -> callback.onDelete(k));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void submitList(List<Kegiatan> data) {
        list.clear();
        list.addAll(data);
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