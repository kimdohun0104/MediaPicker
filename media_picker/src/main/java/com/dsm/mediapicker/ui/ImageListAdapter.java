package com.dsm.mediapicker.ui;

import android.net.Uri;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dsm.mediapicker.R;
import com.dsm.mediapicker.listener.ImageListClickListener;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder> {

    private List<Uri> images = new ArrayList<>();
    private SparseBooleanArray selectedPositions = new SparseBooleanArray();
    private int maxCount;
    private ImageListClickListener listClickListener;

    public void setImages(List<Uri> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    protected ImageListAdapter(int maxCount, ImageListClickListener listClickListener) {
        this.maxCount = maxCount;
        this.listClickListener = listClickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(holder.itemView).load(images.get(position)).into(holder.ivImage);

        if (selectedPositions.get(position)) holder.llCheck.setVisibility(View.VISIBLE);
        else holder.llCheck.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (selectedPositions.get(position)) {
                selectedPositions.put(position, false);
                holder.llCheck.setVisibility(View.GONE);
                listClickListener.onImageClicked(getSelectedItemCount());
            } else {
                if (maxCount == 1) {
                    int index = selectedPositions.indexOfValue(true);
                    if (index != -1) {
                        int originalPosition = selectedPositions.keyAt(index);
                        selectedPositions.put(originalPosition, false);
                        notifyItemChanged(originalPosition);
                    }
                } else if (maxCount == getSelectedItemCount()) return;

                selectedPositions.put(position, true);
                listClickListener.onImageClicked(getSelectedItemCount());
                holder.llCheck.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public List<Uri> getSelectedUris() {
        List<Uri> selected = new ArrayList<>();
        for (int i = 0; i < selectedPositions.size(); i++) {
            if (selectedPositions.valueAt(i))
                selected.add(images.get(selectedPositions.keyAt(i)));
        }
        return selected;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private LinearLayout llCheck;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            llCheck = itemView.findViewById(R.id.ll_check);
        }
    }

    private int getSelectedItemCount() {
        int count = 0;
        for (int i = 0; i < selectedPositions.size(); i++) {
            if (selectedPositions.valueAt(i)) count++;
        }
        return count;
    }
}
