package com.android.roadzenassign.model;

import com.android.roadzenassign.constants.ParamConstant;

import org.json.JSONObject;

public class QuestionModel {
  public String hint;
  public String type;
  public String comment;
  public ValidationModel validationModel;
  public String value;


  public QuestionModel(JSONObject jsonObject){
    hint = jsonObject.optString(ParamConstant.HINT);
    type = jsonObject.optString(ParamConstant.TYPE);
    comment = jsonObject.optString(ParamConstant.COMMENT);

    if (!jsonObject.isNull(ParamConstant.VALIDATION)){
      validationModel = new ValidationModel(jsonObject.optJSONObject(ParamConstant.VALIDATION));
    }
  }
}
