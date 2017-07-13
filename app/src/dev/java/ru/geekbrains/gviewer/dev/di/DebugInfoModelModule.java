package ru.geekbrains.gviewer.dev.di;

import ru.geekbrains.gviewer.di.InfoModelModule;

public class DebugInfoModelModule extends InfoModelModule {
  @Override
  public String provideUsername() {
    return "octocat";
  }
}