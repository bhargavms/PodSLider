package com.bhargavms.podslider;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * A view that looks like this http://codepen.io/chrisgannon/pen/mPoMxq
 */
public class PodSlider extends View {
    private int numberOfPods;
    private Paint mainPaint;
    private Paint podPaint;
    private Pod[] pods;
    private OnPodClickListener mPodClickListener;
    private int currentlySelectedPod = 0;
    private Handler mainHandler;
    private boolean firstDraw = true;
    private ViewPager mViewPager;
    private boolean isViewMeasured = false;

    private float largeAndSmallCircleCurrentCenterX;
    private float largeAndSmallCircleDestCenterX;

    private Rect clipBounds;

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

    public void setPodClickListener(OnPodClickListener listener) {
        this.mPodClickListener = listener;
    }

    private void init(Context c, AttributeSet attrs) {
        TypedArray a = c.getTheme().obtainStyledAttributes(attrs, R.styleable.PodSlider, 0, 0);
        try {
            int mainSliderColor = a.getColor(R.styleable.PodSlider_mainSliderColor, 0);
            int numberOfPods = a.getInt(R.styleable.PodSlider_numberOfPods, 1);
            int podColor = a.getColor(R.styleable.PodSlider_podColor, 0);
            int selectedPodColor = a.getColor(R.styleable.PodSlider_selectedPodColor, Color.WHITE);
            init(numberOfPods, podColor, mainSliderColor, selectedPodColor);
        } finally {
            a.recycle();
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        mainSliderColor = color;
        this.mainPaint.setColor(color);
        invalidate();
    }

    public void setNumberOfPods(int numberOfPods) {
        // please don't provide invalid values :)
        if (numberOfPods <= 0) {
            numberOfPods = 1;
        }
        this.numberOfPods = numberOfPods;
        pods = new Pod[numberOfPods];
        for (int i = 0; i < numberOfPods; i++) {
            pods[i] = new Pod(mainSliderColor, podColor, selectedPodColor, this, i);
        }
        if (!isViewMeasured) {
            return;
        }
        requestLayout();
        post(new Runnable() {
            @Override
            public void run() {
                if (currentlySelectedPod < PodSlider.this.numberOfPods) {
                    setCurrentlySelectedPod(currentlySelectedPod);
                } else {
                    setCurrentlySelectedPod(0);
                }
            }
        });
    }

    private int mainSliderColor;
    private int podColor;
    private int selectedPodColor;

    private void init(int numberOfPods, int podColor, int mainSliderColor, int selectedPodColor) {
        mainHandler = new Handler();
        this.mainSliderColor = mainSliderColor;
        this.podColor = podColor;
        this.selectedPodColor = selectedPodColor;
        clipBounds = new Rect();

        mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainPaint.setColor(mainSliderColor);
        mainPaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        mainPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        podPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        podPaint.setColor(podColor);
        podPaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        podPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        setNumberOfPods(numberOfPods);
        setCurrentlySelectedPod(0);
    }

    public void setCurrentlySelectedPod(int currentlySelectedPod) {
        this.currentlySelectedPod = currentlySelectedPod;
        for (int i = 0; i < numberOfPods; i++) {
            pods[i].setSelected(false);
        }
        pods[currentlySelectedPod].setSelected(true);
        update(pods[currentlySelectedPod].getCenterX());
    }

    private float startX;
    private float startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                float endX = event.getX();
                float endY = event.getY();
                if (isAClick(startX, endX, startY, endY)) {
                    onClick(endX, endY);
                }
                return true;
            default:
                return false;
        }
    }

    private void onClick(float x, float y) {
        for (int i = 0; i < pods.length; i++) {
            Pod pod = pods[i];
            if (pod.doesCoOrdinatesLieInSelectRange(x, y)) {
                setCurrentlySelectedPod(i);
                // propagate click.
                if (mPodClickListener != null)
                    this.mPodClickListener.onPodClick(i);
                if (mViewPager != null)
                    mViewPager.setCurrentItem(i);
                return;
            }
        }
    }

    private void update(float toX) {
        largeAndSmallCircleDestCenterX = toX;
        // animate the pod
        pods[currentlySelectedPod].animatePod();
        // animate the outer circles
        if (largeAndSmallCircleCurrentCenterX == largeAndSmallCircleDestCenterX) {
            return;
        }
        if (largeAndSmallCircleCurrentCenterX > largeAndSmallCircleDestCenterX) {
            // current greater
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (largeAndSmallCircleCurrentCenterX <= largeAndSmallCircleDestCenterX) {
                                mainHandler.removeCallbacks(this);
                            } else {
                                float v = (largeAndSmallCircleCurrentCenterX -
                                        largeAndSmallCircleDestCenterX) /
                                        LARGE_CIRCLE_MOVE_TIME_IN_MS;
                                largeAndSmallCircleCurrentCenterX -= v * TIME_FOR_EACH_INCREMENT_IN_MS;
                                invalidate();
                                mainHandler.postDelayed(this, TIME_FOR_EACH_INCREMENT_IN_MS);
                            }
                        }
                    });
                }
            });
        } else {
            // current is lesser
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (largeAndSmallCircleCurrentCenterX >= largeAndSmallCircleDestCenterX) {
                        mainHandler.removeCallbacks(this);
                    } else {
                        float v = (largeAndSmallCircleDestCenterX -
                                largeAndSmallCircleCurrentCenterX) /
                                LARGE_CIRCLE_MOVE_TIME_IN_MS;
                        largeAndSmallCircleCurrentCenterX += v * TIME_FOR_EACH_INCREMENT_IN_MS;
                        invalidate();
                        mainHandler.postDelayed(this, TIME_FOR_EACH_INCREMENT_IN_MS);
                    }
                }
            });
        }
    }

    public void setUpWithViewPager(ViewPager pager) {
        mViewPager = pager;
        final PagerAdapter adapter = pager.getAdapter();
        if (adapter == null) {
            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
        }
        setNumberOfPods(adapter.getCount());
        setCurrentlySelectedPod(currentlySelectedPod);
        pager.addOnPageChangeListener(new PodSliderOnPageChangeListener(this));
        if (adapter.getCount() > 0) {
            final int curItem = pager.getCurrentItem();
            if (currentlySelectedPod != curItem) {
                setCurrentlySelectedPod(curItem);
            }
        }
    }


    public static final int LARGE_CIRCLE_MOVE_TIME_IN_MS = 100;
    public static final int TIME_FOR_EACH_INCREMENT_IN_MS = 18;

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        if (differenceX > 5f || differenceY > 5f) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(clipBounds);
        // make large circle diameter equal to the height of the canvas.
        float largeCircleRadius = Math.min((getHeight() - getPaddingTop() - getPaddingBottom()) / 2,
                (getWidth() - getPaddingRight() - getPaddingLeft()) / 2);
        float mediumCircleRadius = largeCircleRadius / 1.5f;
        float podRadius = largeCircleRadius * 2 / 7;
        float rectangleRight = clipBounds.right - getPaddingRight() - (largeCircleRadius / 5);
        float rectangleLeft = clipBounds.left + getPaddingLeft() + (largeCircleRadius / 5);
        float rectangleTop = getPaddingTop() + clipBounds.top + (largeCircleRadius / 5);
        float rectangleBottom = clipBounds.bottom - getPaddingBottom() - (largeCircleRadius / 5);

        drawRoundedRect(canvas, rectangleLeft, rectangleTop, rectangleRight, rectangleBottom);
        float podCenterY = rectangleTop + ((rectangleBottom - rectangleTop) / 2);
        if (numberOfPods == 1) {
            // draw one at the center and be done.
            float centerX = rectangleLeft + ((rectangleRight - rectangleLeft) / 2);
            largeAndSmallCircleCurrentCenterX = centerX;
            Pod pod = pods[0];
            pod.setCenter(centerX, podCenterY);
            pod.setPodRadius(podRadius);
            canvas.drawCircle(largeAndSmallCircleCurrentCenterX, podCenterY, largeCircleRadius, mainPaint);
            canvas.drawCircle(largeAndSmallCircleCurrentCenterX, podCenterY, mediumCircleRadius, mainPaint);
            pod.drawPod(canvas);
            return;
        }
        // else you start calculation.
        float startX = rectangleLeft + (rectangleBottom - rectangleTop) / 2;
        float gapBetweenPodCenters = calculateGapBetweenPodCenters(rectangleLeft, rectangleRight,
                rectangleTop, rectangleBottom);
        if (firstDraw) {
            firstDraw = false;
            largeAndSmallCircleCurrentCenterX = startX + currentlySelectedPod * gapBetweenPodCenters;
        }
        canvas.drawCircle(largeAndSmallCircleCurrentCenterX, podCenterY, largeCircleRadius, mainPaint);
        canvas.drawCircle(largeAndSmallCircleCurrentCenterX, podCenterY, mediumCircleRadius, podPaint);
        for (int i = 0, n = numberOfPods; i < n; i++) {
            float podCenterX = startX + i * gapBetweenPodCenters;
            Pod pod = pods[i];
            pod.setPodRadius(podRadius);
            pod.setCenter(podCenterX, podCenterY);
            pod.drawPod(canvas);
        }
    }

    private float calculateGapBetweenPodCenters(float left, float right, float top, float bottom) {
        // The center of leftmost circle is at getHeight() / 2, getHeight() / 2 (by design)
        // The center of rightmost circle is at getWidth() - getHeight /2, getHeight / 2 (by design)
        // So the distance between these 2 points is the difference is their x-axis co ordinates
        // as both have the same y-axis coordinate.
        // Which is nothing but (getWidth() - getHeight / 2) - getHeight / 2
        // which equal to getWidth() - 2 * getHeight / 2
        // which is equal to getWidth() - getHeight()
        float distanceBetweenTheCentersOfPodsAtTheEnd = (right - left) - (bottom - top);
        // Now to determine the distance between the center of each pod
        // I divide the distanceBetweenTheCentersOfPodsAtTheEnd by number of Pods -1
        // because distance between one pod starts at the 0th position.
        return distanceBetweenTheCentersOfPodsAtTheEnd / (numberOfPods - 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        int desiredHeight = (int) (getContext().getResources().getDisplayMetrics().density * 30);

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }
        int desiredWidth = getDesiredWidth(height);

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }
        setMeasuredDimension(width, height);
        isViewMeasured = true;
    }

    private int getDesiredWidth(int height) {
        return height * numberOfPods;
    }

    private void drawRoundedRect(Canvas canvas, float left, float top, float right, float bottom) {
        float radius = Math.min((bottom - top) / 2, (right - left) / 2);
        canvas.drawCircle(left + radius, top + radius, radius, mainPaint);
        canvas.drawCircle(right - radius, top + radius, radius, mainPaint);
        canvas.drawRect(left + radius, top, right - radius, bottom, mainPaint);
    }

    public static class PodSliderOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<PodSlider> mPodSliderRef;
