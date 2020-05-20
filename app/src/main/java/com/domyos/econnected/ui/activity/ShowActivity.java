package com.domyos.econnected.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentTransaction;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.constant.UserPicConstant;
import com.domyos.econnected.constant.Variable;
import com.domyos.econnected.event.EquipmentEvent;
import com.domyos.econnected.event.HeartRateEvent;
import com.domyos.econnected.event.ThirdAppEvent;
import com.domyos.econnected.event.UIEvent;
import com.domyos.econnected.event.UserInfoEvent;
import com.domyos.econnected.heartRate.HeartRateManager;
import com.domyos.econnected.heartRate.HeartRateService;
import com.domyos.econnected.heartRate.ScanHeartRateFragment;
import com.domyos.econnected.receiver.HeartRateReceiver;
import com.domyos.econnected.service.BackgroundService;
import com.domyos.econnected.ui.BaseActivity;
import com.domyos.econnected.ui.fragment.MultimediaFragment;
import com.domyos.econnected.ui.fragment.RaceFragment;
import com.domyos.econnected.ui.fragment.RealListFragment;
import com.domyos.econnected.ui.fragment.RunningMainFragment;
import com.domyos.econnected.ui.fragment.VideoListFragment;
import com.domyos.econnected.ui.fragment.ZhiBoListFragment;
import com.domyos.econnected.ui.fragment.running.SportEquipmentFragment;
import com.domyos.econnected.ui.fragment.user.LoginFragment;
import com.domyos.econnected.ui.fragment.user.RegisterFragment;
import com.domyos.econnected.ui.fragment.user.SetBirthdayFragment;
import com.domyos.econnected.ui.fragment.user.SetHeightFragment;
import com.domyos.econnected.ui.fragment.user.SetWeightFragment;
import com.domyos.econnected.ui.fragment.user.UserClothesFragment;
import com.domyos.econnected.ui.fragment.user.UserInfoFragment;
import com.domyos.econnected.utils.LanguageUtils;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.domyos.econnected.utils.ShowProgressDialog;
import com.ew.ble.library.callback.EwEquipmentManagerCallback;
import com.ew.ble.library.command.BluetoothBikeController;
import com.ew.ble.library.command.BluetoothTmController;
import com.ew.ble.library.entity.BikeSportData;
import com.ew.ble.library.entity.TreadmillDeviceInfo;
import com.ew.ble.library.entity.TreadmillSportData;
import com.ew.ble.library.equipment.EWEquipment;
import com.ew.ble.library.equipment.EWTMEquipment;
import com.ew.ble.library.equipment.EwEquipmentManager;
import com.ew.ble.library.service.BluetoothService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;

public class ShowActivity extends BaseActivity implements HeartRateService.HeartRateCallBack {
	@BindView(R.id.btn_lianjie)
	LinearLayout btn_lianjie;
	@BindView(R.id.userIcon)
	ImageView btn_login_show;
	@BindView(R.id.userNameText)
	TextView userNameText;
	@BindView(R.id.equipmentImg)
	ImageView equipmentImg;
	@BindView(R.id.equipmentNameText)
	TextView equipmentNameText;
/*	@BindView(R.id.btn_heart_rate)
	LinearLayout btn_heart_rate;
	@BindView(R.id.heart_rate)
	ImageView heart_rate;
	@BindView(R.id.heart_rate_text)
	TextView heart_rate_text;*/
	@BindView(R.id.language_img)
	ImageView language_img;


	private LoginFragment loginFragment = new LoginFragment();

	private SportEquipmentFragment sportEquipmentFragment = new SportEquipmentFragment();

	private ScanHeartRateFragment scanHeartRateFragment = new ScanHeartRateFragment();

	private UserInfoFragment userInfoFragment = new UserInfoFragment();

	private SetBirthdayFragment setBirthdayFragment = new SetBirthdayFragment();
	private SetHeightFragment setHeightFragment = new SetHeightFragment();
	private SetWeightFragment setWeightFragment = new SetWeightFragment();
	private UserClothesFragment userClothesFragment = new UserClothesFragment();
	private RegisterFragment registerFragment = new RegisterFragment();



