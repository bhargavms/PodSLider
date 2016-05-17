package com.example.podslider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Pod {
    private float podRadius = 0;
    private int podColor = 0;
    private float cx = 0;
    private float cy = 0;
    private boolean selected = false;

    private Paint podCirclePaint;

    public Pod(float radius, int color) {
        this.podRadius = radius;
        this.podColor = color;
        init();
    }

    public Pod(int color) {
        this.podColor = color;
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
        podCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        podCirclePaint.setColor(podColor);
        podCirclePaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        podCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(cx, cy, podRadius, podCirclePaint);
    }
}
