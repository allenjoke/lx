package com.domyos.econnected.event;

import android.bluetooth.BluetoothDevice;

import com.domyos.econnected.enity.EquipmentInfo;
import com.ew.ble.library.entity.BikeSportData;
import com.ew.ble.library.entity.TreadmillSportData;
import com.ew.ble.library.equipment.EWEquipment;

public class EquipmentEvent {
    public int action;
    public EquipmentInfo equipmentInfo;
    public EWEquipment ewEquipment;
    public TreadmillSportData treadmillSportData;
    public BikeSportData bikeSportData;
    
    
    public EquipmentEvent(int action) {
        this.action = action;
        
    }

    public EquipmentEvent(int action,EWEquipment ewEquipment) {
        this.action=action;
        this.ewEquipment = ewEquipment;
    }

    public EquipmentEvent(int action, EquipmentInfo equipmentInfo) {
        this.action = action;
        this.equipmentInfo = equipmentInfo;
    }
    
    public BluetoothDevice bluetoothDevice;
    
    public EquipmentEvent(BluetoothDevice bluetoothDevice) {
        this.action = ACTION_HEART_RATE_ITEM_CLICK;
        this.bluetoothDevice = bluetoothDevice;
    }
    
    public EquipmentEvent(TreadmillSportData treadmillSportData) {
        this.action = ACTION_TREADMILL_SPORT;
        this.treadmillSportData = treadmillSportData;
    }
    
    public EquipmentEvent(BikeSportData bikeSportData) {
        this.action = ACTION_BIKE_SPORT;
        this.bikeSportData = bikeSportData;
    }
    
    
    public static final int ACTION_EQUIPMENT_CONNECTED = 101;
    public static final int ACTION_EQUIPMENT_DISCONNECT = 102;
    public static final int ACTION_EQUIPMENT_SEARCH = 103;
    public static final int ACTION_EQUIPMENT_PAGE = 107;


    public static final int ACTION_HEART_RATE_ITEM_CLICK = 104;
    
    public static final int ACTION_TREADMILL_SPORT = 105;
    public static final int ACTION_BIKE_SPORT = 106;
    
    
}
