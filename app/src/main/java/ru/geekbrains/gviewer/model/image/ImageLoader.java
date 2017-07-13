package ru.geekbrains.gviewer.model.image;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface ImageLoader<T> {

  void downloadInto(@Nullable String url, @NonNull T placeHolder);

}