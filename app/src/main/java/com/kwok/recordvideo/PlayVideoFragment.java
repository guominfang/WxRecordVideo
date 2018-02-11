package com.kwok.recordvideo;

import android.app.Fragment;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

/**
 * @author gmf
 * @description 使用MediaPlayer 播放
 * @date 2018/2/11.
 */
public class PlayVideoFragment extends Fragment {

    private static final String KEY_VIDEO_URL = "video_url";

    private String mVideoUrl;

    private MediaPlayer player;
    private TextureView mTextureView;
    private Surface surface;

    public static PlayVideoFragment newInstance(String videoPath) {
        Bundle args = new Bundle();
        args.putString(KEY_VIDEO_URL, videoPath);
        PlayVideoFragment fragment = new PlayVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoUrl = getArguments().getString(KEY_VIDEO_URL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_video, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextureView = view.findViewById(R.id.play_video_TextureView);
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                surface = new Surface(surfaceTexture);
                autoPlay();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                stop();
                release();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
    }

    /**
     * 自动播放
     */
    private void autoPlay() {
        //必须在surface创建后才能初始化MediaPlayer,否则不会显示图像
        player = new MediaPlayer();

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //设置显示视频显示在SurfaceView上
        player.setSurface(surface);
        player.setLooping(true);
        try {
            player.setDataSource(mVideoUrl);
            player.prepare();
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放
     */
    private void start() {
        if (player == null || player.isPlaying()) {
            return;
        }
        player.start();
    }

    /**
     * 停止播放
     */
    private void stop() {
        if (player == null || player.isPlaying()) {
            return;
        }
        player.stop();
    }

    /**
     * 释放资源
     */
    private void release() {
        if (player == null) {
            return;
        }
        player.release();
        player = null;
        surface = null;
    }


    @Override
    public void onPause() {
        super.onPause();
        stop();
        release();
    }

    public void onDelVideo() {
        stop();
        release();
        deleteFile();
    }

    private void deleteFile() {
        File file = new File(mVideoUrl);
        if (file.exists()) {
            file.delete();
        }
    }

    public void onSaveVideo() {
        stop();
        release();
    }
}
