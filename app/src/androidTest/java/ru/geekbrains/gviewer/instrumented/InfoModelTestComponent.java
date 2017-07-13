package ru.geekbrains.gviewer.instrumented;

import dagger.Component;
import ru.geekbrains.gviewer.di.InfoModelModule;

@Component(modules = InfoModelModule.class)
public interface InfoModelTestComponent {

  void inject(InfoModelImplTest test);

}