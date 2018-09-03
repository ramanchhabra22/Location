package com.android.roadzenassign.model;

import android.util.Log;

import com.android.roadzenassign.application.CustomApplication;
import com.android.roadzenassign.dagger2.component.DaggerBasicResourcesComponent;
import com.android.roadzenassign.utils.JSONReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ScreenPageHandler {

  private HandlerCallBack mCallBack;

  @Inject
  JSONReader jsonReader;



  @Inject
  public ScreenPageHandler() {
    DaggerBasicResourcesComponent.builder().applicationComponent(CustomApplication.getInstance().getComponent()).build().inject(this);
  }

  public void setHandlerCallBack(HandlerCallBack handlerCallBack) {
    this.mCallBack = handlerCallBack;
  }


  public void getScreenData(String jsonFileName){
    Single.just(parseData(jsonReader.readJSONData(jsonFileName)))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(getSingleObserver());
  }

  private SingleObserver<ScreenModel> getSingleObserver() {
    return new SingleObserver<ScreenModel>() {
      @Override
      public void onSubscribe(Disposable d) {
      }

      @Override
      public void onSuccess(ScreenModel screenModel) {
        mCallBack.onSuccess(screenModel);
      }

      @Override
      public void onError(Throwable e) {

      }
    };
  }

  private ScreenModel parseData(String value){
    ScreenModel model = null;
    try {
      JSONArray array = new JSONArray(value);
      int len = array.length();
      if (len > 0){
        JSONObject object = array.optJSONObject(0);
        model = new ScreenModel(object);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return model;
  }


}
