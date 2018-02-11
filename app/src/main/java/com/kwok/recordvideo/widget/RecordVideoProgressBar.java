package com.kwok.recordvideo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.kwok.recordvideo.R;

/**
 * @author gmf
 * @description 录制视频 进度条
 * @date 2018/2/6.
 */
public class RecordVideoProgressBar extends View {

    /**
     * 背景颜色 or 进度颜色
     */
    private int mBackgroundColor, mProgressColor;

    /**
     * 最大进度， 时间， 毫秒
     */
    private int mMaxProgress;
    /**
     * 当前进度， 毫秒
     */
    private int mCurrentProgress;

    /**
     * 进度条 宽度
     */
    private int mProgressWidth;

    private OnProgressEndListener mOnProgressEndListener;
    private Paint mBgPaint, mProgressPaint;
    private RectF mRectF;

    private int width, height;

    public RecordVideoProgressBar(Context context) {
        this(context, null);
    }

    public RecordVideoProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordVideoProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttributes(context, attrs);
        init();
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RecordVideoProgressBar);
        mBackgroundColor = ta.getColor(R.styleable.RecordVideoProgressBar_backColor, Color.parseColor("#dddddd"));
        mProgressColor = ta.getColor(R.styleable.RecordVideoProgressBar_progressColor, Color.parseColor("#1AAD19"));

        String maxProgress = ta.getString(R.styleable.RecordVideoProgressBar_maxProgress);
        if (TextUtils.isEmpty(maxProgress)) {
            mMaxProgress = 100;
        } else {
            mMaxProgress = Integer.valueOf(maxProgress);
        }
        mProgressWidth = ta.getInt(R.styleable.RecordVideoProgressBar_strokeWidth, 10);
        mCurrentProgress = 0;
        ta.recycle();
    }

    private void init() {
        //背景画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(mBackgroundColor);
        mBgPaint.setAntiAlias(true);

        //背景画笔
        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(mProgressWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mRectF = new RectF();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = this.getWidth();
        height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        // 位置
        mRectF.left = mProgressWidth / 2 + .8f;
        mRectF.top = mProgressWidth / 2 + .8f;
        mRectF.right = width - mProgressWidth / 2 - 1.5f;
        mRectF.bottom = height - mProgressWidth / 2 - 1.5f;

        drawCircleBack(canvas);
        drawProgress(canvas);
    }

    /**
     * 画背景
     */
    private void drawCircleBack(Canvas canvas) {
        canvas.drawCircle(width / 2, width / 2, width / 2 - .5f, mBgPaint);
    }

    private void drawProgress(Canvas canvas) {
        // 绘制圆圈，进度条背景
        if (mCurrentProgress > 0 && mCurrentProgress < mMaxProgress) {
            mProgressPaint.setColor(mProgressColor);
            canvas.drawArc(mRectF, -90, ((float) mCurrentProgress / mMaxProgress) * 360, false, mProgressPaint);
        } else if (mCurrentProgress == 0) {
            mProgressPaint.setColor(Color.TRANSPARENT);
            canvas.drawArc(mRectF, -90, 360, false, mProgressPaint);
        } else if (mCurrentProgress == mMaxProgress) {
            if (mOnProgressEndListener != null) {
                mOnProgressEndListener.onProgressEndListener();
            }
        }
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    public void setProgress(int progress) {
        mCurrentProgress = progress;
        invalidate();
    }

    public void setOnProgressEndListener(OnProgressEndListener onProgressEndListener) {
        mOnProgressEndListener = onProgressEndListener;
    }

    public interface OnProgressEndListener {
        /**
         * 达到最大值
         */
        void onProgressEndListener();
    }
}
