package com.domyos.econnected.ui.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aliyun.player.AliPlayer;
import com.aliyun.player.AliPlayerFactory;
import com.aliyun.player.IPlayer;
import com.aliyun.player.bean.ErrorInfo;
import com.aliyun.player.source.UrlSource;
import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.constant.UserPicConstant;
import com.domyos.econnected.event.EquipmentEvent;
import com.domyos.econnected.event.HeartRateEvent;
import com.domyos.econnected.event.UIEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.utils.LogUtils;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.ew.ble.library.entity.BikeSportData;
import com.ew.ble.library.entity.TreadmillSportData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;


public class ZhiBoShowFragment extends BaseFragment {

    /* @BindView(R.id.show_btn)
     Button show_btn;*/
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
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

    private boolean isLive;
    private AliPlayer aliyunVodPlayer;
    private String url = "rtmp://pull.yuedongtech.com/live/02?auth_key=1572856304-0-0-2adb556a2be7eec3155c5be9239e85c3";
    private ProgressDialog progressDialog;
    private String distanceStr, timeStr, kcalStr, speedStr, inclineStr, bpmStr, rpmStr;
    private Bundle bundle;

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_zhi_bo_show;
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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.domyos.econnected.SEND_SPORT_DATA");
        getActivity().registerReceiver(getSportReceiver, intentFilter);
        EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_VIDEO_FRAGMENT));
        aliyunVodPlayer = AliPlayerFactory.createAliPlayer(getActivity());
        initPlayer();
        prepareAndStart();
        //progressDialog = new ProgressDialog(getActivity());
        progressBar.setVisibility(View.VISIBLE);
        if (bundle != null) {
            refreshUI(bundle);
        } else {
            LogUtils.e("---------ZhiBoShowFragment----bundle null---");
        }

      /*  getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShowProgressDialog.showDialog(progressDialog, "...", getActivity());
            }
        });*/
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

                        live_top_running_bmp.setText("0");
                        live_top_running_speed.setText("0");
                        live_top_running_distance.setText("0");
                        live_top_running_kcal.setText("0");
                        live_top_running_time.setText("0");


                    }
                });

                break;
        }
    }

    private void prepareAndStart() {
        UrlSource urlSource = new UrlSource();
        urlSource.setUri(url);
        aliyunVodPlayer.setDataSource(urlSource);
        aliyunVodPlayer.prepare();
        // 开始播放。
        aliyunVodPlayer.start();
    }

    private void initPlayer() {

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.e("sh_1111", "surfaceCreated");
                aliyunVodPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                aliyunVodPlayer.redraw();
                Log.e("sh_1111", "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.e("sh_1111", "surfaceDestroyed");
                aliyunVodPlayer.setDisplay(null);
            }
        });
        aliyunVodPlayer.setOnCompletionListener(new IPlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {

                //播放完成事件
                Log.e("sh_1111", "播放完成事件");
            }
        });
        aliyunVodPlayer.setOnErrorListener(new IPlayer.OnErrorListener() {
            @Override
            public void onError(ErrorInfo errorInfo) {
                progressBar.setVisibility(View.VISIBLE);
                Log.e("sh_1111", errorInfo.getCode() + "==code--出错事件--,ErrorInfo==" + errorInfo.getMsg());
                //出错事件
            }
        });
        aliyunVodPlayer.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                //准备成功事件
                progressBar.setVisibility(View.GONE);
             /*   if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/

               /* getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowProgressDialog.disProgressDialog(progressDialog);
                    }
                });*/
                Log.e("sh_1111", "准备成功事件");
            }
        });
    }

    @OnClick({/*R.id.show_btn,*/ R.id.live_top_running_back})
    public void onClick(View view) {

        switch (view.getId()) {
            /*case R.id.show_btn:
                if (isLive) {
                    show_btn.setText("pause");
                    aliyunVodPlayer.pause();

                } else {
                    show_btn.setText("start");
                    aliyunVodPlayer.start();
                }
                isLive = !isLive;

                break;*/

            case R.id.live_top_running_back:
                if (aliyunVodPlayer != null) {
                    aliyunVodPlayer.release();
                }
                //homepager形式
                // EventBus.getDefault().post(new CurrEvent(CurrEvent.ACTION_BACK_CURR));
                getFragmentManager().
                        beginTransaction().remove(this).commitAllowingStateLoss();
                break;
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

    @Override
    public void onDestroyView() {
        if (aliyunVodPlayer != null) {
            aliyunVodPlayer.release();
        }

        if (getSportReceiver != null) {
            getActivity().unregisterReceiver(getSportReceiver);
        }

     /*   if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }*/
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        if (aliyunVodPlayer != null) {
            aliyunVodPlayer.release();
        }
       /* if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }*/
        super.onDestroy();
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
