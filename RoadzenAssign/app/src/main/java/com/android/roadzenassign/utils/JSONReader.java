package com.android.roadzenassign.utils;

import android.app.Activity;

import com.android.roadzenassign.application.CustomApplication;
import com.android.roadzenassign.dagger2.scope.ApplicationContext;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

public class JSONReader {

  @Inject
  public JSONReader(){

  }


  public String readJSONData(String name){
    String json = null;
    try {
      InputStream is = CustomApplication.getInstance().getAssets().open(name);
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      json = new String(buffer, "UTF-8");
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    return json;
  }
}
