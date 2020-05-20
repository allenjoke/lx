package com.domyos.econnected.utils;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.domyos.econnected.R;


public class MediaPlayerService extends Service {
    
    MediaPlayer mediaPlayer;
    
    //必须要实现此方法，IBinder对象用于交换数据，此处暂时不实现
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.bg_music);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.setLooping(true);
                mediaPlayer.start();
            }
        });
        Log.e("TAG", "create");
        
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        play();
        Log.e("TAG", "start");
        return super.onStartCommand(intent, flags, startId);
    }
    
    //封装播放
    private void play() {
        mediaPlayer.start();
    }
    
    //service被关闭之前调用
    @Override
    public void onDestroy() {

        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }catch (Exception e){
            Log.e("TAG", "destroyed-------e=="+e);

        }

        Log.e("TAG", "destroyed");
        super.onDestroy();
    }
    
    
}
