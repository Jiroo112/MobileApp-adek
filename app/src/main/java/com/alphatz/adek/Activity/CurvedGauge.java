package com.alphatz.adek.Activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CurvedGauge extends View {

    private Paint backgroundPaint;
    private Paint needlePaint;
    private Paint textPaint;
    private Paint segmentPaint;
    private RectF oval;
    private float progress = 0;
    private String bmiLabel = "";

    public CurvedGauge(Context context) {
        super(context);
        init();
    }

    public CurvedGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurvedGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(60f);
        backgroundPaint.setAntiAlias(true);

        needlePaint = new Paint();
        needlePaint.setColor(Color.BLACK);
        needlePaint.setStrokeWidth(10f);
        needlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        needlePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        segmentPaint = new Paint();
        segmentPaint.setStyle(Paint.Style.STROKE);
        segmentPaint.setStrokeWidth(60f);
        segmentPaint.setAntiAlias(true);

        oval = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 60;

        oval.set(width / 2f - radius, height / 2f - radius, width / 2f + radius, height / 2f + radius);

        // Draw background
        canvas.drawArc(oval, 180, 180, false, backgroundPaint);

        // Draw color segments
        drawColorSegments(canvas, radius);

        // Draw needle
        float needleAngle = 180 + (progress / 100f) * 180f;
        float needleLength = radius - 20;
        float needleX = width / 2f + (float) Math.cos(Math.toRadians(needleAngle)) * needleLength;
        float needleY = height / 2f + (float) Math.sin(Math.toRadians(needleAngle)) * needleLength;
        canvas.drawLine(width / 2f, height / 2f, needleX, needleY, needlePaint);

        // Draw circle at needle base
        canvas.drawCircle(width / 2f, height / 2f, 20f, needlePaint);

        // Draw BMI label
        canvas.drawText(bmiLabel, width / 2f, height / 2f + radius + 80, textPaint);
    }

    private void drawColorSegments(Canvas canvas, int radius) {
        int[] colors = {Color.BLUE, Color.GREEN, Color.YELLOW, Color.parseColor("#FFA500"), Color.RED};
        float[] segments = {0.15f, 0.3f, 0.3f, 0.15f, 0.1f};
        float startAngle = 180f;

        for (int i = 0; i < colors.length; i++) {
            segmentPaint.setColor(colors[i]);
            float sweepAngle = segments[i] * 180f;
            canvas.drawArc(oval, startAngle, sweepAngle, false, segmentPaint);
            startAngle += sweepAngle;
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