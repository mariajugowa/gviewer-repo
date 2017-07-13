package ru.geekbrains.gviewer.presenter;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import ru.geekbrains.gviewer.view.InfoView;

import android.support.annotation.AnyThread;

public interface InfoPresenter extends MvpPresenter<InfoView> {

  @AnyThread
  void loadInformation(boolean pullToRefresh);

}