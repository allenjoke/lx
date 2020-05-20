package com.domyos.econnected.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.domyos.econnected.constant.Variable;
import com.domyos.econnected.event.EquipmentEvent;
import com.domyos.econnected.event.HeartRateEvent;
import com.ew.ble.library.entity.BikeSportData;
import com.ew.ble.library.entity.TreadmillSportData;

import org.greenrobot.eventbus.EventBus;

/**
 * author : SimonWang
 * date : 2019-11-16 19:24
 * description :
 */
public class HeartRateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.domyos.econnected.SEND_HEART_RATE")) {
            int heartRate = intent.getIntExtra("heart_rate", 0);
            if(Variable.icConnectHeart&&heartRate == 0){
                heartRate = 85;
            }
            EventBus.getDefault().post(new HeartRateEvent(HeartRateEvent.HEART_RATE, heartRate));
        }
        
        if (intent.getAction().equals("com.domyos.econnected.SEND_SPORT_DATA")) {
            Bundle bundle = intent.getExtras();
            int mode = bundle.getInt("device_mode");
            if (mode == 0) {
                TreadmillSportData sportData = bundle.getParcelable("device_info");
                EventBus.getDefault().post(new EquipmentEvent(sportData));
            } else {
                BikeSportData sportData = bundle.getParcelable("device_info");
                EventBus.getDefault().post(new EquipmentEvent(sportData));
            }
            
        }
    }
}
