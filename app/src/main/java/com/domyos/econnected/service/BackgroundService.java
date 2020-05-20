package com.domyos.econnected.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IIvviCoreManager;
import android.os.RemoteException;
import android.os.ServiceManager;


import com.android.server.ivvi.IvviCoreConstant;
import com.domyos.econnected.heartRate.HeartRateService;

import org.greenrobot.eventbus.EventBus;


public class BackgroundService extends Service {

    IIvviCoreManager mCoreManager = IIvviCoreManager.Stub.asInterface(ServiceManager.getService("ivviCoreService"));


    private final IBinder mBinder = new LocalBinder();

    private final static String TAG = "####guolong" + HeartRateService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        sendReceiver();

    }

    private void sendReceiver() {

        try {
            double[] data = mCoreManager.getAllResult();
            int value = (int)data[IvviCoreConstant.RESULT_ARR_INDEX_CURR_HEART_RATE];
            Intent intent = new Intent();
            intent.setAction("com.domyos.econnected.SEND_HEART_RATE");
            intent.putExtra("heart_rate", value);
            sendBroadcast(intent);

        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }



}
