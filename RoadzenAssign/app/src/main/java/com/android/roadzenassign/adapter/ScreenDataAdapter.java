package com.android.roadzenassign.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.roadzenassign.R;
import com.android.roadzenassign.activity.ScreenActivity;
import com.android.roadzenassign.constants.TypeConstant;
import com.android.roadzenassign.holder.EditTextHolder;
import com.android.roadzenassign.holder.ImageViewHolder;
import com.android.roadzenassign.model.QuestionModel;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class ScreenDataAdapter extends RecyclerView.Adapter {

  private ArrayList<QuestionModel> questionModelList;
  private Context context;


  public ScreenDataAdapter(Context context, ArrayList<QuestionModel> questionModelList) {
    this.context = context;
    this.questionModelList = questionModelList;
  }


  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    View view;
    switch (viewType) {
      case TypeConstant.TEXT:
      case TypeConstant.TEXT_NUMERIC:
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_edit_text, viewGroup, false);
        return new EditTextHolder(view);
      case TypeConstant.IMAGE:
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_image_view, viewGroup, false);
        return new ImageViewHolder(view);
      default:
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_edit_text, viewGroup, false);
        return new EditTextHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    int viewType = getItemViewType(i);
    final QuestionModel questionModel = questionModelList.get(i);

    switch (viewType) {
      case TypeConstant.TEXT:
        final EditTextHolder textHolder = (EditTextHolder) viewHolder;
        textHolder.editText.setHint(questionModel.hint);
        textHolder.editText.setInputType(InputType.TYPE_CLASS_TEXT);

        if (questionModel.validationModel != null) {
          textHolder.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
              if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                String check = questionModel.validationModel.check;
                String value = textHolder.editText.getText().toString();
                if (check.equals(TypeConstant.CHECK_EMAL)) {
                  if (!((ScreenActivity)context).isValidEmail(value)) {
                    Toast.makeText(context, String.format(context.getResources().getString(R.string.enter_correct), questionModel.hint), Toast.LENGTH_SHORT).show();
                    return false;
                  }
                }

                return true;
              }
              return false;
            }
          });

          textHolder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
              if (!b) {
                String check = questionModel.validationModel.check;
                String value = textHolder.editText.getText().toString();
                if (check.equals(TypeConstant.CHECK_EMAL)) {
                  if (!((ScreenActivity)context).isValidEmail(value)) {
                    Toast.makeText(context, String.format(context.getResources().getString(R.string.enter_correct),questionModel.hint), Toast.LENGTH_SHORT).show();
                  }
                }

              }
            }
          });
        }
        setTextChangeListener(textHolder.editText, questionModel);
        setValue(textHolder.editText,questionModel);
        break;

      case TypeConstant.IMAGE:
        final ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
        imageViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            ((ScreenActivity) context).captureImage(imageViewHolder.imageView);
          }
        });

        break;

      case TypeConstant.TEXT_NUMERIC:
        final EditTextHolder textNumericHolder = (EditTextHolder) viewHolder;
        textNumericHolder.editText.setHint(questionModel.hint);
        textNumericHolder.editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (questionModel.validationModel != null) {
          textNumericHolder.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
              if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                int size = questionModel.validationModel.size;
                String value = textNumericHolder.editText.getText().toString();
                int len = value.length();
                if (len < size) {
                  Toast.makeText(context, String.format(context.getResources().getString(R.string.enter_correct),questionModel.hint), Toast.LENGTH_SHORT).show();
                  return false;
                }
                return true;
              }
              return false;
            }
          });

          textNumericHolder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
              if (!b) {
                int size = questionModel.validationModel.size;
                String value = textNumericHolder.editText.getText().toString();
                int len = value.length();
                if (len < size) {
                  Toast.makeText(context, String.format(context.getResources().getString(R.string.enter_correct),questionModel.hint), Toast.LENGTH_SHORT).show();
                }
              }
            }
          });
        }
        setTextChangeListener(textNumericHolder.editText, questionModel);
        setValue(textNumericHolder.editText,questionModel);
        break;
    }



  }

  private void setValue(EditText editText, QuestionModel questionModel) {
    if (questionModel.hint.equals("Customer Address")){
      ((ScreenActivity)context).setCustomerAddressView(editText);
    }else if(questionModel.hint.equals("ZipCode")){
      ((ScreenActivity)context).setZipCodeView(editText);
    }
  }

  private void setTextChangeListener(EditText editText, final QuestionModel questionModel) {
    editText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void afterTextChanged(Editable editable) {
        questionModel.value = editable.toString();
      }
    });
  }

  @Override
  public int getItemCount() {
    return questionModelList.size();
  }

  @Override
  public int getItemViewType(int position) {
    QuestionModel questionModel = questionModelList.get(position);
    switch (questionModel.type) {
      case TypeConstant.TYPE_TEXT:
        return TypeConstant.TEXT;
      case TypeConstant.TYPE_TEXT_NUMERIC:
        return TypeConstant.TEXT_NUMERIC;
      case TypeConstant.TYPE_IMAGE:
        return TypeConstant.IMAGE;
      default:

        return TypeConstant.TEXT;
    }
  }


}
