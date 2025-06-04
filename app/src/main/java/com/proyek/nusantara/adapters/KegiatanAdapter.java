package com.proyek.nusantara.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.proyek.nusantara.DetailActivity;
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

        // Set teks: judul, tanggal, cerita singkat
        holder.tvJudul.setText(kegiatan.getJudul());
        holder.tvTanggal.setText(kegiatan.getTanggal());
        holder.tvCerita.setText(kegiatan.getCeritaSingkat());

        // Decode Base64 menjadi Bitmap, lalu set ke ImageView
        String base64 = kegiatan.getThumbnailBase64();
        if (base64 != null && !base64.isEmpty()) {
            byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            holder.imgThumbnail.setImageBitmap(bitmap);
        } else {
            // Jika tidak ada Base64, gunakan placeholder
            holder.imgThumbnail.setImageResource(R.drawable.ic_nusantara);
        }

        // Ketika diklik "Lihat Detail", kirim semua data, termasuk base64
        holder.tvLihatDetail.setOnClickListener(v -> {
            // Kirim data ke DetailActivity
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("judul", kegiatan.getJudul());
            intent.putExtra("tanggal", kegiatan.getTanggal());
            intent.putExtra("ceritaSingkat", kegiatan.getCeritaSingkat());
            intent.putExtra("thumbnailBase64", kegiatan.getThumbnailBase64());
            intent.putExtra("isiCerita", kegiatan.getIsiCerita());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return kegiatanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvCerita, tvTanggal, tvLihatDetail;
        ImageView imgThumbnail;

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