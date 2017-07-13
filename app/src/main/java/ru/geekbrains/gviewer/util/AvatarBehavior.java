package ru.geekbrains.gviewer.util;

import ru.geekbrains.gviewer.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Based on https://github.com/saulmm/CoordinatorBehaviorExample
 * */
@SuppressWarnings("unused")
public class AvatarBehavior extends CoordinatorLayout.Behavior<ImageView> {

  private float customFinalHeight;

  private int startXPosition;
  private int startYPosition;
  private int finalXPosition;
  private int finalYPosition;
  private int startHeight;
  private float changeBehaviorPoint;

  // todo implement state saving

  public AvatarBehavior(Context context, AttributeSet attrs) {
    if (attrs != null) {
      TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageBehavior);
      customFinalHeight = array.getDimension(R.styleable.AvatarImageBehavior_finalHeight, 0);
      array.recycle();
    }
  }

  @Override
  public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
    return dependency instanceof Toolbar;
  }

  @Override
  public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
    // todo sometimes still works incorrectly
    tryInitProperties(child, dependency);
    float expandedPercentageFactor = dependency.getY() / startYPosition;
    float yOffset = dependency.getHeight() / 2f * expandedPercentageFactor;
    if (expandedPercentageFactor < changeBehaviorPoint) {
      float heightFactor = (changeBehaviorPoint - expandedPercentageFactor) / changeBehaviorPoint;

      float childHalfHeight = child.getHeight() / 2f;
      float distanceXToSubtract = (startXPosition - finalXPosition) * heightFactor + childHalfHeight;
      float distanceYToSubtract = (startYPosition - finalYPosition) * (1f - expandedPercentageFactor) + childHalfHeight;

      child.setX(startXPosition - distanceXToSubtract);
      child.setY(startYPosition + yOffset - distanceYToSubtract);

      float heightToSubtract = (startHeight - customFinalHeight) * heightFactor;

      CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
      lp.width = (int) (startHeight - heightToSubtract);
      lp.height = (int) (startHeight - heightToSubtract);
      child.setLayoutParams(lp);
    } else {
      float distanceYToSubtract = (startYPosition - finalYPosition) * (1f - expandedPercentageFactor) + startHeight / 2f;

      child.setX(startXPosition - child.getWidth() / 2);
      child.setY(startYPosition + yOffset - distanceYToSubtract);

      CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
      lp.width = startHeight;
      lp.height = startHeight;
      child.setLayoutParams(lp);
    }
    return true;
  }

  private void tryInitProperties(ImageView child, View dependency) {
    if (startXPosition == 0) {
      startXPosition = (int) (child.getX() + child.getWidth() / 2.0);
    }
    if (startYPosition == 0) {
      startYPosition = (int) dependency.getY();
    }
    if (finalXPosition == 0) {
      finalXPosition = child.getResources()
                            .getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + ((int) customFinalHeight / 2);
    }
    if (finalYPosition == 0) {
      finalYPosition = dependency.getHeight() / 2;
    }
    if (startHeight == 0) {
      startHeight = child.getHeight();
    }
    if (changeBehaviorPoint == 0 && startYPosition != 0 && finalYPosition != 0) {
      changeBehaviorPoint = (child.getHeight() - customFinalHeight) / (2f * (startYPosition - finalYPosition));
    }
  }
}