package com.example.podslider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.text.TextPaint;

public class Pod {
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
    private float radiusIncrementFactor = 0;
    private Handler handler;

    private Paint podCirclePaint;
    private Paint selectedPodPaint;
    private Paint mainSliderPaint;
    private Paint selectedPodTextPaint;
    private PodSlider parent;

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
        this.centerText = String.valueOf(position);
        init();
    }

    public void setCenter(float cx, float cy) {
        this.cx = cx;
        this.cy = cy;
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

        mainSliderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainSliderPaint.setColor(mainSliderColor);
        mainSliderPaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        mainSliderPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        selectedPodTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        selectedPodTextPaint.setColor(mainSliderColor);
        selectedPodTextPaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        selectedPodTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        selectedPodTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    public void drawPod(Canvas canvas) {
        if (!selected)
            canvas.drawCircle(cx, cy, podRadius, podCirclePaint);
        else {
//            canvas.drawCircle(cx, cy, canvas.getHeight() / 2, mainSliderPaint);
//            canvas.drawCircle(cx, cy, canvas.getHeight() / 2.7f, podCirclePaint);
            canvas.drawCircle(cx, cy, podRadius + (podRadius * radiusIncrementFactor), selectedPodPaint);
            if (radiusIncrementFactor == MAX_RADIUS_INCREMENT_FACTOR) {
                canvas.drawText(centerText, cx, cy, selectedPodTextPaint);
            }
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void animatePod() {
        if (!selected)
            return;
        radiusIncrementFactor = 0;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (radiusIncrementFactor >= MAX_RADIUS_INCREMENT_FACTOR) {
                    handler.removeCallbacks(this);
                } else {
                    radiusIncrementFactor += MAX_RADIUS_INCREMENT_FACTOR / 50;
                    handler.postDelayed(this, 15);
                    parent.invalidate();
                }
            }
        });
    }
}
