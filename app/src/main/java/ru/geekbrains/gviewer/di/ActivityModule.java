package ru.geekbrains.gviewer.di;

import dagger.Module;
import dagger.Provides;

import android.content.Context;

@Module
public class ActivityModule {

  private final Context context;

  public ActivityModule(Context context) {
    this.context = context;
  }

  @Provides
  public Context provideContext() {
    return context;
  }

}