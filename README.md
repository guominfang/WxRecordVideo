# WxRecordVideo
[Android]仿微信录制小视频

使用了原生的API进行仿微信录制小视频

## 录制
使用 Camera + TextureView + MediaRecorder，进行录制 (这一种方式，在开启录制时会听到 “滴”一声，暂时无法解决，后期考虑换成 5.0以上的 Camera2 + TextureView + MediaRecorder的方式可以解决)

## 播放
使用 TextureView + MediaPlayer，进行播放

## 界面的按钮，使用了自定义View or 属性动画


## 考虑添加功能
1. 录制方式（Camera2 + TextureView + MediaRecorder）
2. 播放方式（TextureView + MediaPlayer）


#### 参考
[MediaUtils](https://github.com/Werb/MediaUtils)

[android-MediaRecorder](https://github.com/googlesamples/android-MediaRecorder)
