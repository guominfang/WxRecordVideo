package com.kwok.recordvideo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kwok.recordvideo.widget.RecordVideoView;
import com.kwok.recordvideo.widget.RecordVideoView.RecordVideoListener;
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

    private RecordVideoFragment mRecordVideoFragment;
    private PlayVideoFragment mPlayVideoFragment;

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

        mRecordVideoFragment = RecordVideoFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.fragment_content, mRecordVideoFragment).commit();

        initView();
        startAnim();
    }

    private void initView() {
        mRecordLayout = findViewById(R.id.record_layout);
        mVideoRecord = findViewById(R.id.record_video_btn_record);
        mVideoRecord.setRecordVideoListener(mRecordVideoListener);
        mSendView = findViewById(R.id.record_SendView);
        mSendView.mBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSendView.mSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveRecord();
            }
        });
    }

    private RecordVideoListener mRecordVideoListener = new RecordVideoListener() {
        @Override
        public void onStartRecord() {
            mRecordVideoFragment.startRecord();
        }

        @Override
        public void onCompleteRecord(boolean isTakePhoto) {
            if (isTakePhoto) {
                Toast.makeText(RecordVideoActivity.this, "录制时间太短", Toast.LENGTH_SHORT).show();
                mRecordVideoFragment.stopRecordShort();
            } else {
                mRecordVideoFragment.stopRecord();
                mSendView.startAnim();
                mRecordLayout.setVisibility(View.GONE);
                mPlayVideoFragment = PlayVideoFragment.
                        newInstance(mRecordVideoFragment.getVideoFilePath());
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_content, mPlayVideoFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    };

    private void onSaveRecord() {
        mPlayVideoFragment.onSaveVideo();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mPlayVideoFragment != null) {
            mSendView.stopAnim();
            mRecordLayout.setVisibility(View.VISIBLE);
            mPlayVideoFragment.onDelVideo();
        }
        super.onBackPressed();
        mPlayVideoFragment = null;
    }

    public void onClose(View view) {
        finish();
    }

    public void onSwitch(View view) {
        mRecordVideoFragment.switchCamera();
    }
}
