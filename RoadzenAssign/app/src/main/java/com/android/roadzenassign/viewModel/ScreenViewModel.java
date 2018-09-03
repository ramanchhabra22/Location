package com.android.roadzenassign.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.android.roadzenassign.application.CustomApplication;
import com.android.roadzenassign.dagger2.component.DaggerBasicResourcesComponent;
import com.android.roadzenassign.model.HandlerCallBack;
import com.android.roadzenassign.model.ScreenModel;
import com.android.roadzenassign.model.ScreenPageHandler;

import javax.inject.Inject;

public class ScreenViewModel extends ViewModel implements HandlerCallBack{


  MutableLiveData<ScreenModel> screenModelData;

  @Inject
  ScreenPageHandler screenPageHandler;

  @Inject
  public ScreenViewModel() {
    DaggerBasicResourcesComponent.builder().applicationComponent(CustomApplication.getInstance().getComponent()).build().inject(this);
    screenPageHandler.setHandlerCallBack(this);
  }


  public MutableLiveData<ScreenModel> getScreenModel(String jsonFileName) {
    if (screenModelData == null){
      screenModelData = new MutableLiveData<>();
      screenPageHandler.getScreenData(jsonFileName);
    }
    return screenModelData;
  }

  @Override
  public void onSuccess(Object object) {
    if (screenModelData != null){
      screenModelData.setValue((ScreenModel) object);
    }

  }

  public void setScreenModelData(MutableLiveData<ScreenModel> screenModelData) {
    this.screenModelData = screenModelData;
  }

  @Override
  public void onFailure(String message) {

  }
}
