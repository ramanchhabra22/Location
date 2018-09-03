
package com.android.roadzenassign.dagger2.component;


import android.content.Context;


import com.android.roadzenassign.application.CustomApplication;
import com.android.roadzenassign.dagger2.module.ApplicationModule;
import com.android.roadzenassign.dagger2.scope.ApplicationContext;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
  @ApplicationContext
  Context getApplicationContext();

  void inject(CustomApplication app);
}