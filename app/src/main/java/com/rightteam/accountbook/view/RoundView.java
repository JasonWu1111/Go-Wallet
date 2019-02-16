package com.rightteam.accountbook.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rightteam.accountbook.R;

/**
 * Created by wuzhiqiang on 2018/8/5
 */
public class RoundView extends View {
    private Paint mPaint;

    public void setPaintColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    public RoundView(Context context) {
        this(context, null);
    }

    public RoundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.blue_dark));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2, mPaint);
    }
}
