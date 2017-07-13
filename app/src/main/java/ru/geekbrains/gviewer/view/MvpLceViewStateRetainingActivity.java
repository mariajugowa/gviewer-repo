package ru.geekbrains.gviewer.view;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class MvpLceViewStateRetainingActivity<CV extends View, M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
        extends MvpLceViewStateActivity<CV, M, V, P> {

  private M data;

  @Override
  protected void onCreate(@Nullable  Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override
  @NonNull
  public LceViewState<M, V> createViewState() {
    return new RetainingLceViewState<>();
  }

  @Override
  public M getData() {
    return data;
  }

  @Override
  public void setData(@NonNull M data) {
    this.data = data;
  }

}