//        private int mScrollState;
//        private int mPendingSelection = -1;

        public PodSliderOnPageChangeListener(PodSlider podSlider) {
            mPodSliderRef = new WeakReference<>(podSlider);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            mScrollState = state;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            final PodSlider podSlider = mPodSliderRef.get();
//            if (podSlider == null) {
//                return;
//            }
            /*float currentX = podSlider.largeAndSmallCircleCurrentCenterX;
            float differenceX;
            if (position == podSlider.currentlySelectedPod) {
                differenceX = podSlider.pods[position + 1].getCenterX() - currentX;
                podSlider.largeAndSmallCircleCurrentCenterX += differenceX * positionOffset;
            } else {
                differenceX = podSlider.pods[position - 1].getCenterX() - currentX;
                podSlider.largeAndSmallCircleCurrentCenterX -= differenceX * positionOffset;
            }
            podSlider.invalidate();*/

            /*if (mPendingSelection == -1 ||
                    podSlider.pods[podSlider.currentlySelectedPod].getCenterX() !=
                            podSlider.pods[mPendingSelection].getCenterX()) {
                float currentX = podSlider.largeAndSmallCircleCurrentCenterX;
                float differenceX;
                if (positionOffset > 0 || mPendingSelection == -1)
                    differenceX = podSlider.pods[position + 1].getCenterX() - currentX;
                else
                    differenceX = podSlider.pods[mPendingSelection].getCenterX() - currentX;
                podSlider.largeAndSmallCircleCurrentCenterX += differenceX * positionOffset;
                podSlider.invalidate();
            }
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                if (mPendingSelection != -1) {
                    podSlider.currentlySelectedPod = mPendingSelection;
                    podSlider.pods[mPendingSelection].animatePod();
                    mPendingSelection = -1;
                }
            }*/
            /*if (mPendingSelection != -1 && mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                podSlider.currentlySelectedPod = mPendingSelection;
                podSlider.pods[mPendingSelection].animatePod();
            }*/
        }

        @Override
        public void onPageSelected(int position) {
            PodSlider podSlider = mPodSliderRef.get();
            if (podSlider != null) {
                podSlider.setCurrentlySelectedPod(position);
            }
        }
    }
}
