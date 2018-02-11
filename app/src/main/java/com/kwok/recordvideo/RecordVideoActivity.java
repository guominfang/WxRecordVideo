package com.kwok.recordvideo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.kwok.recordvideo.widget.RecordVideoView;
import com.kwok.recordvideo.widget.SendView;

/**
 * @author gmf
 * @description 录制视频
 * @date 2018/2/11.
 */
public class RecordVideoActivity extends AppCompatActivity {

    private RelativeLayout mRecordLayout;
    private RecordVideoView mVideoRecord;
    private SendView mSendView;

    public static void start(Context context) {
        Intent starter = new Intent(context, RecordVideoActivity.class);
        context.startActivity(starter);
    }

    public void startAnim() {
        overridePendingTransition(android.R.anim.fade_in, 0);
    }

    @Override
    public void finish() {
        super.finish();
        outAnim();
    }

    public void outAnim() {
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        mRecordLayout = findViewById(R.id.record_layout);
        mVideoRecord = findViewById(R.id.record_video_btn_record);
        mVideoRecord.setRecordVideoListener(new RecordVideoView.RecordVideoListener() {
            @Override
            public void onStartRecord() {

            }

            @Override
            public void onCompleteRecord(boolean isTakePhoto) {
                mSendView.startAnim();
                mRecordLayout.setVisibility(View.GONE);
            }
        });
        mSendView = findViewById(R.id.record_SendView);
        mSendView.mBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelRecord();
            }
        });
        mSendView.mSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveRecord();
            }
        });
        startAnim();
    }

    private void onSaveRecord() {
        finish();
    }

    private void onCancelRecord() {
        mSendView.stopAnim();
        mRecordLayout.setVisibility(View.VISIBLE);
    }

    public void onClose(View view) {
        finish();
    }

    public void onSwitch(View view) {
    }
}
