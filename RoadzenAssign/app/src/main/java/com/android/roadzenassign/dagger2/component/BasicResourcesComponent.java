package com.android.roadzenassign.dagger2.component;

import com.android.roadzenassign.dagger2.scope.PerActivity;
import com.android.roadzenassign.model.ScreenPageHandler;
import com.android.roadzenassign.viewModel.ScreenViewModel;

import dagger.Component;


@PerActivity
@Component(dependencies = ApplicationComponent.class)
public interface BasicResourcesComponent {


  void inject(ScreenViewModel screenViewModel);

  void inject(ScreenPageHandler screenPageHandler);

}
