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
    private Paint arcPaint;
    private RectF oval;
    private float progress = 0; // nilainy dari 0 - 100
    private String bmiLabel = "0";

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
        // inisialiasi warna gauge nya (arc)
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(30f);
        backgroundPaint.setAntiAlias(true);

        // warna buat jarum
        needlePaint = new Paint();
        needlePaint.setColor(Color.BLACK);
        needlePaint.setStrokeWidth(8f);
        needlePaint.setStyle(Paint.Style.STROKE);
        needlePaint.setAntiAlias(true);

        // warna teks (label)
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        // warna progres arc
        arcPaint = new Paint();
        arcPaint.setStrokeWidth(30f);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setAntiAlias(true);

        //ngebuat RectF buat gambar arc
        oval = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 50;

        // oval area untuk arc
        oval.set(width / 2f - radius, height / 2f - radius, width / 2f + radius, height / 2f + radius);

        // background gauge
        canvas.drawArc(oval, 180, 180, false, backgroundPaint);

        // progres berdasarkan value yang sekarang
        float sweepAngle = (progress / 100f) * 180f;
        arcPaint.setColor(getColorForBmi(Float.parseFloat(bmiLabel))); // Get color based on BMI label
        canvas.drawArc(oval, 180, sweepAngle, false, arcPaint);

        // jarum
        float needleAngle = 180 + sweepAngle;
        float needleX = width / 2f + (float) Math.cos(Math.toRadians(needleAngle)) * radius;
        float needleY = height / 2f + (float) Math.sin(Math.toRadians(needleAngle)) * radius;
        canvas.drawLine(width / 2f, height / 2f, needleX, needleY, needlePaint);

        //bmi label
        canvas.drawText(bmiLabel, width / 2f, height / 2f + 30, textPaint);
    }

    // fungsi buat ngewarnain arc sesuai dengan value bmi
    private int getColorForBmi(float bmiValue) {
        // Set colors based on BMI value
        if (bmiValue < 18.5) {
            return Color.RED; // Underweight
        } else if (bmiValue >= 18.5 && bmiValue < 24.9) {
            return Color.GREEN; // Normal
        } else if (bmiValue >= 25 && bmiValue < 30) {
            return Color.YELLOW; // Overweight
        } else {
            return Color.RED; // Obesity
        }
    }

    // fungsi buat progres biar ad animasi
    public void setProgressWithAnimation(float bmiValue) {
        // buat progres ada di dalam kisaran 0 hingga 100 sesuai dg skala BMI
        float mappedValue = Math.max(0, Math.min(100, ((bmiValue - 10) / 30) * 100)); // 10 itu batas bawah, 40 batas atasny
        bmiLabel = String.format("%.1f", bmiValue); // Update label (menunjukkan bmi)

        // menganimasikan progres
        ValueAnimator animator = ValueAnimator.ofFloat(progress, mappedValue);
        animator.setDuration(1000); // delay 1 sec
        animator.addUpdateListener(valueAnimator -> {
            progress = (float) valueAnimator.getAnimatedValue();
            invalidate(); // Redraw view
        });
        animator.start(); // animasi start
    }

    public float getProgress() {
        return progress;
    }
}
