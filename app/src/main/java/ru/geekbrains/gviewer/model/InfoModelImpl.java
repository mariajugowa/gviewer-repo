package ru.geekbrains.gviewer.model;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import ru.geekbrains.gviewer.di.Username;
import ru.geekbrains.gviewer.model.api.GithubService;
import ru.geekbrains.gviewer.model.entity.GithubUser;
import rx.Observable;
import rx.Scheduler;

import android.support.annotation.*;
import android.util.Log;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

public class InfoModelImpl implements InfoModel {

  @NonNull
  private final GithubService restApi;
  @NonNull
  private final String user;
  @NonNull
  private final RealmConfiguration configuration;
  @NonNull
  private final Scheduler scheduler;
  @Nullable
  private Realm realm;

  @Inject
  public InfoModelImpl(@NonNull @Username String user,
                       @NonNull RealmConfiguration configuration,
                       @NonNull Scheduler scheduler,
                       @NonNull GithubService restApi) {
    this.user = user;
    this.configuration = configuration;
    this.scheduler = scheduler;
    this.restApi = restApi;
  }

  @AnyThread
  @NonNull
  @CheckResult
  @Override
  public Observable<? extends List<GithubUser>> updateInfo() {
    return restApi.getUser(user)
                  .observeOn(scheduler)
                  .doOnNext(user -> {
                    if (!checkRealmIsValid()) {
                      throw new IllegalStateException("You should subscribe on #lifecycle() first");
                    }
                    realm.executeTransactionAsync(r -> {
                      GithubUser realmObject = r.where(GithubUser.class).findFirst();
                      if (realmObject == null) {
                        r.copyToRealmOrUpdate(user);
                      } else {
                        String userAvatarUrl = user.getAvatarUrl();
                        if (userAvatarUrl != null && !userAvatarUrl.equals(realmObject.getAvatarUrl())) {
                          realmObject.setAvatarUrl(userAvatarUrl);
                        }
                        String userName = user.getName();
                        if (userName != null && !userName.equals(realmObject.getName())) {
                          realmObject.setName(userName);
                        }
                        String bio = user.getBio();
                        if (bio != null && !bio.equals(realmObject.getBio())) {
                          realmObject.setBio(bio);
                        }
                        String location = user.getLocation();
                        if (location != null && !location.equals(realmObject.getLocation())) {
                          realmObject.setLocation(location);
                        }
                        String company = user.getCompany();
                        if (company != null && !company.equals(realmObject.getCompany())) {
                          realmObject.setCompany(company);
                        }
                        String email = user.getEmail();
                        if (email != null && !email.equals(realmObject.getEmail())) {
                          realmObject.setEmail(email);
                        }
                        String blogUrl = user.getBlogUrl();
                        if (blogUrl != null && !blogUrl.equals(realmObject.getBlogUrl())) {
                          realmObject.setBlogUrl(blogUrl);
                        }
                      }
                    });
                  }).map(Collections::singletonList);
  }

  @AnyThread
  @NonNull
  @CheckResult
  @Override
  public Observable<? extends List<GithubUser>> lifecycle() {
    return Observable.defer(() -> {
      if (checkRealmIsValid()) {
        throw new IllegalStateException("You can't subscribe on lifecycle twice");
      }
      realm = Realm.getInstance(configuration);
      return observeInfo();
    }).doOnUnsubscribe(() -> {
      if (!checkRealmIsValid()) {
        Log.w("InfoModelImpl", "Something maybe goes wrong, realm is already closed");
      }
      realm.close();
      realm = null;
    }).subscribeOn(scheduler);
  }

  @AnyThread
  @NonNull
  @CheckResult
  @Override
  public Observable<? extends List<GithubUser>> observeInfo() {
    return Observable.defer(() -> {
      if (!checkRealmIsValid()) {
        throw new IllegalStateException("You should subscribe on #lifecycle() first");
      }
      return realm.where(GithubUser.class)
                  .findAllAsync()
                  .<RealmResults<GithubUser>>asObservable()
                  .filter(RealmResults::isLoaded);
    }).subscribeOn(scheduler);
  }

  private boolean checkRealmIsValid() {
    return realm != null && !realm.isClosed();
  }

}