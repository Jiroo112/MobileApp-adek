package com.alphatz.adek.Activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class ProgressGauge extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private Paint smallTextPaint;
    private RectF rect;
    private float progress = 0;
    private String currentValue = "";
    private int current = 0;
    private int target = 2500;

    public ProgressGauge(Context context) {
        super(context);
        init();
    }

    public ProgressGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.parseColor("#E6F7F5")); // Light mint color
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setColor(Color.parseColor("#4FD1C5")); // Darker mint color
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#374151")); // Dark gray
        textPaint.setTextSize(50f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);

        smallTextPaint = new Paint();
        smallTextPaint.setColor(Color.parseColor("#6B7280")); // Gray
        smallTextPaint.setTextSize(30f);
        smallTextPaint.setTextAlign(Paint.Align.CENTER);
        smallTextPaint.setAntiAlias(true);

        rect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int padding = 40;
        float gaugeHeight = 60;

        // Set rectangle for gauge
        rect.set(padding, height / 2f - gaugeHeight / 2, width - padding, height / 2f + gaugeHeight / 2);

        // Draw background
        canvas.drawRoundRect(rect, gaugeHeight/2, gaugeHeight/2, backgroundPaint);

        // Draw progress
        if (current > 0) {
            float progressWidth = (current / (float) target) * (width - 2 * padding);
            RectF progressRect = new RectF(padding, height / 2f - gaugeHeight / 2,
                    padding + progressWidth, height / 2f + gaugeHeight / 2);
            canvas.drawRoundRect(progressRect, gaugeHeight/2, gaugeHeight/2, progressPaint);
        }

        // Draw text values
        String currentText = String.valueOf(current);
        canvas.drawText(currentText, width / 2f, rect.top - 30, textPaint);
        
        // Draw "ml" text
        canvas.drawText("ml", width / 2f, rect.top - 5, smallTextPaint);
        
        // Draw target text
        String targetText = "Target: " + target + " ml";
        canvas.drawText(targetText, width / 2f, rect.bottom + 35, smallTextPaint);
    }

    public void setCurrent(int value) {
        this.current = Math.min(value, target);
        invalidate();
    }

    public void setTarget(int target) {
        this.target = target;
        this.current = Math.min(current, target);
        invalidate();
    }

    public void addProgress(int amount) {
        setCurrent(Math.min(current + amount, target));
    }

    public void reset() {
        setCurrent(0);
    }

    public void setProgressWithAnimation(int newValue) {
        ValueAnimator animator = ValueAnimator.ofInt(current, Math.min(newValue, target));
        animator.setDuration(1000);
        animator.addUpdateListener(valueAnimator -> {
            current = (int) valueAnimator.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    public int getCurrent() {
        return current;
    }

    public int getTarget() {
        return target;
    }
}
