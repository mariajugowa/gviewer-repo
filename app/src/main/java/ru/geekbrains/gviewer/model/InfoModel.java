package ru.geekbrains.gviewer.model;

import ru.geekbrains.gviewer.model.entity.GithubUser;
import rx.Observable;

import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;

import java.util.List;

public interface InfoModel {

  @NonNull
  @AnyThread
  Observable<? extends List<GithubUser>> lifecycle();

  @NonNull
  @AnyThread
  Observable<? extends List<GithubUser>> updateInfo();

  @NonNull
  @AnyThread
  Observable<? extends List<GithubUser>> observeInfo();

}