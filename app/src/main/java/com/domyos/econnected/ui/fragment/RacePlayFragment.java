package com.domyos.econnected.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.constant.ApiCenter;
import com.domyos.econnected.constant.UserPicConstant;
import com.domyos.econnected.enity.CheckInCallBackInfo;
import com.domyos.econnected.enity.RaceCheckInfo;
import com.domyos.econnected.event.EquipmentEvent;
import com.domyos.econnected.event.HeartRateEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.ui.view.svg.RunWayView;
import com.domyos.econnected.ui.view.svg.SvgPath;
import com.domyos.econnected.utils.LogUtils;
import com.domyos.econnected.utils.MediaPlayerService;
import com.domyos.econnected.utils.MyVideoView;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.domyos.econnected.utils.ToastUtil;
import com.domyos.econnected.utils.datapter.ListViewAdapter;
import com.ew.ble.library.entity.BikeSportData;
import com.ew.ble.library.entity.TreadmillSportData;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class RacePlayFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private String videoPath;
    private MediaPlayer mMediaPlayer;
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
    @BindView(R.id.raceListView)
    ListView raceListView;
    @BindView(R.id.image_big)
    ImageView image_big;
    @BindView(R.id.image_big_01)
    ImageView image_big_01;
    @BindView(R.id.race_cal)
    FrameLayout race_cal;
    @BindView(R.id.race_distance)
    FrameLayout race_distance;
    @BindView(R.id.race_time)
    FrameLayout race_time;
    @BindView(R.id.race_dp)
    FrameLayout race_dp;
    /* @BindView(R.id.image_small)
     ImageView image_small;*/
    @BindView(R.id.videoView)
    MyVideoView mVideoView;
    @BindView(R.id.one_frameLayout)
    RelativeLayout one_frameLayout;
    @BindView(R.id.view_runway)
    RunWayView runWayView;
    private int videoId;
    private int currPosion = 0;
    Timer sendBroToServTimer = null;
    private boolean sendBroToServ = true;
    private float speed = 0;
    private float currentSpeed = 0;
    private int rpm = 0;
    private float currentRpm = 0;
    private int deviceType = -1;
    private float videoSpeed;
    private Bundle bundle;
    private String distanceStr, timeStr, kcalStr, speedStr, inclineStr, bpmStr, rpmStr;
    private float distance, incline;
    private int time, calorie, heartrate;
    private Timer timer;
    private Intent intent;
    String path;
    boolean isFull = true;

    private int roomId = 0;
    private long sendTime = 0L;
    // 发送心跳包
    private Handler mHandler = new Handler();
    // 每隔2秒发送一次心跳包，检测连接没有断开
    private static final long HEART_BEAT_RATE = 2 * 1000;
    private ListViewAdapter listViewAdapter;
    private CheckInCallBackInfo.RaceCheckOutInfo raceCheckOutInfo;
    private List<CheckInCallBackInfo.RaceCheckOutInfo> list;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    //dialog的view
    private View alertDialogView;
    private ImageView race_dialog_img;
    private TextView race_dialog_name, race_dialog_distance, race_dialog_time, race_dialog_kcal, race_dialog_ok, race_dialog_add;
    //标记自己在当前list的位置
    private int index;
    private String name;
    //列表对比参数
    private int type;
    float[] temps;//几个用户，就传几个distance,这里模拟了2个
    int[] picIds;//用户头像集合
    String[] userNames;
    private int user_id;

    public RacePlayFragment(int videoId) {
        this.videoId = videoId;
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_race_play;
    }


    @Override
    protected void initSomething() {
        //--------------------模拟数据--------------------------------
        int[] resID = new int[]{R.mipmap.hu, R.mipmap.gou, R.mipmap.hou};//用户头像，这个你要从后台去拿，或者拿ID，然后本地一一匹配,看你逻辑
        runWayView.setRunwayLength(5);//跑道长度，随意设置

      /*  new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                temps[0] += 10;
                temps[1] += 20;
                temps[2] += 25;
                runWayView.setProgress(temps, resID);//第一个参数为距离的数据（有几个用户就传几个用户），第二个参数为用户头像的id,要跟第一个距离的用户一一匹配上
            }
        }, 1000, 1000);*/

        //---------------------------------------------------
        //长连接


        if (isVisible()) {
            type = 2;
            name = SharedPreferenceUtils.get(getActivity(), "name", "");

            int picId = SharedPreferenceUtils.get(YDApplication.getInstance(), "picId", 0);
            setAvtar(picId, live_top_running_img);
            String ew_name = (String) YDApplication.getInstance().get("ew_name");
            if (ew_name != null) {
                setListener();
                if (ew_name.contains("JS")) {
                    deviceType = 1;
                } else if (ew_name.contains("EP")) {

                    deviceType = 2;

                } else if (ew_name.contains("TM")) {

                    deviceType = 0;
                }
            }

            if (SharedPreferenceUtils.get(getContext(), "isLogin", false)) {
                live_top_running_name.setText(SharedPreferenceUtils.get(getContext(), "name", ""));
                user_id = SharedPreferenceUtils.get(getActivity(), "user_id", 0);
            }else {
                user_id = 0;
            }
        }
        intent = new Intent(getActivity(), MediaPlayerService.class);
        getActivity().startService(intent);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.domyos.econnected.SEND_SPORT_DATA");
        getActivity().registerReceiver(getSportReceiver, intentFilter);

        if (videoId == 0) {
            //videoPath = Environment.getExternalStorageDirectory().getPath() + "/ew/lasi.mp4";
            videoPath = "/system/media/lasi.mp4";
            image_big.setImageDrawable(getActivity().getDrawable(R.drawable.lasi_map_01));
            image_big_01.setImageDrawable(getActivity().getDrawable(R.drawable.lasi_map_01));
            roomId = 1101;
            runWayView.setmGlyphStrings(SvgPath.pathThree, getActivity());//SVG数据

        } else {
            //videoPath = Environment.getExternalStorageDirectory().getPath() + "/ew/yali.mp4";
            videoPath = "/system/media/yali.mp4";
            image_big.setImageDrawable(getActivity().getDrawable(R.drawable.yali_map_01));
            image_big_01.setImageDrawable(getActivity().getDrawable(R.drawable.yali_map_01));
            roomId = 1102;
            runWayView.setmGlyphStrings(SvgPath.pathOne, getActivity());//SVG数据

        }
        mVideoView.start();
        mVideoView.setVideoPath(videoPath);
        mMediaPlayer = null;
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mMediaPlayer = mp;
                mMediaPlayer.setVolume(0, 0);
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                //mp.start();// 播放
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // mMediaPlayer.start();
            }
        });
        // sendBroToServTimer();

        if (bundle != null) {
            refreshUI(bundle);

        } else {

            if (YDApplication.getInstance().get("ble_connect")!=null&&(boolean) YDApplication.getInstance().get("ble_connect")) {

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mVideoView.pause();
                    }
                }, 800);
            }
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(280, 150);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.rightMargin = 80;
        lp.bottomMargin = 60;
        image_big.setLayoutParams(lp);
        image_big_01.setVisibility(View.GONE);

       /* if (list.size() > 0) {
            listViewAdapter = new ListViewAdapter(getActivity(), list, index);
            raceListView.setAdapter(listViewAdapter);
            raceListView.setOnItemClickListener(this);

        }*/

        alertDialogView = getActivity().getLayoutInflater().inflate(R.layout.race_dialog, null, false);
        builder = new AlertDialog.Builder(getActivity());
        dialog = builder.create();
        dialog.setView(alertDialogView);
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
                time = minutes * 60 + seconds;
                timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
                distance = (float) treadmillSportData.getDistance();

                //TODO TEST
                //runWayView.setProgress(Math.round(distance), true);

                distanceStr = distance + "";
                calorie = treadmillSportData.getCalories();
                kcalStr = calorie + "";

                inclineStr = treadmillSportData.getIncline() + "";
                speedStr = speed + "";
                incline = treadmillSportData.getIncline();
                inclineStr = incline + "";

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
                time = minutes * 60 + seconds;
                timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
                distance = (float) bikeSportData.getDistance();
                BigDecimal bigDecimal = new BigDecimal(bikeSportData.getDistance());
                float f2 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                distanceStr = f2 + "";
                calorie = bikeSportData.getCalories();
                kcalStr = calorie + "";
                speedStr = speed + "";
                bpmStr = bikeSportData.getPluse() + "";
                rpm = bikeSportData.getRpm();
                rpmStr = rpm + "";
                live_top_running_rpm.setText(rpmStr);
                live_top_running_kcal.setText(kcalStr);

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
                    /*try {
                        if (mVideoView != null) {
                            mVideoView.pause();
                        }

                        if (mMediaPlayer != null) {
                            if (mMediaPlayer.isPlaying()) {
                                mMediaPlayer.pause();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    live_top_running_speed.setText("0");
                } else {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.start();
                    } else {
                        mMediaPlayer = new MediaPlayer();
                        mMediaPlayer.setVolume(0, 0);
                        mMediaPlayer.start();
                    }


                    if (rpm < 60 && rpm > 0) {
                        videoSpeed = 0.8f;
                        setBikePlayerSpeed(mMediaPlayer, rpm, videoSpeed);
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
            /*if (list.size() > 0) {
                choiceUser(list, 1);
                //找到自己在当前list中的位置
                for (int i = 0; i < list.size(); i++) {
                    if (name.equals(list.get(i).getUsername())) {
                        index = i;
                    }
                }
                listViewAdapter = new ListViewAdapter(getActivity(), list, index);
                raceListView.setAdapter(listViewAdapter);
                raceListView.setOnItemClickListener(this);


            }*/
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void equipmentEvent(EquipmentEvent event) {
        switch (event.action) {

            case EquipmentEvent.ACTION_EQUIPMENT_DISCONNECT:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        live_top_running_bmp.setText("0");
                        live_top_running_speed.setText("0");
                        live_top_running_distance.setText("00:00");
                        live_top_running_kcal.setText("0");
                        live_top_running_time.setText("0");
                        distance = 0;
                    }
                });

                break;
        }
    }

    private void sendBroToServTimer() {
        sendBroToServTimer = new Timer();
        sendBroToServTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendBroToServ = true;
            }
        }, 2000, 2000);
    }

    //跑步机播放倍速
    private void setPlayerSpeed(MediaPlayer mp, float speed, float vSpeed) {

        if (speed != currentSpeed) {
            currentSpeed = speed;

            try {
                if (android.os.Build.VERSION.SDK_INT >=
                        android.os.Build.VERSION_CODES.M) {
                    //mMediaPlayer = mp;
                    PlaybackParams playbackParams = mp.getPlaybackParams();
                    playbackParams.setSpeed(vSpeed);
                    mp.setPlaybackParams(playbackParams);
                }
            } catch (Exception e) {
                LogUtils.e("------e==" + e);
            }

        }
    }

    //单车播放倍速
    private void setBikePlayerSpeed(MediaPlayer mp, int rpm, float vSpeed) {

        if (rpm != currentRpm) {
            currentRpm = rpm;
            if (android.os.Build.VERSION.SDK_INT >=
                    android.os.Build.VERSION_CODES.M) {
                if (mp != null) {
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mp.pause();
                            PlaybackParams playbackParams = mp.getPlaybackParams();
                            playbackParams.setSpeed(vSpeed);
                            mp.setPlaybackParams(playbackParams);
                        }
                    });
                }
            }
        }
    }

    public void setAvtar(int picId, ImageView imageView) {

        if (picId == 0) {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.touxiang_img));

        }
        if (picId == UserPicConstant.TYPE_01_01) {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_01));

        }

        if (picId == UserPicConstant.TYPE_01_02) {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_02));

        }

        if (picId == UserPicConstant.TYPE_01_03) {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_03));

        }
        if (picId == UserPicConstant.TYPE_02_01) {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_01));

        }

        if (picId == UserPicConstant.TYPE_02_02) {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_02));

        }

        if (picId == UserPicConstant.TYPE_02_03) {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_03));

        }
        if (picId == UserPicConstant.TYPE_03_01) {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_01));

        }

        if (picId == UserPicConstant.TYPE_03_02) {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_02));

        }

        if (picId == UserPicConstant.TYPE_03_03) {
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_03));

        }

    }

    @Override
    public void onDestroy() {
      //  releaseMediaPlayer();
        if (getSportReceiver != null) {
            getActivity().unregisterReceiver(getSportReceiver);
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (intent != null) {
            getActivity().stopService(intent);
        }

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    private void releaseMediaPlayer() {
        try {
            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            if (mVideoView != null) {
                if (mVideoView.isPlaying()) {
                    mVideoView.stopPlayback();
                }
            }
        } catch (Exception e) {
            Log.e("RacePlayFragment", "releaseMediaPlayer-------e==" + e);
        }
    }

    //挑选列表里面的6位，展示在listView中
    public void choiceUser(List<CheckInCallBackInfo.RaceCheckOutInfo> list, float type) {

        //距离参数 对比
        if (type == 2 | type == 4) {

          /*  Comparator<CheckInCallBackInfo.RaceCheckOutInfo> comparable = new Comparator<CheckInCallBackInfo.RaceCheckOutInfo>() {
                @Override
                public int compare(CheckInCallBackInfo.RaceCheckOutInfo raceCheckOutInfo1, CheckInCallBackInfo.RaceCheckOutInfo raceCheckOutInfo2) {
                    DecimalFormat format = new DecimalFormat("#.00");
                    if (raceCheckOutInfo2.getDistance() != raceCheckOutInfo1.getDistance()) {
                        return (int) (raceCheckOutInfo2.getDistance() - raceCheckOutInfo1.getDistance());
                    }
                    return 0;
                }
            };
            //根据距离排序 距离从大到小
            Collections.sort(list, comparable);*/
            Collections.sort(list, new Comparator<CheckInCallBackInfo.RaceCheckOutInfo>() {
                @Override
                public int compare(CheckInCallBackInfo.RaceCheckOutInfo o1, CheckInCallBackInfo.RaceCheckOutInfo o2) {
                    Float id1 = o1.getDistance();
                    Float id2 = o2.getDistance();
                    return id2.compareTo(id1);
                }
            });
        }


        //卡路里参数 对比
        if (type == 1) {
/*
            Comparator<CheckInCallBackInfo.RaceCheckOutInfo> comparable = new Comparator<CheckInCallBackInfo.RaceCheckOutInfo>() {
                @Override
                public int compare(CheckInCallBackInfo.RaceCheckOutInfo raceCheckOutInfo1, CheckInCallBackInfo.RaceCheckOutInfo raceCheckOutInfo2) {
                    DecimalFormat format = new DecimalFormat("#.00");
                    if (raceCheckOutInfo2.getCalorie() != raceCheckOutInfo1.getCalorie()) {
                        return (int) (raceCheckOutInfo2.getTime() - raceCheckOutInfo1.getTime());
                    }
                    return 0;
                }
            };
            Collections.sort(list, comparable);*/
            Collections.sort(list, new Comparator<CheckInCallBackInfo.RaceCheckOutInfo>() {
                @Override
                public int compare(CheckInCallBackInfo.RaceCheckOutInfo o1, CheckInCallBackInfo.RaceCheckOutInfo o2) {
                    Integer id1 = o1.getCalorie();
                    Integer id2 = o2.getCalorie();
                    return id2.compareTo(id1);
                }
            });
        }

        //时间参数 对比
        if (type == 3) {
/*
            Comparator<CheckInCallBackInfo.RaceCheckOutInfo> comparable = new Comparator<CheckInCallBackInfo.RaceCheckOutInfo>() {
                @Override
                public int compare(CheckInCallBackInfo.RaceCheckOutInfo raceCheckOutInfo1, CheckInCallBackInfo.RaceCheckOutInfo raceCheckOutInfo2) {
                    DecimalFormat format = new DecimalFormat("#.00");
                    if (raceCheckOutInfo2.getTime() != raceCheckOutInfo1.getTime()) {
                        return (int) (raceCheckOutInfo2.getDistance() - raceCheckOutInfo1.getDistance());
                    }
                    return 0;
                }
            };
            //根据距离排序 距离从大到小
            Collections.sort(list, comparable);*/
            Collections.sort(list, new Comparator<CheckInCallBackInfo.RaceCheckOutInfo>() {
                @Override
                public int compare(CheckInCallBackInfo.RaceCheckOutInfo o1, CheckInCallBackInfo.RaceCheckOutInfo o2) {
                    Long id1 = o1.getTime();
                    Long id2 = o2.getTime();
                    return id2.compareTo(id1);
                }
            });
        }
    }

    //平面地图的用户排序
    public void sortUser(List<CheckInCallBackInfo.RaceCheckOutInfo> list) {
       /* Comparator<CheckInCallBackInfo.RaceCheckOutInfo> comparable = new Comparator<CheckInCallBackInfo.RaceCheckOutInfo>() {
            @Override
            public int compare(CheckInCallBackInfo.RaceCheckOutInfo raceCheckOutInfo1, CheckInCallBackInfo.RaceCheckOutInfo raceCheckOutInfo2) {
                DecimalFormat format = new DecimalFormat("#.00");
                if (raceCheckOutInfo2.getDistance() != raceCheckOutInfo1.getDistance()) {
                    return (int) (raceCheckOutInfo2.getDistance() - raceCheckOutInfo1.getDistance());
                }
                return 0;
            }
        };
        //根据距离排序 距离从大到小
        Collections.sort(list, comparable);*/
        //创建和list个数一样的长度数组
        //目前用户少 后面优化 取出六位用户
        temps = new float[list.size()];
        picIds = new int[list.size()];
        userNames = new String[list.size()];
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                temps[i] = list.get(i).getDistance();
                picIds[i] = list.get(i).getPicId();
                userNames[i] = list.get(i).getUsername();
            }

        } else {
            temps[0] = distance;
            picIds[0] = SharedPreferenceUtils.get(YDApplication.getInstance(), "picId", 0);
            userNames[0] = SharedPreferenceUtils.get(YDApplication.getInstance(), "username", "");
        }
        //刷新位置
        if (runWayView != null) {
            runWayView.setProgress(temps, picIds, userNames);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void HeartRate(HeartRateEvent event) {
        switch (event.action) {
            case HeartRateEvent.HEART_RATE:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        heartrate = event.heartRate;
                        live_top_running_bmp.setText(heartrate + "");
                    }
                });
                break;
        }
    }

    @OnClick({R.id.live_top_running_back, R.id.videoView, R.id.image_big, R.id.image_big_01, R.id.race_time, R.id.race_distance, R.id.race_cal, R.id.race_dp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.live_top_running_back:
                live_top_running_kcal.setText("0");
                live_top_running_bmp.setText("0");
                live_top_running_distance.setText("0");
                live_top_running_time.setText("0");

                if (mSocket != null) {
                    String msg = logoutData();
                    mSocket.send(msg);
                }
                removeFragment();
                break;
            case R.id.videoView:
            case R.id.image_big:
            case R.id.image_big_01:
                isFull = !isFull;
                changVideo(isFull);
                if (isFull) {
                    runWayView.setVisibility(View.GONE);
                } else {
                    runWayView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.race_cal:
                type = 1;
                race_cal.setBackground(getActivity().getDrawable(R.drawable.grey_corner_white));
                race_distance.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                race_dp.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                race_time.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                break;
            case R.id.race_distance:
                type = 2;
                race_cal.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                race_distance.setBackground(getActivity().getDrawable(R.drawable.grey_corner_white));
                race_dp.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                race_time.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                break;

            case R.id.race_dp:
                type = 4;
                race_cal.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                race_distance.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                race_dp.setBackground(getActivity().getDrawable(R.drawable.grey_corner_white));
                race_time.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                break;


            case R.id.race_time:
                type = 3;
                race_cal.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                race_distance.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                race_dp.setBackground(getActivity().getDrawable(R.drawable.grey_corner));
                race_time.setBackground(getActivity().getDrawable(R.drawable.grey_corner_white));
                break;
        }

    }


    private void removeFragment() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mVideoView != null) {
            if (mVideoView.isPlaying()) {
                mVideoView.pause();
            }
            mVideoView.stopPlayback();
        }
        if (mMediaPlayer != null) {

            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;


            } catch (Exception e) {
                Log.e("releaseMediaPlayer", "releaseMediaPlayer-------e==" + e);
            }

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


    public void changVideo(boolean fullscreen) {

        if (fullscreen) {//设置RelativeLayout的全屏模式
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mVideoView.setLayoutParams(layoutParams);
            image_big.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(280, 150);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.rightMargin = 80;
            lp.bottomMargin = 60;
            image_big.setLayoutParams(lp);
            image_big_01.setVisibility(View.GONE);

        } else {//设置RelativeLayout的窗口模式
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            image_big.setLayoutParams(layoutParams);
            image_big.setVisibility(View.GONE);
            image_big_01.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(280, 150);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.rightMargin = 80;
            lp.bottomMargin = 60;
            mVideoView.setLayoutParams(lp);

        }
    }


    // 发送心跳包
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            String message = sendData();
            if (mSocket != null) {
                mSocket.send(message);
            }
            sendTime = System.currentTimeMillis();
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    // 拉取数据
    private Runnable getDataRunnable = new Runnable() {
        @Override
        public void run() {
            String message = getData();
            if (mSocket != null) {
                mSocket.send(message);
            }
            sendTime = System.currentTimeMillis();
            mHandler.postDelayed(this, 1000); //每隔一定的时间，对长连接进行一次心跳检测
        }
    };

    private WebSocket mSocket;
    //private static final String BASE_URL_WS = "ws://120.27.225.193:8080/ShEway/realtime";

    private void setListener() {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .build();

        Request request = new Request.Builder().url(ApiCenter.BASE_URL_WS).build();
        EchoWebSocketListener socketListener = new EchoWebSocketListener();

        // 刚进入界面，就开启心跳检测
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);


        mOkHttpClient.newWebSocket(request, socketListener);
        mOkHttpClient.dispatcher().executorService().shutdown();

    }

    //选中显示详情
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (i != index) {
            raceCheckOutInfo = list.get(i);
            race_dialog_img = alertDialogView.findViewById(R.id.race_dialog_img);
            race_dialog_distance = alertDialogView.findViewById(R.id.race_dialog_distance);
            race_dialog_name = alertDialogView.findViewById(R.id.race_dialog_name);
            race_dialog_time = alertDialogView.findViewById(R.id.race_dialog_time);
            race_dialog_kcal = alertDialogView.findViewById(R.id.race_dialog_kcal);
            race_dialog_ok = alertDialogView.findViewById(R.id.race_dialog_dismiss);
            race_dialog_add = alertDialogView.findViewById(R.id.race_dialog_add);
            race_dialog_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.toast(getActivity(), "to do");
                }
            });
       /* race_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });*/

            int pic = raceCheckOutInfo.getPicId();
            setAvtar(pic, race_dialog_img);
            race_dialog_distance.setText(String.format("%1.2f", raceCheckOutInfo.getDistance()));
            race_dialog_name.setText(raceCheckOutInfo.getUsername());
            //int time = Integer.parseInt(raceCheckOutInfo.getTime());
            int time = (int) raceCheckOutInfo.getTime();
            int seconds = time % 60;
            int minutes = (time / 60) % 60;
            String timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
            race_dialog_time.setText(timeStr);
            race_dialog_kcal.setText(raceCheckOutInfo.getCalorie() + "");
            dialog.show();
        }
    }

    public class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            mSocket = webSocket;
            LogUtils.d("----连接成功！");
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            LogUtils.d("----receive bytes:" + bytes.hex());
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            LogUtils.d("----服务器端发送来的信息：" + text);
            String text_json = text.trim();
            Gson g = new Gson();
            CheckInCallBackInfo s = g.fromJson(text_json, CheckInCallBackInfo.class);
            if (s.getMsgId() == 30002) {
                String message = getData();
                if (mSocket != null) {
                    mSocket.send(message);
                }
            }
            if (s.getMsgId() == 30004) {

                list = s.getData();


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (list.size() > 0) {
                            choiceUser(list, type);
                            //刷新位置
                            sortUser(list);
                            //找到自己在当前list中的位置
                            for (int i = 0; i < list.size(); i++) {
                                if (name.equals(list.get(i).getUsername())) {
                                    index = i;
                                }
                            }

                            listViewAdapter = new ListViewAdapter(getActivity(), list, index, type);
                            if(listViewAdapter==null){
                                return;
                            }
                            raceListView.setAdapter(listViewAdapter);
                            raceListView.setOnItemClickListener(RacePlayFragment.this);
                        }
                    }
                });

            }

            if (s.getMsgId() == 30006) {
                LogUtils.d("-删除---服务器端发送来的信息：" + text);
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            LogUtils.d("----closed:" + reason);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            LogUtils.d("-----closing:" + reason + "-----code==" + code);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            LogUtils.d("------failure:" + t.getMessage());
        }
    }

    private String sendData() {

        String json = "";
        Gson gson = new Gson();
        RaceCheckInfo.CheckInfo checkInfo = new RaceCheckInfo.CheckInfo();
        checkInfo.setDistance(distance);
        checkInfo.setCalorie(calorie);
        checkInfo.setHeartrate(heartrate);
        checkInfo.setIncline(incline);
        checkInfo.setDeviceType(deviceType);
        checkInfo.setRpm(rpm);
        checkInfo.setSpeed(speed);
        checkInfo.setTime(time + "");
        RaceCheckInfo raceCheckInfo = new RaceCheckInfo();
        raceCheckInfo.setMsgId(30001);
        raceCheckInfo.setRoomId(roomId);
        raceCheckInfo.setUserId(user_id);
        raceCheckInfo.setData(checkInfo);
        json = gson.toJson(raceCheckInfo);

        Log.e("TAG", "sendData: " + json);
        return json;
    }

    private String getData() {

        String json = "";
        Map<String, Object> map = new HashMap<>();
        map.put("msgId", "30003");
        map.put("roomId", roomId);
        Gson gson = new Gson();

        json = gson.toJson(map);
        Log.e("TAG", "getData: " + json);
        return json;
    }

    //退出房间
    private String logoutData() {

        String json = "";
        Map<String, Object> map = new HashMap<>();
        map.put("msgId", "30005");
        map.put("roomId", roomId);
        map.put("userId", user_id);
        Gson gson = new Gson();
        json = gson.toJson(map);
        Log.e("TAG", "logoutData: " + json);
        return json;

    }


}
