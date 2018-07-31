package com.rightteam.accountbook.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rightteam.accountbook.R;
import com.rightteam.accountbook.constants.ResDef;

import java.util.List;

/**
 * Created by JasonWu on 8/1/2018
 */
public class RingView extends View {
    private List<Float> mPers;
    private Paint mPaint;
    private float mCircleWidth = 100;

    public RingView(Context context) {
        this(context, null, -1);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);
    }

    public void setPers(List<Float> pers){
        mPers = pers;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 0;
        RectF rectF = new RectF(mCircleWidth / 2, mCircleWidth / 2, getMeasuredWidth() - mCircleWidth / 2, getMeasuredWidth() - mCircleWidth / 2);
        if(mPers == null || mPers.size() == 0){
            mPaint.setColor(getResources().getColor(R.color.light_grey));
            canvas.drawArc(rectF, startAngle, 360, false, mPaint);
        }else {
           for(int i = 0; i < mPers.size(); i++){
               float sweep = mPers.get(i) * 360;
               mPaint.setColor(getResources().getColor(ResDef.TYPE_COLORS[i]));
               canvas.drawArc(rectF, startAngle,  sweep, false, mPaint);
               startAngle += sweep;
           }
        }
    }
}
