package com.android.roadzenassign.model;

import com.android.roadzenassign.constants.ParamConstant;

import org.json.JSONObject;

public class ValidationModel {
  public int size;
  public String check;

  public ValidationModel(JSONObject jsonObject){
    size = jsonObject.optInt(ParamConstant.SIZE);
    check = jsonObject.optString(ParamConstant.CHECK);
  }
}
