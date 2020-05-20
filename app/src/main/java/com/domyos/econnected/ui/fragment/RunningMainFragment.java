package com.domyos.econnected.ui.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.biz.SportDataBiz;
import com.domyos.econnected.biz.iml.SportDataBizImpl;
import com.domyos.econnected.biz.listener.UpLoadCallBack;
import com.domyos.econnected.constant.Constant;
import com.domyos.econnected.constant.RunningConnectType;
import com.domyos.econnected.constant.UserPicConstant;
import com.domyos.econnected.enity.SportDataInfo;
import com.domyos.econnected.enity.UIEventInfo;
import com.domyos.econnected.event.EquipmentEvent;
import com.domyos.econnected.event.HeartRateEvent;
import com.domyos.econnected.event.UIEvent;
import com.domyos.econnected.heartRate.HeartRateManager;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.ui.activity.TtsSettings;
import com.domyos.econnected.ui.fragment.running.SportEquipmentFragment;
import com.domyos.econnected.ui.fragment.user.LoginFragment;
import com.domyos.econnected.ui.view.HeartRateLineChartView;
import com.domyos.econnected.utils.JsonParser;
import com.domyos.econnected.utils.LogUtils;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.ew.ble.library.command.BluetoothTmController;
import com.ew.ble.library.constants.ProtocolInsData;
import com.ew.ble.library.entity.BikeSportData;
import com.ew.ble.library.entity.MemoryValue;
import com.ew.ble.library.entity.TreadmillSportData;
import com.ew.ble.library.equipment.EwEquipmentManager;
import com.ew.ble.library.utils.Utils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunningMainFragment extends BaseFragment implements UpLoadCallBack {

    @BindView(R.id.running_distance)
    TextView running_distance;
    @BindView(R.id.running_kcal)
    TextView running_kcal;
    @BindView(R.id.running_time)
    TextView running_time;
    @BindView(R.id.running_speed)
    TextView running_speed;
    @BindView(R.id.running_incline)
    TextView running_incline;
    /* @BindView(R.id.incline_img)
     ImageView incline_img;*/
    @BindView(R.id.running_bmp)
    TextView running_bmp;
    @BindView(R.id.start_running)
    ImageView start_running;
    @BindView(R.id.running_report)
    RelativeLayout running_report;
    @BindView(R.id.running_pause)
    ImageView running_pause;
    @BindView(R.id.running_stop)
    ImageView running_stop;
    @BindView(R.id.running_state)
    LinearLayout running_state;
    @BindView(R.id.mCountNumberText)
    TextView mCountNumberText;
    @BindView(R.id.view_heart_rate)
    HeartRateLineChartView heartRateLineChartView;
    @BindView(R.id.rpm_layout)
    LinearLayout rpm_layout;
    @BindView(R.id.incline_layout)
    LinearLayout incline_layout;
    @BindView(R.id.running_rpm)
    TextView running_rpm;

    @BindView(R.id.top_bar_img)
    ImageView top_bar_img;
    @BindView(R.id.top_bar_name)
    TextView top_bar_name;
    @BindView(R.id.top_bar_equipmentImg)
    ImageView top_bar_equipmentImg;
    @BindView(R.id.top_bar_equipmentNameText)
    TextView top_bar_equipmentNameText;
    @BindView(R.id.top_bar_lianjie)
    ImageView top_bar_lianjie;
    @BindView(R.id.top_bar_Fragment)
    LinearLayout top_bar_Fragment;
    @BindView(R.id.blue_text)
    TextView blue_text;
    @BindView(R.id.green_text)
    TextView green_text;
    @BindView(R.id.yellow_text)
    TextView yellow_text;
    @BindView(R.id.brown_text)
    TextView brown_text;
    @BindView(R.id.red_text)
    TextView red_text;
    @BindView(R.id.running_avg_bom)
    TextView running_avg_bom;
    @BindView(R.id.running_max_bpm)
    TextView running_max_bpm;
    @BindView(R.id.heart_img)
    ImageView heart_img;


    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认云端发音人
    public static String voicerCloud = "xiaoyan";
    // 默认本地发音人
    public static String voicerLocal = "xiaoyan";
    //缓冲进度
    private int mPercentForBuffering = 0;
    //播放进度
    private int mPercentForPlaying = 0;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private Toast mToast;
    private SharedPreferences mSharedPreferences;


    private String TAG = "Xunfei";
    // 唤醒结果内容
    private String resultString;

    // 设置门限值 ： 门限值越低越容易被唤醒
    private final static int MAX = 3000;
    private final static int MIN = 0;
    private int curThresh = 1450;
    private String threshStr = "门限值：";
    private String keep_alive = "1";
    private String ivwNetMode = "0";


    // 云端发音人列表
    private String[] cloudVoicersEntries;
    private String[] cloudVoicersValue;

    // 本地发音人列表
    private String[] localVoicersEntries;
    private String[] localVoicersValue;
    // 语音唤醒对象
    private VoiceWakeuper mIvw;
    ////////////////////////////

    private boolean isPause = false;
    private boolean isRunning = false;
    private boolean isStop = true;
    private int deviceMode = -1;
    private int avgHeartrate;
    private int maxHeartRate;
    private TreadmillSportData mTreadmillSportData;
    private BikeSportData mBikeSportData;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private List<String> listSpeed;
    private List<String> listIncline;
    private SportDataBiz sportDataBiz = new SportDataBizImpl();
    public static final String LOGINFRAGMENT = "com.domyos.econnected.ui.fragment.user.LoginFragment";
    private LoginFragment loginFragment = new LoginFragment();
    public static final String SHOW_EQUIPMENT_FRAGMENT = "com.domyos.econnected.ui.fragment.running.SportEquipmentFragment";
    private SportEquipmentFragment sportEquipmentFragment = new SportEquipmentFragment();
    /**
     * 心率历史记录
     */
    private HashMap<Integer, Integer> curvesHeartRateDataMap = new HashMap<>();

    private HeartRateManager heartRateManager;
    private String ew_name;
    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_running_main;
    }

    @Override
    protected void initSomething() {
        if (isVisible()) {

            int picId = SharedPreferenceUtils.get(YDApplication.getInstance(), "picId", 0);
            setAvtar(picId);
            ew_name = (String) YDApplication.getInstance().get("ew_name");
            if (ew_name != null) {
                top_bar_lianjie.setVisibility(View.GONE);
                top_bar_Fragment.setVisibility(View.VISIBLE);
                if (ew_name.contains("JS")) {
                    Log.e("initSomething", "----------------------" + ew_name);
                    deviceMode = 1;
                    top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.bike));
                } else if (ew_name.contains("EP")) {

                    deviceMode = 2;
                    top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.icon_equipment_ep));

                } else if (ew_name.contains("TM")) {
                    deviceMode = 0;
                    top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.treadmill));
                }

                top_bar_equipmentNameText.setText(ew_name);
            }


            if (SharedPreferenceUtils.get(getContext(), "isLogin", false)) {
                top_bar_name.setText(SharedPreferenceUtils.get(getContext(), "name", ""));
            }
        }
        SpeechUtility.createUtility(getActivity(), SpeechConstant.APPID + Constant.XUNFEI_APPID);

        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), mTtsInitListener);
        // 云端发音人名称列表
        cloudVoicersEntries = getResources().getStringArray(R.array.voicer_cloud_entries);
        cloudVoicersValue = getResources().getStringArray(R.array.voicer_cloud_values);
        // 本地发音人名称列表
        localVoicersEntries = getResources().getStringArray(R.array.voicer_local_entries);
        localVoicersValue = getResources().getStringArray(R.array.voicer_local_values);
        mSharedPreferences = getActivity().getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        listSpeed = new ArrayList<>();
        for (int i = 1; i < 19; i++) {
            listSpeed.add("速度" + i);
        }
        listIncline = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            listIncline.add("坡度" + i);
        }
        //心率

        heartRateManager = HeartRateManager.getInstance();
    }


    public void setAvtar(int picId) {
        if(picId==0){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.touxiang_img));

        }
        if (picId == UserPicConstant.TYPE_01_01) {
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_01_big));

        }

        if (picId == UserPicConstant.TYPE_01_02) {
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_02_big));

        }

        if (picId == UserPicConstant.TYPE_01_03) {
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_03_big));

        }
        if (picId == UserPicConstant.TYPE_02_01) {
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_01_big));

        }

        if (picId == UserPicConstant.TYPE_02_02) {
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_02_big));

        }

        if (picId == UserPicConstant.TYPE_02_03) {
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_03_big));

        }
        if (picId == UserPicConstant.TYPE_03_01) {
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_01_big));

        }

        if (picId == UserPicConstant.TYPE_03_02) {
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_02_big));

        }

        if (picId == UserPicConstant.TYPE_03_03) {
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_03_big));

        }

    }

    @OnClick({R.id.running_pause, R.id.running_stop, R.id.running_report, R.id.start_running, R.id.top_bar_back, R.id.top_bar_lianjie})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_bar_back:
                if (!isStop) {
                    stopRunning();
                } else {
                    getFragmentManager().popBackStack();
                }
                break;
            case R.id.running_pause:
                BluetoothTmController.pauseSport();
                break;
            case R.id.running_stop:
                BluetoothTmController.stopSport();
                break;
            case R.id.running_report:
                showDialog("to do");
                //EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_REPORT_FRAGMENT));
                break;
            case R.id.start_running:
                BluetoothTmController.startSport();
                break;
            case R.id.top_bar_lianjie:
                showEWEquipment();
                break;

        }
    }


    private void showEWEquipment() {
        FragmentTransaction transition = getChildFragmentManager().beginTransaction();
        if (!isFragmentAdded(SHOW_EQUIPMENT_FRAGMENT)) {
            transition.add(R.id.selectorEquipment, sportEquipmentFragment, SHOW_EQUIPMENT_FRAGMENT);
            transition.show(sportEquipmentFragment);
        } else {
            transition.show(sportEquipmentFragment);
        }
        transition.commitAllowingStateLoss();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void equipmentEvent(EquipmentEvent event) {
        switch (event.action) {
            case EquipmentEvent.ACTION_EQUIPMENT_CONNECTED:
                if (isVisible()) {
                    top_bar_lianjie.setVisibility(View.GONE);
                    top_bar_Fragment.setVisibility(View.VISIBLE);
                    if (event.ewEquipment.getPeripheral().getName().contains("EW_JS")) {

                        top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.bike));
                    }
                    top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.treadmill));
                    top_bar_equipmentNameText.setText(event.ewEquipment.getPeripheral().getName());
                }
                break;
            case EquipmentEvent.ACTION_EQUIPMENT_DISCONNECT:
                top_bar_lianjie.setVisibility(View.VISIBLE);
                top_bar_Fragment.setVisibility(View.GONE);
                showDialog(getActivity().getString(R.string.reconnect));

                break;
        }
    }

    private void pause() {
        running_state.setVisibility(View.VISIBLE);
        if (isRunning) {//暂停
            Log.e("sh_123", "pause");
            BluetoothTmController.pauseSport();
            running_pause.setImageDrawable(getActivity().getDrawable(R.drawable.start_running));
        } else {
            start();
            Log.e("sh_123", "pause---start");
            running_pause.setImageDrawable(getActivity().getDrawable(R.drawable.pause_running));
        }
    }

    private void start() {
        //判断是否连接设备蓝牙
        if (EwEquipmentManager.getInstance().isConnected()) {
            mCountNumberText.setVisibility(View.VISIBLE);
            Log.e("sh_123", "start");
            BluetoothTmController.startSport();
            EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_HIDE_TAB));
        } else {
            UIEventInfo uiEventInfo = new UIEventInfo();
            uiEventInfo.setType(RunningConnectType.RUNNING_MAIN);
            EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_RUN_EQUIPMENT, uiEventInfo));
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.selectorEquipment, new SportEquipmentFragment())
                    .commitAllowingStateLoss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUIEvent(UIEvent event) {
        switch (event.action) {
            case UIEvent.ACTION_RUN_EQUIPMENT:
                //显示浮窗
                if (event.eventInfo.getType() == RunningConnectType.RUNNING_MAIN) {
                    Log.e("sh_123", "----RunningMainFragment----ACTION_RUN_EQUIPMENT==" + event.eventInfo.getType());
                    // speak_text.setVisibility(View.VISIBLE);
                    /*  floatBtn.show();*/
                }
                break;
            case UIEvent.ACTION_LOGIN_FRAGMENT:
                upLoadSportData();
                break;

        }
    }


    private void stopRunning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.create();
        builder.setMessage("Do you want to end your exercise？");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (!isStop) {//停止
                    BluetoothTmController.stopSport();
                }
                getFragmentManager().popBackStack();
            }
        });
        builder.show();
    }

    public void startSpeechClick() {
        //初始化识别无UI识别对象
        //使用SpeechRecognizer对象，可根据回调消息自定义界面；
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(getActivity(), null);
        //设置参数
        mIat.setParameter(SpeechConstant.PARAMS, "iat");      //应用领域
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn"); //语音
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin"); //普通话
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);//引擎
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");//返回结果格式
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        //开始听写
        mIat.startListening(mRecoListener);
    }

    private RecognizerListener mRecoListener = new RecognizerListener() {
        //音量0-30
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
        }

        //开始录音
        @Override
        public void onBeginOfSpeech() {
            Log.e("sh_123", "-----------开始录音-----------");
          /*  ColorStateList colorStateList = ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary);
            floatBtn.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            floatBtn.setBackgroundTintList(colorStateList);*/
            //floatBtn.setBackgroundTintList(getColorStateListTest(R.color.colorPrimary));

        }

        //结束录音
        @Override
        public void onEndOfSpeech() {
            startWakeUp();
            LogUtils.e("xuefei_，结束录音");
          /*  ColorStateList colorStateList = ContextCompat.getColorStateList(getActivity(), R.color.black);
            floatBtn.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            floatBtn.setBackgroundTintList(colorStateList);
            //floatBtn.setBackgroundTintList(getColorStateListTest(R.color.black));*/

        }

        //返回结果
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            if (mIvw == null) {
                mIvw = VoiceWakeuper.getWakeuper();
            }
            System.out.println(recognizerResult.getResultString());
            printResult(recognizerResult);
            Log.e("xuefei_log", "返回结果==" + recognizerResult.getResultString());
        }


        @Override
        public void onError(SpeechError speechError) {
            Log.e("xuefei_log", "onError----" + speechError);
         /*   ColorStateList colorStateList = ContextCompat.getColorStateList(getActivity(), R.color.black);
            floatBtn.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            floatBtn.setBackgroundTintList(colorStateList);
            //floatBtn.setBackgroundTintList(getColorStateListTest(R.color.black));*/

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    //输出结果
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        //判断语音操作字符
        getRunCommand(resultBuffer);

    }


    public void getRunCommand(StringBuffer stringBuffer) {
        String str = stringBuffer.toString();
        setParam();
        Log.e(TAG, "---------" + str + "--------------");
        if (str.contains("开始跑步") | str.contains("开始运行") | str.contains("启动跑步机") | str.contains("启动") | str.contains("开始")) {
            start();
            mTts.startSpeaking("启动跑步机", mTtsListener);
        }
        if (str.contains("暂停跑步机") | str.contains("跑步机暂停") | str.contains("暂停")) {
            if (isRunning) {//停止
                mTts.startSpeaking("跑步机已暂停", mTtsListener);
                BluetoothTmController.pauseSport();
                //  running_pause.setImageDrawable(getActivity().getDrawable(R.drawable.start_running));
            }
        }
        if (str.contains("停止运行") | str.contains("停止跑步") | str.contains("跑步机停止") | str.contains("停止")) {
            if (!isStop) {//停止
                mTts.startSpeaking("跑步机已停止", mTtsListener);
                BluetoothTmController.stopSport();
            }
        }
        if (str.contains("增加坡度") | str.contains("坡度增加")) {
            int currentIncline = mTreadmillSportData.getIncline();
            if ((currentIncline + 1) <= MemoryValue.incline_max) {
                mTts.startSpeaking("坡度已增加", mTtsListener);
                BluetoothTmController.setSpeedAndIncline(mTreadmillSportData.getSpeed(), currentIncline + 1);
            }
        }
        if (str.contains("减小坡度") | str.contains("坡度减小")) {
            int currentIncline = mTreadmillSportData.getIncline();
            if ((currentIncline - 1) >= MemoryValue.incline_min) {
                mTts.startSpeaking("坡度已减小", mTtsListener);
                BluetoothTmController.setSpeedAndIncline(mTreadmillSportData.getSpeed(), currentIncline - 1);
            }
        }
        if (str.contains("增加速度") | str.contains("速度增加")) {
            float currentSpeed = mTreadmillSportData.getSpeed();

            double setSpeed = Double.parseDouble(Utils.formatKeepOneFloat((currentSpeed + 0.1)));
            if (setSpeed <= MemoryValue.speed_max) {
                mTts.startSpeaking("速度已增加", mTtsListener);
                BluetoothTmController.setSpeedAndIncline(setSpeed, mTreadmillSportData.getIncline());
            }
        }
        if (str.contains("减小速度") | str.contains("速度减小")) {
            float currentSpeed = mTreadmillSportData.getSpeed();
            double setSpeed = Double.parseDouble(Utils.formatKeepOneFloat((currentSpeed - 0.1)));
            if (setSpeed >= MemoryValue.speed_min) {
                mTts.startSpeaking("速度已减小", mTtsListener);
                BluetoothTmController.setSpeedAndIncline(setSpeed, mTreadmillSportData.getIncline());
            }

        }

        for (int y = 0; y < listSpeed.size(); y++) {
            if (str.equals(listSpeed.get(y))) {
                if (y < MemoryValue.speed_min) {
                    BluetoothTmController.setSpeedAndIncline(MemoryValue.speed_min, mTreadmillSportData.getIncline());
                } else {
                    LogUtils.e("------str-------" + (y + 1));
                    BluetoothTmController.setSpeedAndIncline(y + 1, mTreadmillSportData.getIncline());
                }
                mTts.startSpeaking(str, mTtsListener);
            }
        }

        for (int y = 0; y < listIncline.size(); y++) {

            if (str.equals(listIncline.get(y))) {
                BluetoothTmController.setSpeedAndIncline(mTreadmillSportData.getSpeed(), y);
                mTts.startSpeaking(str, mTtsListener);
               /* if ((y) >= MemoryValue.incline_min) {
                    BluetoothTmController.setSpeedAndIncline(mTreadmillSportData.getSpeed(), y);
                }
                if ((y) <= MemoryValue.incline_max) {
                    BluetoothTmController.setSpeedAndIncline(mTreadmillSportData.getSpeed(), y);
                }*/

            }
        }
        if (str.contains("速度吧")) {

            BluetoothTmController.setSpeedAndIncline(8, mTreadmillSportData.getIncline());
        }
        if (str.contains("坡度吧")) {

            BluetoothTmController.setSpeedAndIncline(mTreadmillSportData.getSpeed(), 8);
        }
        if (str.contains("速度零")) {
            mTts.startSpeaking("速度已最小", mTtsListener);
            BluetoothTmController.setSpeedAndIncline(MemoryValue.speed_min, mTreadmillSportData.getIncline());
        }
        if (str.contains("速度一")) {
            mTts.startSpeaking("速度设置为一", mTtsListener);
            BluetoothTmController.setSpeedAndIncline(1, mTreadmillSportData.getIncline());
        }
        if (str.contains("坡度零")) {
            mTts.startSpeaking(str, mTtsListener);
            BluetoothTmController.setSpeedAndIncline(mTreadmillSportData.getSpeed(), 0);
        }
        if (str.contains("坡度一")) {
            mTts.startSpeaking("坡度设置为1", mTtsListener);
            BluetoothTmController.setSpeedAndIncline(mTreadmillSportData.getSpeed(), 1);

        }

    }

    private ColorStateList getColorStateListTest(int colorRes) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };
        int color = ContextCompat.getColor(getActivity(), colorRes);
        int[] colors = new int[]{color, color, color, color};
        return new ColorStateList(states, colors);
    }

    @Override
    public void upLoadSuccess(boolean upLoad, String msg) {
        LogUtils.e("----------upLoadSuccess-----------"+msg);

    }

    @Override
    public void upLoadFailed(String msg) {
        showDialog(msg);
    }

    public void startWakeUp() {
        //非空判断，防止因空指针使程序崩溃
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            resultString = "";
            // 清空参数
            mIvw.setParameter(SpeechConstant.PARAMS, null);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + curThresh);
            // 设置唤醒模式
            mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置持续进行唤醒
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, keep_alive);
            // 设置闭环优化网络模式
            mIvw.setParameter(SpeechConstant.IVW_NET_MODE, ivwNetMode);
            // 设置唤醒资源路径
            mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getResource());
            // 设置唤醒录音保存路径，保存最近一分钟的音频
            mIvw.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
            mIvw.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");

            // 启动唤醒
            mIvw.startListening(mWakeuperListener);
            int ret = mIvw.queryResource(getResource(), requestListener);
            LogUtils.e(TAG + "---updateResource ret:" + ret);
        } else {
            LogUtils.e(TAG + "---唤醒未初始化");
        }

    }

    // 查询资源请求回调监听
    private RequestListener requestListener = new RequestListener() {
        @Override
        public void onEvent(int eventType, Bundle params) {
            // 以下代码用于获取查询会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                Log.d(TAG, "sid:" + params.getString(SpeechEvent.KEY_EVENT_SESSION_ID));
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error != null) {
                Log.d(TAG, "error:" + error.getErrorCode());
                LogUtils.e(error.getPlainDescription(true));
            }
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            try {
                String resultInfo = new String(buffer, "utf-8");
                Log.d(TAG, "resultInfo:" + resultInfo);

                JSONTokener tokener = new JSONTokener(resultInfo);
                JSONObject object = new JSONObject(tokener);

                int ret = object.getInt("ret");
                if (ret == 0) {
                    String uri = object.getString("dlurl");
                    String md5 = object.getString("md5");
                    Log.d(TAG, "uri:" + uri);
                    Log.d(TAG, "md5:" + md5);
                    LogUtils.e("请求成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private WakeuperListener mWakeuperListener = new WakeuperListener() {
        @Override
        public void onResult(WakeuperResult result) {
            Log.d(TAG, "onResult");
            if (!"1".equalsIgnoreCase(keep_alive)) {
            }
            try {
                String text = result.getResultString();
                JSONObject object;
                object = new JSONObject(text);
                StringBuffer buffer = new StringBuffer();
                buffer.append("【RAW】 " + text);
                buffer.append("\n");
                buffer.append("【操作类型】" + object.optString("sst"));
                buffer.append("\n");
                buffer.append("【唤醒词id】" + object.optString("id"));
                buffer.append("\n");
                buffer.append("【得分】" + object.optString("score"));
                buffer.append("\n");
                buffer.append("【前端点】" + object.optString("bos"));
                buffer.append("\n");
                buffer.append("【尾端点】" + object.optString("eos"));
                resultString = buffer.toString();
                int score = Integer.parseInt(object.optString("score"));
                if (score > 1400) {
                    mIvw.destroy();

                    setParam();
                    int code = mTts.startSpeaking("你好", mTtsListener);

                    if (code != ErrorCode.SUCCESS) {
                        LogUtils.e("语音合成失败,错误码: " + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                    }
                    Log.e("sh_1234", "---------score==" + object.optString("score"));
                } else {
                    Log.e("sh_1234", "-score==" + object.optString("score"));
                }

            } catch (JSONException e) {
                resultString = "结果解析出错";
                e.printStackTrace();
            }

        }

        @Override
        public void onError(SpeechError error) {
            LogUtils.e(error.getPlainDescription(true));
        }

        @Override
        public void onBeginOfSpeech() {
            Log.e("sh_1234", "------onBeginOfSpeech-------");
        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            switch (eventType) {
                // EVENT_RECORD_DATA 事件仅在 NOTIFY_RECORD_DATA 参数值为 真 时返回
                case SpeechEvent.EVENT_RECORD_DATA:
                    final byte[] audio = obj.getByteArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    Log.i(TAG, "ivw audio length: " + audio.length);
                    break;
            }
        }

        @Override
        public void onVolumeChanged(int volume) {

        }
    };

    private String getResource() {
        final String resPath = ResourceUtil.generateResourcePath(getContext(), ResourceUtil.RESOURCE_TYPE.assets, "ivw/" + "5db03b6a" + ".jet");
        Log.d(TAG, "resPath: " + resPath);
        return resPath;
    }


    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                LogUtils.e("初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            LogUtils.e(TAG + "--开始播放");
        }

        @Override
        public void onSpeakPaused() {
            LogUtils.e(TAG + "--暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            LogUtils.e(TAG + "--继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            LogUtils.e(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            LogUtils.e(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                startSpeechClick();
                Log.d(TAG, "onCompleted------------播放完成 ");

            } else if (error != null) {
                LogUtils.e(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}

            //实时音频流输出参考
			/*if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
				byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
				Log.e("MscSpeechLog", "buf is =" + buf);
			}*/
        }
    };

    /**
     * 参数设置
     */
    private void setParam() {
        LogUtils.e(TAG + "--setParam");
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        //设置合成
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            //设置使用云端引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicerCloud);
        } else {
            //设置使用本地引擎
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
            //设置发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);
        }
        //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY,"1");//支持实时音频流抛出，仅在synthesizeToUri条件下支持
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    //获取发音人资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(getActivity(), ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(getActivity(), ResourceUtil.RESOURCE_TYPE.assets, "tts/" + RunningMainFragment.voicerLocal + ".jet"));
        return tempBuffer.toString();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁合成对象
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.destroy();
        }
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

    public void showDialog(String msg) {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.create();
            builder.setMessage(msg);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
            builder.show();
        }

    }

    public boolean isFragmentAdded(String tag) {
        return getChildFragmentManager().findFragmentByTag(tag) != null;
    }


    int count = 0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void HeartRate(HeartRateEvent event) {
        switch (event.action) {
            case HeartRateEvent.HEART_RATE:
                heart_img.setImageDrawable(getActivity().getDrawable(R.mipmap.icon_heart_rate_connected));
                count++;
                heartRateLineChartView.updateData(CaculateHeartRate(event.heartRate, count), count);

                running_avg_bom.setText(heartRateManager.getAvgHeartRate() + "");
                running_max_bpm.setText(heartRateManager.getMaxHeartRate() + "");

                if (heartRateManager.getWarmUp() <= 300) {
                    blue_text.setText(heartRateManager.getWarmUp() / 60+"");
                }

                if (heartRateManager.getCalorieBurn() <= 180) {

                    green_text.setText(heartRateManager.getCalorieBurn() / 60+"");
                }
                if (heartRateManager.getAerobic() <= 1200) {

                    yellow_text.setText(heartRateManager.getAerobic() / 60+"");
                }
                if (heartRateManager.getAnaerobic() <= 60) {
                    brown_text.setText(heartRateManager.getAnaerobic() / 60+"");
                }

                if (heartRateManager.getLimit() <= 240) {
                    red_text.setText(heartRateManager.getLimit() / 60+"");
                }

             /*   avgHeartrate = heartRateManager.getAvgHeartRate();
                maxHeartRate = heartRateManager.getMaxHeartRate();
                */

                Log.d("simon", "max heart=" + heartRateManager.getMaxHeartRate());
                Log.d("simon", "avg heart=" + heartRateManager.getAvgHeartRate());
                Log.d("simon", "热身时间=" + heartRateManager.getWarmUp());
                Log.d("simon", "燃脂时间=" + heartRateManager.getCalorieBurn());
                Log.d("simon", "有氧时间=" + heartRateManager.getAerobic());
                Log.d("simon", "无氧时间=" + heartRateManager.getAnaerobic());
                Log.d("simon", "极限时间=" + heartRateManager.getLimit());
                break;
        }
    }

    private int[] CaculateHeartRate(int heartRate, int runTime) {
        curvesHeartRateDataMap.put(runTime, heartRate);
        heartRateManager.addHeartRate(heartRate);
        int[] heartRateArr = new int[curvesHeartRateDataMap.size()];
        //五分钟范围
        if (curvesHeartRateDataMap.size() >= 60) {
            // 跑够65秒之后，开始从后往前取值
            heartRateArr = new int[60];
            for (int i = 0; i < heartRateArr.length; i++) {
                Integer tmp = curvesHeartRateDataMap.get(runTime - (60 - i));
                if (tmp == null) {
                    tmp = 0;
                }
                if (tmp != 0) {
                    heartRateArr[i] = tmp;
                }
            }
        } else {
            for (int j = 0; j < curvesHeartRateDataMap.size(); j++) {
                Integer tmp = curvesHeartRateDataMap.get(j);
                if (tmp == null) {
                    tmp = 0;
                }
                if (tmp != 0) {
                    heartRateArr[j] = tmp;
                }
            }
        }
        heartRateLineChartView.updateData(heartRateArr, runTime);
        running_bmp.setText(String.valueOf(heartRate));

        return heartRateArr;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEquipmentEvent(EquipmentEvent event) {
        switch (event.action) {
            case EquipmentEvent.ACTION_TREADMILL_SPORT:
                startWake();
                changeTreadmillUI(event.treadmillSportData);
                break;
            case EquipmentEvent.ACTION_BIKE_SPORT:
                changeBikeUI(event.bikeSportData);
                break;
        }
    }

    private boolean isStart = false;

    public void startWake() {
        if (isVisible()) {
            // 初始化唤醒对象
            if (!isStart) {
                LogUtils.e("---------startWake---------");
                mIvw = VoiceWakeuper.createWakeuper(getActivity(), null);
                startWakeUp();
                isStart = true;
            }
        }
    }

    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private void changeTreadmillUI(TreadmillSportData treadmillSportData) {
        LogUtils.e("---------changeTreadmillUI---------" + treadmillSportData.toString());
        running_state.setVisibility(View.VISIBLE);
        incline_layout.setVisibility(View.VISIBLE);
        rpm_layout.setVisibility(View.GONE);
        int sportStatus = treadmillSportData.getStatus();
        int minutes = treadmillSportData.getMinute();
        int seconds = treadmillSportData.getSecond();
        double distance = treadmillSportData.getDistance();
        int calories = treadmillSportData.getCalories();
        float speed = treadmillSportData.getSpeed();
        int incline = treadmillSportData.getIncline();
        String timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
        int countNumber = treadmillSportData.getCountNumber();
        running_time.setText(timeStr);
        running_distance.setText(decimalFormat.format(distance));
        running_kcal.setText(calories + "");
        running_speed.setText(decimalFormat.format(speed));
        running_speed.setText(Utils.formatKeepOneFloat(speed) + "");
        running_incline.setText(incline + "");
        mCountNumberText.setText(countNumber + "");
        getSportStatus(sportStatus);
        mTreadmillSportData = treadmillSportData;
    }

    private void changeBikeUI(BikeSportData sportData) {

        LogUtils.e("---------changeBikeUI---------" + sportData.toString());
        running_state.setVisibility(View.GONE);
        incline_layout.setVisibility(View.GONE);
        rpm_layout.setVisibility(View.VISIBLE);
        float speed = sportData.getSpeed();
        int minutes = sportData.getMinute();
        int seconds = sportData.getSecond();
        String timeStr = (minutes < 10 ? ("0" + minutes) : ("" + minutes)) + ":" + (seconds < 10 ? ("0" + seconds) : ("" + seconds));
        int calories = sportData.getCalories();
        double distance = sportData.getDistance();
        LogUtils.e("---------changeBikeUI-----dis----" + distance);
        running_time.setText(timeStr);
        running_distance.setText(decimalFormat.format(distance));
        running_kcal.setText(calories + "");
        running_speed.setText(decimalFormat.format(speed));
        running_speed.setText(Utils.formatKeepOneFloat(speed) + "");
        running_rpm.setText(sportData.getRpm() + "");
        mBikeSportData = sportData;
    }

    private void getSportStatus(int sportStatus) {
        switch (sportStatus) {
            case ProtocolInsData.PR_MODE_IDLE:
            case ProtocolInsData.PR_MODE_NONE:
                isPause = false;
                isStop = true;
                isRunning = false;
                running_pause.setVisibility(View.GONE);
                running_stop.setVisibility(View.GONE);
                start_running.setVisibility(View.VISIBLE);
                mCountNumberText.setVisibility(View.GONE);
                break;
            case ProtocolInsData.PR_MODE_SYSCOUNT:
                isPause = false;
                isStop = false;
                isRunning = false;
                mCountNumberText.setVisibility(View.VISIBLE);
                break;
            case ProtocolInsData.PR_MODE_SPORT:
                isPause = false;
                isStop = false;
                isRunning = true;
                running_pause.setVisibility(View.VISIBLE);
                running_stop.setVisibility(View.VISIBLE);
                start_running.setVisibility(View.GONE);
                mCountNumberText.setVisibility(View.GONE);
                break;
            case ProtocolInsData.PR_MODE_PAUSE:
                isPause = true;
                isStop = false;
                isRunning = false;
                running_pause.setVisibility(View.GONE);
                running_stop.setVisibility(View.VISIBLE);
                start_running.setVisibility(View.VISIBLE);
                mCountNumberText.setVisibility(View.GONE);
                break;
            case ProtocolInsData.PR_MODE_SPORTOVER:
                running_state.setClickable(true);
                isPause = false;
                isStop = true;
                isRunning = false;
                upLoadSportData();
                running_pause.setVisibility(View.GONE);
                running_stop.setVisibility(View.GONE);
                start_running.setVisibility(View.VISIBLE);
                mCountNumberText.setVisibility(View.GONE);
                break;
        }
    }

    private void upLoadSportData() {
        long time = System.currentTimeMillis();
        SportDataInfo sportDataInfo = new SportDataInfo();

        if(ew_name.contains("TM")){
        sportDataInfo.setTimestamp(time + "");
        sportDataInfo.setCalorie(mTreadmillSportData.getCalories());
        sportDataInfo.setType(deviceMode);
        sportDataInfo.setOdometer((int) mTreadmillSportData.getDistance());
        sportDataInfo.setAvg_hr(avgHeartrate);
        sportDataInfo.setAvg_speed((int)mTreadmillSportData.getSpeed());
        sportDataInfo.setAvg_res(1);
        sportDataInfo.setAvg_rise(1);
        sportDataInfo.setAvg_rev(1);
        sportDataInfo.setAvg_watt(1);
        sportDataInfo.setElapse(mTreadmillSportData.getMinute() * 60 + mTreadmillSportData.getSecond());
        sportDataInfo.setMax_hr(maxHeartRate);
        sportDataInfo.setMax_rev(1);
        sportDataInfo.setMax_res(1);
        sportDataInfo.setMax_rise(1);
        sportDataInfo.setMax_speed(1);
        sportDataInfo.setMax_watt(1);

        }

        if(ew_name.contains("JS")) {
            sportDataInfo.setTimestamp(time + "");
            sportDataInfo.setCalorie(mBikeSportData.getCalories());
            sportDataInfo.setType(deviceMode);
            sportDataInfo.setOdometer((int) mBikeSportData.getDistance());
            sportDataInfo.setAvg_hr(avgHeartrate);
            sportDataInfo.setAvg_speed((int)mBikeSportData.getSpeed());
            sportDataInfo.setAvg_res(1);
            sportDataInfo.setAvg_rise(1);
            sportDataInfo.setAvg_rev(1);
            sportDataInfo.setAvg_watt(1);
            sportDataInfo.setElapse(mBikeSportData.getMinute() * 60 + mBikeSportData.getSecond());
            sportDataInfo.setMax_hr(maxHeartRate);
            sportDataInfo.setMax_rev(mBikeSportData.getRpm());
            sportDataInfo.setMax_res(1);
            sportDataInfo.setMax_rise(1);
            sportDataInfo.setMax_speed(1);
            sportDataInfo.setMax_watt(1);
        }

            sportDataBiz.upLoadSportData(sportDataInfo, this);
    }

}
