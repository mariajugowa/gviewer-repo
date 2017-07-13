package ru.geekbrains.gviewer.model.api;

import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.geekbrains.gviewer.model.entity.GithubUser;
import rx.Observable;

public interface GithubService {

  @GET("users/{user}")
  Observable<GithubUser> getUser(@Path("user") String user);

}