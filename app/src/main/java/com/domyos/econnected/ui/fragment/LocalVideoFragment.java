package com.domyos.econnected.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.constant.UserPicConstant;
import com.domyos.econnected.event.CurrEvent;
import com.domyos.econnected.event.EquipmentEvent;
import com.domyos.econnected.event.HeartRateEvent;
import com.domyos.econnected.event.UIEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.ui.view.CustomVideoView;
import com.domyos.econnected.utils.LogUtils;
import com.domyos.econnected.utils.MusicPlayService;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.domyos.econnected.utils.ToastUtil;
import com.ew.ble.library.entity.BikeSportData;
import com.ew.ble.library.entity.TreadmillSportData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by HouWei on 16/8/23.
 */

public class LocalVideoFragment extends BaseFragment {
    @BindView(R.id.mVideoView)
    CustomVideoView mMVideoView;
    @BindView(R.id.mVideoProgressSeekBar)
    SeekBar mMVideoProgressSeekBar;
    @BindView(R.id.mVideoTimeTextView)
    TextView mMVideoTimeTextView;
    @BindView(R.id.mVideoTime)
    TextView mMVideoTime;
    @BindView(R.id.mPlayImageView)
    FrameLayout mMPlayImageView;
    @BindView(R.id.volumeLayout)
    LinearLayout volumeLayout;
    @BindView(R.id.volumeDown)
    ImageView volumeDown;
    @BindView(R.id.volumeSeekBar)
    SeekBar volumeSeekBar;
    @BindView(R.id.volumeUp)
    ImageView volumeUp;
    @BindView(R.id.playVideo_vol)
    FrameLayout playVideo_vol;
    @BindView(R.id.mPlay_img)
    ImageView mPlay_img;
    @BindView(R.id.live_top_running_distance)
    TextView live_top_running_distance;
    @BindView(R.id.live_top_running_kcal)
    TextView live_top_running_kcal;
    @BindView(R.id.live_top_running_time)
    TextView live_top_running_time;
    @BindView(R.id.live_top_running_incline)
    TextView live_top_running_incline;
    @BindView(R.id.live_top_running_incline_layout)
    LinearLayout live_top_running_incline_layout;
    @BindView(R.id.live_top_running_speed)
    TextView live_top_running_speed;
    @BindView(R.id.live_top_running_name)
    TextView live_top_running_name;
    @BindView(R.id.live_top_running_img)
    ImageView live_top_running_img;
    @BindView(R.id.live_top_running_bmp)
    TextView live_top_running_bmp;
    @BindView(R.id.live_top_running_bpm_layout)
    LinearLayout live_top_running_bpm_layout;
    @BindView(R.id.live_top_running_rpm)
    TextView live_top_running_rpm;
    @BindView(R.id.live_top_running_rpm_layout)
    LinearLayout live_top_running_rpm_layout;


    private int maxVolume, currentVolume;
    private AudioManager audioManager;

    // 保存视频播放进度
    private int mCurrentPosition = 0;
    private int mTotalDuration = 0;

    private Handler mHandler;
    private String path = "";
    private Bundle bundle;
    private String distanceStr, timeStr, kcalStr, speedStr, inclineStr, bpmStr, rpmStr;
    /**
     * 返回布局文件id
     *
     * @return
     */
    private Timer mTimer;

    private String item;

    public LocalVideoFragment(String item) {
        this.item = item;
    }

