package com.example.podslider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Pod extends View {
    private int podRadius = 0;
    private int podColor = 0;
    private float cx = 0;
    private float cy = 0;

    private Paint podCirclePaint;

    public Pod(Context context, int radius, int color) {
        super(context);
        this.podRadius = radius;
        this.podColor = color;
        init();
    }

    public void setCenter(float cx, float cy) {
        this.cx = cx;
        this.cy = cy;
    }

    private void init() {
        podCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        podCirclePaint.setColor(podColor);
        podCirclePaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        podCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(cx, cy, podRadius, podCirclePaint);
    }
}
