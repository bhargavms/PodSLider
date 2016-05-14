package com.example.podslider;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * A view that looks like this http://codepen.io/chrisgannon/pen/mPoMxq
 */
public class PodSlider extends View {
    private int numberOfPods;
    private Paint mainPaint;
    private Pod[] pods;

    public PodSlider(Context context) {
        super(context);
    }

    public PodSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PodSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public PodSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context c, AttributeSet attrs) {
        TypedArray a = c.getTheme().obtainStyledAttributes(attrs, R.styleable.PodSlider, 0, 0);
        try {
            int mainSliderColor = a.getColor(R.styleable.PodSlider_mainSliderColor, 0);
            int numberOfPods = a.getInt(R.styleable.PodSlider_numberOfPods, 1);
            int podColor = a.getColor(R.styleable.PodSlider_podColor, 0);
            init(numberOfPods, podColor, mainSliderColor);
        } finally {
            a.recycle();
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        this.mainPaint.setColor(color);
        invalidate();
    }

    public void setNumberOfPods(int numberOfPods) {
        this.numberOfPods = numberOfPods;
    }

    private void init(int numberOfPods, int podColor, int mainSliderColor) {
        this.numberOfPods = numberOfPods;
        pods = new Pod[numberOfPods];
        mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainPaint.setColor(mainSliderColor);
        mainPaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        mainPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        for (int i = 0; i < numberOfPods; i++) {
            pods[i] = new Pod(podColor);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float left = 0;
        float top = 0;
        float width = getWidth();
        float height = getHeight();

        drawRoundedRect(canvas, left, top, width, height);

        if (numberOfPods == 1) {
            // draw one at the center and be done.
            float centerX = width / 2;
            float centerY = height / 2;
            Pod pod = pods[0];
            pod.setCenter(centerX, centerY);
            pod.setPodRadius(height/6);
            pod.draw(canvas);
            return;
        }
        // else you start calculation.
        float podCenterY = height / 2;
        float startX = height / 2;

        float gapBetweenPodCenters = calculateGapBetweenPodCenters();
        for (int i = 0, n = numberOfPods; i < n; i++) {
            float podCenterX = startX + i * gapBetweenPodCenters;
            Pod pod = pods[i];
            pod.setPodRadius(height/6);
            pod.setCenter(podCenterX, podCenterY);
            pod.draw(canvas);
        }
    }

    private float calculateGapBetweenPodCenters() {
        // The center of leftmost circle is at getHeight() / 2, getHeight() / 2 (by design)
        // The center of rightmost circle is at getWidth() - getHeight /2, getHeight / 2 (by design)
        // So the distance between these 2 points is the difference is their x-axis co ordinates
        // as both have the same y-axis coordinate.
        // Which is nothing but (getWidth() - getHeight / 2) - getHeight / 2
        // which equal to getWidth() - 2 * getHeight / 2
        // which is equal to getWidth() - getHeight()
        float distanceBetweenTheCentersOfPodsAtTheEnd = getWidth() - getHeight();
        // Now to determine the distance between the center of each pod
        // I divide the distanceBetweenTheCentersOfPodsAtTheEnd by number of Pods
        return distanceBetweenTheCentersOfPodsAtTheEnd / (numberOfPods - 1);
    }

    private void drawRoundedRect(Canvas canvas, float left, float top, float right, float bottom) {
        float radius = getHeight() / 2;
        canvas.drawCircle(radius, radius, radius, mainPaint);
        canvas.drawCircle(right - radius, radius, radius, mainPaint);
        canvas.drawRect(left + radius, top, right - radius, bottom, mainPaint);
    }
}