    /**
     * 一些界面的初始化操作
     */
    @Override
    protected void initSomething() {
        if (isVisible()) {
            if (SharedPreferenceUtils.get(getContext(), "isLogin", false)) {
                live_top_running_name.setText(SharedPreferenceUtils.get(getContext(), "name", ""));
            }
            int picId = SharedPreferenceUtils.get(YDApplication.getInstance(), "picId", 0);
            setAvtar(picId);
        }
        EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_VIDEO_FRAGMENT));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.domyos.econnected.SEND_SPORT_DATA");
        getActivity().registerReceiver(getSportReceiver, intentFilter);
        initViews();
        setListener();
        // 启动线程 每0.5秒更新一次播放进度
        mTimer = new Timer();
        startPlayProgressTrack();
        if (item.equals("item01")) {
            //path = Environment.getExternalStorageDirectory().getPath() + "/ew/item01.mp4";
            path = "/system/media/item01.mp4";
        }
        if (item.equals("item02")) {
           // path = Environment.getExternalStorageDirectory().getPath() + "/ew/item02.mp4";
            path = "/system/media/item02.mp4";
        }
        if (item.equals("item03")) {
            //path = Environment.getExternalStorageDirectory().getPath() + "/ew/item03.mp4";
            path = "/system/media/item03.mp4";
        }

        startPlay(path);
        setVolume();
        Drawable drawable = getNewDrawable(getActivity(), R.drawable.video_player_seekbar_img, 30, 35);
        mMVideoProgressSeekBar.setThumb(drawable);
        if (bundle != null) {
            refreshUI(bundle);
        }
    }

    public void setAvtar(int picId) {

        if (picId == 0) {
            live_top_running_img.setImageDrawable(getActivity().getDrawable(R.drawable.touxiang_img));

        }


        if (picId == UserPicConstant.TYPE_01_01) {
            live_top_running_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_01));

        }

        if (picId == UserPicConstant.TYPE_01_02) {
            live_top_running_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_02));

        }

        if (picId == UserPicConstant.TYPE_01_03) {
            live_top_running_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_03));

        }
        if (picId == UserPicConstant.TYPE_02_01) {
            live_top_running_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_01));

        }

        if (picId == UserPicConstant.TYPE_02_02) {
            live_top_running_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_02));

        }

        if (picId == UserPicConstant.TYPE_02_03) {
            live_top_running_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_03));

        }
        if (picId == UserPicConstant.TYPE_03_01) {
            live_top_running_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_01));

        }

        if (picId == UserPicConstant.TYPE_03_02) {
            live_top_running_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_02));

        }

        if (picId == UserPicConstant.TYPE_03_03) {
            live_top_running_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_03));
        }
    }

    private void refreshUI(Bundle bundle) {
        if (isVisible()) {
            if (bundle.getInt("device_mode", 0) == 0) {
                live_top_running_rpm_layout.setVisibility(View.GONE);
                live_top_running_incline_layout.setVisibility(View.VISIBLE);
                //跑步机
                TreadmillSportData treadmillSportData = bundle.getParcelable("device_info");
                int minutes = treadmillSportData.getMinute();
                int seconds = treadmillSportData.getSecond();
                timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
                distanceStr = treadmillSportData.getDistance() + "";
                kcalStr = treadmillSportData.getCalories() + "";
                inclineStr = treadmillSportData.getIncline() + "";
                speedStr = treadmillSportData.getSpeed() + "";
                inclineStr = treadmillSportData.getIncline() + "";
                live_top_running_incline.setText(inclineStr);
                bpmStr = treadmillSportData.getPulse() + "";
                live_top_running_speed.setText(speedStr);
            }
            if (bundle.getInt("device_mode", 1) == 1) {
                //单车
                live_top_running_incline_layout.setVisibility(View.GONE);
                live_top_running_bpm_layout.setBackground(getActivity().getDrawable(R.drawable.border_right));
                live_top_running_rpm_layout.setVisibility(View.VISIBLE);
                BikeSportData bikeSportData = bundle.getParcelable("device_info");
                int minutes = bikeSportData.getMinute();
                int seconds = bikeSportData.getSecond();
                timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
                BigDecimal bigDecimal = new BigDecimal(bikeSportData.getDistance());
                float f2 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                distanceStr = f2 + "";
                kcalStr = bikeSportData.getCalories() + "";
                speedStr = bikeSportData.getSpeed() + "";
                live_top_running_incline.setText(inclineStr);
                bpmStr = bikeSportData.getPluse() + "";
                rpmStr = bikeSportData.getRpm() + "";
                live_top_running_rpm.setText(rpmStr);

                live_top_running_speed.setText(speedStr);

            }
            live_top_running_distance.setText(distanceStr);
            live_top_running_kcal.setText(kcalStr);
            live_top_running_time.setText(timeStr);


        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void equipmentEvent(EquipmentEvent event) {
        switch (event.action) {

            case EquipmentEvent.ACTION_EQUIPMENT_DISCONNECT:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        live_top_running_speed.setText("0");
                        live_top_running_distance.setText("0");
                        live_top_running_kcal.setText("0");
                        live_top_running_time.setText("0");

                    }
                });

                break;
        }
    }

    private void initViews() {
        mHandler = new Handler();
        // 停止播放音乐
        getContext().startService(new Intent(
                MusicPlayService.ACTION_PAUSE).setPackage(getContext().getPackageName()));
        mMVideoView.setZOrderOnTop(false);

    }

    private void setListener() {
        mMVideoProgressSeekBar.setVisibility(View.VISIBLE);
        mMVideoProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mMVideoView.isPlaying()) {
                        mMVideoView.seekTo(progress);
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mMVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mTotalDuration = mMVideoView.getDuration();
            }
        });
        mMVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ToastUtil.toast(getContext(), "error");
                getFragmentManager()
                        .beginTransaction()
                        .remove(LocalVideoFragment.this)
                        .commitAllowingStateLoss();
                return true;
            }
        });
        mMVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //mp.start();
                //mp.setLooping(true);
                Log.e("sh_1234", "onCompletion");
                mMVideoView.start();
            }
        });
    }

    private void startPlayProgressTrack() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mMVideoView != null && mMVideoView.isPlaying()) {
                    if (mMVideoView != null) {
                        mCurrentPosition = mMVideoView.getCurrentPosition();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshProgress();
                            }
                        });
                    }
                }
            }
        }, 0, 500);
    }

    /**
     * 更新界面的播放进度
     */
    private void refreshProgress() {
        String totalStr = milliSecondsToFormatTimeString(mTotalDuration);
        String currentStr = milliSecondsToFormatTimeString(mCurrentPosition);
        if (mMVideoTimeTextView == null) {
            return;
        }
        mMVideoTime.setText(currentStr);
        mMVideoTimeTextView.setText(totalStr);

        mMVideoProgressSeekBar.setMax(mTotalDuration);
        mMVideoProgressSeekBar.setProgress(mCurrentPosition);

    }


    private void startPlay(String path) {

        mMVideoView.requestFocus();
        if (mPasued && mCurrentPosition > 0) {
            mMVideoView.start();
        } else {
            Uri uri = Uri.parse(path);
            mCurrentPosition = 0;
            mMVideoView.setVideoURI(uri);
            mTotalDuration = mMVideoView.getDuration();
            mMVideoView.start();
        }
        mPasued = false;
        mPlay_img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.video_player_start));
    }

    private boolean mPasued = false;

    private void pausePlay() {
        if (mMVideoView.isPlaying() && !mPasued) {
            mCurrentPosition = mMVideoView.getCurrentPosition();
            mMVideoView.pause();
            mPasued = true;
        }
    }

    @Override
    public void onDestroy() {
        if (getSportReceiver != null) {
            getActivity().unregisterReceiver(getSportReceiver);
        }


        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void HeartRate(HeartRateEvent event) {
        switch (event.action) {
            case HeartRateEvent.HEART_RATE:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        live_top_running_bmp.setText(event.heartRate + "");
                    }
                });
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTimer != null) {
            mTimer.cancel();
        }
        mCurrentPosition = 0;
        if (mMVideoView != null && mMVideoView.isPlaying()) {
            mMVideoView.clearFocus();
            mMVideoView.stopPlayback();
        }
        if (mVolumeReceiver != null) {
            getContext().unregisterReceiver(mVolumeReceiver);
        }

    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_local_video;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mMVideoView == null) {
            return;
        }
        if (hidden) {
            if (mMVideoView.isPlaying()) {
                getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
            }
        }
    }

    boolean isVolumeShow = false;
    boolean isPlay = true;

    @OnClick({R.id.mVideoFrameLayout, R.id.mPlayImageView, R.id.live_top_running_back, R.id.mVolShow, R.id.volumeDown, R.id.volumeUp, R.id.playVideo_vol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mVolShow:
                setVolumeShow();
                //  volumeLayout.setVisibility(View.GONE);
                break;
            case R.id.mPlayImageView:
                if (mMVideoView.isPlaying()) {
                    mPlay_img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.video_player_stop));
                    pausePlay();
                } else {
                    startPlay(path);
                    mPlay_img.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.video_player_start));

                }
                break;
            case R.id.live_top_running_back:
                pausePlay();
                EventBus.getDefault().post(new CurrEvent(CurrEvent.ACTION_BACK_CURR));
                getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
                break;
            case R.id.volumeDown:
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
                if (currentVolume >= 1) {
                    volumeSeekBar.setProgress(currentVolume - 1);
                }
                break;
            case R.id.volumeUp:
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
                if (currentVolume < maxVolume) {
                    volumeSeekBar.setProgress(currentVolume + 1);
                }
                break;

            case R.id.playVideo_vol:
                setVolumeShow();
                break;
            case R.id.mVideoFrameLayout:
                // mViewVideo_bottom.setVisibility(View.VISIBLE);
                break;

        }
    }


    public void setVolumeShow() {
        if (isVolumeShow) {
            volumeLayout.setVisibility(View.INVISIBLE);
            isVolumeShow = false;
        } else {
            volumeLayout.setVisibility(View.VISIBLE);
            isVolumeShow = true;
        }

    }

    /**
     * 将给定的毫秒数转换成00:00:00样式的字符串
     *
     * @param milliseconds 待转换的毫秒数
     */
    public static String milliSecondsToFormatTimeString(long milliseconds) {
        String finalTimerString = "";
        int hours, minutes, seconds;

        hours = (int) (milliseconds / (1000 * 60 * 60));
        minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            finalTimerString = String.format(Locale.getDefault(), "%02d%02d:%02d", hours, minutes, seconds);
        } else {
            finalTimerString = String.format(Locale.getDefault(), "%02d:%02d",
                    minutes, seconds);
        }
        return finalTimerString;
    }


    /***
     * 设置音量
     */
    private void setVolume() {

        myRegisterReceiver();//注册同步更新的广播
        audioManager = (AudioManager) getActivity().getSystemService(AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统最大音量
        volumeSeekBar.setMax(maxVolume);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
        volumeSeekBar.setProgress(currentVolume);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

            }
        });
    }

    private MyVolumeReceiver mVolumeReceiver;

    /**
     * 注册当音量发生变化时接收的广播
     */
    private void myRegisterReceiver() {
        mVolumeReceiver = new MyVolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        getActivity().registerReceiver(mVolumeReceiver, filter);
    }

    /**
     * 处理音量变化时的界面显示
     *
     * @author long
     */
    private class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果音量发生变化则更改seekbar的位置
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION") && audioManager != null) {
                //AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 当前的媒体音量
                volumeSeekBar.setProgress(currentVolume);
            }
        }
    }

    //调用函数缩小图片
    public BitmapDrawable getNewDrawable(Context context, int restId, int dstWidth, int dstHeight) {
        Bitmap Bmp = BitmapFactory.decodeResource(
                context.getResources(), restId);
        Bitmap bmp = Bmp.createScaledBitmap(Bmp, dstWidth, dstHeight, true);
        BitmapDrawable d = new BitmapDrawable(bmp);
        Bitmap bitmap = d.getBitmap();
        if (bitmap.getDensity() == Bitmap.DENSITY_NONE) {
            d.setTargetDensity(context.getResources().getDisplayMetrics());
        }
        return d;
    }

    public BroadcastReceiver getSportReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.domyos.econnected.SEND_SPORT_DATA")) {
                // 接收到广播传来的数据
                bundle = intent.getExtras();
                refreshUI(bundle);

            } else {
                LogUtils.e("no ");
            }
        }
    };


}
