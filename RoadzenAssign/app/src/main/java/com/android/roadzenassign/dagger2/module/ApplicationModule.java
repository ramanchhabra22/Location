package com.android.roadzenassign.dagger2.module;

import android.app.Application;
import android.content.Context;

import com.android.roadzenassign.dagger2.scope.ApplicationContext;

import dagger.Module;
import dagger.Provides;


@Module
public class ApplicationModule {

  private final Application mApplication;

  public ApplicationModule(Application app) {
    mApplication = app;
  }

  @Provides
  @ApplicationContext
  Context provideContext() {
    return mApplication;
  }
}
