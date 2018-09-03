package com.android.roadzenassign.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public abstract class BaseActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentLayout());
    initComponents();
    initListner();
    initModel();
    callObserver();
  }

  public abstract int getContentLayout();

  public abstract void initComponents();

  public abstract void initListner();
  public abstract void initModel();

  public abstract void callObserver();

}

