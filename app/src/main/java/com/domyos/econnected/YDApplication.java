package com.domyos.econnected;/**
 * Created by HouWei on 16/7/6.
 */

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.domyos.econnected.constant.ApiCenter;
import com.domyos.econnected.constant.Variable;
import com.domyos.econnected.utils.LanguageUtils;
import com.domyos.econnected.utils.LogUtils;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.ew.ble.library.equipment.EWEPEquipment;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

/**
 * Author HouWei
 * Date 16/7/6
 * Time 18:10
 * Package com.yuedong.apps.treadmill
 */
public class YDApplication extends Application {
    
    
    private static YDApplication mInstance;
    
    public static YDApplication getInstance() {
        return mInstance;
    }
    public static boolean isConnected = false;
    public static EWEPEquipment ewepEquipment;
    private HashMap<String, Object> map = new HashMap<String, Object>();

    public void put(String key,Object object){
        map.put(key, object);
    }

    public  Object get(String key){

        return map.get(key);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().init(this);
        MultiDex.install(this);
        mInstance = this;
        Variable.isAuthSuc = false;
        // 初始化网络服务
        ApiCenter.init(getApplicationContext());
        changeLanguage();
       // updateResources(this);
        
    }
    
    
    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
    
    public void changeLanguage() {
        try {
            boolean isEnglish = SharedPreferenceUtils.get(this, "isEnglish", false);
            if (isEnglish) {
                LanguageUtils.updateLocale(getApplicationContext(), Locale.ENGLISH);
            } else {
                LanguageUtils.updateLocale(getApplicationContext(), Locale.SIMPLIFIED_CHINESE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            LanguageUtils.updateLocale(getApplicationContext(), Locale.SIMPLIFIED_CHINESE);
        }
    }
    
    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResources(Context context) {
        boolean isEnglish = false;
        try {
            isEnglish = SharedPreferenceUtils.get(this, "isEnglish", false);
        } catch (NullPointerException e) {
            e.printStackTrace();
            
        }
        Resources resources = context.getResources();
        Locale locale;

        if (isEnglish) {
            locale = Locale.ENGLISH;
        } else {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        changeLanguage();
      //  updateResources(this);
    }

  /*  @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(getContext(base));

    }

    public Context getContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 8.0需要使用createConfigurationContext处理
            return updateResources(context);
        } else {
            return context;
        }
    }*/

}