	private static EWEquipment mEwEquipment;
	boolean isLogin;
	private Intent heartRateIntent;
    //private HeartRateService heartRateService;
    private BackgroundService backgroundService;
	private HeartRateReceiver heartRateReceiver;
	private boolean isEnglish = false;
	private Timer timer;
	private ProgressDialog progressDialog;
	private float dis = 0;
	private boolean isConnectedDevice = false;
	private boolean isToYoutube = false;
	//运动manager
	private BikeSportData bikeSportData;


	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothManager mBluetoothManager = null;


	private RunningMainFragment runningMainFragment = new RunningMainFragment();
	private ZhiBoListFragment zhiBoListFragment = new ZhiBoListFragment();
	private VideoListFragment localVideoFragment = new VideoListFragment();
	private RaceFragment raceFragment = new RaceFragment();
	private RealListFragment realListFragment = new RealListFragment();


	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_show;
	}

	@Override
	protected void initSomething() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.domyos.econnected.SEND_HEART_RATE");
		intentFilter.addAction("com.domyos.econnected.SEND_SPORT_DATA");
		heartRateReceiver = new HeartRateReceiver();
		registerReceiver(heartRateReceiver, intentFilter);
		heartRateIntent = new Intent(getApplicationContext(), BackgroundService.class);
		bindService(heartRateIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
		blueisenable();
		progressDialog = new ProgressDialog(this);
		if (!permissionCheck()) {
			if (Build.VERSION.SDK_INT >= 23) {
				ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
			} else {
				showNoPermissionTip(getString(noPermissionTip[mNoPermissionIndex]));
				finish();
			}
		}

		isEnglish = SharedPreferenceUtils.get(YDApplication.getInstance(), "isEnglish", false);
		int picId = SharedPreferenceUtils.get(this, "picId", 0);
		setAvtar(picId);
		if (isEnglish) {
			language_img.setImageResource(R.drawable.ch);
		} else {
			language_img.setImageResource(R.drawable.am);
		}

		if (EwEquipmentManager.getInstance().isConnected()) {
			btn_lianjie.setClickable(false);
			String ew = SharedPreferenceUtils.get(this, "ewName", "");
			if (!ew.equals("") && ew != null) {
				equipmentNameText.setText(ew);
				if (ew.contains("EW-JS")) {
					equipmentImg.setImageDrawable(getDrawable(R.drawable.bike));
				} else if (ew.contains("EW-EP")) {
					equipmentImg.setImageDrawable(getDrawable(R.drawable.icon_equipment_ep));
				} else if (ew.contains("EW-TM")) {
					equipmentImg.setImageDrawable(getDrawable(R.drawable.treadmill));

				}
			}
		}
		//蓝牙未连接标志
		YDApplication.getInstance().put("ble_connect", false);
/*
		btn_heart_rate.setClickable(true);
		heart_rate.setImageResource(R.mipmap.icon_heart_rate_unconnected);
		heart_rate_text.setText(R.string.heart_rate_bracelet);*/

	}

	/**
	 * 判断蓝牙是否开启
	 *
	 * @return
	 */
	public void blueisenable() {
		mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}

	public static String timeParse(long duration) {
		String time = "";
		long minute = duration / 60000;
		long seconds = duration % 60000;
		long second = Math.round((float) seconds / 1000);
		if (minute < 10) {
			time += "0";
		}
		time += minute + ":";
		if (second < 10) {
			time += "0";
		}
		time += second;
		return time;
	}

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
            backgroundService = ((BackgroundService.LocalBinder) service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
            backgroundService = null;
		}
	};


	@Subscribe(threadMode = ThreadMode.MAIN)
	public void changePage(UIEvent event) {
		switch (event.action) {
			case UIEvent.ACTION_LOGIN_FRAGMENT:
				// 偏好设置，当用户登录后，下次进入不需要再次登录
				isLogin = true;
				userNameText.setText(event.eventInfo.getType());
				int picId = SharedPreferenceUtils.get(this, "picId", 0);
				setAvtar(picId);

				break;
			case UIEvent.ACTION_SHOW_LOGINFRAGMENT:
				showLoginFragment();
				break;
			case UIEvent.ACTION_EQUIPMENT_NAME:
				break;
			case UIEvent.ACTION_RUN_EQUIPMENT:
				String ew_name = (String) YDApplication.getInstance().get("ew_name");
				if (ew_name.contains("EW-JS")) {
					equipmentImg.setImageDrawable(getDrawable(R.drawable.bike));
				} else if (ew_name.contains("EW-EP")) {
					equipmentImg.setImageDrawable(getDrawable(R.drawable.icon_equipment_ep));

				} else if (ew_name.contains("EW-TM")) {

					equipmentImg.setImageDrawable(getDrawable(R.drawable.treadmill));
				}
				btn_lianjie.setClickable(false);

				break;
			case UIEvent.ACTION_REGISTER_FRAGMENT:

				showRegisterFragment();
				break;
			case UIEvent.ACTION_AGE_FRAGMENT:
				showAgeFragment();
				break;

			case UIEvent.ACTION_HEIGHT_FRAGMENT:
				showHeightFragment();
				break;

			case UIEvent.ACTION_WEIGHT_FRAGMENT:
				showWeightFragment();

				break;

			case UIEvent.ACTION_CLOTHES_FRAGMENT:
				showClothesFragment();

				break;


		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void equipmentEvent(EquipmentEvent event) {
		switch (event.action) {
			case EquipmentEvent.ACTION_EQUIPMENT_SEARCH:
				mEwEquipment = event.equipmentInfo.getEwEquipment();
				connectEquipment(mEwEquipment);
				break;
			case EquipmentEvent.ACTION_HEART_RATE_ITEM_CLICK:
				BluetoothDevice device = event.bluetoothDevice;
				//heartRateService.connect(device.getAddress(), this);
				/*heart_rate_text.setText(device.getName());
				SharedPreferenceUtils.put(this, "heart", device.getName());
				YDApplication.getInstance().put("heart",device.getName());*/
				break;
			case EquipmentEvent.ACTION_EQUIPMENT_CONNECTED:
				SharedPreferenceUtils.put(this, "ewName", event.ewEquipment.getPeripheral().getName());
				break;
			case EquipmentEvent.ACTION_EQUIPMENT_DISCONNECT:
				showDialog(getString(R.string.reconnect));
				equipmentNameText.setText(R.string.my_device);
				equipmentImg.setImageDrawable(getDrawable(R.drawable.icon_bluetooth_off));
				break;
		}

	}


	@Subscribe(threadMode = ThreadMode.MAIN)
	public void HeartRate(HeartRateEvent event) {
		switch (event.heartRate) {
			case HeartRateEvent.DISCONNTED_HEART_RATE:
				YDApplication.getInstance().put("heart",null);
				/*btn_heart_rate.setClickable(true);
				heart_rate.setImageResource(R.mipmap.icon_heart_rate_unconnected);
				heart_rate_text.setText(R.string.heart_rate_bracelet);*/
				break;
		}
	}


	@Subscribe(threadMode = ThreadMode.MAIN)
	public void getUserEvent(UserInfoEvent event) {
		switch (event.action) {
			case UserInfoEvent.ACTION_LOGOUT_SUCCESS:
				isLogin = false;
				SharedPreferenceUtils.put(this, "isLogin", false);
				SharedPreferenceUtils.put(this, "name", "");
				SharedPreferenceUtils.put(this, "picId", 0);
				userNameText.setText("");
				btn_login_show.setImageDrawable(getDrawable(R.drawable.touxiang_img));

				break;

			case UserInfoEvent.ACTION_USER_BACK:
				int picId = SharedPreferenceUtils.get(this, "picId", 0);
				setAvtar(picId);
				break;
		}
	}


	private void showRegisterFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, registerFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}

	private void showClothesFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, userClothesFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}

	private void showWeightFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, setWeightFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}

	private void showHeightFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, setHeightFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();

	}

	private void showAgeFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, setBirthdayFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}

	private void showLoginFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, loginFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}

	private void showLogoutFragment() {

		getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, userInfoFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();

	}


	public void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.create();
		builder.setMessage(msg);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
				if (msg.equals("用户未登录")) {
					EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_SHOW_LOGINFRAGMENT));
				}
				if (msg.equals("断开设备")) {
					if (EwEquipmentManager.getInstance().isConnected()) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								btn_lianjie.setClickable(true);
								isConnectedDevice = false;
								equipmentNameText.setText(R.string.my_device);
								equipmentImg.setImageDrawable(getDrawable(R.drawable.icon_bluetooth_off));
								EventBus.getDefault().post(new EquipmentEvent(EquipmentEvent.ACTION_EQUIPMENT_DISCONNECT, mEwEquipment));
								try {
									EwEquipmentManager.getInstance().cancelData(mEwEquipment);
								} catch (Exception e) {
									e.printStackTrace();
								}

								YDApplication.getInstance().put("ew_name", null);
								YDApplication.getInstance().put("ble_connect", false);
							}

						});
					}
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onResume() {

		super.onResume();
		isToYoutube = false;
		if (!permissionCheck()) {
			if (Build.VERSION.SDK_INT >= 23) {
				ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
			} else {
				showNoPermissionTip(getString(noPermissionTip[mNoPermissionIndex]));
				finish();
			}
		}
		isLogin = SharedPreferenceUtils.get(this, "isLogin", false);
		String name = SharedPreferenceUtils.get(this, "name", "");
		if (isLogin) {
			userNameText.setText(name);
		} else {
			userNameText.setText("");
		}

		if (EwEquipmentManager.getInstance().isConnected()) {
			btn_lianjie.setClickable(false);
			String ew = SharedPreferenceUtils.get(this, "ewName", "");
			if (!ew.equals("") && ew != null) {
				equipmentNameText.setText(ew);
				if (ew.contains("EW-JS")) {
					equipmentImg.setImageDrawable(getDrawable(R.drawable.bike));
				} else if (ew.contains("EW-EP")) {
					equipmentImg.setImageDrawable(getDrawable(R.drawable.icon_equipment_ep));
				} else if (ew.contains("EW-TM")) {
					equipmentImg.setImageDrawable(getDrawable(R.drawable.treadmill));
				}
			}
		}
		/*String heartName = (String)YDApplication.getInstance().get("heart");
		if (heartName!=null) {
			btn_heart_rate.setClickable(false);
			heart_rate_text.setText(heartName);
			heart_rate.setImageResource(R.mipmap.icon_heart_rate_connected);
		} else {
			btn_heart_rate.setClickable(true);
			heart_rate.setImageResource(R.mipmap.icon_heart_rate_unconnected);
			heart_rate_text.setText(R.string.heart_rate_bracelet);

		}*/


	}

	@Override
	protected void onStop() {
		super.onStop();
		String heartName = SharedPreferenceUtils.get(this, "heart", "");
		if (!heartName.equals("")) {
			SharedPreferenceUtils.put(this, "heart", heartName);
		} else {
			SharedPreferenceUtils.put(this, "heart", "");

		}

	}

	private void showNoPermissionTip(String tip) {
		Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
	}

	private int mNoPermissionIndex = 0;
	private final int PERMISSION_REQUEST_CODE = 1;
	private final String[] permissionManifest = {
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.CAMERA,
			Manifest.permission.BLUETOOTH,
			Manifest.permission.RECORD_AUDIO,
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE
	};
	private final int[] noPermissionTip = {
			R.string.no_coarse_location_permission,
			R.string.no_fine_location_permission,
			R.string.no_camera_permission,
			R.string.no_record_bluetooth_permission,
			R.string.no_record_audio_permission,
			R.string.no_read_phone_state_permission,
			R.string.no_write_external_storage_permission,
			R.string.no_read_external_storage_permission,
	};

	private boolean permissionCheck() {
		int permissionCheck = PackageManager.PERMISSION_GRANTED;
		String permission;
		for (int i = 0; i < permissionManifest.length; i++) {
			permission = permissionManifest[i];
			mNoPermissionIndex = i;
			if (PermissionChecker.checkSelfPermission(this, permission)
					!= PermissionChecker.PERMISSION_GRANTED) {
				permissionCheck = PackageManager.PERMISSION_DENIED;
			}
		}
		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			return false;
		} else {
			return true;
		}
	}


	@OnClick({/*R.id.btn_main_map,*/ R.id.userIcon/*, R.id.btn_heart_rate*/, R.id.language_img, R.id.btn_lianjie, R.id.btn_dianbo, R.id.btn_zhibo, R.id.btn_yundong, R.id.btn_jingsai, R.id.btn_shijing})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_dianbo:
				showVideoFragment();
				break;
			case R.id.btn_zhibo:
				showMultimedia();
				//  showZhiBoFragment();
              /*
                2020/03/09注释  增加多媒体页面
                // 通过包名获取要跳转的app，创建intent对象
                Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                // 这里如果intent为空，就说名没有安装要跳转的应用嘛
                if (i != null) {
                    startActivity(i);
                    isToYoutube = true;
                } else {
                    // 没有安装要跳转的app应用，提醒一下
                }*/
				break;
			case R.id.btn_yundong:
				showRunningFragment();
				break;
			case R.id.btn_jingsai:
				showJingSai();
				break;
			case R.id.btn_shijing:
				showShijing();
				break;
			case R.id.btn_lianjie:
				if (!EwEquipmentManager.getInstance().isConnected()) {
					showEWEquipment();
				} else {
					showDialog(getString(R.string.dis_device));
				}
				break;
			case R.id.userIcon:
				isLogin = SharedPreferenceUtils.get(this, "isLogin", false);
				if (isLogin) {
					showLogoutFragment();
				} else {
					showLoginFragment();
				}
				break;
          /*  case R.id.btn_main_map:
                //谷歌地图
                *//*Intent it = new Intent(ShowActivity.this, GoogleMapActivity.class);
                startActivity(it);*//*
                Intent it = new Intent(ShowActivity.this, GoogleMapDemoActivity.class);
                startActivity(it);
                break;*/
		/*	case R.id.btn_heart_rate:
				showHeartRate();
				break;*/
			case R.id.language_img:
				if (isEnglish) {
					isEnglish = false;
					language_img.setImageResource(R.drawable.am);
					LanguageUtils.updateLocale(getApplicationContext(), Locale.SIMPLIFIED_CHINESE);
				} else {
					isEnglish = true;
					language_img.setImageResource(R.drawable.ch);
					LanguageUtils.updateLocale(getApplicationContext(), Locale.ENGLISH);
				}
				SharedPreferenceUtils.put(getApplicationContext(), "isEnglish", isEnglish);
				restartApp();
				break;


		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void thirdEvent(ThirdAppEvent event){
		switch (event.action){
			case ThirdAppEvent.ACTION_NETFLIX:
				Intent netflix = getPackageManager().getLaunchIntentForPackage("com.netflix.mediaclient");
				if (netflix != null) {
					startActivity(netflix);
				} else {
					Log.e("ShowActivity","netflix-------null---");
				}
				break;
			case ThirdAppEvent.ACTION_HULU:
				//Intent hulu = getPackageManager().getLaunchIntentForPackage("com.skcss.hd");
				Intent hulu = getPackageManager().getLaunchIntentForPackage("com.hulu.plus");
				if (hulu != null) {
					startActivity(hulu);
				} else {
					Log.e("ShowActivity","hulu-------null---");

				}
				break;
			case ThirdAppEvent.ACTION_AMAZON:
				Intent amazon = getPackageManager().getLaunchIntentForPackage("com.amazon.avod.thirdpartyclient");
				if (amazon != null) {
					startActivity(amazon);
				} else {
					Log.e("ShowActivity","amazon-------null---");

				}
				break;
			case ThirdAppEvent.ACTION_YOUTUBE:
				// 通过包名获取要跳转的app，创建intent对象
				Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
				// 这里如果intent为空，就说名没有安装要跳转的应用嘛
				if (i != null) {
					startActivity(i);
					isToYoutube = true;
				} else {
					// 没有安装要跳转的app应用，提醒一下
				}
				break;
		}
	}
	private void showMultimedia() {

		getSupportFragmentManager().beginTransaction().replace(R.id.bofangFrameLayout, new MultimediaFragment())
				.addToBackStack(null)
				.commitAllowingStateLoss();

	}
	private void showEWEquipment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.bofangFrameLayout, sportEquipmentFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}

	private void showJingSai() {

		getSupportFragmentManager().beginTransaction().replace(R.id.bofangFrameLayout, raceFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();

	}

	private void showZhiBoFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.bofangFrameLayout, zhiBoListFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}

	private void showVideoFragment() {

		getSupportFragmentManager().beginTransaction().replace(R.id.bofangFrameLayout, localVideoFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}


	private void showRunningFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, runningMainFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}


	private void showShijing() {


		getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, realListFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}

	/**
	 * 隐藏所有的页面
	 *
	 * @param transaction
	 * @return
	 */
	private FragmentTransaction hideAllTabs(FragmentTransaction transaction) {
		if (runningMainFragment != null) {
			transaction.hide(runningMainFragment);
		}
		if (zhiBoListFragment != null) {
			transaction.hide(zhiBoListFragment);
		}
		if (localVideoFragment != null) {
			transaction.hide(localVideoFragment);
		}

		return transaction;
	}


	public void setAvtar(int picId) {

		if (picId == UserPicConstant.TYPE_01_01) {
			btn_login_show.setImageDrawable(getDrawable(R.drawable.pic_01_01));
		}

		if (picId == UserPicConstant.TYPE_01_02) {
			btn_login_show.setImageDrawable(getDrawable(R.drawable.pic_01_02));

		}

		if (picId == UserPicConstant.TYPE_01_03) {
			btn_login_show.setImageDrawable(getDrawable(R.drawable.pic_01_03));

		}
		if (picId == UserPicConstant.TYPE_02_01) {
			btn_login_show.setImageDrawable(getDrawable(R.drawable.pic_02_01));

		}

		if (picId == UserPicConstant.TYPE_02_02) {
			btn_login_show.setImageDrawable(getDrawable(R.drawable.pic_02_02));

		}

		if (picId == UserPicConstant.TYPE_02_03) {
			btn_login_show.setImageDrawable(getDrawable(R.drawable.pic_02_03));

		}
		if (picId == UserPicConstant.TYPE_03_01) {
			btn_login_show.setImageDrawable(getDrawable(R.drawable.pic_03_01));

		}

		if (picId == UserPicConstant.TYPE_03_02) {
			btn_login_show.setImageDrawable(getDrawable(R.drawable.pic_03_02));

		}

		if (picId == UserPicConstant.TYPE_03_03) {
			btn_login_show.setImageDrawable(getDrawable(R.drawable.pic_03_03));

		}


	}


	private BluetoothService bleService;

	private int device_mode = -1;
	private float calories = 0;
	private int bikeSportTime = 0;

	private void connectEquipment(EWEquipment mEwEquipment) {
		ShowProgressDialog.showDialog(progressDialog, "loading...", this);
		EwEquipmentManager.getInstance().connectEquipment(mEwEquipment);
		EwEquipmentManager.getInstance().setEquipmentManagerCallBack(new EwEquipmentManagerCallback() {
			@Override
			public void equipmentManagerDidConnectEquipment(final EWEquipment ewEquipment) {
				bleService = ewEquipment.getPeripheral().getBleService();
			}

			@Override
			public void equipmentManagerDidDisconnectEquipment(EWEquipment ewEquipment) {
				calories = 0;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						btn_lianjie.setClickable(true);
						isConnectedDevice = false;
						equipmentNameText.setText(R.string.my_device);
						equipmentImg.setImageDrawable(getDrawable(R.drawable.icon_bluetooth_off));
						EventBus.getDefault().post(new EquipmentEvent(EquipmentEvent.ACTION_EQUIPMENT_DISCONNECT, ewEquipment));
						try {
							EwEquipmentManager.getInstance().cancelData(ewEquipment);
						} catch (Exception e) {
							Log.e("HomeAcivity", "------e==" + e);
						}


						ShowProgressDialog.disProgressDialog(progressDialog);
						YDApplication.getInstance().put("ew_name", null);
						YDApplication.getInstance().put("ble_connect", false);
					}


				});
			}

			@Override
			public void equipmentManagerLinkMachineSuccessful(EWEquipment ewEquipment) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						isConnectedDevice = true;
						equipmentNameText.setText(ewEquipment.getPeripheral().getName());
						// equipmentImg.setImageDrawable(getDrawable(R.drawable.icon_bluetooth_on));
						if (ewEquipment.getPeripheral().getName().contains("EW-JS")) {
							equipmentImg.setImageDrawable(getDrawable(R.drawable.bike));
						} else if (ewEquipment.getPeripheral().getName().contains("EW-EP")) {
							equipmentImg.setImageDrawable(getDrawable(R.drawable.icon_equipment_ep));
						} else if (ewEquipment.getPeripheral().getName().contains("EW-TM")) {
							equipmentImg.setImageDrawable(getDrawable(R.drawable.treadmill));

						}
						SharedPreferenceUtils.put(ShowActivity.this, "ewName", ewEquipment.getPeripheral().getName());

						YDApplication.getInstance().put("ew_name", ewEquipment.getPeripheral().getName());
						YDApplication.getInstance().put("ble_connect", true);
						EventBus.getDefault().post(new EquipmentEvent(EquipmentEvent.ACTION_EQUIPMENT_CONNECTED, ewEquipment));
						// btn_lianjie.setClickable(false);
						ShowProgressDialog.disProgressDialog(progressDialog);
					}
				});
			}

			@Override
			public void equipmentManagerGetDeviceInfoSuccessful(EWEquipment ewEquipment) {

			}

			@Override
			public void equipmentManagerDidDiscoverServices(EWEquipment
																	ewEquipment, BluetoothGattService[] services, int status) {
				bleService = ewEquipment.getPeripheral().getBleService();
				TreadmillDeviceInfo.macAddress = ewEquipment.getPeripheral().getAddress();
				if (ewEquipment instanceof EWTMEquipment) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							device_mode = 0;
							BluetoothTmController.getInstance(bleService).initDeviceConnection();
						}
					});
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							device_mode = 1;
							BluetoothBikeController.getInstance(bleService).initDeviceConnection();
						}
					});
				}
			}

			@Override
			public void equipmentManagerAvailBLEData(final EWEquipment ewEquipment,
													 final byte[] bReceived) {
				if (ewEquipment instanceof EWTMEquipment) {
					EwEquipmentManager.getInstance().parseTmEquipmentData(ewEquipment, bReceived);
				} else {
					EwEquipmentManager.getInstance().parseBikeEquipmentData(ewEquipment, bReceived);
				}
			}
		});

		EwEquipmentManager.getInstance().setEWEquipmentSportDataCallBack(new EwEquipmentManager.EWEquipmentSportDataCallBack() {
			@Override
			public void equipmentTreadmillSportData(EWEquipment ewEquipment, TreadmillSportData treadmillSportData) {
				//Log.e("sunnny", "treadmill info: " + treadmillSportData.toString());
				device_mode = 0;
				Intent intent = new Intent("com.domyos.econnected.SEND_SPORT_DATA");
				Bundle bundle = new Bundle();
				bundle.putInt("device_mode", 0);
				bundle.putParcelable("device_info", treadmillSportData);
				bundle.putFloat("speed", treadmillSportData.getSpeed());
				bundle.putInt("status", treadmillSportData.getStatus());
				bundle.putInt("device_mode", device_mode);
				intent.putExtras(bundle);
				sendBroadcast(intent);

				int minutes = treadmillSportData.getMinute();
				int seconds = treadmillSportData.getSecond();
				String timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
				String calories = treadmillSportData.getCalories() + "";
				String distance = treadmillSportData.getDistance() + "";


				Intent intentRunAr = new Intent("com.domyos.econnected.SEND_SPEED");
				Bundle bundleRunAr = new Bundle();
				bundleRunAr.putInt("device_mode", 0);
				bundleRunAr.putFloat("speed", treadmillSportData.getSpeed());
				bundleRunAr.putInt("status", treadmillSportData.getStatus());
				bundleRunAr.putInt("rpm", 0);
				String speed = treadmillSportData.getSpeed() + "";
				String[] sportArr = new String[]{timeStr, distance, calories, speed};
				bundleRunAr.putStringArray("sport_data", sportArr);
				intentRunAr.putExtras(bundleRunAr);
				sendBroadcast(intentRunAr);
			}

			@Override
			public void equipmentBikeSportData(EWEquipment ewEquipment, BikeSportData bikeSportData) {

				int timeSecond = bikeSportData.getSecond() + bikeSportData.getMinute() * 60;
				//float rpm = bikeSportData.getRpm() / 3.7f;
				//测试用
				float rpm = bikeSportData.getRpm();

				Log.e("HomeAcivity", "---------bikeSportData------rpm==" + bikeSportData.getRpm());

				//float rpm = bikeSportData.getRpm();
				if (bikeSportTime == 0 && timeSecond != 0) {
					calories = (float) ((rpm * 87 * 5 * timeSecond) / 60000f * 0.24);
				}
				if (timeSecond != bikeSportTime) {
					calories = calories + (float) ((rpm * 87 * 5) / 60000f * 0.24);
					Log.e("sunny", "calories: " + calories);
					bikeSportTime = timeSecond;
				}
				bikeSportData.setCalories((int) calories);

				device_mode = 1;
				float speed = rpm * 0.00038f * 478;
				BigDecimal b = new BigDecimal(speed);
				float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

				int minutes = bikeSportData.getMinute();
				int seconds = bikeSportData.getSecond();
				float seconds_speed = (speed * 1000f) / 3600f;
				BigDecimal bigDecimal = new BigDecimal(seconds_speed / 1000f);
				float f2 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
				dis += seconds_speed;

				bikeSportData.setDistance(dis / 1000f);
				bikeSportData.setSpeed(f1);
				bikeSportData.setRpm(new Double(rpm).intValue());
				Log.e("HomeAcivity", "---------bikeSportData------" + bikeSportData.toString());
				Intent intent = new Intent("com.domyos.econnected.SEND_SPORT_DATA");
				Bundle bundle = new Bundle();
				bundle.putInt("device_mode", 1);
				bundle.putParcelable("device_info", bikeSportData);
				bundle.putFloat("speed", f1);
				bundle.putInt("status", 3);
				bundle.putInt("device_mode", device_mode);
				intent.putExtras(bundle);
				sendBroadcast(intent);

				String timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
				String calories = bikeSportData.getCalories() + "";

				Intent intentRunAr = new Intent("com.domyos.econnected.SEND_SPEED");
				Bundle bundleRunAr = new Bundle();
				bundleRunAr.putInt("device_mode", 1);
				bundleRunAr.putFloat("speed", f1);
				bundleRunAr.putInt("status", 3);

				bundleRunAr.putInt("rpm", new Double(rpm).intValue());
				String[] sportArr = new String[]{timeStr, "", calories, speed + ""};
				bundleRunAr.putStringArray("sport_data", sportArr);
				intentRunAr.putExtras(bundleRunAr);
				sendBroadcast(intentRunAr);
			}

			@Override
			public void equipmentErrorMessage(EWEquipment ewEquipment, String s, int i) {

			}
		});
	}


	public void restartApp() {
		new Handler().postDelayed(() -> {
			Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
			LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(LaunchIntent);
		}, 100);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int count = getSupportFragmentManager().getBackStackEntryCount();
			if (count != 0) {
				getSupportFragmentManager().popBackStack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void connected() {
		HeartRateManager.getInstance().removeAllData();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
			/*	Variable.icConnectHeart = true;
				btn_heart_rate.setClickable(false);
				heart_rate.setImageResource(R.mipmap.icon_heart_rate_connected);*/
			}
		});
	}

	@Override
	public void disconnected() {
		HeartRateManager.getInstance().removeAllData();
		SharedPreferenceUtils.put(this, "heart", "");
		YDApplication.getInstance().put("heart",null);
		Variable.icConnectHeart = false;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
			/*	btn_heart_rate.setClickable(true);
				heart_rate.setImageResource(R.mipmap.icon_heart_rate_unconnected);
				heart_rate_text.setText(R.string.heart_rate_bracelet);*/
			}
		});
	}


	private void showHeartRate() {
		getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, scanHeartRateFragment)
				.addToBackStack(null)
				.commitAllowingStateLoss();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		HeartRateManager.getInstance().removeAllData();
		SharedPreferenceUtils.put(this, "heart", "");
		//btn_heart_rate.setClickable(true);
		if (EwEquipmentManager.getInstance().isConnected()) {
			EwEquipmentManager.getInstance().cancelData(mEwEquipment);
			YDApplication.getInstance().put("ew_name", null);
			YDApplication.getInstance().put("ble_connect", false);
			SharedPreferenceUtils.put(this, "ewName", "");
		}

		if (timer != null) {
			timer.cancel();
			timer = null;

		}
		unbindService(mServiceConnection);
		unregisterReceiver(heartRateReceiver);

	}
}
