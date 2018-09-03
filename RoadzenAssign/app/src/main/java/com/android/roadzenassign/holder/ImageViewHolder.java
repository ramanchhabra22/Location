package com.android.roadzenassign.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.roadzenassign.R;

public class ImageViewHolder extends RecyclerView.ViewHolder{
  public ImageView imageView;
  public ImageViewHolder(@NonNull View itemView) {
    super(itemView);
    imageView = itemView.findViewById(R.id.ifv_image_view);
  }
}
