package com.domyos.econnected.enity;


import com.ew.ble.library.equipment.EWEquipment;

public class EquipmentInfo {
    private boolean isConnect;
    private int equipmentId;
    private EWEquipment ewEquipment;

    public EWEquipment getEwEquipment() {
        return ewEquipment;
    }

    public void setEwEquipment(EWEquipment ewEquipment) {
        this.ewEquipment = ewEquipment;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }
}
