package ru.geekbrains.gviewer.di;

import dagger.Component;
import ru.geekbrains.gviewer.presenter.InfoPresenter;
import ru.geekbrains.gviewer.view.InfoActivity;

@Component(modules = { InfoModelModule.class, InfoViewModule.class, InfoPresenterModule.class, ActivityModule.class, })
public interface InfoComponent {

  void inject(InfoActivity activity);

  InfoPresenter presenter();

}