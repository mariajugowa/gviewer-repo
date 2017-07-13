package ru.geekbrains.gviewer.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import ru.geekbrains.gviewer.model.InfoModel;
import ru.geekbrains.gviewer.view.InfoView;
import rx.Subscription;

import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import java.util.List;

public class InfoPresenterImpl extends MvpBasePresenter<InfoView> implements InfoPresenter {

  @NonNull
  private final InfoModel model;
  @Nullable
  private Subscription observing;
  @Nullable
  private Subscription updating;

  public InfoPresenterImpl(@NonNull InfoModel model) {
    this.model = model;
  }

  @Override
  public void attachView(@NonNull InfoView attached) {
    super.attachView(attached);
    if (!isSubscribed(observing)) {
      observing = model.lifecycle()
                       .filter(list -> !list.isEmpty())
                       .map(list -> list.get(0))
                       .subscribe(s -> {
                         InfoView view = getView();
                         if (isViewAttached()) {
                           view.setData(s);
                           view.showContent();
                         }
                       });
    }
  }

  @Override
  public void detachView(boolean retainInstance) {
    super.detachView(retainInstance);
    if (!retainInstance) {
      tryToUnsubscribe(observing);
      tryToUnsubscribe(updating);
    }
  }

  @Override
  @AnyThread
  public void loadInformation(boolean pullToRefresh) {
    tryToUnsubscribe(updating);
    updating = model.observeInfo()
                    .map(List::isEmpty)
                    .doOnNext(isEmpty -> getView().showLoading(!isEmpty))
                    .zipWith(model.updateInfo().isEmpty().onErrorReturn(t -> true), Pair::create)
                    .subscribe(pair -> {
                      boolean isViewEmpty = pair.first;
                      boolean isErrorCaused = pair.second;
                      if (isErrorCaused && isViewAttached()) {
                        getView().showError(new Throwable("Error during network operation"), !isViewEmpty);
                      }
                    });
  }

  private void tryToUnsubscribe(@Nullable Subscription subscription) {
    if (isSubscribed(subscription)) {
      subscription.unsubscribe();
    }
  }

  private boolean isSubscribed(@Nullable Subscription subscription) {
    return subscription != null && !subscription.isUnsubscribed();
  }

}