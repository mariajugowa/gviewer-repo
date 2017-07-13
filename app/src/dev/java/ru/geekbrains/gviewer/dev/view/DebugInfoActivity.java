package ru.geekbrains.gviewer.dev.view;

import ru.geekbrains.gviewer.dev.di.DebugInfoModelModule;
import ru.geekbrains.gviewer.di.DaggerInfoComponent;
import ru.geekbrains.gviewer.view.InfoActivity;

public class DebugInfoActivity extends InfoActivity {

  @Override
  protected DaggerInfoComponent.Builder createComponentBuilder() {
    return super.createComponentBuilder().infoModelModule(new DebugInfoModelModule());
  }

}