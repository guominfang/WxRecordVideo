package com.kwok.recordvideo.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kwok.recordvideo.R;

/**
 * @author gmf
 * @description 录制视频按钮
 * @date 2018/2/6.
 */
public class RecordVideoView extends RelativeLayout {

    /**
     * 刷新进度的频率
     */
    private static final int UPDATE_TIME = 100;
    private static final int IS_TAKE_TIME = 650;

    private RecordVideoProgressBar mProgressBar;
    private TextView mCircleView;

    private RecordVideoListener mRecordVideoListener;
    private boolean isRecording = false;

    private int mRecordTime; //毫秒级
    private int mMaxRecordTime = 10000;//默认六十秒

    public RecordVideoView(Context context) {
        this(context, null);
    }

    public RecordVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_record_video_btn, this);
        mProgressBar = findViewById(R.id.RecordVideoProgressBar);
        mProgressBar.setMaxProgress(mMaxRecordTime);
        mCircleView = findViewById(R.id.circle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mRecordTime = 0;
                startAnim();
                mProgressBar.setProgress(mRecordTime);
                isRecording = true;
                if (mRecordVideoListener != null) {
                    mRecordVideoListener.onStartRecord();
                }
                mHandler.postDelayed(mRecordTimeRunnable, UPDATE_TIME);
                break;
            case MotionEvent.ACTION_UP:
                stopRecord();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            default:
        }

        return true;
    }

    private void stopRecord() {
        if (isRecording) {
            isRecording = false;
            mProgressBar.setProgress(0);
            stopAnim();
            if (mRecordVideoListener != null) {
                mRecordVideoListener.onCompleteRecord(mRecordTime < IS_TAKE_TIME);
            }
        }
    }

    private Handler mHandler = new Handler();

    private Runnable mRecordTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRecording) {
                if (mRecordTime > mMaxRecordTime) {
                    return;
                }
                mRecordTime += UPDATE_TIME;
                mProgressBar.setProgress(mRecordTime);

                if (mRecordTime == mMaxRecordTime) {
                    stopRecord();
                    return;
                }

                mHandler.postDelayed(this, UPDATE_TIME);
            }
        }
    };

    public boolean isRecording() {
        return isRecording;
    }

    /**
     * 开始动画
     */
    private void startAnim() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mCircleView, "scaleX", 1, 0.5f),
                ObjectAnimator.ofFloat(mCircleView, "scaleY", 1, 0.5f),
                ObjectAnimator.ofFloat(mProgressBar, "scaleX", 1, 1.3f),
                ObjectAnimator.ofFloat(mProgressBar, "scaleY", 1, 1.3f)
        );
        set.setDuration(250).start();
    }

    /**
     * 结束动画
     */
    private void stopAnim() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mCircleView, "scaleX", 0.5f, 1f),
                ObjectAnimator.ofFloat(mCircleView, "scaleY", 0.5f, 1f),
                ObjectAnimator.ofFloat(mProgressBar, "scaleX", 1.3f, 1f),
                ObjectAnimator.ofFloat(mProgressBar, "scaleY", 1.3f, 1f)
        );
        set.setDuration(250).start();
    }

    public void setRecordVideoListener(RecordVideoListener listener) {
        mRecordVideoListener = listener;
    }

    public interface RecordVideoListener {
        /**
         * 开始录制
         */
        void onStartRecord();

        /**
         * 完成
         *
         * @param isTakePhoto true 时间太短，考虑进行只获取图片
         */
        void onCompleteRecord(boolean isTakePhoto);
    }
}
