package com.domyos.econnected.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IIvviCoreManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;


import com.android.server.ivvi.IvviCoreConstant;
import com.domyos.econnected.heartRate.HeartRateService;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;


public class BackgroundService extends Service {

   private IIvviCoreManager mCoreManager;

    private Timer timer;
    private final IBinder mBinder = new LocalBinder();

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
        mCoreManager = IIvviCoreManager.Stub.asInterface(ServiceManager.getService("ivviCoreService"));
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendReceiver();
            }
        },0,1000);

        Log.e("backgroundService","onCreate");
    }

    private void sendReceiver() {

        try {

            double[] data = mCoreManager.getAllResult();
            int value = (int)data[IvviCoreConstant.RESULT_ARR_INDEX_CURR_HEART_RATE];
            Log.e("backgroundService","heart_rate=="+value);
            Intent intent = new Intent();
            intent.setAction("com.domyos.econnected.SEND_HEART_RATE");
            intent.putExtra("heart_rate", value);
            sendBroadcast(intent);

        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }



}
