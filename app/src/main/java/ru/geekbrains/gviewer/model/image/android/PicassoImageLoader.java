package ru.geekbrains.gviewer.model.image.android;

import com.squareup.picasso.Picasso;
import ru.geekbrains.gviewer.R;
import ru.geekbrains.gviewer.model.image.ImageLoader;
import ru.geekbrains.gviewer.util.CircleTransform;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

public class PicassoImageLoader implements ImageLoader<ImageView> {

  @NonNull
  private final Picasso picasso;

  public PicassoImageLoader(@NonNull Picasso picasso) {
    this.picasso = picasso;
  }

  @Override
  public void downloadInto(@Nullable String url, @NonNull ImageView placeHolder) {
    picasso.load(url)
           .placeholder(R.drawable.avatar_circle_grey_64dp)
           .transform(new CircleTransform())
           .into(placeHolder);
  }

}