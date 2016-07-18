package com.bhargavms.podslider;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;

class Pod {
    private int position = 0;
    private String centerText;
    private float podRadius = 0;
    private int podColor = 0;
    private int selectedPodColor = Color.WHITE;
    private int mainSliderColor = 0;
    private float cx = 0;
    private float cy = 0;
    private boolean selected = false;
    private static final float MAX_RADIUS_INCREMENT_FACTOR = 0.3f;
    private float radiusIncrementor = 0;
    private Handler handler;
    private Drawable mDrawable;

    private Paint podCirclePaint;
    private Paint selectedPodPaint;
    private Paint selectedPodTextPaint;
    private PodSlider parent;

    private PodSlider.DrawableSize mDrawableSize;

    public Pod(float radius, int color, int mainSliderColor, int selectedPodColor, PodSlider parent) {
        this.mainSliderColor = mainSliderColor;
        this.podRadius = radius;
        this.podColor = color;
        this.selectedPodColor = selectedPodColor;
        this.parent = parent;
        init();
    }

    public Pod(int mainSliderColor, int color,
               int selectedPodColor, PodSlider parent, int position) {
        this.mainSliderColor = mainSliderColor;
        this.podColor = color;
        this.selectedPodColor = selectedPodColor;
        this.parent = parent;
        this.position = position;
        this.centerText = String.valueOf(position + 1);
        init();
    }

    public Pod(int mainSliderColor, int color,
               int selectedPodColor, PodSlider parent, int position, String centerText) {
        this.mainSliderColor = mainSliderColor;
        this.podColor = color;
        this.selectedPodColor = selectedPodColor;
        this.parent = parent;
        this.position = position;
        this.centerText = centerText;
        init();
    }

    public void setCenter(float cx, float cy) {
        this.cx = cx;
        this.cy = cy;
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
    }

    public void setCenterDrawable(Drawable drawable, PodSlider.DrawableSize size) {
        this.mDrawable = drawable;
        mDrawableSize = size;
    }

    public int getPosition() {
        return position;
    }

    public float getCenterX() {
        return cx;
    }

    public float getCenterY() {
        return cy;
    }

    public void setPodRadius(float radius) {
        this.podRadius = radius;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private void init() {
        handler = new Handler();
        podCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        podCirclePaint.setColor(podColor);
        podCirclePaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        podCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        selectedPodPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPodPaint.setColor(selectedPodColor);
        selectedPodPaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        selectedPodPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        Paint mainSliderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainSliderPaint.setColor(mainSliderColor);
        mainSliderPaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        mainSliderPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        selectedPodTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        selectedPodTextPaint.setColor(mainSliderColor);
        selectedPodTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        selectedPodTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        selectedPodTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void drawPod(Canvas canvas) {
        if (!selected)
            canvas.drawCircle(cx, cy, podRadius, podCirclePaint);
        else {
            canvas.drawCircle(cx, cy, podRadius + (podRadius * radiusIncrementor), selectedPodPaint);
            float textSize = (podRadius + MAX_RADIUS_INCREMENT_FACTOR) * 2;
            selectedPodTextPaint.setTextSize(textSize);
            if (mDrawable == null) {
                canvas.drawText(centerText,
                        cx,
                        // http://stackoverflow.com/questions/11120392/android-center-text-on-canvas
                        cy - ((selectedPodTextPaint.descent() + selectedPodTextPaint.ascent()) / 2),
                        selectedPodTextPaint);
            } else {
                float diameter;
                switch (mDrawableSize) {
                    case FIT_LARGE_CIRCLE:
                        diameter = parent.largeCircleRadius * 2;
                        break;
                    case FIT_MEDIUM_CIRCLE:
                        diameter = parent.mediumCircleRadius * 2;
                        break;
                    case FIT_POD_CIRCLE:
                        diameter = (podRadius + MAX_RADIUS_INCREMENT_FACTOR) * 2;
                        break;
                    default:
                        diameter = (podRadius + MAX_RADIUS_INCREMENT_FACTOR) * 2;
                }
                float squareSideLengthBy2 = (getSquareSideLength(diameter) / 2);
                mDrawable.setBounds(
                        (int) (cx - squareSideLengthBy2),
                        (int) (cy - squareSideLengthBy2),
                        (int) (cx + squareSideLengthBy2),
                        (int) (cy + squareSideLengthBy2)
                );
                mDrawable.draw(canvas);
            }
        }
    }

    private float getSquareSideLength(float radius) {
        return (float) Math.sqrt(Math.pow(radius, 2) / 2);
    }

    public boolean doesCoOrdinatesLieInSelectRange(float x, float y) {
        float range = podRadius * (7 / 3);
        return x > cx - range && x < cx + range && y > cy - range && y < cy + range;
    }

    public boolean isSelected() {
        return selected;
    }

    public void animatePod() {
        if (!selected)
            return;
        radiusIncrementor = 0;
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (radiusIncrementor >= MAX_RADIUS_INCREMENT_FACTOR) {
//                    handler.removeCallbacks(this);
//                } else {
//                    float v = (MAX_RADIUS_INCREMENT_FACTOR - radiusIncrementor) /
//                            PodSlider.LARGE_CIRCLE_MOVE_TIME_IN_MS;
//                    radiusIncrementor += v * PodSlider.TIME_FOR_EACH_INCREMENT_IN_MS;
//                    handler.postDelayed(this, PodSlider.TIME_FOR_EACH_INCREMENT_IN_MS);
//                    parent.invalidate();
//                }
//            }
//        });

        ValueAnimator animator = ValueAnimator.ofFloat(radiusIncrementor, MAX_RADIUS_INCREMENT_FACTOR);
        animator.setDuration(PodSlider.ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radiusIncrementor = (float) animation.getAnimatedValue();
                int left = (int) (cx - podRadius);
                int right = (int) (cx + podRadius);
                int top = (int) (cy - podRadius);
                int bottom = (int) (cy + podRadius);
                parent.invalidate(left, top, right, bottom);
            }
        });
        animator.start();
    }
}
