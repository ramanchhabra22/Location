package com.android.roadzenassign.model;

import com.android.roadzenassign.constants.ParamConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

public class ScreenModel {
  public ArrayList<QuestionModel> questionList;
  public String header;

  public ScreenModel(JSONObject jsonObject) {
    header = jsonObject.optString(ParamConstant.HEADER);
    if (!jsonObject.isNull(ParamConstant.QUESTIONS)) {
      JSONArray array = jsonObject.optJSONArray(ParamConstant.QUESTIONS);
      int len = array.length();
      questionList = new ArrayList<>(len);
      for (int i = 0; i < len; i++) {
        JSONObject questionObj = array.optJSONObject(i);
        QuestionModel questionModel = new QuestionModel(questionObj);
        questionList.add(questionModel);
      }
    }
  }
}
