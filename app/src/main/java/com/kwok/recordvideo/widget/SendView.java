package com.kwok.recordvideo.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.kwok.recordvideo.R;

/**
 * @author gmf
 * @description
 * @date 2018/2/11.
 */
public class SendView extends RelativeLayout {

    public RelativeLayout mBackLayout, mSelectLayout;

    public SendView(Context context) {
        this(context, null);
    }

    public SendView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_view_send_btn, this);
        mBackLayout = findViewById(R.id.back_layout);
        mSelectLayout = findViewById(R.id.select_layout);
        setVisibility(GONE);
    }

    public void startAnim() {
        setVisibility(VISIBLE);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mBackLayout, "translationX", 0, -360),
                ObjectAnimator.ofFloat(mSelectLayout, "translationX", 0, 360)
        );
        set.setDuration(300).start();
    }

    public void stopAnim() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mBackLayout, "translationX", -360, 0),
                ObjectAnimator.ofFloat(mSelectLayout, "translationX", 360, 0)
        );
        set.setDuration(300).start();
        setVisibility(GONE);
    }

}
