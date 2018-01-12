package com.example.administrator.appupdatedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.View;


/**
 * <pre>
 *     author : Leero
 *     e-mail : 925230519@qq.com
 *     time  : 2017-11-25
 *     desc  :
 *     version: 1.0
 * </pre>
 */
public class CircleProgressBar extends View {
    private int progress;

    private float progressWidth;
    private int progressColor;
    private int backgroundColor;
    private Paint mPaint;
    private int mRadius;
    private RectF oval;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, 0, 0);
        // background
        progress = array.getInt(R.styleable.CircleProgressBar_progress, 35);
        progressWidth = array.getDimension(R.styleable.CircleProgressBar_progressWidth, 20);
        backgroundColor = array.getColor(R.styleable.CircleProgressBar_backgroundColor, Color.GRAY);
        // progress
        progress = array.getInt(R.styleable.CircleProgressBar_progress, 35);
        progressWidth = array.getDimension(R.styleable.CircleProgressBar_progressWidth, 20);
        progressColor = array.getColor(R.styleable.CircleProgressBar_progressColor, Color.BLUE);
        array.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init(w, h);
    }

    private void init(int w, int h) {
        this.mRadius = Math.min(w, h) / 2;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        oval = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize / 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = heightSize / 2;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 1. drawBackground
        drawBackground(canvas);
        // 2.drawProgress
        drawProgress(canvas);
    }

    private void drawBackground(Canvas canvas) {
        mPaint.setColor(backgroundColor);
        float proRadius = mRadius - progressWidth / 2;
        oval.set(mRadius - proRadius, mRadius - proRadius, mRadius + proRadius, mRadius + proRadius);
        mPaint.setStrokeWidth(progressWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(false);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(oval, -90, 360, false, mPaint);
    }

    private void drawProgress(Canvas canvas) {
        mPaint.setColor(progressColor);
        float proRadius = mRadius - progressWidth / 2;
        oval.set(mRadius - proRadius, mRadius - proRadius, mRadius + proRadius, mRadius + proRadius);
        mPaint.setStrokeWidth(progressWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(false);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(oval, -90, progress * 1f / 100 * 360, false, mPaint);
    }

    public synchronized void setProgress(@IntRange(from = 0, to = 100) int progress) {
        this.progress = progress;
        postInvalidate();
    }
}
