package ru.geekbrains.gviewer.di;

import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import ru.geekbrains.gviewer.model.image.ImageLoader;
import ru.geekbrains.gviewer.model.image.android.PicassoImageLoader;

import android.content.Context;
import android.widget.ImageView;

@Module
public class InfoViewModule {

  @Provides
  public ImageLoader<ImageView> providesAvatarLoader(Context context) {
    return new PicassoImageLoader(Picasso.with(context));
  }

}