package com.android.roadzenassign.application;

import android.app.Application;

import com.android.roadzenassign.dagger2.component.ApplicationComponent;
import com.android.roadzenassign.dagger2.component.DaggerApplicationComponent;
import com.android.roadzenassign.dagger2.module.ApplicationModule;

public class CustomApplication extends Application {

  private ApplicationComponent component;

  private static CustomApplication mInstance;


  @Override
  public void onCreate() {
    super.onCreate();

    mInstance = this;

    component = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        .build();
  }

  public ApplicationComponent getComponent() {
    return component;
  }


  public static synchronized CustomApplication getInstance() {
    return mInstance;
  }
}
