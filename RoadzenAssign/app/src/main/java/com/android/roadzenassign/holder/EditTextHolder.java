package com.android.roadzenassign.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.android.roadzenassign.R;

public class EditTextHolder extends RecyclerView.ViewHolder {
  public EditText editText;

  public EditTextHolder(@NonNull View itemView) {
    super(itemView);
    editText = itemView.findViewById(R.id.iet_text);
  }

}
