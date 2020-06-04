package com.bytedance.videoplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bytedance.videoplayer.R;

public class VideoActivity extends AppCompatActivity {
    private Button buttonFull;
    private CustomVideoView videoView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video);
        setTitle("VideoView");

        buttonFull = findViewById(R.id.buttonFull);
        buttonFull.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置videoView全屏播放

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
                videoView.setLayoutParams(params);
            }
        });

        videoView = (CustomVideoView) findViewById(R.id.videoView);
        videoView.setVideoPath(getVideoPath(R.raw.bytedance));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.setMediaController(new MediaController(VideoActivity.this));//默认的视频控制器
            }
        });
    }


    @Override//重载onConfigurationChanged需要在mainfests中设置权限和捕获事件类型
    public void onConfigurationChanged(Configuration newConfig) {//监听屏幕方向改变
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//横屏
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            videoView.setLayoutParams(params);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){//竖屏
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dip2px(this,200f));
            videoView.setLayoutParams(params);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    public static int dip2px(Context context, float dpValue) {//常用的把dip值转换为pix像素值的方法
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);    }


    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }
}
