package com.domyos.econnected.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.constant.Constant;
import com.domyos.econnected.constant.UserPicConstant;
import com.domyos.econnected.enity.google.Route;
import com.domyos.econnected.enity.google.SnappedWayPoints;
import com.domyos.econnected.event.EquipmentEvent;
import com.domyos.econnected.event.HeartRateEvent;
import com.domyos.econnected.net.googleapi.GoogleWebApi;
import com.domyos.econnected.ui.BaseActivity;
import com.domyos.econnected.ui.view.ProgressDialogUtil;
import com.domyos.econnected.utils.LogUtils;
import com.domyos.econnected.utils.PermissionUtils;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.domyos.econnected.utils.SphericalUtil;
import com.ew.ble.library.entity.BikeSportData;
import com.ew.ble.library.entity.TreadmillSportData;
import com.ew.ble.library.equipment.EwEquipmentManager;
import com.ew.ble.library.utils.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.iflytek.cloud.Setting;
import com.zhy.autolayout.utils.L;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleMapActivity extends BaseActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMyLocationButtonClickListener,
		GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks, View.OnClickListener {


	@BindView(R.id.mMapTypeBaseButton)
	ImageButton mMapTypeBaseButton;
	@BindView(R.id.mMapTypeSatelliteButton)
	ImageButton mMapTypeSatelliteButton;
	/*  @BindView(R.id.mMapType3DButton)
      ImageButton mMapType3DButton;*/
	@BindView(R.id.mResetButton)
	ImageButton mResetButton;
	@BindView(R.id.mKeyWordsEditText)
	TextView mKeyWordsEditText;
	@BindView(R.id.mSearchButton)
	ImageView mSearchButton;
	@BindView(R.id.mSearchLayout)
	LinearLayout mSearchLayout;
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


	private MapFragment mMapFragment;
	private StreetViewPanoramaFragment mStreetViewPanoramaFragment;
	private StreetViewPanorama mStreetViewPanorama;
	private GoogleMap mGoogleMap;


	private int mMapOnClickTimes = 0;
	private PolylineOptions mSnapedRoute;

	private Marker mStartMarker;
	private Marker mEndMarker;
	private Marker mUserMarker;

	private boolean mIsRunning = false;
	private boolean mHasRoute = false; // 是否已经规划好了路线
	private boolean isDialogShow = false;
	private double mTargetDistance = 0;
	private long mStartTime = 0;

	//进入地图时的距离
	private float totalDistance;
	//进入地图模式标志
	private boolean isFirst;
	private String distanceStr, timeStr, kcalStr, speedStr, inclineStr, bpmStr, rpmStr;
	private Bundle bundle;
	private GoogleApiClient mGoogleApiClient;
	private View mapView;
	private Retrofit mapRet = new Retrofit.Builder()
			.baseUrl(Constant.UrlPath.GOOGLE_MAP_API_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();
	private GoogleWebApi.MapApi mapApi = mapRet.create(GoogleWebApi.MapApi.class);


	private Retrofit roadRet = new Retrofit.Builder()
			.baseUrl(Constant.UrlPath.GOOGLE_ROADS_API_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();
	private GoogleWebApi.RoadsApi roadsApi = roadRet.create(GoogleWebApi.RoadsApi.class);
	private com.google.android.gms.location.places.Place mPlace;
	private int PLACE_PICKER_REQUEST = 1;
	private PlacesClient places;
	@SuppressLint("NewApi")
	private void resetMap() {

		if (mGoogleMap != null) {
			mGoogleMap.clear();
		}
		mMapOnClickTimes = 0;
		mSnapedRoute = null;
		mTargetDistance = 0;
		mStartMarker = null;
		mEndMarker = null;
		mUserMarker = null;
		mStartTime = 0;
		mHasRoute = false;
	}


	@Override
	protected void onStart() {
		super.onStart();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		//mKeyWordsEditText.setText("");
		isDialogShow = false;
		totalDistance = 0;
		if (mGoogleApiClient != null) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onDestroy() {
		if (startDialog != null && startDialog.isShowing()) {
			startDialog.dismiss();
			startDialog = null;
		}
		if (getSportReceiver != null) {
			unregisterReceiver(getSportReceiver);
		}
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

	@Override
	protected void initSomething() {
		if (EwEquipmentManager.getInstance().isConnected()) {
			mIsRunning = true;
		}

		setSearch();

		// Create an instance of GoogleAPIClient.
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addApi(LocationServices.API)
					.build();
		}

		mMapFragment = MapFragment.newInstance();
		getFragmentManager().beginTransaction().add(R.id.mMapViewFrameLayout, mMapFragment).commitAllowingStateLoss();
		mMapFragment.getMapAsync(this);
		mMapTypeBaseButton.setSelected(true);

		int picId = SharedPreferenceUtils.get(YDApplication.getInstance(), "picId", 0);
		setAvtar(picId);
		if (SharedPreferenceUtils.get(this, "isLogin", false)) {
			live_top_running_name.setText(SharedPreferenceUtils.get(this, "name", ""));
		}


		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.domyos.econnected.SEND_SPORT_DATA");
		registerReceiver(getSportReceiver, intentFilter);

		if (bundle != null) {
			refreshUI(bundle);
		} else {
			LogUtils.e("---------GoogleMapActivity----bundle null---");
		}

	}

	//搜索内容Client
	private void setSearch() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Places.initialize(getApplicationContext(), "AIzaSyA7VNrcdWs8SKCd9vBX8as3m94WDP1gyCQ");
					places = Places.createClient(GoogleMapActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();


	}

	public void setAvtar(int picId) {
		if (picId == 0) {
			live_top_running_img.setImageDrawable(getDrawable(R.drawable.touxiang_img));

		}
		if (picId == UserPicConstant.TYPE_01_01) {
			live_top_running_img.setImageDrawable(getDrawable(R.drawable.pic_01_01));

		}

		if (picId == UserPicConstant.TYPE_01_02) {
			live_top_running_img.setImageDrawable(getDrawable(R.drawable.pic_01_02));

		}

		if (picId == UserPicConstant.TYPE_01_03) {
			live_top_running_img.setImageDrawable(getDrawable(R.drawable.pic_01_03));

		}
		if (picId == UserPicConstant.TYPE_02_01) {
			live_top_running_img.setImageDrawable(getDrawable(R.drawable.pic_02_01));

		}

		if (picId == UserPicConstant.TYPE_02_02) {
			live_top_running_img.setImageDrawable(getDrawable(R.drawable.pic_02_02));

		}

		if (picId == UserPicConstant.TYPE_02_03) {
			live_top_running_img.setImageDrawable(getDrawable(R.drawable.pic_02_03));

		}
		if (picId == UserPicConstant.TYPE_03_01) {
			live_top_running_img.setImageDrawable(getDrawable(R.drawable.pic_03_01));

		}

		if (picId == UserPicConstant.TYPE_03_02) {
			live_top_running_img.setImageDrawable(getDrawable(R.drawable.pic_03_02));

		}

		if (picId == UserPicConstant.TYPE_03_03) {
			live_top_running_img.setImageDrawable(getDrawable(R.drawable.pic_03_03));

		}

	}

	private void refreshUI(Bundle bundle) {

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
			live_top_running_bpm_layout.setBackground(getDrawable(R.drawable.border_right));
			live_top_running_rpm_layout.setVisibility(View.VISIBLE);
			BikeSportData bikeSportData = bundle.getParcelable("device_info");
			int minutes = bikeSportData.getMinute();
			int seconds = bikeSportData.getSecond();
			timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
			BigDecimal bigDecimal = new BigDecimal(bikeSportData.getDistance());
			float f2 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			if (isFirst) {
				isFirst = false;
				totalDistance = f2;
			}
			distanceStr = f2 + "";
			kcalStr = bikeSportData.getCalories() + "";
			speedStr = bikeSportData.getSpeed() + "";
			live_top_running_incline.setText(inclineStr);
			bpmStr = bikeSportData.getPluse() + "";
			rpmStr = bikeSportData.getRpm() + "";
			live_top_running_rpm.setText(rpmStr);
			live_top_running_speed.setText(speedStr);

			calculateMapDistance(bikeSportData);
		}

		live_top_running_distance.setText(distanceStr);
		live_top_running_kcal.setText(kcalStr);
		live_top_running_time.setText(timeStr);


	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void HeartRate(HeartRateEvent event) {
		switch (event.action) {
			case HeartRateEvent.HEART_RATE:
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						live_top_running_bmp.setText(event.heartRate + "");

					}
				});
				break;
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void equipmentEvent(EquipmentEvent event) {
		switch (event.action) {

			case EquipmentEvent.ACTION_EQUIPMENT_DISCONNECT:
				runOnUiThread(new Runnable() {
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

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_treadmill_map;
	}

	private boolean startSport() {
		if (mHasRoute) {
			mSearchLayout.setVisibility(View.GONE);
			hideOrDisplayMapButton(false);
			if (mStartTime <= 0) {
				mStartTime = System.currentTimeMillis();
			}
			// startTest();
			return true;
		}
		return false;
	}


	@OnClick({R.id.live_top_running_back, R.id.mSearchButton, R.id.mSearchLayout, R.id.mResetButton, R.id.mMapTypeBaseButton, R.id.mMapTypeSatelliteButton/*, R.id.mMapType3DButton*/})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.live_top_running_back:
				if (!EwEquipmentManager.getInstance().isConnected()) {
					totalDistance = 0;
					Intent intent = new Intent(this, ShowActivity.class);
					startActivity(intent);
					finish();
				} else {
					finishSport("返回");
				}

				break;
			case R.id.mSearchButton:
			case R.id.mSearchLayout:
				searchPlace();
				break;
			case R.id.mResetButton:
				resetMap();
				break;

			case R.id.mMapTypeBaseButton:
				if (mGoogleMap != null) {
					if (mStreetViewPanoramaFragment != null) {
						getFragmentManager().beginTransaction().hide(mStreetViewPanoramaFragment).show(mMapFragment).commit();
					}
					mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					changeMapButtonState(R.id.mMapTypeBaseButton);
					if (!mIsRunning) {
						mSearchLayout.setVisibility(View.VISIBLE);
					}
					mSearchLayout.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.mMapTypeSatelliteButton:
				if (mGoogleMap != null) {
					if (mStreetViewPanoramaFragment != null) {
						getFragmentManager().beginTransaction().hide(mStreetViewPanoramaFragment).show(mMapFragment).commit();
					}
					mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
					changeMapButtonState(R.id.mMapTypeSatelliteButton);
					mSearchLayout.setVisibility(View.GONE);
				}
				break;
          /*  case R.id.mMapType3DButton:
                if (mStreetViewPanoramaFragment == null) {
                    mStreetViewPanoramaFragment = StreetViewPanoramaFragment.newInstance();
                    getFragmentManager().beginTransaction().hide(mMapFragment).add(R.id.mMapViewFrameLayout, mStreetViewPanoramaFragment).commit();
                    mStreetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
                } else {
                    getFragmentManager().beginTransaction().show(mStreetViewPanoramaFragment).hide(mMapFragment).commit();
                }
                changeMapButtonState(R.id.mMapType3DButton);
                if (currentLatLng != null) {
                    updateStreetView(currentLatLng, mCurrentHeading);
                }
                mSearchLayout.setVisibility(View.GONE);
                break;*/
			default:
				break;
		}

	}

	int AUTOCOMPLETE_REQUEST_CODE = 1;

	private void searchPlace() {
		List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
		Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
		startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Place place = Autocomplete.getPlaceFromIntent(data);
				Log.i("GoogleMapActivity", "Place: " + place.getName() + ", " + place.getId());
				moveToLocation(place.getLatLng());
			} else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
				// TODO: Handle the error.
				Status status = Autocomplete.getStatusFromIntent(data);
				Log.i("GoogleMapActivity", status.getStatusMessage());
			} else if (resultCode == RESULT_CANCELED) {
				// The user canceled the operation.
			}
		}
        /*if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                mPlace = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", mPlace.getName());
                mKeyWordsEditText.setText(mPlace.getName());
            }
        }*/
	}

	private void moveToLocation(LatLng location) {
		if (location == null) {
			return;
		}
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

		if (mStreetViewPanorama != null) {
			mStreetViewPanorama.setPosition(location);
		}
	}

	private LatLng myLocation = null;

	private void snapRoute(Route mRoute) {
		StringBuilder sb = new StringBuilder();
		if (mRoute.getRoutes().size() <= 0) {
			return;
		}
		for (int i = 0; mRoute.getRoutes().size() > 0 && i < mRoute.getRoutes().get(0).getLegs().size(); i++) {
			List<Route.RoutesEntity.LegsEntity.StepsEntity> routeStepList = mRoute.getRoutes().get(0).getLegs().get(i).getSteps();
			for (Route.RoutesEntity.LegsEntity.StepsEntity step : routeStepList) {
				sb.append(step.getStart_location().getLat())
						.append(",")
						.append(step.getStart_location().getLng())
						.append("|").append(step.getEnd_location().getLat())
						.append(",")
						.append(step.getEnd_location().getLng())
						.append("|");
			}
		}

		String path = sb.substring(0, sb.length() - 1).toString();
		L.e("path:  " + path);

		Call<SnappedWayPoints> call = roadsApi.snapRoute(path, "true", Constant.MapConfiguration.GOOGLE_MAP_API_KEY);
		call.enqueue(new Callback<SnappedWayPoints>() {
			@Override
			public void onResponse(Call<SnappedWayPoints> call, Response<SnappedWayPoints> response) {
				SnappedWayPoints result = response.body();
				Log.e("GoogleMapActivity", "-----------------------" + response.body().toString());
				drawRoute(result);
				ProgressDialogUtil.dismissProgressDialog();

			}

			@Override
			public void onFailure(Call<SnappedWayPoints> call, Throwable t) {
				ProgressDialogUtil.dismissProgressDialog();

				t.printStackTrace();
			}
		});
	}


	private void drawRoute(SnappedWayPoints snappedWayPoints) {
		PolylineOptions options = new PolylineOptions();
		List<SnappedWayPoints.SnappedPointsEntity> list = snappedWayPoints.getSnappedPoints();
		for (SnappedWayPoints.SnappedPointsEntity point : list) {
			options.add(new LatLng(point.getLocation().getLatitude(), point.getLocation().getLongitude()));
		}
		mSnapedRoute = options;
		// 计算总路程
		mTargetDistance = SphericalUtil.computeLength(mSnapedRoute.getPoints());
		mGoogleMap.addPolyline(options);
		mHasRoute = true;
		isFirst = true;
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(getString(R.string.confirm_distance))
				.setMessage(getString(R.string.planning_the_route_string) + Utils.formatKeepTwoFloat(mTargetDistance / 1000f) + "km")
				.setCancelable(false)
				.setPositiveButton(getString(R.string.start_capital), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
                       /* mSportInformationLayout.setVisibility(View.VISIBLE);
                        mToggleSportInformationLayoutButton.setRotation(180.0f);*/
						lastUpdateStreetViewTime = System.currentTimeMillis();
						startSport();
					}
				})
				.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						resetMap();
					}
				}).create().show();


	}


	private double mCurrentHeading = 0;

	private double distance = -1;
	private LatLng currentLatLng;

	private void calculateMapDistance(BikeSportData sportData) {
		Log.e("GoogleMapActivity", "countDis: ");
		if (!mIsRunning) {
			return;
		}
		double currentDistance = sportData.getDistance() * 1000;
		if (distance == currentDistance) {
			Log.e("GoogleMapActivity", "distance: " + distance + "  ----》" + currentDistance);
			return;
		}
		distance = currentDistance;
		double countDis = 0;

		if (mSnapedRoute != null) {
			for (int i = 0; i < mSnapedRoute.getPoints().size() - 1; i++) {
				// 计算每一分段的长度
				LatLng startLatLng = mSnapedRoute.getPoints().get(i);
				LatLng endLatLng = mSnapedRoute.getPoints().get(i + 1);
				countDis += SphericalUtil.computeDistanceBetween(startLatLng, endLatLng);
				Log.e("GoogleMapActivity", "countDis: " + countDis + " currentDistance: " + currentDistance);
				if (countDis > currentDistance) {
					currentDistance = countDis - currentDistance;
					mCurrentHeading = SphericalUtil.computeHeading(startLatLng, endLatLng);
					currentLatLng = SphericalUtil.computeOffsetOrigin(endLatLng, currentDistance, mCurrentHeading);
					drawUserMarker(currentLatLng);
					updateStreetView(currentLatLng, mCurrentHeading);
					break;
				}
			}

			//设定的距离已经骑行完成
			//可以结束骑行
			if (mTargetDistance <= (currentDistance - totalDistance)) {
				if (!isDialogShow) {
					isDialogShow = true;
					finishSport("");

				}

			}
		}
	}

	private void finishSport(String msg) {
		totalDistance = 0;
		//弹窗提醒是否退出地图模式
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if (msg.equals("返回")) {
			builder.setMessage(getString(R.string.confirm_end));
			builder.setNegativeButton(R.string.go, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					dialogInterface.dismiss();
				}
			});
			builder.setNegativeButton(getString(R.string.cancel), null);

		} else {
			builder.setMessage(getString(R.string.determine_the_exits));
		}

		builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				totalDistance = 0;
				Intent backShow = new Intent(GoogleMapActivity.this, ShowActivity.class);
				startActivity(backShow);
				finish();
			}
		});
		builder.setCancelable(false);
		builder.create().show();


	}

	private long lastUpdateStreetViewTime = 0;

	/**
	 * 每5秒更新一次街景
	 *
	 * @param latLng
	 * @param heading
	 */
	private void updateStreetView(LatLng latLng, double heading) {
		// boolean isLast = System.currentTimeMillis() <= (lastUpdateStreetViewTime + Constant.Values.STREET_VIEW_REFRESH_TIME);
		if (!mIsRunning || mStreetViewPanorama == null) {
			Log.e("GoogleMapActivity", "mIsRunning: " + mIsRunning);
			return;
		}
		Log.e("GoogleMapActivity", "latLng-->  latitude:  " + latLng.latitude + " longitude: " + latLng.longitude);
		lastUpdateStreetViewTime = System.currentTimeMillis();
		long duration = 1000;
		float tilt = 0;
		StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()
				.zoom(mStreetViewPanorama.getPanoramaCamera().zoom)
				.bearing((float) heading)
				.tilt(tilt)
				.build();
		mStreetViewPanorama.setPosition(latLng, 100);
		mStreetViewPanorama.animateTo(camera, duration);
	}


	private void drawUserMarker(LatLng position) {
		if (mUserMarker == null) {
			mUserMarker = mGoogleMap.addMarker(new MarkerOptions().position(position));
			mUserMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.dituzuobiao));
		}
		mUserMarker.setPosition(position);
		moveToLocation(position);
	}

	Dialog startDialog = null;

	private void hideOrDisplayMapButton(boolean flag) {
		if (flag) {
			mKeyWordsEditText.setVisibility(View.VISIBLE);
			mSearchButton.setVisibility(View.VISIBLE);
			mResetButton.setVisibility(View.VISIBLE);
		} else {
			mKeyWordsEditText.setVisibility(View.GONE);
			mSearchButton.setVisibility(View.GONE);
			mResetButton.setVisibility(View.GONE);
		}
	}

	private void changeMapButtonState(int buttonId) {
		mMapTypeBaseButton.setSelected(false);
		mMapTypeSatelliteButton.setSelected(false);
		// mMapType3DButton.setSelected(false);
		try {
			switch (buttonId) {
				case R.id.mMapTypeSatelliteButton:
					mMapTypeSatelliteButton.setSelected(true);
					break;
          /*  case R.id.mMapType3DButton:
                mMapType3DButton.setSelected(true);
                break;*/
				case R.id.mMapTypeBaseButton:
				default:
					mMapTypeBaseButton.setSelected(true);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		mGoogleMap.setOnMyLocationButtonClickListener(this);
		mGoogleMap.setOnMyLocationClickListener(this);
		if (EwEquipmentManager.getInstance().isConnected()) {
			mGoogleMap.setOnMapLongClickListener(this);
		}
		//改变定位的图标位置
		mapView = mMapFragment.getView();
		if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
			// Get the button view
			View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
			// and next place it, on bottom right (as Google Maps app)
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
			// position on right bottom
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			layoutParams.setMargins(0, 0, 80, 60);
		}
		enableMyLocation();
	}

	@Override
	public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
		this.mStreetViewPanorama = streetViewPanorama;
		Log.e("GoogleMapActivity", "onStreetViewPanoramaReady");
		// 不允许手动前后移动
		mStreetViewPanorama.setUserNavigationEnabled(false);
		// 不允许移动镜头
		mStreetViewPanorama.setPanningGesturesEnabled(false);
		if (currentLatLng != null) {
			updateStreetView(currentLatLng, mCurrentHeading);
		} else if (myLocation != null) {
			StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()
					.zoom(mStreetViewPanorama.getPanoramaCamera().zoom)
					.tilt(0)
					.bearing(mStreetViewPanorama.getPanoramaCamera().bearing)
					.build();
			// mStreetViewPanorama.setPosition(myLocation);
			mStreetViewPanorama.setPosition(BERLIN);
			//  mStreetViewPanorama.animateTo(camera, 500);
		} else {
			mStreetViewPanorama.setPosition(BERLIN);
		}
	}

	// private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
	private static final LatLng BERLIN = new LatLng(13.40713, 52.51050);

	@Override
	public void onMapLongClick(LatLng latLng) {
		L.e("lat:" + latLng.latitude);
		L.e("lng:" + latLng.longitude);
		// mKeyWordsEditText.setText("");
		mMapOnClickTimes++;
		if (mMapOnClickTimes == 1) {
			Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
			marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_start));
			mStartMarker = marker;
		} else if (mMapOnClickTimes == 2) {
			Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng));
			marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_end));
			mEndMarker = marker;
			String startStr = mStartMarker.getPosition().latitude + "," + mStartMarker.getPosition().longitude;
			String endStr = mEndMarker.getPosition().latitude + "," + mEndMarker.getPosition().longitude;
			ProgressDialogUtil.showProgressDialog(mContext);
			Call<Route> call = mapApi.getRoute(startStr, endStr, Constant.MapConfiguration.GOOGLE_MAP_API_KEY);
			call.enqueue(new Callback<Route>() {
				@Override
				public void onResponse(Call<Route> call, Response<Route> response) {
					L.e("GoogleMapActivity  normalGet: " + response.body().toString());
					Route route = response.body();
					snapRoute(route);
				}

				@Override
				public void onFailure(Call<Route> call, Throwable t) {
					resetMap();
					ProgressDialogUtil.dismissProgressDialog();
					t.printStackTrace();
				}
			});
		}
	}

	@Override
	public boolean onMyLocationButtonClick() {
		return false;
	}

	@Override
	public void onMyLocationClick(@NonNull Location location) {
		final Location mLocation = location;
		try {
			// mKeyWordsEditText.setText("");
			myLocation = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
			Log.e("GoogleMapActivity", "myLocation : ");
			moveToLocation(myLocation);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
	private boolean mPermissionDenied = false;

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
			return;
		}

		if (PermissionUtils.isPermissionGranted(permissions, grantResults,
				Manifest.permission.ACCESS_FINE_LOCATION)) {
			// Enable the my location layer if the permission has been granted.
			enableMyLocation();
		} else {
			// Display the missing permission error dialog when the fragments resume.
			mPermissionDenied = true;
		}
	}


	private void enableMyLocation() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			// Permission to access the location is missing.
			PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
					Manifest.permission.ACCESS_FINE_LOCATION, true);

		} else if (mGoogleMap != null) {
			// Access to the location has been granted to the app.
			Log.e("GoogleMapActivity", "hhhhhh");
			mGoogleMap.setMyLocationEnabled(true);
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			mGoogleMap.setIndoorEnabled(true);
		}
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		try {
			if(location!=null){
				myLocation = new LatLng(location.getLatitude(), location.getLongitude());
				moveToLocation(myLocation);
			}else {
				Log.e("GoogleMapActivity","location is null");
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onPointerCaptureChanged(boolean hasCapture) {

	}
}
