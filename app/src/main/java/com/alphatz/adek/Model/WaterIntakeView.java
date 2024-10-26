package com.alphatz.adek.Model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class WaterIntakeView extends View {
    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private Paint smallTextPaint;
    private RectF circleRect;
    private int current = 0;
    private int target = 2500;
    private float strokeWidth = 40f;

    public WaterIntakeView(Context context) {
        super(context);
        init();
    }

    public WaterIntakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterIntakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize background circle paint
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.parseColor("#E6F7F5"));  // Light mint color
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setAntiAlias(true);

        // Initialize progress circle paint
        progressPaint = new Paint();
        progressPaint.setColor(Color.parseColor("#4FD1C5"));  // Darker mint color
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setAntiAlias(true);

        // Initialize text paint for current ml
        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#374151")); // Dark gray
        textPaint.setTextSize(80f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true); // Make text bold to match design

        // Initialize text paint for target text
        smallTextPaint = new Paint();
        smallTextPaint.setColor(Color.parseColor("#6B7280")); // Gray
        smallTextPaint.setTextSize(30f);
        smallTextPaint.setTextAlign(Paint.Align.CENTER);
        smallTextPaint.setAntiAlias(true);

        circleRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int xPad, int yPad) {
        super.onSizeChanged(w, h, xPad, yPad);
        // Calculate circle bounds
        float diameter = Math.min(w, h) - strokeWidth;
        float left = (w - diameter) / 2;
        float top = (h - diameter) / 2;
        circleRect.set(left, top, left + diameter, top + diameter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw background circle
        canvas.drawArc(circleRect, 0, 360, false, backgroundPaint);

        // Draw progress arc only if there's progress to show
        if (current > 0) {
            float sweepAngle = (current * 360f) / target;
            canvas.drawArc(circleRect, -90, sweepAngle, false, progressPaint);
        }

        // Draw current ml text
        float centerX = circleRect.centerX();
        float centerY = circleRect.centerY();

        // Adjust Y position to account for text height
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textHeight = fm.bottom - fm.top;
        float textOffset = textHeight / 2 - fm.bottom;

        canvas.drawText(String.valueOf(current), centerX, centerY + textOffset, textPaint);

        // Draw "ml" text
        canvas.drawText("ml", centerX, centerY + textOffset + 40, smallTextPaint);

        // Draw target text
        String targetText = "Target: " + target + " ml";
        canvas.drawText(targetText, centerX, centerY + textOffset + 80, smallTextPaint);
    }

    // Setter methods for current and target values
    public void setCurrent(int current) {
        this.current = Math.min(current, target); // Ensure current doesn't exceed target
        invalidate();
    }

    public void setTarget(int target) {
        this.target = target;
        this.current = Math.min(current, target); // Adjust current if needed
        invalidate();
    }

    // Getter methods
    public int getCurrent() {
        return current;
    }

    public int getTarget() {
        return target;
    }

    // Method to add water intake
    public void addWater(int amount) {
        setCurrent(Math.min(current + amount, target));
    }

    // Method to reset water intake
    public void reset() {
        setCurrent(0);
    }
}