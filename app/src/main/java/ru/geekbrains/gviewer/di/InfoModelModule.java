package ru.geekbrains.gviewer.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.geekbrains.gviewer.model.InfoModel;
import ru.geekbrains.gviewer.model.InfoModelImpl;
import ru.geekbrains.gviewer.model.api.GithubService;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class InfoModelModule {

  @Provides
  public RealmConfiguration provideRealmConfiguration() {
    return new RealmConfiguration.Builder().build();
  }

  @Provides
  public Scheduler provideScheduler() {
    return AndroidSchedulers.mainThread();
  }

  @Provides
  @Endpoint
  public String provideEndpoint() {
    return "https://api.github.com/";
  }

  @Provides
  public GithubService provideRestApi(@Endpoint String endpoint, OkHttpClient client, Gson gson) {
    return new Retrofit.Builder().baseUrl(endpoint)
                                 .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                                 .addConverterFactory(GsonConverterFactory.create(gson))
                                 .client(client)
                                 .build()
                                 .create(GithubService.class);
  }

  @Provides
  public OkHttpClient provideClient() {
    return new OkHttpClient.Builder().build();
  }

  @Provides
  public Gson provideGson() {
    return new GsonBuilder().create();
  }

  @Provides
  @Username
  public String provideUsername() {
    return "nationalsecurityagency";
  }

  @Provides
  public InfoModel provideModel(@Username String user, RealmConfiguration configuration,
                                Scheduler scheduler, GithubService restApi) {
    return new InfoModelImpl(user, configuration, scheduler, restApi);
  }

}