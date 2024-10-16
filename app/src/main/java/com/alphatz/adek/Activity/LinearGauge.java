package com.alphatz.adek.Activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class LinearGauge extends View {

    private Paint backgroundPaint;
    private Paint needlePaint;
    private Paint textPaint;
    private Paint segmentPaint;
    private RectF rect;
    private float progress = 0; // value from 0 - 100
    private String bmiLabel = "";

    public LinearGauge(Context context) {
        super(context);
        init();
    }

    public LinearGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAntiAlias(true);

        needlePaint = new Paint();
        needlePaint.setColor(Color.BLACK);
        needlePaint.setStrokeWidth(5f);
        needlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        needlePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        segmentPaint = new Paint();
        segmentPaint.setStyle(Paint.Style.FILL);
        segmentPaint.setAntiAlias(true);

        rect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int padding = 40;
        float gaugeHeight = 60;

        rect.set(padding, height / 2f - gaugeHeight / 2, width - padding, height / 2f + gaugeHeight / 2);

        // Draw background
        canvas.drawRect(rect, backgroundPaint);

        // Draw color segments
        drawColorSegments(canvas);

        // Draw needle (indicator)
        float needleX = padding + (progress / 100f) * (width - 2 * padding);
        canvas.drawLine(needleX, rect.top, needleX, rect.bottom, needlePaint);

        // Draw BMI label above the needle
        canvas.drawText(bmiLabel, needleX, rect.top - 20, textPaint);
    }

    private void drawColorSegments(Canvas canvas) {
        int[] colors = {Color.BLUE, Color.GREEN, Color.YELLOW, Color.parseColor("#FFA500"), Color.RED};
        float[] segments = {0.15f, 0.3f, 0.3f, 0.15f, 0.1f};
        float startX = rect.left;
        float segmentWidth = rect.width();

        for (int i = 0; i < colors.length; i++) {
            segmentPaint.setColor(colors[i]);
            float segmentLength = segments[i] * segmentWidth;
            canvas.drawRect(startX, rect.top, startX + segmentLength, rect.bottom, segmentPaint);
            startX += segmentLength;
        }
    }

    public void setProgressWithAnimation(float bmiValue) {
        float mappedValue = Math.max(0, Math.min(100, ((bmiValue - 10) / 30) * 100));
        bmiLabel = String.format("%.1f", bmiValue);

        ValueAnimator animator = ValueAnimator.ofFloat(progress, mappedValue);
        animator.setDuration(1000);
        animator.addUpdateListener(valueAnimator -> {
            progress = (float) valueAnimator.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    public float getProgress() {
        return progress;
    }
}
