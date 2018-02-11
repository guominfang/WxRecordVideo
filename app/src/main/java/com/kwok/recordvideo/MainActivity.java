package com.kwok.recordvideo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author gmf
 */
public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final int RC_RECORD_VIDEO_PERMISSION = 10;

    private static String[] mRecordVideoPermission = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRecordVideo(View view) {
        startRecordVideo();
    }

    @AfterPermissionGranted(RC_RECORD_VIDEO_PERMISSION)
    private void startRecordVideo() {
        if (EasyPermissions.hasPermissions(this, mRecordVideoPermission)) {
            RecordVideoActivity.start(this);
        } else {
            EasyPermissions.requestPermissions(this, "录制视频需要相机，麦克风，存储权限",
                    RC_RECORD_VIDEO_PERMISSION, mRecordVideoPermission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            startRecordVideo();
        }
    }
}
