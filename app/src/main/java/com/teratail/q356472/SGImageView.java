package com.teratail.q356472;

import android.content.Context;
import android.graphics.*;
import android.util.*;
import android.view.*;

public class SGImageView extends androidx.appcompat.widget.AppCompatImageView {
  private static final String TAG = "SGImageView";

  public SGImageView(Context context) {
    this(context, null, 0);
  }
  public SGImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }
  public SGImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    setScaleType(ScaleType.MATRIX);

    ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    GestureDetector gestureDetector = new GestureDetector(context, new MoveListener());

    setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent ev) {
        boolean r = scaleGestureDetector.onTouchEvent(ev);
        return gestureDetector.onTouchEvent(ev) || r || SGImageView.super.onTouchEvent(ev);
      }
    });
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    //Log.d(TAG, "onLayout");
    super.onLayout(changed, left, top, right, bottom);

    if (getWidth() == 0 || getHeight() == 0 || getDrawable() == null) return;

    init();
  }

  private void init() {
    //Log.d(TAG, "init");
    float viewWidth = getWidth();
    float viewHeight = getHeight();
    float imageWidth = getDrawable().getIntrinsicWidth();
    float imageHeight = getDrawable().getIntrinsicHeight();

    //初期表示比率・座標計算
    float scaleWidth = viewWidth / imageWidth;
    float scaleHeight = viewHeight / imageHeight;
    float activeScale, activeX, activeY;
    if(scaleWidth < scaleHeight) {
      activeScale = scaleWidth;
      activeX = 0f;
      activeY = (viewHeight - (imageHeight * activeScale)) /2f;
    } else {
      activeScale = scaleHeight;
      activeX = (viewWidth - (imageWidth * activeScale)) /2f;
      activeY = 0f;
    }

    Matrix matrix = new Matrix();
    matrix.setScale(activeScale, activeScale, activeX, activeY);
    setImageMatrix(matrix);
  }

  private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
      //Log.d(TAG,"onScale");
      Matrix matrix = getImageMatrix();
      float scale = detector.getScaleFactor();
      matrix.postScale(scale, scale, detector.getFocusX(), detector.getFocusY());
      invalidate();

      return true;
    }
  }

  private class MoveListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onDoubleTap(MotionEvent e) {
      //Log.d(TAG,"onDoubleTap");
      init();
      return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
      //Log.d(TAG,"onScroll");
      Matrix matrix = getImageMatrix();
      matrix.postTranslate(-distanceX, -distanceY);
      invalidate();

      return true;
    }
  }
}