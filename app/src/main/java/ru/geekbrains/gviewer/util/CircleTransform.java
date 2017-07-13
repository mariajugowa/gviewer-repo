package ru.geekbrains.gviewer.util;

import com.squareup.picasso.Transformation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import static android.graphics.BitmapShader.*;

/**
 * https://gist.github.com/julianshen/5829333
 * Created by julian on 13/6/21.
 */
public class CircleTransform implements Transformation {

  @Override
  public Bitmap transform(Bitmap source) {
    int size = Math.min(source.getWidth(), source.getHeight());

    Bitmap squaredBitmap = Bitmap.createBitmap(source,
            (source.getWidth() - size) / 2, (source.getHeight() - size) / 2, size, size);
    if (!squaredBitmap.equals(source)) {
      source.recycle();
    }

    Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    BitmapShader shader = new BitmapShader(squaredBitmap, TileMode.CLAMP, TileMode.CLAMP);
    paint.setShader(shader);
    paint.setAntiAlias(true);

    float radius = size / 2f;
    canvas.drawCircle(radius, radius, radius, paint);

    squaredBitmap.recycle();
    return bitmap;
  }

  @Override
  public String key() {
    return "circle";
  }
}