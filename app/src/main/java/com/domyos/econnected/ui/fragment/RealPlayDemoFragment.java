package com.domyos.econnected.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
import com.domyos.econnected.utils.MyVideoView;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.ew.ble.library.entity.BikeSportData;
import com.ew.ble.library.entity.TreadmillSportData;
import com.kuaige.player.listener.VideoListener;
import com.kuaige.player.player.VideoPlayer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class RealPlayDemoFragment extends BaseFragment {

	private String videoPath;

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
	@BindView(R.id.video)
	SurfaceView mVideoView;

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
	private int user_id;

	private IjkMediaPlayer mPlayer;


	public RealPlayDemoFragment(int videoId) {
		this.videoId = videoId;
	}

	@Override
	protected int getRootViewLayoutId() {
		return R.layout.fragment_real_demo_play;
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
		} else if(videoId == 1){
			videoPath = "/system/media/yali.mp4";
		}

		mVideoView.getHolder().addCallback(callback);

		if (bundle != null) {
			refreshUI(bundle);

		} else {
			if (YDApplication.getInstance().get("ble_connect") != null && (boolean) YDApplication.getInstance().get("ble_connect")) {

				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						mPlayer.pause();
					}
				}, 800);
			} else {
				if (mPlayer != null) {
					mPlayer.start();
					Log.e("RealPlayDemoFragment", "---------mPlayer!=null------");

				}
			}
			live_top_running_distance.setText("0.0");
			live_top_running_kcal.setText("0");
			live_top_running_time.setText("00:00");
			live_top_running_speed.setText("0.0");
			if (live_top_running_rpm != null) {
				live_top_running_rpm.setText("0");
			} else {
				live_top_running_incline.setText("0");
			}

		}
	}

	private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			createPlayer();
			mPlayer.setDisplay(mVideoView.getHolder());
			Log.e("RealPlayDemoFragment", "---------surfaceCreated------");
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			Log.e("RealPlayDemoFragment", "---------surfaceChanged------");
			mPlayer.setDisplay(mVideoView.getHolder());

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.e("RealPlayDemoFragment", "---------surfaceDestroyed------");

			if (mVideoView != null) {
				mVideoView.getHolder().removeCallback(callback);
				mVideoView = null;
			}
		}
	};


	private void createPlayer() {
		if (mPlayer == null) {
            /*// 设置倍速，应该是0-2的float类型，可以测试一下
            mPlayer.setSpeed(1.0f);*/
			mPlayer = new IjkMediaPlayer();
		}else {
			mPlayer = null;
			mPlayer = new IjkMediaPlayer();

		}

		// 设置调用prepareAsync不自动播放，即调用start才开始播放
		mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1);
		mPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1L);
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mPlayer.setDataSource(videoPath);
			Log.e("RealPlayDemoFragment", "---------videoPath------" + videoPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {

				Log.e("RealPlayDemoFragment", "---------videoPath------" + i+"----i1----"+i1);

				return false;
			}
		});
		mPlayer.prepareAsync();

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
					if (mPlayer.isPlaying()) {
						mPlayer.pause();
					}

				} else {
					if (!mPlayer.isPlaying()) {
						mPlayer.start();
					}
					if (mPlayer != null) {
						if (!mPlayer.isPlaying()) {
							mPlayer.start();
						}

						if (speed < 4 && speed > 0) {
							videoSpeed = 0.5f;
							mPlayer.setSpeed(videoSpeed);
						}
						if (speed < 6 && speed >= 4) {
							mPlayer.setSpeed(videoSpeed);
						}
						if (speed < 8 && speed >= 6) {
							videoSpeed = 1.5f;
							mPlayer.setSpeed(videoSpeed);

						}
						if (speed < 22 && speed >= 8) {
							videoSpeed = 2.0f;
							mPlayer.setSpeed(videoSpeed);

						}
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

					if (timer != null) {
						timer.cancel();
						timer = null;

					}
					timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							if(mPlayer!=null){
								mPlayer.pause();
							}

						}
					}, 1000);

					live_top_running_speed.setText("0");
				} else {

					if (mPlayer != null) {
						if (!mPlayer.isPlaying()) {
							mPlayer.start();
						}

						if (rpm < 60 && rpm > 0) {
							videoSpeed = 0.8f;
							mPlayer.setSpeed(videoSpeed);
						}
						if (rpm < 90 && rpm >= 60) {
							videoSpeed = 1.0f;
							mPlayer.setSpeed(videoSpeed);
						}
						if (rpm < 120 && rpm >= 90) {
							videoSpeed = 1.5f;
							mPlayer.setSpeed(videoSpeed);
						}
						if (rpm >= 120) {
							videoSpeed = 2.0f;
							mPlayer.setSpeed(videoSpeed);
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



    /*//跑步机播放倍速
    private void setPlayerSpeed(MediaPlayer mp, float speed, float vSpeed) {

        if (speed != currentSpeed && mp != null) {
            currentSpeed = speed;

            if (android.os.Build.VERSION.SDK_INT >=
                    android.os.Build.VERSION_CODES.M) {
                //mMediaPlayer = mp;
                PlaybackParams playbackParams = mp.getPlaybackParams();
                playbackParams.setSpeed(vSpeed);
                mp.setPlaybackParams(playbackParams);
            }
        }
    }*/

  /*  //单车播放倍速
    private void setBikePlayerSpeed(MediaPlayer mp, int rpm, float vSpeed) {

        if (rpm != currentRpm && mp != null) {
            currentRpm = rpm;
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    PlaybackParams playbackParams = mp.getPlaybackParams();
                    playbackParams.setSpeed(vSpeed);
                    mp.setPlaybackParams(playbackParams);
                    mMediaPlayer.start();
                }

            } catch (Exception e) {
                Log.e("setBikePlayerSpeed", "------------e==" + e);
            }

        }
    }*/

	@Override
	public void onDestroy() {
		releaseMediaPlayer();
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

		super.onDestroy();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

	}

	private void releaseMediaPlayer() {
		if (mPlayer != null) {

			try {
				if (mPlayer.isPlaying()) {
					mPlayer.stop();
				}
				mPlayer.reset();
				mPlayer.release();
				mPlayer = null;


			} catch (Exception e) {
				Log.e("releaseMediaPlayer", "releaseMediaPlayer---111----e==" + e);
			}

		}
		IjkMediaPlayer.native_profileEnd();
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

		if (mPlayer != null) {

			try {
				if (mPlayer.isPlaying()) {
					mPlayer.stop();
				}
				mPlayer.reset();
				mPlayer.release();
				mPlayer = null;
				IjkMediaPlayer.native_profileEnd();

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


}
