package com.domyos.econnected.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.constant.UserPicConstant;
import com.domyos.econnected.event.EquipmentEvent;
import com.domyos.econnected.event.HeartRateEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.utils.LogUtils;
import com.domyos.econnected.utils.MediaPlayerService;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.ew.ble.library.entity.BikeSportData;
import com.ew.ble.library.entity.TreadmillSportData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class RealPlayFragment extends BaseFragment {

    private String videoPath;
    private static MediaPlayer mMediaPlayer;

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
    @BindView(R.id.realVideoView)
    VideoView mVideoView;

    private int videoId;
    private int currPosion = 0;
    Timer sendBroToServTimer = null;
    private boolean sendBroToServ = true;
    private float speed = 0;
    private float currentSpeed = 0;
    private int rpm = 0;
    private float currentRpm = 0;

    private float videoSpeed;
    private Bundle bundle;
    private String distanceStr, timeStr, kcalStr, speedStr, inclineStr, bpmStr, rpmStr;
    private Timer timer;
    private Intent intent;

    public RealPlayFragment(int videoId) {
        this.videoId = videoId;
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_real_play;
    }

    @Override
    protected void initSomething() {
        if (isVisible()) {
            int picId = SharedPreferenceUtils.get(YDApplication.getInstance(), "picId", 0);
            setAvtar(picId);
            if (SharedPreferenceUtils.get(getContext(), "isLogin", false)) {
                live_top_running_name.setText(SharedPreferenceUtils.get(getContext(), "name", ""));
            }
        }
        intent = new Intent(getActivity(), MediaPlayerService.class);
        getActivity().startService(intent);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.domyos.econnected.SEND_SPORT_DATA");
        getActivity().registerReceiver(getSportReceiver, intentFilter);

        if (videoId == 0) {
            videoPath = "/system/media/lasi.mp4";
        } else {
            videoPath = "/system/media/yali.mp4";
        }
        mVideoView.start();
        mVideoView.setVideoPath(videoPath);
        mVideoView.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(android.media.MediaPlayer mp) {
                mp.setLooping(true);
                mMediaPlayer = mp;
                mMediaPlayer.setVolume(0, 0);
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
        });
        mVideoView.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(android.media.MediaPlayer mp) {
                // mMediaPlayer.start();
            }
        });

        if (bundle != null) {
            refreshUI(bundle);

        } else {
            if (YDApplication.getInstance().get("ble_connect") != null && (boolean) YDApplication.getInstance().get("ble_connect")) {

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mVideoView.pause();
                    }
                }, 800);
            }


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
                speed = treadmillSportData.getSpeed();
                timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
                distanceStr = treadmillSportData.getDistance() + "";
                kcalStr = treadmillSportData.getCalories() + "";
                inclineStr = treadmillSportData.getIncline() + "";
                speedStr = treadmillSportData.getSpeed() + "";
                inclineStr = treadmillSportData.getIncline() + "";
                live_top_running_incline.setText(inclineStr);
                bpmStr = treadmillSportData.getPulse() + "";
                live_top_running_distance.setText(distanceStr);
                live_top_running_kcal.setText(kcalStr);
                live_top_running_time.setText(timeStr);
                live_top_running_speed.setText(speedStr);
                if (speed == 0) {
                    if (mVideoView.isPlaying()) {
                        mVideoView.pause();
                    }
                    if (mMediaPlayer != null) {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                        }
                    }
                    // live_top_running_speed.setText(speedStr);

                } else {

                    if (mMediaPlayer != null) {
                        mMediaPlayer.start();

                    } else {
                        mMediaPlayer = new MediaPlayer();
                        mMediaPlayer.setVolume(0, 0);
                        mMediaPlayer.start();
                    }


                    if (speed < 4 && speed > 0) {
                        videoSpeed = 0.5f;
                        setPlayerSpeed(mMediaPlayer, speed, videoSpeed);
                    }
                    if (speed < 6 && speed >= 4) {
                        videoSpeed = 1.0f;
                        setPlayerSpeed(mMediaPlayer, speed, videoSpeed);
                    }
                    if (speed < 8 && speed >= 6) {
                        videoSpeed = 1.5f;
                        setPlayerSpeed(mMediaPlayer, speed, videoSpeed);

                    }
                    if (speed < 22 && speed >= 8) {
                        videoSpeed = 2.0f;
                        setPlayerSpeed(mMediaPlayer, speed, videoSpeed);
                    }

                }

            }
            if (bundle.getInt("device_mode", 1) == 1) {
                //单车
                live_top_running_incline_layout.setVisibility(View.GONE);
                live_top_running_bpm_layout.setBackground(getActivity().getDrawable(R.drawable.border_right));
                live_top_running_rpm_layout.setVisibility(View.VISIBLE);

                BikeSportData bikeSportData = bundle.getParcelable("device_info");
                speed = bikeSportData.getSpeed();
                int minutes = bikeSportData.getMinute();
                int seconds = bikeSportData.getSecond();
                timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
                BigDecimal bigDecimal = new BigDecimal(bikeSportData.getDistance());
                float f2 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                distanceStr = f2 + "";
                kcalStr = bikeSportData.getCalories() + "";
                speedStr = bikeSportData.getSpeed() + "";
                bpmStr = bikeSportData.getPluse() + "";
                rpm = bikeSportData.getRpm();
                rpmStr = rpm + "";
                live_top_running_rpm.setText(rpmStr);

                if (rpm == 0) {
                    try {

                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (mVideoView != null) {
                                    mVideoView.pause();
                                }

                                /*if (mMediaPlayer != null) {
                                    if (mMediaPlayer.isPlaying()) {
                                        mMediaPlayer.pause();
                                    }
                                }*/
                            }
                        }, 800);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    live_top_running_speed.setText("0");
                } else {
                    if (mMediaPlayer != null) {
                        try {

                            if (!mMediaPlayer.isPlaying()) {
                                mMediaPlayer.start();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (rpm < 60 && rpm > 0) {
                            videoSpeed = 0.8f;
                            setBikePlayerSpeed(mMediaPlayer, rpm, 1.0f);
                        }
                        if (rpm < 90 && rpm >= 60) {
                            videoSpeed = 1.0f;
                            setBikePlayerSpeed(mMediaPlayer, rpm, videoSpeed);
                        }
                        if (rpm < 120 && rpm >= 90) {
                            videoSpeed = 1.5f;
                            setBikePlayerSpeed(mMediaPlayer, rpm, videoSpeed);
                        }
                        if (rpm >= 120) {
                            videoSpeed = 2.0f;
                            setBikePlayerSpeed(mMediaPlayer, rpm, videoSpeed);
                        }
                        live_top_running_distance.setText(distanceStr);
                        live_top_running_kcal.setText(kcalStr);
                        live_top_running_time.setText(timeStr);
                        live_top_running_speed.setText(speedStr);

                    }
                }
            }
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
                        live_top_running_time.setText("00:00 ");
                    }
                });

                break;
        }
    }


    //跑步机播放倍速
    private void setPlayerSpeed(MediaPlayer mp, float speed, float vSpeed) {

        if (speed != currentSpeed) {
            currentSpeed = speed;

            if (android.os.Build.VERSION.SDK_INT >=
                    android.os.Build.VERSION_CODES.M) {
                //mMediaPlayer = mp;
                try {
                    if (mp != null) {
                        mp.pause();
                        PlaybackParams playbackParams = mp.getPlaybackParams();
                        playbackParams.setSpeed(vSpeed);
                        mp.setPlaybackParams(playbackParams);
                    }

                } catch (Exception e) {
                    Log.e("setPlayerSpeed", "------------e==" + e);

                }

            } else {

                Log.e("setPlayerSpeed", "--------android6.0以下版本------");

            }
        }
    }

    //单车播放倍速
    private void setBikePlayerSpeed(MediaPlayer mp, int rpm, float vSpeed) {

        if (rpm != currentRpm) {
            currentRpm = rpm;

            if (android.os.Build.VERSION.SDK_INT >=
                    android.os.Build.VERSION_CODES.M) {
                try {
                    if (mp != null) {
                        PlaybackParams playbackParams = mp.getPlaybackParams();
                        playbackParams.setSpeed(vSpeed);
                        mp.setPlaybackParams(playbackParams);
                    }
                } catch (Exception e) {
                    Log.e("setBikePlayerSpeed", "------------e==" + e);
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        if (getSportReceiver != null) {
            getActivity().unregisterReceiver(getSportReceiver);
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }


        super.onDestroy();
        //releaseMediaPlayer();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {

            try {

                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
                if (mVideoView != null) {
                    if (mVideoView.isPlaying()) {

                        mVideoView.stopPlayback();
                    }
                }

            } catch (Exception e) {
                Log.e("releaseMediaPlayer", "releaseMediaPlayer---111----e==" + e);
            }

        }
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


    @OnClick({R.id.live_top_running_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.live_top_running_back:
                removeFragment();
                break;

        }

    }

    private void removeFragment() {
        if (mVideoView != null) {
            Log.e("releaseMediaPlayer", "removeFragment-------mVideoView");

            if (mVideoView.isPlaying()) {
                mVideoView.pause();
                Log.e("releaseMediaPlayer", "removeFragment-------stopPlayback");

            }
            mVideoView.stopPlayback();
        }
        if (mMediaPlayer != null) {

            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    Log.e("releaseMediaPlayer", "removeFragment-------mMediaPlayer");

                }
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
                Log.e("releaseMediaPlayer", "removeFragment-------mMediaPlayer = null;");


            } catch (Exception e) {
                Log.e("releaseMediaPlayer", "removeFragment-------e==" + e);
            }
        }
        if (intent != null) {
            getActivity().stopService(intent);
        }

        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }


    public BroadcastReceiver getSportReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.domyos.econnected.SEND_SPORT_DATA")) {
                // 接收到广播传来的数据
                bundle = intent.getExtras();

                if (bundle != null) {
                    refreshUI(bundle);
                } else {
                }

            } else if (action.equals("com.domyos.econnected.SEND_HEART_DATA")) {

            }
        }
    };


}
