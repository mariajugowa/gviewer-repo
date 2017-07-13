package ru.geekbrains.gviewer.di;

import dagger.Module;
import dagger.Provides;
import ru.geekbrains.gviewer.model.InfoModel;
import ru.geekbrains.gviewer.presenter.InfoPresenter;
import ru.geekbrains.gviewer.presenter.InfoPresenterImpl;

@Module
public class InfoPresenterModule {

  @Provides
  public InfoPresenter providePresenter(InfoModel model) {
    return new InfoPresenterImpl(model);
  }

}