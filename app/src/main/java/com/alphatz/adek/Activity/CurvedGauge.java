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
    private float progress = 0; // Value of progress from 0 to 100
    private String bmiLabel = "0"; // Label to display in the center

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
        // Initialize the paint for the gauge background (arc)
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(30f);
        backgroundPaint.setAntiAlias(true);

        // Initialize the paint for the needle
        needlePaint = new Paint();
        needlePaint.setColor(Color.BLACK);
        needlePaint.setStrokeWidth(8f);
        needlePaint.setStyle(Paint.Style.STROKE);
        needlePaint.setAntiAlias(true);

        // Initialize the paint for the text (label)
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        // Initialize the paint for the progress arc
        arcPaint = new Paint();
        arcPaint.setStrokeWidth(30f);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setAntiAlias(true);

        // Create RectF for drawing the arc
        oval = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 50;

        // Set the oval area for the arc
        oval.set(width / 2f - radius, height / 2f - radius, width / 2f + radius, height / 2f + radius);

        // Draw the background gauge
        canvas.drawArc(oval, 180, 180, false, backgroundPaint);

        // Draw the progress based on the current value
        float sweepAngle = (progress / 100f) * 180f;
        arcPaint.setColor(getColorForBmi(Float.parseFloat(bmiLabel))); // Get color based on BMI label
        canvas.drawArc(oval, 180, sweepAngle, false, arcPaint);

        // Draw the needle
        float needleAngle = 180 + sweepAngle;
        float needleX = width / 2f + (float) Math.cos(Math.toRadians(needleAngle)) * radius;
        float needleY = height / 2f + (float) Math.sin(Math.toRadians(needleAngle)) * radius;
        canvas.drawLine(width / 2f, height / 2f, needleX, needleY, needlePaint);

        // Draw the BMI label
        canvas.drawText(bmiLabel, width / 2f, height / 2f + 30, textPaint);
    }

    // Function to get the color based on BMI value
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

    // Function to set progress with animation
    public void setProgressWithAnimation(float bmiValue) {
        // Ensure the progress is within 0 to 100 range based on BMI scale
        float mappedValue = Math.max(0, Math.min(100, ((bmiValue - 10) / 30) * 100)); // Assuming 10 as the lower limit and 40 as the upper limit
        bmiLabel = String.format("%.1f", bmiValue); // Update the label to show the actual BMI value

        // Animate the progress
        ValueAnimator animator = ValueAnimator.ofFloat(progress, mappedValue);
        animator.setDuration(1000); // 1 second duration
        animator.addUpdateListener(valueAnimator -> {
            progress = (float) valueAnimator.getAnimatedValue();
            invalidate(); // Redraw the view
        });
        animator.start(); // Start the animation
    }

    // Getter for progress if needed
    public float getProgress() {
        return progress;
    }
}
